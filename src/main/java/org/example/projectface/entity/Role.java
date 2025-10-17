package org.example.projectface.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    student,
    teacher,
    director;

    @JsonCreator
    public static Role forValue(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
};
