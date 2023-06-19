package com.efc.reactiveflashcards.api.controller;

import com.efc.reactiveflashcards.api.controller.request.UserRequest;
import com.efc.reactiveflashcards.api.controller.response.UserResponse;
import com.efc.reactiveflashcards.api.mapper.UserMapper;
import com.efc.reactiveflashcards.core.validation.MongoId;
import com.efc.reactiveflashcards.domain.service.UserService;
import com.efc.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest request) {
        return userService
                .save(userMapper.toDocument(request))
                .doFirst(() -> log.info("==== saving user with follow data {}", request))
                .map(userMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userQueryService.findById(id)
                .doFirst(() -> log.info("==== finding user with id: {}", id))
                .map(userMapper::toResponse);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                    @Valid @RequestBody final UserRequest request) {
        return userService.update(userMapper.toDocument(request, id))
                .doFirst(() -> log.info("==== updating user with follow info [body: {}, id: {}]", request, id))
                .map(userMapper::toResponse);
    }
}
