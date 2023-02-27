package ru.pizzaneo.products.monitoring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SigmaPingObject {
    @JsonProperty("version")
    private final String version;

    @JsonCreator
    public SigmaPingObject(@JsonProperty("version") String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
