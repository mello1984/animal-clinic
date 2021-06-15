package ru.butakov.animalclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.animalclinic.domain.Animal;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByName(String name);
}
