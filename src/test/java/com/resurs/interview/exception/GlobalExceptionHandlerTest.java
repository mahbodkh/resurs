package com.resurs.interview.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleExternalServiceException() {
        // given
        ExternalServiceException ex = new ExternalServiceException("External service error");

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleExternalServiceException(ex);

        // then
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals(HttpStatus.BAD_GATEWAY.value(), response.getBody().getStatus());
        assertEquals("External service error", response.getBody().getMessage());
    }

    @Test
    void testHandleDatabaseException() {
        // given
        DatabaseException ex = new DatabaseException("Database error");

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDatabaseException(ex);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Database error", response.getBody().getMessage());
    }
}