package ru.butakov.animalclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.animalclinic.domain.Breed;

import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long> {
    Optional<Breed> findByName(String name);
}
