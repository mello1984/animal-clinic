package ru.butakov.animalclinic.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.butakov.animalclinic.domain.Breed;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.BreedDto;
import ru.butakov.animalclinic.exceptions.AnimalApiException;
import ru.butakov.animalclinic.service.BreedService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(properties = "spring.main.lazy-initialization=true")
class BreedControllerUnitTest {
    @Autowired
    BreedController breedController;
    @MockBean
    BreedService breedService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getBreeds_thenReturnListOfBreedsDto() throws Exception {
        Kind kind = Kind.builder().name("kindname").build();
        Breed breed = Breed.builder().id(1).name("name").kind(kind).build();
        BreedDto breedDto = new BreedDto(breed);

        List<BreedDto> expected = List.of(breedDto);
        Mockito.when(breedService.getAllDto()).thenReturn(expected);

        mockMvc.perform(get("/api/breeds"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(breedService).getAllDto();
        Mockito.verifyNoMoreInteractions(breedService);
    }

    @Test
    void getBreed_thenReturnOk() throws Exception {
        long id = 100;
        Kind kind = Kind.builder().name("kindname").build();
        Breed breed = Breed.builder().id(id).name("name").kind(kind).build();
        BreedDto breedDto = new BreedDto(breed);

        Mockito.when(breedService.getBreedDtoById(id)).thenReturn(breedDto);
        mockMvc.perform(get("/api/breeds/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(breedDto)));

        Mockito.verify(breedService).getBreedDtoById(id);
        Mockito.verifyNoMoreInteractions(breedService);
    }

    @Test
    void getBreed_thenReturnBadRequest() throws Exception {
        long id = 100;
        String message = "message";
        Mockito.when(breedService.getBreedDtoById(id)).thenThrow(new AnimalApiException(HttpStatus.BAD_REQUEST, message));
        mockMvc.perform(get("/api/breeds/" + id))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message)
                        .hasMessageContaining(HttpStatus.BAD_REQUEST.name()));

        Mockito.verify(breedService).getBreedDtoById(id);
        Mockito.verifyNoMoreInteractions(breedService);
    }

    @Test
    void add_thenReturnOk() throws Exception {
        long id = 100;
        String name = "name";
        String kindname = "kindname";
        Kind kind = Kind.builder().name(kindname).build();
        Breed breed = Breed.builder().id(id).name(name).kind(kind).build();
        BreedDto breedDto = new BreedDto(breed);

        Mockito.when(breedService.addAndReturnDto(name, kindname)).thenReturn(breedDto);
        mockMvc.perform(
                post("/api/breeds/")
                        .param("name", name)
                        .param("kind", kindname))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(breedDto)));

        Mockito.verify(breedService).addAndReturnDto(name, kindname);
        Mockito.verifyNoMoreInteractions(breedService);
    }

    @Test
    void add_thenReturnNotFound() throws Exception {
        String name = "name";
        String kindname = "kindname";
        String message = "message";

        Mockito.when(breedService.addAndReturnDto(name, kindname)).thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));
        mockMvc.perform(
                post("/api/breeds/")
                        .param("name", name)
                        .param("kind", kindname))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message)
                        .hasMessageContaining(HttpStatus.NOT_FOUND.name()));

        Mockito.verify(breedService).addAndReturnDto(name, kindname);
        Mockito.verifyNoMoreInteractions(breedService);
    }

    @Test
    void update_thenReturnOk() throws Exception {

        long id = 100;
        String name = "name";
        String kindname = "kindname";
        Kind kind = Kind.builder().name(kindname).build();

        Breed update = Breed.builder().id(200).name(name).kind(kind).build();
        BreedDto updateDto = new BreedDto(update);

        Breed after = Breed.builder().id(id).name(name).kind(kind).build();
        BreedDto afterDto = new BreedDto(after);

        Mockito.when(breedService.update(id, updateDto)).thenReturn(afterDto);
        mockMvc.perform(patch("/api/breeds/" + id)
                .content(objectMapper.writeValueAsString(updateDto)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(afterDto)));

        Mockito.verify(breedService).update(id, updateDto);
        Mockito.verifyNoMoreInteractions(breedService);
    }

    @Test
    void update_thenReturnNotFound() throws Exception {
        long id = 100;
        String name = "name";
        String kindname = "kindname";
        Kind kind = Kind.builder().name(kindname).build();

        Breed update = Breed.builder().id(200).name(name).kind(kind).build();
        BreedDto updateDto = new BreedDto(update);

        String message = "message";
        Mockito.when(breedService.update(id, updateDto)).thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));
        mockMvc.perform(
                patch("/api/breeds/" + id)
                        .content(objectMapper.writeValueAsString(updateDto)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message)
                        .hasMessageContaining(HttpStatus.NOT_FOUND.name()));

        Mockito.verify(breedService).update(id, updateDto);
        Mockito.verifyNoMoreInteractions(breedService);
    }

    @Test
    void delete_thenReturnOk() throws Exception {
        long id = 100;
        mockMvc.perform(delete("/api/breeds/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(breedService).delete(id);
        Mockito.verifyNoMoreInteractions(breedService);

    }

    @Test
    void delete_thenReturnNotFound() throws Exception {
        long id = 100;
        String message = "message";
        Mockito.doThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message)).when(breedService).delete(id);

        mockMvc.perform(delete("/api/breeds/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(HttpStatus.NOT_FOUND.name())
                        .hasMessageContaining(message));

        Mockito.verify(breedService).delete(id);
        Mockito.verifyNoMoreInteractions(breedService);

    }
}