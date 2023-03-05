package ru.pizzaneo.products.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.pizzaneo.products.domain.AuthBody;

import java.util.Collections;

@Service
public class TicketServiceSigma implements TicketService {

    private final String sigmaHost;
    private final String basicString;
    private final String grantType;
    private final String userName;
    private final String passWord;

    private final RestTemplate restTemplate;

    public TicketServiceSigma(
            @Value("${pizza.sigma.host}") String sigmaHost
            , @Value("${pizza.sigma.basic-string}") String basicString
            , @Value("${pizza.sigma.auth.grant-type}") String grantType
            , @Value("${pizza.sigma.auth.username}") String userName
            , @Value("${pizza.sigma.auth.password}") String passWord
            , RestTemplate restTemplate) {
        this.sigmaHost = sigmaHost;
        this.basicString = basicString;
        this.restTemplate = restTemplate;
        this.grantType = grantType;
        this.userName = userName;
        this.passWord = passWord;
    }

    @Override
    public String getTicket() {
        String authUrl = "oauth/token";
        String url = this.sigmaHost + authUrl;
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        headers.add("authorization", "Basic " + this.basicString);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("grant_type", this.grantType);
        body.add("username", this.userName);
        body.add("password", this.passWord);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<AuthBody> response = this.restTemplate.postForEntity(url, requestEntity, AuthBody.class);

        return response.getBody().getAccess_token();
    }
}
