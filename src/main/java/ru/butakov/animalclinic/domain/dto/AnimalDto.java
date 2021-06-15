package ru.butakov.animalclinic.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.butakov.animalclinic.domain.Animal;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor
public class AnimalDto {
    long id;
    String name;
    LocalDate birthday;
    String kind;
    String breed;
    String contactPerson;

    public AnimalDto(Animal animal) {
        this.id = animal.getId();
        this.name = animal.getName();
        this.birthday = animal.getBirthday();
        this.kind = animal.getKind() != null ? animal.getKind().getName() : "";
        this.breed = animal.getBreed() != null ? animal.getBreed().getName() : "";
        this.contactPerson = animal.getContactPerson() != null ? animal.getContactPerson().getName() : "";
    }
}
