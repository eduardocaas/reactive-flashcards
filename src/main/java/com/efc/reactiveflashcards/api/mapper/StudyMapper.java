package com.efc.reactiveflashcards.api.mapper;

import com.efc.reactiveflashcards.api.controller.request.StudyRequest;
import com.efc.reactiveflashcards.api.controller.response.QuestionResponse;
import com.efc.reactiveflashcards.domain.document.Question;
import com.efc.reactiveflashcards.domain.document.StudyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "SPRING")
public interface StudyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studyDeck.deckId", source = "deckId")
    @Mapping(target = "studyDeck.cards", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    StudyDocument toDocument(final StudyRequest request);

    QuestionResponse toResponse(final Question question);
}
