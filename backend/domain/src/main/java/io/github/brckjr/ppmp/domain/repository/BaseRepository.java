package io.github.brckjr.ppmp.domain.repository;


import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseRepository<D extends BaseModel> {

  Optional<D> findById(UUID id);

  List<D> findAll();

  D persist(D dto);

  D update(UUID uuid, D dto);

  void deleteById(UUID id);

  long count();
}
