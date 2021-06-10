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
    void findByName() {
        String name = "Home cat";
        Breed breed = new Breed();
        breed.setName(name);
        breedRepository.save(breed);

        Optional<Breed> actual = breedRepository.findByName(name);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getName()).isEqualTo(name);
    }
}