package com.efc.reactiveflashcards.domain.service.query;

import com.efc.reactiveflashcards.domain.document.DeckDocument;
import com.efc.reactiveflashcards.domain.exception.NotFoundException;
import com.efc.reactiveflashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.efc.reactiveflashcards.domain.exception.BaseErrorMessage.DECK_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class DeckQueryService {

    private final DeckRepository deckRepository;

    public Mono<DeckDocument> findById(final String id) {
        return deckRepository.findById(id)
                .doFirst(() -> log.info("==== try to find deck with id: {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NotFoundException(DECK_NOT_FOUND.params(id).getMessage()))));
    }
}
