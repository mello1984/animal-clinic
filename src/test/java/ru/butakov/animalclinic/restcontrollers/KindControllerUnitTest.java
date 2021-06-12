package ru.butakov.animalclinic.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.KindDto;
import ru.butakov.animalclinic.exceptions.AnimalApiException;
import ru.butakov.animalclinic.service.KindService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(properties = "spring.main.lazy-initialization=true")
@AutoConfigureMockMvc
class KindControllerUnitTest {
    @Autowired
    KindController kindController;
    @MockBean
    KindService kindService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getKinds() throws Exception {
        KindDto kindDto1 = new KindDto(Kind.builder().id(1).name("kind1").build());
        KindDto kindDto2 = new KindDto(Kind.builder().id(2).name("kind2").build());
        List<KindDto> expected = List.of(kindDto1, kindDto2);

        Mockito.when(kindService.findAllDto()).thenReturn(expected);
        mockMvc.perform(get("/api/kinds"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(kindService).findAllDto();
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void getKindSuccessful() throws Exception {
        long id = 1;
        KindDto expected = new KindDto(Kind.builder().id(id).name("kind").build());
        Mockito.when(kindService.getKindDtoById(id)).thenReturn(expected);

        mockMvc.perform(get("/api/kinds/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(kindService).getKindDtoById(id);
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void getKindByIdFailedNotSuchKind() throws Exception {
        long id = 1;
        String message = "Exception message";
        Mockito.when(kindService.getKindDtoById(id)).thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));

        mockMvc.perform(get("/api/kinds/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(AnimalApiException.class)
                        .hasMessageContaining(message));

        Mockito.verify(kindService).getKindDtoById(id);
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void addSuccessful() throws Exception {
        String name = "Dog";
        KindDto expected = new KindDto(Kind.builder().id(1).name(name).build());
        Mockito.when(kindService.addAndReturnDto(name)).thenReturn(expected);

        mockMvc.perform(
                post("/api/kinds/")
                        .param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(kindService).addAndReturnDto(name);
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void addFailed() throws Exception {
        String name = "Dog";
        String message = "Exception message";
        Mockito.when(kindService.addAndReturnDto(name)).thenThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message));

        mockMvc.perform(
                post("/api/kinds/")
                        .param("name", name))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(
                        result -> assertThat(result.getResolvedException())
                                .isInstanceOf(AnimalApiException.class)
                                .hasMessageContaining(message));

        Mockito.verify(kindService).addAndReturnDto(name);
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void updateSuccessful() throws Exception {
        long id = 1;
        String cat = "Cat";
        KindDto update = new KindDto(Kind.builder().id(1000).name(cat).build());
        KindDto after = new KindDto(Kind.builder().id(id).name(cat).build());

        Mockito.when(kindService.update(id, update)).thenReturn(after);
        mockMvc.perform(patch("/api/kinds/" + id)
                .content(objectMapper.writeValueAsString(update)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(after)));

        Mockito.verify(kindService).update(id, update);
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void updateFailed() throws Exception {
        long id = 1;
        String cat = "Cat";
        KindDto update = new KindDto(Kind.builder().id(1000).name(cat).build());

        String message = "message";
        Mockito.when(kindService.update(id, update)).thenThrow(new AnimalApiException(HttpStatus.BAD_REQUEST, message));
        mockMvc.perform(patch("/api/kinds/" + id)
                .content(objectMapper.writeValueAsString(update)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(AnimalApiException.class)
                                .hasMessageContaining(message));

        Mockito.verify(kindService).update(id, update);
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void deleteSuccessful() throws Exception {
        long id = 1;

        mockMvc.perform(delete("/api/kinds/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(kindService).delete(id);
        Mockito.verifyNoMoreInteractions(kindService);
    }

    @Test
    void deleteFailedNoSuchId() throws Exception {
        long id = 1;

        String message = "message";
        Mockito.doThrow(new AnimalApiException(HttpStatus.NOT_FOUND, message))
                .when(kindService)
                .delete(id);

        mockMvc.perform(delete("/api/kinds/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(
                        result -> assertThat(result.getResolvedException())
                                .isInstanceOf(AnimalApiException.class)
                                .hasMessageContaining(message));

        Mockito.verify(kindService).delete(id);
        Mockito.verifyNoMoreInteractions(kindService);
    }
}