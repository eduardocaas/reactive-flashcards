package com.efc.reactiveflashcards.api.mapper;

import com.efc.reactiveflashcards.api.controller.request.UserRequest;
import com.efc.reactiveflashcards.api.controller.response.UserResponse;
import com.efc.reactiveflashcards.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDocument toDocument(final UserRequest request);
    UserResponse toResponse(final UserDocument document);

}
