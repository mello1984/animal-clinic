package ru.butakov.animalclinic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AnimalApiException extends ResponseStatusException {
    public AnimalApiException(HttpStatus status) {
        super(status);
    }

    public AnimalApiException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public AnimalApiException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public AnimalApiException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
