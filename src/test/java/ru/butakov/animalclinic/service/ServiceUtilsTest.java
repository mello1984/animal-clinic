package ru.butakov.animalclinic.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.butakov.animalclinic.exceptions.AnimalApiBadRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
class ServiceUtilsTest {
    @Autowired
    private ServiceUtils serviceUtils;
    @Mock
    private FindService<Object> findService;

    @Test
    void checkExistsSuchNameOrThrowException() {
        String name = "object";
        Mockito.when(findService.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceUtils.checkExistsSuchNameOrThrow(findService, Object.class, name))
                .isInstanceOf(AnimalApiBadRequest.class)
                .hasMessage(String.format("Object with such name <%s> not exists", name));

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
    void checkNotExistsSuchNameOrThrowException() {
        String name = "object";
        Object expected = new Object();
        Mockito.when(findService.findByName(name)).thenReturn(Optional.of(expected));

        assertThatThrownBy(() -> serviceUtils.checkNotExistsSuchNameOrThrow(findService, Object.class, name))
                .isInstanceOf(AnimalApiBadRequest.class)
                .hasMessage(String.format("Object with such name <%s> already exists", name));

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
}