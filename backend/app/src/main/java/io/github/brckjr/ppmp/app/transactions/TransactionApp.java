package io.github.brckjr.ppmp.app.transactions;

import io.github.brckjr.ppmp.api.transactions.TransactionApi;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionsDto;
import io.github.brckjr.ppmp.app.transactions.mapper.TransactionsDtoMapper;
import io.github.brckjr.ppmp.domain.service.transactions.TransactionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class TransactionApp implements TransactionApi {

    private final TransactionService service;
    private final TransactionsDtoMapper mapper = Mappers.getMapper(TransactionsDtoMapper.class);

    @Inject
    public TransactionApp(TransactionService service) {
        this.service = service;
    }

    @Override
    public TransactionsDto getTransactions() {
        return mapper.toDto(service.getTransactions());
    }
}
