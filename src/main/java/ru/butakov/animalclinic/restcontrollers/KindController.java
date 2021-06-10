package ru.butakov.animalclinic.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
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
    ResponseEntity<List<KindDto>> getKinds() {
        List<KindDto> kindDtos = kindService.findAllDto();
        return ResponseEntity.ok(kindDtos);
    }

    @GetMapping("/{id}")
    ResponseEntity<KindDto> getKind(@PathVariable("id") long id) {
        KindDto kindDto = kindService.getKindDtoById(id);
        return ResponseEntity.ok(kindDto);
    }

    @PostMapping
    ResponseEntity<KindDto> add(@RequestParam("name") String name) {
        KindDto kindDto = kindService.addAndReturnDto(name);
        return ResponseEntity.ok(kindDto);
    }

    @PatchMapping("/{id}")
    ResponseEntity<KindDto> update(@PathVariable("id") long id, @RequestBody KindDto update) {
        KindDto result = kindService.update(id, update);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<KindDto> delete(@PathVariable("id") long id) {
        kindService.delete(id);
        return ResponseEntity.ok().build();
    }

}
