package io.github.brckjr.ppmp.domain.service.transactions;

import io.github.brckjr.ppmp.domain.enums.Currency;
import io.github.brckjr.ppmp.domain.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.transaction.TransactionDetail;
import io.github.brckjr.ppmp.domain.model.transaction.Transactions;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TransactionService {

    public Transactions getTransactions() {
        return new Transactions(
                List.of(new TransactionDetail(UUID.randomUUID(), LocalDate.now(), TransactionType.BUY, "UNKNOWN", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, Currency.USD)),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }
}
