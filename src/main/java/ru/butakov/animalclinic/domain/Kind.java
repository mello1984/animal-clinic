package ru.butakov.animalclinic.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "kinds")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
public class Kind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @EqualsAndHashCode.Include
    long id;

    @NonNull
    String name;

    @OneToMany(mappedBy = "kind")
    @Builder.Default
    Set<Breed> breeds = new HashSet<>();

    @OneToMany(mappedBy = "kind", cascade = CascadeType.ALL)
    @Builder.Default
    Set<Animal> animals = new HashSet<>();
}
