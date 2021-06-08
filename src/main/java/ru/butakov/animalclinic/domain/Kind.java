package ru.butakov.animalclinic.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "kinds")
@RequiredArgsConstructor
public class Kind implements HasName{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NonNull
    String name;

    @OneToMany (mappedBy = "kind")
    Set<Breed> breeds = new HashSet<>();

    @OneToMany(mappedBy = "kind", cascade = CascadeType.ALL)
    Set<Animal> animals = new HashSet<>();


}
