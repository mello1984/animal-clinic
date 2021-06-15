package ru.butakov.animalclinic.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.butakov.animalclinic.dao.AnimalRepository;
import ru.butakov.animalclinic.domain.Animal;
import ru.butakov.animalclinic.domain.dto.AnimalDto;
import ru.butakov.animalclinic.exceptions.AnimalApiException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
class AnimalServiceImplTest {
    @Autowired
    AnimalService animalService;
    @MockBean
    AnimalRepository animalRepository;
    @MockBean
    ServiceUtils serviceUtils;

    @Test
    void findById() {
        long id = 1;
        Optional<Animal> expected = Optional.of(Animal.builder().id(id).name("Cat").build());
        Mockito.when(animalRepository.findById(id)).thenReturn(expected);

        Optional<Animal> actual = animalService.findById(id);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(animalRepository).findById(id);
        Mockito.verifyNoMoreInteractions(animalRepository);
    }

    @Test
    void findByName() {
        String name = "Cat";
        Optional<Animal> expected = Optional.of(Animal.builder().id(100).name(name).build());
        Mockito.when(animalRepository.findByName(name)).thenReturn(expected);

        Optional<Animal> actual = animalService.findByName(name);
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(animalRepository).findByName(name);
        Mockito.verifyNoMoreInteractions(animalRepository);
    }

    @Test
    void findAll() {
        List<Animal> expected = List.of(Animal.builder().id(1).name("cat").build());
        Mockito.when(animalRepository.findAll()).thenReturn(expected);

        List<Animal> actual = animalService.findAll();
        assertThat(actual).isEqualTo(expected);
        Mockito.verify(animalRepository).findAll();
        Mockito.verifyNoMoreInteractions(animalRepository);
    }

    @Test
    void findAllDto() {
        Animal animal = Animal.builder().id(1).name("cat").build();
        List<Animal> animals = List.of(animal);
        Mockito.when(animalRepository.findAll()).thenReturn(animals);

        List<AnimalDto> expected = List.of(new AnimalDto(animal));
        List<AnimalDto> actual = animalService.findAllDto();

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(animalRepository).findAll();
        Mockito.verifyNoMoreInteractions(animalRepository);
    }

    @Test
    void addAndReturnDto_thenSuccessful() {
        String name = "Cat";
        Animal animal = Animal.builder().name(name).build();
        Mockito.when(animalRepository.save(animal)).thenReturn(animal);

        AnimalDto expected = new AnimalDto(animal);
        AnimalDto actual = animalService.addAndReturnDto(name);

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(serviceUtils).checkNotExistsSuchNameOrThrow(animalService, Animal.class, name);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verify(animalRepository).save(animal);
        Mockito.verifyNoMoreInteractions(animalRepository);
    }

    @Test
    void addAndReturnDto_thenFailed() {
        String name = "Cat";
        Animal animal = Animal.builder().name(name).build();

        String message = "message";
        Mockito.doThrow(new AnimalApiException(HttpStatus.BAD_REQUEST, message))
                .when(serviceUtils).checkNotExistsSuchNameOrThrow(animalService, Animal.class, name);

        assertThatThrownBy(() -> animalService.addAndReturnDto(name))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(message)
                .hasMessageContaining(HttpStatus.BAD_REQUEST.name());

        Mockito.verify(serviceUtils).checkNotExistsSuchNameOrThrow(animalService, Animal.class, name);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(animalRepository);
    }

    @Test
    void getAnimalDtoById_thenSuccessful() {
        long id = 1;
        Animal animal = Animal.builder().id(id).name("Cat").build();
        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(animalService, Animal.class, id)).thenReturn(animal);
        AnimalDto expected = new AnimalDto(animal);
        AnimalDto actual = animalService.getAnimalDtoById(id);

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(animalService, Animal.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
    }

    @Test
    void getAnimalDtoById_thenFailed() {
        long id = 1;
        String message = "message";
        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(animalService, Animal.class, id))
                .thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));

        assertThatThrownBy(() -> animalService.getAnimalDtoById(id))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(message)
                .hasMessageContaining(HttpStatus.NOT_FOUND.name());

        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(animalService, Animal.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
    }

    @Test
    void update_thenSuccessful() {
        long id = 1;
        String name1 = "name1";
        LocalDate birthday1 = LocalDate.of(2019, 10, 10);
        String name2 = "name2";
        LocalDate birthday2 = LocalDate.of(2020, 1, 1);

        AnimalDto updateDto = new AnimalDto(Animal.builder().id(2).name(name2).birthday(birthday2).build());
        Animal before = Animal.builder().id(id).name(name1).birthday(birthday1).build();
        Animal after = Animal.builder().id(id).name(name2).birthday(birthday2).build();
        AnimalDto expected = new AnimalDto(after);

        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(animalService, Animal.class, id)).thenReturn(before);
        Mockito.when(animalRepository.save(after)).thenReturn(after);

        AnimalDto actual = animalService.update(id, updateDto);

        assertThat(actual).isEqualTo(expected);
        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(animalService, Animal.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verify(animalRepository).save(after);
        Mockito.verifyNoMoreInteractions(animalRepository);
    }

    @Test
    void update_thenFailed() {
        long id = 1;
        String name2 = "name2";
        LocalDate birthday2 = LocalDate.of(2020, 1, 1);

        AnimalDto updateDto = new AnimalDto(Animal.builder().id(2).name(name2).birthday(birthday2).build());
        Animal after = Animal.builder().id(id).name(name2).birthday(birthday2).build();

        String message = "message";
        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(animalService, Animal.class, id))
                .thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));

        assertThatThrownBy(() -> animalService.update(id, updateDto))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(message)
                .hasMessageContaining(HttpStatus.NOT_FOUND.name());

        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(animalService, Animal.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(animalRepository);
    }

    @Test
    void delete_thenSuccessful() {
        long id = 1000;
        Animal animal = Animal.builder().id(id).build();
        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(animalService, Animal.class, id)).thenReturn(animal);
        animalService.delete(id);

        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(animalService, Animal.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verify(animalRepository).delete(animal);
        Mockito.verifyNoMoreInteractions(animalRepository);
    }

    @Test
    void delete_thenFailed() {
        long id = 1000;
        String message = "message";
        Mockito.when(serviceUtils.checkExistsSuchIdOrThrow(animalService, Animal.class, id))
                .thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));

        assertThatThrownBy(() -> animalService.delete(id))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(message)
                .hasMessageContaining(HttpStatus.NOT_FOUND.name());

        Mockito.verify(serviceUtils).checkExistsSuchIdOrThrow(animalService, Animal.class, id);
        Mockito.verifyNoMoreInteractions(serviceUtils);
        Mockito.verifyNoInteractions(animalRepository);
    }
}