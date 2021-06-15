package ru.butakov.animalclinic.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.butakov.animalclinic.domain.Animal;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnimalRepositoryTest {
    @Autowired
    AnimalRepository animalRepository;

    @Test
    void findByName_thenReturnAnimal() {
        String name = "Cat";
        Animal expected = Animal.builder().id(1).name(name).build();

        animalRepository.save(expected);
        Optional<Animal> actual = animalRepository.findByName(name);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findByName_thenReturnEmpty() {
        String name = "Cat";
        Optional<Animal> actual = animalRepository.findByName(name);
        assertThat(actual).isEqualTo(Optional.empty());
    }
}