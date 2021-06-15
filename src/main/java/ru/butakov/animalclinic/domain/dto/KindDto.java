package ru.butakov.animalclinic.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.butakov.animalclinic.domain.Breed;
import ru.butakov.animalclinic.domain.Kind;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor
public class KindDto {
    long id;
    String name;
    Set<String> breeds;

    public KindDto(Kind kind) {
        this.id = kind.getId();
        this.name = kind.getName();
        this.breeds = kind.getBreeds().stream().map(Breed::getName).collect(Collectors.toUnmodifiableSet());
    }
}
