package ru.butakov.animalclinic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnimalApiBadRequest extends RuntimeException {
    public AnimalApiBadRequest(String message) {
        super(message);
    }

    public AnimalApiBadRequest(String message, Throwable cause) {
        super(message, cause);
    }
}
