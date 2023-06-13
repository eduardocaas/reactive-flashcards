package com.efc.reactiveflashcards.domain.service;

import com.efc.reactiveflashcards.domain.document.UserDocument;
import com.efc.reactiveflashcards.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserDocument> save(final UserDocument document) {
        return userRepository.save(document)
                .doFirst(() -> log.info("==== try to save a follow document {}", document));
    }
}
