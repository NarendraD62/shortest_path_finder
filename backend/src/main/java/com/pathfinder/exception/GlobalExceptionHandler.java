package com.pathfinder.exception;

import java.time.Instant;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public record ErrorResponse(Instant timestamp, int status, String error, String message, Map<String,String> details) { }
    @ExceptionHandler(PathfindingException.class)
    public ResponseEntity<ErrorResponse> handlePathfinding(PathfindingException ex) {
        return error(ex.getStatus(),ex.getMessage(),Map.of());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> details=new TreeMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e->details.putIfAbsent(e.getField(),e.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(e->details.putIfAbsent(e.getObjectName(),e.getDefaultMessage()));
        String message=details.values().stream().findFirst().orElse("Request validation failed.");
        return error(HttpStatus.BAD_REQUEST,message,details);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return error(HttpStatus.BAD_REQUEST,"Malformed request or unknown algorithm value.",Map.of());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred.",Map.of());
    }
    private ResponseEntity<ErrorResponse> error(HttpStatus status,String message,Map<String,String> details) {
        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(),status.value(),status.getReasonPhrase(),message,details));
    }
}
