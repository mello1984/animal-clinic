package ru.butakov.animalclinic.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.butakov.animalclinic.domain.Kind;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class KindRepositoryTest {
    @Autowired
    KindRepository kindRepository;

    @Test
    void findByName() {
        Kind kind = new Kind();
        String name = "Dog";
        kind.setName(name);
        kindRepository.save(kind);

        Optional<Kind> kindOptional = kindRepository.findByName(name);
        assertThat(kindOptional).isNotEmpty();
        assertThat(kindOptional.get().getName()).isEqualTo(name);
    }
}