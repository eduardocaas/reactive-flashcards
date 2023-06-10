package com.efc.reactiveflashcards.domain.document;

import lombok.Builder;

import java.util.Set;

public record StudyDeck(String name,
                        String description,
                        Set<StudyCard> cards) {

    @Builder(toBuilder = true)
    public StudyDeck { }
}
