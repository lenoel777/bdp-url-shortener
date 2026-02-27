package com.bdpit5.bus.util;

import com.bdpit5.bus.dto.ErrorMessage;
import com.bdpit5.bus.dto.ErrorSchema;
import com.bdpit5.bus.dto.UrlResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<UrlResponse> handleRuntime(RuntimeException ex) {

        return buildErrorResponse(
                "500",
                "Terjadi kesalahan pada sistem",
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<UrlResponse> handleIllegal(IllegalArgumentException ex) {

        return buildErrorResponse(
                "400",
                ex.getMessage(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UrlResponse> handleGeneral(Exception ex) {

        return buildErrorResponse(
                "500",
                "Terjadi kesalahan tidak terduga",
                "Unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<UrlResponse> buildErrorResponse(
            String code,
            String idMessage,
            String enMessage,
            HttpStatus status
    ) {

        ErrorMessage errorMessage =
                new ErrorMessage(idMessage, enMessage);

        ErrorSchema errorSchema =
                new ErrorSchema(errorMessage, code);

        UrlResponse response =
                UrlResponse.builder()
                        .errorSchema(errorSchema)
                        .outputSchemas(List.of())
                        .build();

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<UrlResponse> handleValidation(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return buildErrorResponse(
                "400",
                "Url tidak boleh kosong",
                message,
                HttpStatus.BAD_REQUEST
        );
    }
}
