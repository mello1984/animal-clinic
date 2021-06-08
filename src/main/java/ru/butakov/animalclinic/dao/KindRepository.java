package ru.butakov.animalclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.animalclinic.domain.Kind;

import java.util.Optional;

public interface KindRepository extends JpaRepository<Kind, Long> {
    Optional<Kind> findByName(String name);
}
