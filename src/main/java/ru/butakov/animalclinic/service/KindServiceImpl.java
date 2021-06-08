package ru.butakov.animalclinic.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.animalclinic.dao.KindRepository;
import ru.butakov.animalclinic.domain.Kind;
import ru.butakov.animalclinic.domain.dto.KindDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KindServiceImpl implements KindService {
    @Autowired
    KindRepository kindRepository;
    @Autowired
    ServiceUtils serviceUtils;

    @Override
    public List<Kind> findAll() {
        return kindRepository.findAll();
    }

    @Override
    public Optional<Kind> findById(long id) {
        return kindRepository.findById(id);
    }

    @Override
    public Optional<Kind> findByName(String name) {
        return kindRepository.findByName(name);
    }

    @Override
    public Kind save(Kind kind) {
        return kindRepository.save(kind);
    }

    @Override
    public KindDto addAndReturnDto(String name) {
        serviceUtils.checkNotExistsSuchNameOrThrow(this, Kind.class, name);
        Kind kind = new Kind();
        kind.setName(name);
        kind = kindRepository.save(kind);
        return new KindDto(kind);
    }

    @Override
    public List<KindDto> findAllDto() {
        return kindRepository.findAll().stream().map(KindDto::new).collect(Collectors.toList());
    }
}
