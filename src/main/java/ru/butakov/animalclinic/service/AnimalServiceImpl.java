package ru.butakov.animalclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.animalclinic.dao.AnimalRepository;
import ru.butakov.animalclinic.domain.Animal;
import ru.butakov.animalclinic.domain.dto.AnimalDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalServiceImpl implements AnimalService {
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    ServiceUtils serviceUtils;

    @Override
    public Optional<Animal> findById(long id) {
        return animalRepository.findById(id);
    }

    @Override
    public Optional<Animal> findByName(String name) {
        return animalRepository.findByName(name);
    }

    @Override
    public List<Animal> findAll() {
        return animalRepository.findAll();
    }

    @Override
    public List<AnimalDto> findAllDto() {
        return animalRepository.findAll().stream().map(AnimalDto::new).collect(Collectors.toList());
    }

    @Override
    public AnimalDto addAndReturnDto(String name) {
        serviceUtils.checkNotExistsSuchNameOrThrow(this, Animal.class, name);
        Animal animal = Animal.builder().name(name).build();
        animal = animalRepository.save(animal);
        return new AnimalDto(animal);
    }

    @Override
    public AnimalDto getAnimalDtoById(long id) {
        Animal animal = serviceUtils.checkExistsSuchIdOrThrow(this, Animal.class, id);
        return new AnimalDto(animal);
    }

    @Override
    public AnimalDto update(long id, AnimalDto update) {
        Animal animal = serviceUtils.checkExistsSuchIdOrThrow(this, Animal.class, id);
        animal.setName(update.getName());
        animal.setBirthday(update.getBirthday());
        animal = animalRepository.save(animal);
        return new AnimalDto(animal);
    }

    @Override
    public void delete(long id) {
        Animal animal = serviceUtils.checkExistsSuchIdOrThrow(this, Animal.class, id);
        animalRepository.delete(animal);
    }
}
