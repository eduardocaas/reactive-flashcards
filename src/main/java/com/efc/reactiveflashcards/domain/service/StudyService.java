package com.efc.reactiveflashcards.domain.service;

import com.efc.reactiveflashcards.domain.document.Card;
import com.efc.reactiveflashcards.domain.document.StudyDocument;
import com.efc.reactiveflashcards.domain.exception.DeckInStudyException;
import com.efc.reactiveflashcards.domain.exception.NotFoundException;
import com.efc.reactiveflashcards.domain.mapper.StudyDomainMapper;
import com.efc.reactiveflashcards.domain.repository.StudyRepository;
import com.efc.reactiveflashcards.domain.service.query.DeckQueryService;
import com.efc.reactiveflashcards.domain.service.query.StudyQueryService;
import com.efc.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static com.efc.reactiveflashcards.domain.exception.BaseErrorMessage.DECK_IN_STUDY;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {

    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyQueryService studyQueryService;
    private final StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper;

    public Mono<StudyDocument> start(final StudyDocument document) {
        return verifyStudy(document)
                .then(userQueryService.findById(document.userId()))
                .flatMap(user -> deckQueryService.findById(document.studyDeck().deckId()))
                .flatMap(deck -> getCards(document, deck.cards()))
                .map(study -> study.toBuilder()
                        .question(studyDomainMapper.generateRandomQuestion(study.studyDeck().cards())).build())
                .doFirst(() -> log.info("==== generating a first random question"))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("==== study saved {}", study));
    }

    private Mono<StudyDocument> getCards(final StudyDocument document, final Set<Card> cards) {
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("==== copy cards to new study"))
                .map(studyDomainMapper::toStudyCard)
                .collectList()
                .map(studyCards -> document.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> document.toBuilder().studyDeck(studyDeck).build());
    }

    private Mono<Void> verifyStudy(final StudyDocument document) {
        return studyQueryService.findPendingStudyUserIdAndDeckId(document.userId(), document.studyDeck().deckId())
                .flatMap(study -> Mono.defer(() ->
                        Mono.error(new DeckInStudyException(DECK_IN_STUDY
                                .params(document.userId(), document.studyDeck().deckId()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();
    }
}
