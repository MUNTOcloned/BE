package edu.muntoclone.exception.handle;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String path;
    private int status;
    private String message;
    private List<FieldError> errors;
    private String code;

    public ErrorResponse(String path, String message, int status, String code) {
        this.path = path;
        this.message = message;
        this.status = status;
        this.errors = new ArrayList<>();
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String path, String message, int status, List<FieldError> errors, String code) {
        this.path = path;
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }

    public static ErrorResponse of(String path, ErrorCode code) {
        return new ErrorResponse(path, code.getMessage(), code.getStatus(), code.getCode());
    }

    public static ErrorResponse of(String path, ErrorCode code, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .map(fd -> FieldError.builder()
                        .field(fd.getField())
                        .value(fd.getRejectedValue() == null ? "null" : fd.getRejectedValue().toString())
                        .reason(fd.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return new ErrorResponse(path, code.getMessage(), code.getStatus(), fieldErrors, code.getCode());
    }
}
