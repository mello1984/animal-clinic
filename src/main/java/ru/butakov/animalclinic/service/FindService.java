package ru.butakov.animalclinic.service;

import java.util.List;
import java.util.Optional;

public interface FindService<T> {
    Optional<T> findById(long id);

    Optional<T> findByName(String name);

    List<T> findAll();

}
