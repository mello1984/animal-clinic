package ru.butakov.animalclinic.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.butakov.animalclinic.domain.Breed;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BreedRepositoryTest {
    @Autowired
    BreedRepository breedRepository;

    @Test
    void findByName_thenReturnBreed() {
        String name = "Home cat";
        Breed expected = new Breed();
        expected.setName(name);
        breedRepository.save(expected);

        Optional<Breed> actual = breedRepository.findByName(name);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo(name);
    }

    @Test
    void findByName_thenReturnEmpty() {
        String name = "Home cat";
        Optional<Breed> actual = breedRepository.findByName(name);
        assertThat(actual).isEqualTo(Optional.empty());
    }
}