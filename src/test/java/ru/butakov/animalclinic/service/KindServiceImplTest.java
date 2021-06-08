package ru.butakov.animalclinic.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.butakov.animalclinic.dao.KindRepository;
import ru.butakov.animalclinic.domain.Breed;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.BreedDto;
import ru.butakov.animalclinic.domain.dto.KindDto;
import ru.butakov.animalclinic.exceptions.AnimalApiBadRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
class KindServiceImplTest {
    @Autowired
    KindService kindService;
    @MockBean
    KindRepository kindRepository;
    @MockBean
    ServiceUtils serviceUtils;


    @Test
    void findAll() {
        Kind kind1 = new Kind();
        kind1.setId(1);
        kind1.setName("kind1");

        Kind kind2 = new Kind();
        kind2.setId(2);
        kind2.setName("kind2");

        List<Kind> expected = List.of(kind1, kind2);
        Mockito.when(kindRepository.findAll()).thenReturn(expected);

        List<Kind> actual = kindService.findAll();
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).findAll();
        Mockito.verifyNoMoreInteractions(kindRepository);
    }


    @Test
    void findById() {
        long id = 1;
        Kind kind = new Kind();
        kind.setId(id);
        kind.setName("kind");

        Optional<Kind> expected = Optional.of(kind);
        Mockito.when(kindRepository.findById(id)).thenReturn(expected);

        Optional<Kind> actual = kindService.findById(id);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).findById(id);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void findByName() {
        String name = "kind";
        Kind kind = new Kind();
        kind.setId(1);
        kind.setName(name);

        Optional<Kind> expected = Optional.of(kind);
        Mockito.when(kindRepository.findByName(name)).thenReturn(expected);

        Optional<Kind> actual = kindService.findByName(name);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).findByName(name);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void save() {
        Kind kind = new Kind();
        kind.setId(1);
        kind.setName("kind");

        Kind expected = new Kind();
        expected.setId(2);
        expected.setName("kind name");

        Mockito.when(kindRepository.save(kind)).thenReturn(expected);
        Kind actual = kindService.save(kind);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).save(kind);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void addAndReturnDtoSuccessful() {
        String name = "kind name";

        Kind kind = new Kind();
        kind.setId(1);
        kind.setName(name);

        KindDto expected = new KindDto(kind);
        Mockito.when(kindRepository.save(Mockito.any(Kind.class))).thenReturn(kind);

        KindDto actual = kindService.addAndReturnDto(name);

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(serviceUtils).checkNotExistsSuchNameOrThrow(kindService, Kind.class, name);
        Mockito.verifyNoMoreInteractions(serviceUtils);

        Mockito.verify(kindRepository).save(Mockito.any(Kind.class));
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void addAndReturnDtoExists() {
        String name = "kind name";
        String message = "message";

        Mockito.doThrow(new AnimalApiBadRequest(message))
                .when(serviceUtils)
                .checkNotExistsSuchNameOrThrow(kindService, Kind.class, name);

        assertThatThrownBy(() -> kindService.addAndReturnDto(name));
        Mockito.verify(serviceUtils).checkNotExistsSuchNameOrThrow(kindService, Kind.class, name);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(kindRepository);
    }


    @Test
    void findAllDtoEmptyList() {
        Mockito.when(kindRepository.findAll()).thenReturn(Collections.emptyList());
        List<KindDto> actual = kindService.findAllDto();

        assertThat(actual).isEqualTo(Collections.emptyList());
        Mockito.verify(kindRepository).findAll();
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void findAllDtoSuccessful() {
        Kind kind = new Kind();
        kind.setId(1);
        kind.setName("name");

        Mockito.when(kindRepository.findAll()).thenReturn(List.of(kind));
        List<KindDto> expected = List.of(new KindDto(kind));
        List<KindDto> actual = kindService.findAllDto();

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).findAll();
        Mockito.verifyNoMoreInteractions(kindRepository);
    }
}