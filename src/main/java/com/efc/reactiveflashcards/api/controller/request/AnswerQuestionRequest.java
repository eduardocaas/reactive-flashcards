package com.efc.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record AnswerQuestionRequest(@JsonProperty("answer")
                                    @Size(min = 1, max = 500)
                                    @NotBlank
                                    String answer) {

    @Builder(toBuilder = true)
    public AnswerQuestionRequest { }
}
