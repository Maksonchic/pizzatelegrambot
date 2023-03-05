package ru.pizzaneo.clients.monitoring;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component("clients")
public class HealthCheckClients implements HealthIndicator {

    private final String pingUrl;

    public HealthCheckClients(@Value("${management.clients.ping-url}") String pingUrl) {
        this.pingUrl = pingUrl;
    }

    @Override
    public Health health() {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setReadTimeout(Duration.ofSeconds(1))
                .build();
        HttpEntity<Request> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(this.pingUrl, HttpMethod.GET, entity, String.class);
        } catch (RuntimeException e) {
            return Health.down().withDetail("message", "connection exception").build();
        }

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return Health.up().withDetail("message", "Всё окей").build();
        } else {
            return Health.down().withDetail("message", "clients server down").build();
        }
    }
}
