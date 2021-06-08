package ru.butakov.animalclinic.restcontrollers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.butakov.animalclinic.dao.AnimalRepository;
import ru.butakov.animalclinic.domain.Animal;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimalController {
    @Autowired
    AnimalRepository animalRepository;

    @GetMapping
    public ResponseEntity<List<Animal>> getAnimals() {
        return new ResponseEntity<>(animalRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Animal> add(@RequestParam() String name) {
        Animal animal = new Animal();
        animal.setName(name);

        animal = animalRepository.save(animal);
        return new ResponseEntity<>(animal, HttpStatus.OK);

    }

}
