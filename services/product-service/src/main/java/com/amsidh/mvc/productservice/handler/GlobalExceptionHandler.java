package com.amsidh.mvc.productservice.handler;

import com.amsidh.mvc.productservice.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ProductPurchaseException.class)
        public ResponseEntity<String> handle(ProductPurchaseException productPurchaseException) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(productPurchaseException.getMessage());
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<String> handle(EntityNotFoundException entityNotFoundException) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(entityNotFoundException.getMessage());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException methodArgumentNotValidException) {
                var errors = methodArgumentNotValidException.getAllErrors().stream()
                                .collect(
                                                java.util.stream.Collectors.toMap(
                                                                error -> ((FieldError) error).getField(),
                                                                DefaultMessageSourceResolvable::getDefaultMessage,
                                                                (existing, replacement) -> existing + "; "
                                                                                + replacement));
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ErrorResponse(errors));
        }
}
