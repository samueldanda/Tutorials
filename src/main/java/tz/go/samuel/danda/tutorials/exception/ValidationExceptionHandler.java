package tz.go.samuel.danda.tutorials.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> requestBodyNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, Object> result = new HashMap<>();
        result.put("http_status", HttpStatus.BAD_REQUEST.value());
        result.put("error_message", HttpStatus.BAD_REQUEST.name());
        result.put("error_reasons", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> requestBodyNotValid(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        errors.add(ex.getMessage());

//        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, Object> result = new HashMap<>();
        result.put("http_status", HttpStatus.BAD_REQUEST.value());
        result.put("error_message", HttpStatus.BAD_REQUEST.name());
        result.put("error_reasons", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> requestParamsOrPathVariablesNotValid(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errors;
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("http_status", HttpStatus.BAD_REQUEST.value());
        result.put("error_message", HttpStatus.BAD_REQUEST.name());
        result.put("error_reasons", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }


}
