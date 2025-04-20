package com.kalavastra.api.exception;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested resource (e.g. a User) cannot be found.
 * Triggers an HTTP 404 NOT FOUND response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	
	/**
     * Unique ID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Create a new exception with the given message.
     *
     * @param message the detail message (typically includes the resource type and identifier).
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Create a new exception with the given message and root cause.
     *
     * @param message the detail message.
     * @param cause   the original exception that caused this one to be thrown.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Entity-field-value constructor for standardized messages.
     *
     * @param resourceName the name of the entity (e.g. "User")
     * @param fieldName    the field used to look up (e.g. "email")
     * @param fieldValue   the value not found (e.g. "foo@bar.com")
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
