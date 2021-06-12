package ru.butakov.animalclinic.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.butakov.animalclinic.dao.BreedRepository;
import ru.butakov.animalclinic.domain.Breed;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.BreedDto;
import ru.butakov.animalclinic.exceptions.AnimalApiException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
class BreedServiceImplTest {
    @Autowired
    BreedService breedService;
    @MockBean
    BreedRepository breedRepository;
    @MockBean
    KindService kindService;
    @MockBean
    ServiceUtils serviceUtils;

    @Test
    void findAll() {
        Breed breed1 = new Breed();
        breed1.setId(1);
        breed1.setName("dog");

        Breed breed2 = new Breed();
        breed2.setId(2);
        breed2.setName("cat");

        List<Breed> expected = List.of(breed1, breed2);
        Mockito.when(breedRepository.findAll()).thenReturn(expected);

        List<Breed> actual = breedService.findAll();
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(breedRepository).findAll();
        Mockito.verifyNoMoreInteractions(breedRepository);
    }

    @Test
    void findById() {
        long id = 1;
        Breed breed = new Breed();
        breed.setId(id);
        breed.setName("dog");

        Optional<Breed> expected = Optional.of(breed);
        Mockito.when(breedRepository.findById(id)).thenReturn(expected);

        Optional<Breed> actual = breedService.findById(id);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(breedRepository).findById(id);
        Mockito.verifyNoMoreInteractions(breedRepository);
    }

    @Test
    void findByName() {
        String name = "dog";
        Breed breed = new Breed();
        breed.setId(1);
        breed.setName(name);

        Optional<Breed> expected = Optional.of(breed);
        Mockito.when(breedRepository.findByName(name)).thenReturn(expected);

        Optional<Breed> actual = breedService.findByName(name);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(breedRepository).findByName(name);
        Mockito.verifyNoMoreInteractions(breedRepository);
    }

    @Test
    void save() {
        Breed breed = new Breed();
        breed.setId(1);
        breed.setName("dog");

        Breed expected = new Breed();
        expected.setId(2);
        expected.setName("dog killer");

        Mockito.when(breedRepository.save(breed)).thenReturn(expected);
        Breed actual = breedService.save(breed);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(breedRepository).save(breed);
        Mockito.verifyNoMoreInteractions(breedRepository);
    }

    @Test
    void addAndReturnDtoSuccessful() {

        String kindName = "Cat";
        Kind kind = Kind.builder().id(2).name(kindName).build();

        long id = 5;
        String name = "Home cat";
        Breed breed = new Breed();
        breed.setId(id);
        breed.setName(name);
        breed.setKind(kind);

        Mockito.when(serviceUtils.checkExistsSuchNameOrThrow(kindService, Kind.class, kindName)).thenReturn(kind);
        Mockito.when(breedRepository.save(Mockito.any(Breed.class))).thenReturn(breed);
        BreedDto expected = new BreedDto(id, name, kindName);
        BreedDto actual = breedService.addAndReturnDto(name, kindName);

        assertThat(actual).isEqualTo(expected);

        Mockito.verify(serviceUtils).checkExistsSuchNameOrThrow(kindService, Kind.class, kindName);
        Mockito.verify(serviceUtils).checkNotExistsSuchNameOrThrow(breedService, Breed.class, name);
        Mockito.verifyNoMoreInteractions(serviceUtils);

        Mockito.verify(breedRepository).save(Mockito.any(Breed.class));
        Mockito.verifyNoMoreInteractions(breedRepository);
    }

    @Test
    void addAndReturnDtoNotExistingKind() {
        String kindName = "Cat";
        String name = "Home cat";
        String message = "message";
        Mockito.when(serviceUtils.checkExistsSuchNameOrThrow(kindService, Kind.class, kindName))
                .thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));

        assertThatThrownBy(() -> breedService.addAndReturnDto(name, kindName))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(message);

        Mockito.verify(serviceUtils).checkExistsSuchNameOrThrow(kindService, Kind.class, kindName);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(breedRepository);
    }

    @Test
    void addAndReturnDtoNotExistingBreed() {

        String kindName = "Cat";
        String name = "Home cat";
        String message = "message";
        Kind kind = new Kind();

        Mockito.when(serviceUtils.checkExistsSuchNameOrThrow(kindService, Kind.class, kindName)).thenReturn(kind);
        Mockito.doThrow(new AnimalApiException(HttpStatus.BAD_REQUEST, message))
                .when(serviceUtils)
                .checkNotExistsSuchNameOrThrow(breedService, Breed.class, name);

        assertThatThrownBy(() -> breedService.addAndReturnDto(name, kindName))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(message);

        Mockito.verify(serviceUtils).checkExistsSuchNameOrThrow(kindService, Kind.class, kindName);
        Mockito.verify(serviceUtils).checkNotExistsSuchNameOrThrow(breedService, Breed.class, name);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(breedRepository);
    }

    @Test
    void findAllDtoEmptyList() {
        Mockito.when(breedRepository.findAll()).thenReturn(Collections.emptyList());
        List<BreedDto> actual = breedService.findAllDto();

        assertThat(actual).isEqualTo(Collections.emptyList());
        Mockito.verify(breedRepository).findAll();
        Mockito.verifyNoMoreInteractions(breedRepository);
    }

    @Test
    void findAllDtoSuccessful() {
        String kindName = "Cat";
        Kind kind = Kind.builder().id(2).name(kindName).build();

        long id = 5;
        String name = "Home cat";
        Breed breed = new Breed();
        breed.setId(id);
        breed.setName(name);
        breed.setKind(kind);

        Mockito.when(breedRepository.findAll()).thenReturn(List.of(breed));
        List<BreedDto> expected = List.of(new BreedDto(id, name, kindName));
        List<BreedDto> actual = breedService.findAllDto();

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(breedRepository).findAll();
        Mockito.verifyNoMoreInteractions(breedRepository);
    }
}