package ru.pizzaneo.telegram.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.pizzaneo.models.dto.MenuGroupsDto;
import ru.pizzaneo.models.dto.MenuMatrixDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
@Cacheable("products")
public class ProductsAdapterImpl implements ProductsAdapter {

    private final RestTemplate restTemplate;
    private final String url;

    public ProductsAdapterImpl(RestTemplate restTemplate, @Value("${connector.products.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Override
    public MenuGroupsDto getProductGroups() throws URISyntaxException {
        return restTemplate.getForObject(
                new URI(this.url + "groups")
                , MenuGroupsDto.class
        );
    }

    @Override
    public MenuMatrixDto getProductsListInGroup(long groupId, int page, int size) throws URISyntaxException {
        return restTemplate.getForObject(
                new URI(this.url + "groups/" + groupId + "/" + page + "/" + size)
                , MenuMatrixDto.class
        );
    }

    @Override
    public MenuMatrixDto getProductsListByIds(List<String> variationIds) throws URISyntaxException {
        return restTemplate.getForObject(
                new URI(this.url + "products/" + String.join(",", variationIds) + "/all")
                , MenuMatrixDto.class
        );
    }
}
