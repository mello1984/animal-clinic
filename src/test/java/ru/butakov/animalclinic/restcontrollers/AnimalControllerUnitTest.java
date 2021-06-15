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
import ru.butakov.animalclinic.domain.Animal;
import ru.butakov.animalclinic.domain.dto.AnimalDto;
import ru.butakov.animalclinic.exceptions.AnimalApiException;
import ru.butakov.animalclinic.service.AnimalService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(properties = "spring.main.lazy-initialization=true")
class AnimalControllerUnitTest {
    @Autowired
    AnimalController animalController;
    @MockBean
    AnimalService animalService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getAnimals_thenReturnListOfAnimalsDto() throws Exception {

        AnimalDto animalDto = new AnimalDto(Animal.builder().id(1).name("Cat").build());
        List<AnimalDto> expected = List.of(animalDto);
        Mockito.when(animalService.findAllDto()).thenReturn(expected);

        mockMvc.perform(get("/api/animals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(animalService).findAllDto();
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    void add_thenReturnOk() throws Exception {
        String name = "Cat";
        AnimalDto expected = new AnimalDto(Animal.builder().id(1).name(name).build());
        Mockito.when(animalService.addAndReturnDto(name)).thenReturn(expected);

        mockMvc.perform(post("/api/animals")
                .param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(animalService).addAndReturnDto(name);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    void add_thenBadRequest() throws Exception {
        String name = "Cat";
        String message = "message";
        Mockito.doThrow(new AnimalApiException(HttpStatus.BAD_REQUEST, message))
                .when(animalService).addAndReturnDto(name);

        mockMvc.perform(post("/api/animals")
                .param("name", name))
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message)
                        .hasMessageContaining(HttpStatus.BAD_REQUEST.name()));

        Mockito.verify(animalService).addAndReturnDto(name);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    void getAnimal_thenReturnAnimalDto() throws Exception {
        long id = 1;
        AnimalDto expected = new AnimalDto(Animal.builder().id(id).name("Cat").build());
        Mockito.when(animalService.getAnimalDtoById(id)).thenReturn(expected);

        mockMvc.perform(get("/api/animals/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(animalService).getAnimalDtoById(id);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    void getAnimal_thenNotFound() throws Exception {
        long id = 1;
        String message = "message";
        Mockito.doThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message)).when(animalService).getAnimalDtoById(id);

        mockMvc.perform(get("/api/animals/" + id))
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message)
                        .hasMessageContaining(HttpStatus.NOT_FOUND.name()));

        Mockito.verify(animalService).getAnimalDtoById(id);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    void update_thenSuccessful() throws Exception {
        long id = 1;
        AnimalDto expected = new AnimalDto(Animal.builder().id(id).name("name1").build());
        AnimalDto updateDto = new AnimalDto(Animal.builder().id(2).name("name2").build());

        Mockito.when(animalService.update(id, updateDto)).thenReturn(expected);
        mockMvc.perform(
                patch("/api/animals/" + id)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(animalService).update(id, updateDto);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    void update_thenNotFound() throws Exception {
        long id = 1;
        AnimalDto updateDto = new AnimalDto(Animal.builder().id(2).name("name2").build());

        String message = "message";
        Mockito.when(animalService.update(id, updateDto)).thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));
        mockMvc.perform(
                patch("/api/animals/" + id)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message)
                        .hasMessageContaining(HttpStatus.NOT_FOUND.name()));


        Mockito.verify(animalService).update(id, updateDto);
        Mockito.verifyNoMoreInteractions(animalService);
    }


    @Test
    void delete_thenSuccessful() throws Exception {
        long id = 1000;

        mockMvc.perform(delete("/api/animals/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(animalService).delete(id);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    void delete_thenNotFound() throws Exception {
        long id = 1000;
        String message = "message";
        Mockito.doThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message))
                .when(animalService).delete(id);

        mockMvc.perform(delete("/api/animals/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message));

        Mockito.verify(animalService).delete(id);
        Mockito.verifyNoMoreInteractions(animalService);
    }
}