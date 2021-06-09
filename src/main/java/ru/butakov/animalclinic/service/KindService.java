package ru.butakov.animalclinic.service;

import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.KindDto;

import java.util.List;

public interface KindService extends FindService<Kind> {

    Kind save(Kind kind);

    KindDto addAndReturnDto(String name);

    List<KindDto> findAllDto();

    void delete(long id);

    KindDto getKindDtoById(long id);

    KindDto update(long id, KindDto kindDto);
}
