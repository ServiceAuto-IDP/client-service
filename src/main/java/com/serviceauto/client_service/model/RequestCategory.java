package com.serviceauto.client_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.serviceauto.client_service.exception.InvalidRequestCategoryException;
import java.util.Arrays;

public enum RequestCategory {
    MECHANICAL("mechanical"),
    ELECTRICAL("electrical"),
    PAINTING("painting"),
    TIRE_SERVICE("tire_service");

    private final String value;

    RequestCategory(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RequestCategory fromValue(String value) {
        return Arrays.stream(values())
                .filter(category -> category.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestCategoryException(
                        "Invalid category. Accepted values: mechanical, electrical, painting, tire_service"
                ));
    }
}
