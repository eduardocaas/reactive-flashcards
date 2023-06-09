package com.efc.reactiveflashcards.domain.mapper;

import com.efc.reactiveflashcards.domain.document.Card;
import com.efc.reactiveflashcards.domain.document.Question;
import com.efc.reactiveflashcards.domain.document.StudyCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

@Mapper(componentModel = "SPRING")
public interface StudyDomainMapper {

    StudyCard toStudyCard(final Card card);

    default Question generateRandomQuestion(Set<StudyCard> cards) {
        var values = new ArrayList<>(cards);
        var random = new Random();
        var position = random.nextInt(values.size());
        return toQuestion(values.get(position));
    }

    @Mapping(target = "asked", source = "front")
    @Mapping(target = "askedIn", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "answered", ignore = true)
    @Mapping(target = "answeredIn", ignore = true)
    @Mapping(target = "expected", source = "back")
    Question toQuestion(final StudyCard card);
}
