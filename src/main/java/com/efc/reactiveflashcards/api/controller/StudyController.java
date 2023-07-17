package com.efc.reactiveflashcards.api.controller;

import com.efc.reactiveflashcards.api.controller.request.StudyRequest;
import com.efc.reactiveflashcards.api.controller.response.QuestionResponse;
import com.efc.reactiveflashcards.api.mapper.StudyMapper;
import com.efc.reactiveflashcards.core.validation.MongoId;
import com.efc.reactiveflashcards.domain.service.StudyService;
import com.efc.reactiveflashcards.domain.service.query.StudyQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("studies")
@Slf4j
@AllArgsConstructor
public class StudyController {

    private final StudyService service;
    private final StudyQueryService queryService;
    private final StudyMapper mapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> start(@Valid @RequestBody final StudyRequest request) {
        return service.start(mapper.toDocument(request))
                .doFirst(() -> log.info("==== try to create a study with follow request {}", request))
                .map(document -> mapper.toResponse(document.getLastPendingQuestion(), document.id()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}/current-question")
    public Mono<QuestionResponse> getCurrentQuestion(@Valid @PathVariable
                                                         @MongoId(message = "{studyController.id}") final String id) {
        return queryService.getLastPendingQuestion(id)
                .doFirst(() -> log.info("==== try to get a next question in study {}", id))
                .map(question -> mapper.toResponse(question, id));
    }

}
