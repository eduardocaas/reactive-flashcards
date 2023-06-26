package com.efc.reactiveflashcards.domain.document;

import java.time.OffsetDateTime;

public record Question(String asked,
                       OffsetDateTime askedIn,
                       String answered,
                       OffsetDateTime answeredIn,
                       String expected) {
}
