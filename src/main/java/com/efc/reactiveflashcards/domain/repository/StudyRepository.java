package com.efc.reactiveflashcards.domain.repository;

import com.efc.reactiveflashcards.domain.document.StudyDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StudyRepository extends ReactiveMongoRepository<StudyDocument, String> {

    Mono<StudyDocument> findByStudyDeckDeckId(final String deckId);
}
