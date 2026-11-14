package com.pathfinder.exception;

import org.springframework.http.HttpStatus;

public class PathfindingException extends RuntimeException {
    private final HttpStatus status;
    public PathfindingException(HttpStatus status, String message) { super(message); this.status = status; }
    public HttpStatus getStatus() { return status; }
}
