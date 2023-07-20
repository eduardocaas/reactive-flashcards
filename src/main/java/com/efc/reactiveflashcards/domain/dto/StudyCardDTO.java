package com.efc.reactiveflashcards.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public record StudyCardDTO(String front,
                           String back) {

    public static StudyCardBuilder builder() {
        return new StudyCardBuilder();
    }

    public StudyCardBuilder toBuilder() {
        return new StudyCardBuilder(front, back);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyCardBuilder {
        private String front;
        private String back;

        public StudyCardBuilder front(final String front) {
            this.front = front;
            return this;
        }

        public StudyCardBuilder back(final String back) {
            this.back = back;
            return this;
        }

        public StudyCardDTO build() {
            return new StudyCardDTO(front, back);
        }
    }
}
