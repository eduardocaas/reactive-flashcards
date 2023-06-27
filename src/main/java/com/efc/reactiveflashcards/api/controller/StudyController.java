package com.efc.reactiveflashcards.api.controller;

import com.efc.reactiveflashcards.api.controller.request.StudyRequest;
import com.efc.reactiveflashcards.api.controller.response.QuestionResponse;
import com.efc.reactiveflashcards.api.mapper.StudyMapper;
import com.efc.reactiveflashcards.domain.service.StudyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("studies")
@Slf4j
@AllArgsConstructor
public class StudyController {

    private final StudyService service;
    private final StudyMapper mapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> start(final StudyRequest request) {
        return service.start(mapper.toDocument(request))
                .doFirst(() -> log.info("==== try to create a study with follow request {}", request))
                .map(document -> mapper.toResponse(document.getLastQuestionPending()));
    }

}
