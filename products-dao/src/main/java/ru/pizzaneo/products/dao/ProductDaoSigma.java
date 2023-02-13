package ru.pizzaneo.products.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.pizzaneo.products.domain.CurrentProductRow;
import ru.pizzaneo.products.domain.MenuGroups;
import ru.pizzaneo.products.domain.MenuGroupsGroups;
import ru.pizzaneo.products.domain.MenuMatrix;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductDaoSigma implements ProductDao {

    private final long deliveryGroupId;
    private final String sigmaHost;
    private final String menuId;
    private final String companyId;
    private final RestTemplate restTemplate;

    public ProductDaoSigma(
            @Value("${pizza.sigma.delivery-group-id}") long deliveryGroupId
            , @Value("${pizza.sigma.host}") String sigmaHost
            , @Value("${pizza.sigma.menu-id}") String menuId
            , @Value("${pizza.sigma.company-id}") String companyId
            , RestTemplate restTemplate) {
        this.deliveryGroupId = deliveryGroupId;
        this.sigmaHost = sigmaHost;
        this.menuId = menuId;
        this.companyId = companyId;
        this.restTemplate = restTemplate;
    }

    @Override
    public MenuGroups requestMenuGroups(final String ticket) {
        String url = this.sigmaHost + "/rest/v2/groups?path=%2A&menuId=" + this.menuId + "&page=0&size=0";

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getRequestEntity(ticket);

        ResponseEntity<MenuGroups> response = restTemplate.exchange(
                URI.create(url)
                , HttpMethod.GET
                , requestEntity
                , MenuGroups.class);

        List<MenuGroupsGroups> groups = Objects.requireNonNull(response.getBody())
                .groups().stream().filter(g -> g.id() != this.deliveryGroupId).collect(Collectors.toList());

        return new MenuGroups(groups);
    }

    @Override
    public MenuMatrix requestMenuMatrix(String ticket, long groupId, int page, int size) {
        String url = this.sigmaHost + "/rest/v2/companies/" + this.companyId + "/menu-matrix" +
                "?page=" + page + "&size=" + size + "&menuId=" + this.menuId + "&groupId=" +
                (groupId == 0 ? "" : groupId) + "&sort=name%2Cdesc";

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getRequestEntity(ticket);
        ResponseEntity<MenuMatrix> response = restTemplate.exchange(
                URI.create(url)
                , HttpMethod.GET
                , requestEntity
                , MenuMatrix.class);

        return new MenuMatrix(Objects.requireNonNull(response.getBody())
                .rows()
                .stream().filter(row -> !(row.groups().contains(this.deliveryGroupId) && row.groups().size() == 1))
                .toList());
    }

    @Override
    public CurrentProductRow requestProductRow(String ticket, String productId) {
        String url = this.sigmaHost + "/rest/v2/companies/" + this.companyId +
                "/menus/" + this.menuId +
                "/menu-products/" + productId;

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getRequestEntity(ticket);

        ResponseEntity<CurrentProductRow> response = restTemplate.exchange(
                URI.create(url)
                , HttpMethod.GET
                , requestEntity
                , CurrentProductRow.class);

        return response.getBody();
    }

    @Override
    public List<CurrentProductRow> requestProductsRows(String ticket, List<String> productIds) {
        String urlPattern = this.sigmaHost + "/rest/v2/companies/" + this.companyId +
                "/menus/" + this.menuId +
                "/menu-products/";

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getRequestEntity(ticket);

        ResponseEntity<CurrentProductRow> response;
        List<CurrentProductRow> rows = new ArrayList<>();
        for (String productId : productIds) {
            response = restTemplate.exchange(
                    urlPattern + productId
                    , HttpMethod.GET
                    , requestEntity
                    , CurrentProductRow.class);
            rows.add(response.getBody());
        }

        return rows;
    }

    private static HttpEntity<MultiValueMap<String, Object>> getRequestEntity(String ticket) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String languages = "fr-ch, fr;q=0.9, en-*;q=0.8, de;q=0.7, *;q=0.5";
        headers.setAcceptLanguage(Locale.LanguageRange.parse(languages));
        headers.add("authorization", "Bearer " + ticket);
        return new HttpEntity<>(headers);
    }
}
