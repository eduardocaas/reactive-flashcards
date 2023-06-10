package com.efc.reactiveflashcards.domain.document;

import lombok.Builder;

public record Card(String front,
                   String back) {

    @Builder(toBuilder = true)
    public Card { }
}
