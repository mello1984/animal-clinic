package ru.butakov.animalclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.animalclinic.domain.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
