package io.github.brckjr.ppmp.app.dashboard.mapper;

import io.github.brckjr.ppmp.api.dashboard.dto.DashboardDto;
import io.github.brckjr.ppmp.api.performance.dto.EquityPointDto;
import io.github.brckjr.ppmp.api.shared.AllocationSliceDto;
import io.github.brckjr.ppmp.app.shared.AllocationSliceDtoMapper;
import io.github.brckjr.ppmp.common.enums.AssetClass;
import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.Region;
import io.github.brckjr.ppmp.common.enums.Sector;
import io.github.brckjr.ppmp.domain.model.dashboard.DashboardSummary;
import io.github.brckjr.ppmp.domain.model.dashboard.KpiMetrics;
import io.github.brckjr.ppmp.domain.model.shared.EquityPoint;
import io.github.brckjr.ppmp.domain.model.shared.allocation.AssetAllocationSlice;
import io.github.brckjr.ppmp.domain.model.shared.allocation.GeographicAllocationSlice;
import io.github.brckjr.ppmp.domain.model.shared.allocation.SectorAllocationSlice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DashboardDtoMapperTest {

  DashboardDtoMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(DashboardDtoMapper.class);
    inject(mapper, "allocationSliceDtoMapper", Mappers.getMapper(AllocationSliceDtoMapper.class));
  }

  @Test
  @DisplayName("Should map DashboardSummary domain model completely to DashboardDto")
  void shouldMapDashboardSummaryToDto() {
    DashboardSummary domainSummary = getDashboardSummary();

    DashboardDto dtoResult = mapper.toDto(domainSummary);

    assertThat(dtoResult).isNotNull();
    assertThat(dtoResult.kpis().totalPortfolioValue()).isEqualByComparingTo("10000.00");
    assertThat(dtoResult.kpis().totalPortfolioReturnPct()).isEqualByComparingTo("5.00");
    assertThat(dtoResult.kpis().dailyTotalPortfolioPL()).isEqualByComparingTo("50.00");
    assertThat(dtoResult.kpis().dailyTotalPortfolioPLPct()).isEqualByComparingTo("1.45");
    assertThat(dtoResult.kpis().totalPortfolioGain()).isEqualByComparingTo("1200.00");
    assertThat(dtoResult.kpis().totalPortfolioAnnualizedReturn()).isEqualByComparingTo("12.50");
    assertThat(dtoResult.kpis().totalPortfolioCashPosition()).isEqualByComparingTo("1000.00");
    assertThat(dtoResult.kpis().totalPortfolioInitialInvested()).isEqualByComparingTo("9000.00");

    assertThat(dtoResult.portfolioValueCurve())
        .containsExactly(
            new EquityPointDto(
                LocalDate.of(2026, 1, 15),
                new BigDecimal("9500.00"),
                Currency.USD
            ),
            new EquityPointDto(
                LocalDate.of(2026, 2, 15),
                new BigDecimal("10500.00"),
                Currency.USD
            )
        );

    assertThat(dtoResult.allocationByAssetClass())
        .containsExactly(
            new AllocationSliceDto("STOCKS", new BigDecimal("95.00")),
            new AllocationSliceDto("CASH", new BigDecimal("5.00"))
        );

    assertThat(dtoResult.allocationBySector())
        .containsExactly(
            new AllocationSliceDto("TECHNOLOGY", new BigDecimal("70.00")),
            new AllocationSliceDto("FINANCIALS", new BigDecimal("30.00"))
        );

    assertThat(dtoResult.allocationByRegion())
        .containsExactly(
            new AllocationSliceDto("US", new BigDecimal("80.00")),
            new AllocationSliceDto("EUROPE", new BigDecimal("20.00"))
        );
  }

  @Test
  @DisplayName("Should return null safely when mapping null source summary")
  void shouldMapNullSafely() {
    DashboardDto dtoResult = mapper.toDto(null);

    assertThat(dtoResult).isNull();
  }

  private static DashboardSummary getDashboardSummary() {
    KpiMetrics kpis = new KpiMetrics(
        new BigDecimal("10000.00"),
        new BigDecimal("5.00"),
        new BigDecimal("50.00"),
        new BigDecimal("1.45"),
        new BigDecimal("1200.00"),
        new BigDecimal("12.50"),
        new BigDecimal("1000.00"),
        new BigDecimal("9000.00")
    );

    List<EquityPoint> portfolioValueCurve = List.of(
        new EquityPoint(
            LocalDate.of(2026, 1, 15),
            new BigDecimal("9500.00"),
            Currency.USD
        ),
        new EquityPoint(
            LocalDate.of(2026, 2, 15),
            new BigDecimal("10500.00"),
            Currency.USD
        )
    );

    List<AssetAllocationSlice> assetAllocationSlices = List.of(
        new AssetAllocationSlice(AssetClass.STOCKS, new BigDecimal("95.00")),
        new AssetAllocationSlice(AssetClass.CASH, new BigDecimal("5.00"))
    );

    List<SectorAllocationSlice> sectorAllocationSlices = List.of(
        new SectorAllocationSlice(Sector.TECHNOLOGY, new BigDecimal("70.00")),
        new SectorAllocationSlice(Sector.FINANCIALS, new BigDecimal("30.00"))
    );

    List<GeographicAllocationSlice> geographicAllocationSlices = List.of(
        new GeographicAllocationSlice(Region.US, new BigDecimal("80.00")),
        new GeographicAllocationSlice(Region.EUROPE, new BigDecimal("20.00"))
    );

    return new DashboardSummary(
        kpis,
        portfolioValueCurve,
        assetAllocationSlices,
        sectorAllocationSlices,
        geographicAllocationSlices
    );
  }


  private static void inject(Object target, String fieldName, Object value) {
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (ReflectiveOperationException exception) {
      throw new IllegalStateException("Failed to wire mapper dependency: " + fieldName, exception);
    }
  }
}
