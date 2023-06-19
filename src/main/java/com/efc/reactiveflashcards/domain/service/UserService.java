package com.efc.reactiveflashcards.domain.service;

import com.efc.reactiveflashcards.domain.document.UserDocument;
import com.efc.reactiveflashcards.domain.repository.UserRepository;
import com.efc.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument document) {
        return userRepository.save(document)
                .doFirst(() -> log.info("==== try to save a user with follow info {}", document));
    }

    public Mono<UserDocument> update(final UserDocument document) {
        return userQueryService.findById(document.id())
                .map(user -> document.toBuilder()
                                .createdAt(user.createdAt())
                                .updatedAt(user.updatedAt())
                                .build())
                .flatMap(userRepository::save)
                .doFirst(() -> log.info("==== try to update a user with follow info {}", document));
    }

    public Mono<Void> delete(final String id) {
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info("==== try to delete a user with follow id: {}", id));
    }
}
