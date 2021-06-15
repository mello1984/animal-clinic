package ru.butakov.animalclinic.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.butakov.animalclinic.domain.Kind;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class KindRepositoryTest {
    @Autowired
    KindRepository kindRepository;

    @Test
    void findByName_thenReturnKind() {
        Kind expected = new Kind();
        String name = "Dog";
        expected.setName(name);
        kindRepository.save(expected);

        Optional<Kind> kindOptional = kindRepository.findByName(name);
        assertThat(kindOptional).isNotEmpty();
        assertThat(kindOptional.get().getName()).isEqualTo(name);
    }

    @Test
    void findByName_thenReturnEmpty() {
        String name = "Dog";
        Optional<Kind> kindOptional = kindRepository.findByName(name);
        assertThat(kindOptional).isEqualTo(Optional.empty());
    }
}