package com.efc.reactiveflashcards.api.controller;

import com.efc.reactiveflashcards.api.controller.request.DeckRequest;
import com.efc.reactiveflashcards.api.controller.response.DeckResponse;
import com.efc.reactiveflashcards.api.mapper.DeckMapper;
import com.efc.reactiveflashcards.core.validation.MongoId;
import com.efc.reactiveflashcards.domain.service.DeckService;
import com.efc.reactiveflashcards.domain.service.query.DeckQueryService;
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
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController {

    private final DeckService deckService;
    private final DeckQueryService deckQueryService;
    private final DeckMapper deckMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest request) {
        return deckService
                .save(deckMapper.toDocument(request))
                .doFirst(() -> log.info("==== saving a deck with follow data {}", request))
                .map(deckMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> findById(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id) {
        return deckQueryService.findById(id)
                .doFirst(() -> log.info("==== finding deck with id: {}", id))
                .map(deckMapper::toResponse);
    }
}
