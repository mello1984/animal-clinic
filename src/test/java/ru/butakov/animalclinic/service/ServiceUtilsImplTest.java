package ru.butakov.animalclinic.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.butakov.animalclinic.exceptions.AnimalApiException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
class ServiceUtilsImplTest {
    @Autowired
    private ServiceUtils serviceUtils;
    @Mock
    private FindService<Object> findService;

    @Test
    void checkExistsSuchNameOrThrowFailed() {
        String name = "object";
        Mockito.when(findService.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceUtils.checkExistsSuchNameOrThrow(findService, Object.class, name))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(String.format("Object with such name <%s> not exists", name));

        Mockito.verify(findService).findByName(name);
        Mockito.verifyNoMoreInteractions(findService);
    }

    @Test
    void checkExistsSuchNameOrThrowSuccessful() {
        String name = "object";
        Object expected = new Object();
        Mockito.when(findService.findByName(name)).thenReturn(Optional.of(expected));

        Object actual = serviceUtils.checkExistsSuchNameOrThrow(findService, Object.class, name);
        assertThat(actual).isEqualTo(expected);

        Mockito.verify(findService).findByName(name);
        Mockito.verifyNoMoreInteractions(findService);
    }

    @Test
    void checkNotExistsSuchNameOrThrowFailed() {
        String name = "object";
        Object expected = new Object();
        Mockito.when(findService.findByName(name)).thenReturn(Optional.of(expected));

        assertThatThrownBy(() -> serviceUtils.checkNotExistsSuchNameOrThrow(findService, Object.class, name))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(String.format("Object with such name <%s> already exists", name));

        Mockito.verify(findService).findByName(name);
        Mockito.verifyNoMoreInteractions(findService);
    }

    @Test
    void checkNotExistsSuchNameOrThrowSuccessful() {
        String name = "object";
        Mockito.when(findService.findByName(name)).thenReturn(Optional.empty());

        assertThatNoException().isThrownBy(() -> serviceUtils.checkNotExistsSuchNameOrThrow(findService, Object.class, name));

        Mockito.verify(findService).findByName(name);
        Mockito.verifyNoMoreInteractions(findService);
    }

    @Test
    void checkExistsSuchIdOrThrowFailed() {
        long id = 2;
        Mockito.when(findService.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceUtils.checkExistsSuchIdOrThrow(findService, Object.class, id))
                .isInstanceOf(AnimalApiException.class)
                .hasMessageContaining(String.format("Object with such id <%s> not exists", id));

        Mockito.verify(findService).findById(id);
        Mockito.verifyNoMoreInteractions(findService);
    }

    @Test

    void checkExistsSuchIdOrThrowSuccessful() {
        long id = 3;
        Object expected = new Object();
        Mockito.when(findService.findById(id)).thenReturn(Optional.of(expected));

        Object actual = serviceUtils.checkExistsSuchIdOrThrow(findService, Object.class, id);
        assertThat(actual).isEqualTo(expected);

        Mockito.verify(findService).findById(id);
        Mockito.verifyNoMoreInteractions(findService);
    }
}