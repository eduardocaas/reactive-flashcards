package com.efc.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public record DeckRequest(@NotBlank
                          @Size(min = 1, max = 255)
                          @JsonProperty("name")
                          String name,
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @JsonProperty("description")
                          String description,
                          @Valid
                          @Size(min = 3)
                          @JsonProperty("cards")
                          Set<CardRequest> cards) {

    @Builder(toBuilder = true)
    public DeckRequest { }
}
