package ru.butakov.animalclinic.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.butakov.animalclinic.dao.KindRepository;
import ru.butakov.animalclinic.domain.Animal;
import ru.butakov.animalclinic.domain.Breed;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.KindDto;
import ru.butakov.animalclinic.exceptions.AnimalApiException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Kind kind1 = Kind.builder().id(1).name("Dog").build();
        Kind kind2 = Kind.builder().id(2).name("Cat").build();

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
        Kind kind = Kind.builder().id(id).name("Dog").build();

        Optional<Kind> expected = Optional.of(kind);
        Mockito.when(kindRepository.findById(id)).thenReturn(expected);

        Optional<Kind> actual = kindService.findById(id);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).findById(id);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void findByName() {
        String name = "Dog";
        Kind kind = Kind.builder().id(1).name(name).build();

        Optional<Kind> expected = Optional.of(kind);
        Mockito.when(kindRepository.findByName(name)).thenReturn(expected);

        Optional<Kind> actual = kindService.findByName(name);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).findByName(name);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void save() {
        Kind kind = Kind.builder().id(1).name("Dog").build();
        Kind expected = Kind.builder().id(2).name("Killer dog").build();

        Mockito.when(kindRepository.save(kind)).thenReturn(expected);
        Kind actual = kindService.save(kind);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).save(kind);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void addAndReturnDtoSuccessful() {
        String name = "Dog";
        Kind kind = Kind.builder().id(1).name(name).build();

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
        String name = "Dog";
        String message = "message";

        Mockito.doThrow(new AnimalApiException(HttpStatus.BAD_REQUEST, message))
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
        Kind kind = Kind.builder().id(1).name("Dog").build();

        Mockito.when(kindRepository.findAll()).thenReturn(List.of(kind));
        List<KindDto> expected = List.of(new KindDto(kind));
        List<KindDto> actual = kindService.findAllDto();

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(kindRepository).findAll();
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void deleteSuccessful() {
        long id = 1;
        Kind kind = Kind.builder().id(id).name("Dog").build();

        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(kindService, Kind.class, id)).thenReturn(kind);
        kindService.delete(id);

        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(kindService, Kind.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verify(kindRepository).delete(kind);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void deleteFailed() {
        long id = 1;
        Mockito.doThrow(new AnimalApiException(HttpStatus.NOT_FOUND, "message"))
                .when(serviceUtils).checkExistsSuchIdOrThrow(kindService, Kind.class, id);

        assertThatThrownBy(() -> kindService.delete(id));
        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(kindService, Kind.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(kindRepository);
    }


    @Test
    void getKindDtoByIdSuccessful() {
        long id = 1;
        Kind kind = Kind.builder().id(id).name("Dog").build();
        KindDto expected = new KindDto(kind);
        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(kindService, Kind.class, id)).thenReturn(kind);
        KindDto actual = kindService.getKindDtoById(id);

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(kindService, Kind.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
    }

    @Test
    void getKindDtoByIdFailed() {
        long id = 1;
        Mockito.doThrow(new AnimalApiException(HttpStatus.NOT_FOUND, "message"))
                .when(serviceUtils)
                .checkExistsSuchIdOrThrow(kindService, Kind.class, id);

        assertThatThrownBy(() -> kindService.getKindDtoById(id));
        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(kindService, Kind.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
    }

    @Test
    void updateSuccessful() {
        Breed breed = new Breed();
        breed.setName("breed");
        Animal animal = new Animal();

        long id = 1;
        String nameBefore = "Dog";
        String nameAfter = "Cat";
        KindDto kindDto = new KindDto(2, nameAfter, Collections.emptySet());

        Kind kind = Kind.builder().id(id).name(nameBefore).breeds(Set.of(breed)).animals(Set.of(animal)).build();

        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(kindService, Kind.class, id)).thenReturn(kind);
        Mockito.when(kindRepository.save(kind)).thenReturn(kind);
        KindDto expected = new KindDto(id, nameAfter, Set.of(breed.getName()));
        KindDto actual = kindService.update(id, kindDto);

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(kindService, Kind.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verify(kindRepository).save(kind);
        Mockito.verifyNoMoreInteractions(kindRepository);
    }

    @Test
    void updateFailed() {
        long id = 1;
        KindDto kindDto = new KindDto(2, "Cat", Collections.emptySet());

        String message = "message";
        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(kindService, Kind.class, id))
                .thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));
        assertThatThrownBy(() -> kindService.update(id, kindDto))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(message);

        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(kindService, Kind.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(kindRepository);
    }
}