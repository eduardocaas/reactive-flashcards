package com.efc.reactiveflashcards.api.exceptionhandler;

import com.efc.reactiveflashcards.api.controller.response.ProblemResponse;
import com.efc.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static com.efc.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_EXCEPTION;
import static com.efc.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOW;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(ReactiveFlashcardsException.class, e -> handleReactiveFlashcardsException(exchange, e))
                .onErrorResume(Exception.class, e -> handleException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> handleJsonProcessingException(exchange, e))
                .then();
    }

    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange,
                                                       final MethodNotAllowedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                })
                .map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== MethodNotAllowedException: Method [{}] is not allowed at [{}]",
                                        exchange.getRequest().getMethod(),
                                        exchange.getRequest().getPath().value(), ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleReactiveFlashcardsException(final ServerWebExchange exchange,
                                                         final ReactiveFlashcardsException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                })
                .map(message -> buildError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== ReactiveFlashcardsException: ", ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleException(final ServerWebExchange exchange,
                                       final Exception ex) {
        return Mono.fromCallable(() -> {
            prepareExchange(exchange, INTERNAL_SERVER_ERROR);
            return GENERIC_EXCEPTION.getMessage();
        })
            .map(message -> buildError(INTERNAL_SERVER_ERROR, message))
            .doFirst(() -> log.error("==== Exception: ", ex))
            .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleJsonProcessingException(final ServerWebExchange exchange,
                                                     final JsonProcessingException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                })
                .map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== JsonProcessingException: Failed to map exception for the request {} ",
                                        exchange.getRequest().getMethod(), ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ProblemResponse response) {
        return exchange
                .getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(objectMapper
                                                                        .writeValueAsBytes(response))));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode) {
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);
    }

    private ProblemResponse buildError(final HttpStatus status, final String errorDescription) {
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDescription)
                .build();
    }
}
