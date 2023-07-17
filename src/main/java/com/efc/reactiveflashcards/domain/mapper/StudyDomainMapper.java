package com.efc.reactiveflashcards.domain.mapper;

import com.efc.reactiveflashcards.domain.document.Card;
import com.efc.reactiveflashcards.domain.document.Question;
import com.efc.reactiveflashcards.domain.document.StudyCard;
import com.efc.reactiveflashcards.domain.document.StudyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "answered", ignore = true)
    @Mapping(target = "answeredIn", ignore = true)
    @Mapping(target = "expected", source = "back")
    Question toQuestion(final StudyCard card);

    default StudyDocument answer(final StudyDocument document, final String answer) {
        var currentQuestion = document.getLastPendingQuestion();
        var questions = document.questions();
        var currentIndexQuestion = questions.indexOf(currentQuestion);
        currentQuestion = currentQuestion.toBuilder().answered(answer).build();
        questions.set(currentIndexQuestion, currentQuestion);
        return document.toBuilder().questions(questions).build();
    }
}
