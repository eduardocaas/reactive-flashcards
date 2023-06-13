package com.efc.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.annotation.Id;

public record UserResponse(@JsonProperty("id")
                           String id,
                           @JsonProperty("name")
                           String name,
                           @JsonProperty("email")
                           String email) {

    @Builder(toBuilder = true)
    public UserResponse { }
}
