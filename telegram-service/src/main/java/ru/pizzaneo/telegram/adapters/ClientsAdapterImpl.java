package ru.pizzaneo.telegram.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.pizzaneo.models.dto.ClientDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClientsAdapterImpl implements ClientsAdapter {

    private final RestTemplate restTemplate;
    private final String clientsServiceHost;

    public ClientsAdapterImpl(
            @Value("${connector.clients.url}") String clientsServiceHost
            , RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.clientsServiceHost = clientsServiceHost;
    }

    @Override
    public ClientDto getClient(final long chatId) throws URISyntaxException {
        return restTemplate.getForObject(
                new URI(this.clientsServiceHost + "clients/" + chatId)
                , ClientDto.class
        );
    }

    @Override
    public void saveProductToClient(final long chatId, final String variationId) {
        String resourceUrl = this.clientsServiceHost + "clients/basket/" + chatId;
        HttpEntity<String> requestUpdate = new HttpEntity<>(variationId, new HttpHeaders());
        this.restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Void.class);
    }

    @Override
    public void deleteProductFromClient(final long chatId, final String variationId) {
        String resourceUrl = this.clientsServiceHost + "clients/basket";
        Map<String, String> params = new HashMap<>();
        params.put("chatId", String.valueOf(chatId));
        params.put("productId", variationId);
        HttpEntity<Map<String, String>> requestUpdate = new HttpEntity<>(params, new HttpHeaders());
        this.restTemplate.exchange(
                resourceUrl
                , HttpMethod.DELETE
                , requestUpdate
                , Void.class);
    }

    @Override
    public void clearClientBasket(final long chatId) {
        this.restTemplate.delete(this.clientsServiceHost + "clients/" + chatId + "/basket");
    }
}
