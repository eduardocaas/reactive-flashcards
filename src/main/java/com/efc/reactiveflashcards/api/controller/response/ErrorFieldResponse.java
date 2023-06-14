package com.efc.reactiveflashcards.api.controller.response;

import lombok.Builder;

public record ErrorFieldResponse(String field,
                                 String message) {

    @Builder(toBuilder = true)
    public ErrorFieldResponse { }
}
