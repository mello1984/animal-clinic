package ru.butakov.animalclinic.restcontrollers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        List<BreedDto> breedDtos = breedService.findAllDto();
        return new ResponseEntity<>(breedDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BreedDto> add(@RequestParam("name") String name, @RequestParam("kind") String kindName) {
        BreedDto breedDto = breedService.addAndReturnDto(name, kindName);
        return new ResponseEntity<>(breedDto, HttpStatus.OK);
    }

}
