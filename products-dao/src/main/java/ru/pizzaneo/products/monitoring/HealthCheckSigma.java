package ru.pizzaneo.products.monitoring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component("products")
public class HealthCheckSigma implements HealthIndicator {

    private final String pingUrl;

    public HealthCheckSigma(@Value("${management.products.ping-url}") String pingUrl) {
        this.pingUrl = pingUrl;
    }

    @Override
    public Health health() {

        RestTemplate restTemplate = new RestTemplateBuilder()
                .setReadTimeout(Duration.ofSeconds(3))
                .build();

        SigmaPingObject forObject;
        try {
            forObject = restTemplate.getForObject(
                    this.pingUrl
                    , SigmaPingObject.class);
        } catch (RestClientException e) {
            return Health
                    .down()
                    .withDetail("message", "sigma server down")
                    .build();
        }

        if (forObject != null && !forObject.getVersion().equals("")) {
            return Health
                    .up()
                    .withDetail("message", "Всё окей")
                    .build();
        } else {
            return Health
                    .down()
                    .withDetail("message", "sigma server down")
                    .build();
        }
    }
}
