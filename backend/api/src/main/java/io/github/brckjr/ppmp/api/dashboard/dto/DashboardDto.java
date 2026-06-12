package io.github.brckjr.ppmp.api.dashboard.dto;

import io.github.brckjr.ppmp.api.holding.dto.HoldingDto;
import io.github.brckjr.ppmp.api.risk.dto.RiskMetricsDto;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistItemDto;

import java.util.List;

public record DashboardDto(
        PortfolioOverviewDto overview,
        List<HoldingDto> topHoldings,
        List<TransactionDto> recentTransactions,
        List<WatchlistItemDto> watchlist,
        RiskMetricsDto risk
) {}
