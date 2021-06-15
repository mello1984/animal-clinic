package ru.butakov.animalclinic.service;

public interface ServiceUtils {
    <T> T checkExistsSuchNameOrThrow(FindService<T> findService, Class<T> tClass, String name);

    <T> void checkNotExistsSuchNameOrThrow(FindService<T> findService, Class<T> tClass, String name);

    <T> T checkExistsSuchIdOrThrow(FindService<T> findService, Class<T> tClass, long id);
}
