package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.persistence.entity.BaseEntity;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

public interface BaseMapper<D extends BaseModel, E extends BaseEntity> {

    D toModel(E entity);

    E toEntity(D model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromModel(D model, @MappingTarget E entity);

    default <T> T unwrapOptional(Optional<T> optional) {
        return optional.orElse(null);
    }

}