package tz.go.samuel.danda.tutorials.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RequestBodyParseErrorExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> requestBodyNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        Map<String, Object> result = new HashMap<>();
        result.put("http_status", HttpStatus.BAD_REQUEST.value());
        result.put("error_message", HttpStatus.BAD_REQUEST.name());
        result.put("error_reasons", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
