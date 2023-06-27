package com.efc.reactiveflashcards.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "studies")
public record StudyDocument(@Id
                        String id,
                        String userId,
                        StudyDeck studyDeck,
                        List<Question> questions,
                        @CreatedDate
                        @Field("created_at")
                        OffsetDateTime createdAt,
                        @LastModifiedDate
                        @Field("updated_at")
                        OffsetDateTime updatedAt) {

    @Builder(toBuilder = true)
    public StudyDocument { }

    public void addQuestion(final Question question) {
        questions.add(question);
    }
    public Question getLastQuestionPending() {
        return questions.stream().filter(question ->
                Objects.isNull(question.answeredIn())).findFirst().orElseThrow();
    }
}
