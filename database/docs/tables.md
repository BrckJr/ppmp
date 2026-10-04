# Tables

| Name | Description                                                                                                                                                                                    |
|------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| portfolio | Main container for holdings which include transactions                                                                                                                                         |
| transaction | Store every portfolio event, owned by exactly one user (`user_uuid`); From here, one can derive holdings, average buy-in, P/L, cash balance, etc.                                                                                       |
| user | Basic metadata about the users and their login (`password_hash`, bcrypt; empty for users of an external identity provider)                                                                     |
| instrument | Securities master table with one row per investable instrument: stock, ETF, bond, crypto, cash-like product, etc. Some transactions might not need an instrument like deposit, withdrawal, ... |
 | instrument_prices | Price history of each instrument, including daily open, low, high and closing prices as well as adjusted closing prices                                                                        | 

Benchmark Indices are marked as special instruments and their prices are stored in the instrument_prices table.

Potential extensions for faster dashboard experience:

`position_snapshot`:
- portfolio_id
- instrument_id
- snapshot_date
- quantity
- avg_buy_price
- market_price
- market_value
- unrealized_pl
- unrealized_pl_pct

Stores daily or hourly portfolio-level metrics. This table supports your dashboard and performance chart.

`portfolio_snapshot`:
- portfolio_id
- snapshot_date
- portfolio_id
- snapshot_date
- portfolio_value
- cash_value
- invested_capital
- daily_pl
- total_return
- annualized_return

This is the fastest way to render the holdings page and historical exposures.

`allocation_snapshot`:
- portfolio_id
- snapshot_date
- allocation_type (asset_class, sector, industry, country)
- bucket_name
- weight
- market_value

Stores daily allocation breakdowns.

`risk_metrics_snapshot`:
- portfolio_id
- snapshot_date
- volatility_annualized
- sharpe_ratio
- max_drawdown
- cagr
- risk_score
- value_at_risk
- beta
- tracking_error