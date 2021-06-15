package ru.butakov.animalclinic.service;

import ru.butakov.animalclinic.domain.Animal;
import ru.butakov.animalclinic.domain.dto.AnimalDto;

import java.util.List;

public interface AnimalService extends FindService<Animal> {

    List<AnimalDto> findAllDto();

    AnimalDto addAndReturnDto(String name);

    AnimalDto getAnimalDtoById(long id);

    AnimalDto update(long id, AnimalDto update);

    void delete(long id);
}
