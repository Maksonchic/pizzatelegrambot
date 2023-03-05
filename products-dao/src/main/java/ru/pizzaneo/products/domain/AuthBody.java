package ru.pizzaneo.products.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class AuthBody {
    @JsonProperty("access_token")
    private String access_token;

    public AuthBody(@JsonProperty("access_token") String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }
}
