package com.hirehub.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data @Builder @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private int status;
    private String error;
    private String message;
    private String path;
    @Builder.Default
    private Instant timestamp = Instant.now();
    private List<FieldError> fieldErrors;

    @Data @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}
