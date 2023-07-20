package com.efc.reactiveflashcards.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

public record StudyDeckDTO(String deckId,
                           Set<StudyCardDTO> cards) {

    public static StudyDeckBuilder builder() {
        return new StudyDeckBuilder();
    }

    public StudyDeckBuilder toBuilder() {
        return new StudyDeckBuilder(deckId, cards);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyDeckBuilder {
        private String deckId;
        private Set<StudyCardDTO> cards = new HashSet<>();

        public StudyDeckBuilder deckId(final String deckId){
            this.deckId = deckId;
            return this;
        }

        public StudyDeckBuilder cards(final Set<StudyCardDTO> cards){
            this.cards = cards;
            return this;
        }

        public StudyDeckDTO build() {
            return new StudyDeckDTO(deckId, cards);
        }
    }
}
