package ru.butakov.animalclinic.restcontrollers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.butakov.animalclinic.domain.dto.BreedDto;
import ru.butakov.animalclinic.service.BreedService;

import java.util.List;

@RestController
@RequestMapping("/api/breeds")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BreedController {
    @Autowired
    BreedService breedService;

    @GetMapping
    public ResponseEntity<List<BreedDto>> getBreeds() {
        List<BreedDto> breedDtos = breedService.getAllDto();
        return ResponseEntity.ok(breedDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BreedDto> getBreed(@PathVariable("id") long id) {
        return ResponseEntity.ok(breedService.getBreedDtoById(id));
    }

    @PostMapping
    public ResponseEntity<BreedDto> add(@RequestParam("name") String name, @RequestParam("kind") String kindName) {
        return ResponseEntity.ok(breedService.addAndReturnDto(name, kindName));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BreedDto> update(@PathVariable("id") long id, @RequestBody BreedDto breedDto){
        BreedDto result = breedService.update(id, breedDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BreedDto> delete(@PathVariable("id") long id){
        breedService.delete(id);
        return ResponseEntity.ok().build();
    }
}
