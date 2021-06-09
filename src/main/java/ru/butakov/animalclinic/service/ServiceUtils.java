package ru.butakov.animalclinic.service;

import org.springframework.stereotype.Component;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.exceptions.AnimalApiBadRequest;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class ServiceUtils {
    public <T> T checkExistsSuchNameOrThrow(FindService<T> findService, Class<T> tClass, String name) {
        Optional<T> tOptional = findService.findByName(name);
        if (tOptional.isEmpty()) {
            String message = MessageFormat.format("{0} with such name <{1}> not exists", tClass.getSimpleName(), name);
            throw new AnimalApiBadRequest(message);
        }
        return tOptional.get();
    }

    public <T> void checkNotExistsSuchNameOrThrow(FindService<T> findService, Class<T> tClass, String name) {
        Optional<T> tOptional = findService.findByName(name);
        if (tOptional.isPresent()) {
            String message = MessageFormat.format("{0} with such name <{1}> already exists", tClass.getSimpleName(), name);
            throw new AnimalApiBadRequest(message);
        }
    }


    public <T> T checkExistsSuchIdOrThrow(FindService<T> findService, Class<T> tClass, long id) {
        Optional<T> tOptional = findService.findById(id);
        if (tOptional.isEmpty()) {
            String message = MessageFormat.format("{0} with such id <{1}> not exists", tClass.getSimpleName(), id);
            throw new AnimalApiBadRequest(message);
        }
        return tOptional.get();
    }
}
