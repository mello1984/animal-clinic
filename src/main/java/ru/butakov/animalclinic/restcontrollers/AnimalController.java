package ru.butakov.animalclinic.restcontrollers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.butakov.animalclinic.domain.dto.AnimalDto;
import ru.butakov.animalclinic.service.AnimalService;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimalController {
    @Autowired
    AnimalService animalService;

    @GetMapping
    public ResponseEntity<List<AnimalDto>> getAnimals() {
        return ResponseEntity.ok(animalService.findAllDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> getAnimal(@PathVariable("id") long id) {
        AnimalDto animalDto = animalService.getAnimalDtoById(id);
        return ResponseEntity.ok(animalDto);
    }

    @PostMapping
    public ResponseEntity<AnimalDto> add(@RequestParam() String name) {
        AnimalDto animalDto = animalService.addAndReturnDto(name);
        return ResponseEntity.ok(animalDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnimalDto> update(@PathVariable("id") long id, @RequestBody AnimalDto update) {
        AnimalDto result = animalService.update(id, update);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AnimalDto> delete(@PathVariable("id") long id) {
        animalService.delete(id);
        return ResponseEntity.ok().build();
    }

}
