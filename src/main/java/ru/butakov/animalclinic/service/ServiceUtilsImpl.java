package ru.butakov.animalclinic.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.butakov.animalclinic.exceptions.AnimalApiException;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class ServiceUtilsImpl implements ServiceUtils{
    @Override
    public <T> T checkExistsSuchNameOrThrow(FindService<T> findService, Class<T> tClass, String name) {
        Optional<T> tOptional = findService.findByName(name);
        if (tOptional.isEmpty()) {
            String message = MessageFormat.format("{0} with such name <{1}> not exists", tClass.getSimpleName(), name);
            throw new AnimalApiException(HttpStatus.NOT_FOUND, message);
        }
        return tOptional.get();
    }

    @Override
    public <T> void checkNotExistsSuchNameOrThrow(FindService<T> findService, Class<T> tClass, String name) {
        Optional<T> tOptional = findService.findByName(name);
        if (tOptional.isPresent()) {
            String message = MessageFormat.format("{0} with such name <{1}> already exists", tClass.getSimpleName(), name);
            throw new AnimalApiException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Override
    public <T> T checkExistsSuchIdOrThrow(FindService<T> findService, Class<T> tClass, long id) {
        Optional<T> tOptional = findService.findById(id);
        if (tOptional.isEmpty()) {
            String message = MessageFormat.format("{0} with such id <{1}> not exists", tClass.getSimpleName(), id);
            throw new AnimalApiException(HttpStatus.NOT_FOUND, message);
        }
        return tOptional.get();
    }
}
