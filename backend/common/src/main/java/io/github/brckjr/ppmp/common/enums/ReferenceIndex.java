package io.github.brckjr.ppmp.common.enums;

public enum ReferenceIndex {
  SP_500("S&P 500"),
  MSCI_WORLD("MSCI World"),
  NASDAQ_100("NASDAQ 100");

  private final String displayName;

  ReferenceIndex(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return this.displayName;
  }
}
