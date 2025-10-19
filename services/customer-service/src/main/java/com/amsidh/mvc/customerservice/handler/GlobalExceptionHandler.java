package com.amsidh.mvc.customerservice.handler;

import com.amsidh.mvc.customerservice.exception.CustomerNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException customerNotFoundException) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(customerNotFoundException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        var errors = methodArgumentNotValidException.getAllErrors().stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                error -> ((FieldError) error).getField(),
                                DefaultMessageSourceResolvable::getDefaultMessage
                        )
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}
