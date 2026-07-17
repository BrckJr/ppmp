package io.github.brckjr.ppmp.domain.model.holding;

import java.util.List;

public record Holdings(
    List<HoldingDetail> holdings
) {
}
