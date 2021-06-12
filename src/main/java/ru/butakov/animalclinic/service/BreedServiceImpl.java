package ru.butakov.animalclinic.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.animalclinic.dao.BreedRepository;
import ru.butakov.animalclinic.domain.Breed;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.BreedDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BreedServiceImpl implements BreedService {
    @Autowired
    BreedRepository breedRepository;
    @Autowired
    KindService kindService;
    @Autowired
    ServiceUtils serviceUtils;

    @Override
    public List<Breed> findAll() {
        return breedRepository.findAll();
    }

    @Override
    public Optional<Breed> findById(long id) {
        return breedRepository.findById(id);
    }

    @Override
    public Optional<Breed> findByName(String name) {
        return breedRepository.findByName(name);
    }

    @Override
    public Breed save(Breed breed) {
        return breedRepository.save(breed);
    }

    @Override
    public BreedDto addAndReturnDto(String name, String kindName) {
        Kind kind = serviceUtils.checkExistsSuchNameOrThrow(kindService, Kind.class, kindName);

        serviceUtils.checkNotExistsSuchNameOrThrow(this, Breed.class, name);

        Breed breed = new Breed();
        breed.setName(name);
        breed.setKind(kind);
        breed = breedRepository.save(breed);
        return new BreedDto(breed);
    }

    @Override
    public List<BreedDto> getAllDto() {
        return breedRepository.findAll().stream().map(BreedDto::new).collect(Collectors.toList());
    }

    @Override
    public BreedDto getBreedDtoById(long id) {
        Breed breed = serviceUtils.checkExistsSuchIdOrThrow(this, Breed.class, id);
        return new BreedDto(breed);
    }

    @Override
    public BreedDto update(long id, BreedDto breedDto) {
        Breed breed = serviceUtils.checkExistsSuchIdOrThrow(this, Breed.class, id);
        breed.setName(breedDto.getName());
        breed = breedRepository.save(breed);
        return new BreedDto(breed);
    }

    @Override
    public void delete(long id) {
        Breed breed = serviceUtils.checkExistsSuchIdOrThrow(this, Breed.class, id);
        breedRepository.delete(breed);
    }
}
