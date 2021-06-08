package ru.butakov.animalclinic.domain.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.butakov.animalclinic.domain.Breed;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@EqualsAndHashCode
public class BreedDto {
    long id;
    String name;
    String kind;

    public BreedDto(Breed breed) {
        this.id = breed.getId();
        this.name = breed.getName();
        this.kind = breed.getKind().getName();
    }

    public BreedDto(long id, String name, String kind) {
        this.id = id;
        this.name = name;
        this.kind = kind;
    }
}
