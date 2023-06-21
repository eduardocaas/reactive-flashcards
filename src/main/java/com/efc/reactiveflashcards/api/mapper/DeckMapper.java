package com.efc.reactiveflashcards.api.mapper;

import com.efc.reactiveflashcards.api.controller.request.DeckRequest;
import com.efc.reactiveflashcards.api.controller.response.DeckResponse;
import com.efc.reactiveflashcards.domain.document.DeckDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "SPRING")
public interface DeckMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DeckDocument toDocument(final DeckRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DeckDocument toDocument(final DeckRequest request, final String id);

    DeckResponse toResponse(final DeckDocument document);
}
