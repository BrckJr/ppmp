package io.github.brckjr.ppmp.persistence.repository;

import com.speedment.jpastreamer.application.JPAStreamer;
import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.repository.BaseRepository;
import io.github.brckjr.ppmp.persistence.entity.BaseEntity;
import io.github.brckjr.ppmp.persistence.mapper.BaseMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BaseRepositoryImpl<D extends BaseModel, E extends BaseEntity>
        implements BaseRepository<D> {

    protected BaseMapper<D, E> mapper;
    protected Class<E> entityClass;

    @Inject
    protected JPAStreamer jpaStreamer;

    @Inject
    protected EntityManager entityManager;

    public BaseRepositoryImpl(BaseMapper<D, E> mapper, Class<E> entityClass) {
        this.mapper = mapper;
        this.entityClass = entityClass;
    }

    public BaseRepositoryImpl() {
        // required for proxying
    }

    @Override
    public Optional<D> findById(UUID id) {
        return jpaStreamer.stream(entityClass)
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .map(mapper::toModel);
    }

    @Override
    public List<D> findAll() {
        return jpaStreamer.stream(entityClass)
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public D create(D model) {
        E entity = mapper.toEntity(model);
        entityManager.persist(entity);
        entityManager.flush();
        return mapper.toModel(entity);
    }

    @Override
    @Transactional
    public D update(UUID id, D model) {
        E entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new IllegalArgumentException("Entity not found with id: " + id);
        }
        mapper.updateEntityFromModel(model, entity);
        entityManager.merge(entity);
        entityManager.flush();
        return mapper.toModel(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        E entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new IllegalArgumentException("Entity not found with id: " + id);
        }
        entityManager.remove(entity);
    }

    @Override
    public long count() {
        return jpaStreamer.stream(entityClass).count();
    }
}