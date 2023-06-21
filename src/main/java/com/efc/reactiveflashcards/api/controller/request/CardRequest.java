package com.efc.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CardRequest(@NotBlank
                          @Size(min = 1, max = 255)
                          @JsonProperty("front")
                          String front,
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @JsonProperty("back")
                          String back) {

    @Builder(toBuilder = true)
    public CardRequest { }
}
