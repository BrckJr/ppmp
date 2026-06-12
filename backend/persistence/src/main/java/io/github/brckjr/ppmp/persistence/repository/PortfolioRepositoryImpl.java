package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.portfolio.Portfolio;
import io.github.brckjr.ppmp.domain.repository.PortfolioRepository;
import io.github.brckjr.ppmp.persistence.entity.PortfolioEntity;
import io.github.brckjr.ppmp.persistence.mapper.PortfolioMapper;
import jakarta.inject.Inject;

public class PortfolioRepositoryImpl extends BaseRepositoryImpl<Portfolio, PortfolioEntity> implements PortfolioRepository {

    private final PortfolioMapper mapper;

    @Inject
    public PortfolioRepositoryImpl(PortfolioMapper mapper) {
        super(mapper, PortfolioEntity.class);
        this.mapper = mapper;
    }
}
