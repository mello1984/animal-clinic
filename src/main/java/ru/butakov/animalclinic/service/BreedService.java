package ru.butakov.animalclinic.service;

import ru.butakov.animalclinic.domain.Breed;
import ru.butakov.animalclinic.domain.dto.BreedDto;

import java.util.List;

public interface BreedService extends FindService<Breed> {

    Breed save(Breed breed);

    BreedDto addAndReturnDto(String name, String kindName);

    List<BreedDto> getAllDto();

    BreedDto getBreedDtoById(long id);

    BreedDto update(long id, BreedDto breedDto);

    void delete(long id);
}
