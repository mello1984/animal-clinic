package ru.butakov.animalclinic.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.butakov.animalclinic.domain.dto.KindDto;
import ru.butakov.animalclinic.service.KindService;

import java.util.List;

@RestController
@RequestMapping("/api/kinds")
public class KindController {
    @Autowired
    KindService kindService;

    @GetMapping
    ResponseEntity<List<KindDto>> getAnimalTypes() {
        List<KindDto> kindDtos = kindService.findAllDto();
        return new ResponseEntity<>(kindDtos, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<KindDto> add(@RequestParam("name") String name) {
        KindDto kindDto = kindService.addAndReturnDto(name);
        return new ResponseEntity<>(kindDto, HttpStatus.OK);
    }

}
