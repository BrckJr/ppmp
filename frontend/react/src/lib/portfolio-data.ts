// Mock portfolio data for the dashboard. Numbers chosen to look realistic.

export type AssetClass = "Stocks" | "ETFs" | "Bonds" | "Cash" | "Crypto";
export type Sector =
  | "Technology"
  | "Healthcare"
  | "Industrials"
  | "Financials"
  | "Energy"
  | "Consumer";
export type Region = "US" | "Europe" | "Asia" | "Emerging Markets";

export interface Holding {
  ticker: string;
  name: string;
  shares: number;
  avgCost: number;
  price: number;
  assetClass: AssetClass;
  sector: Sector;
  region: Region;
  marketCap: number; // in billions
  pe: number;
  forwardPe: number;
  eps: number;
  revenueGrowth: number; // %
  roe: number; // %
  debtEquity: number;
  dividendYield: number; // %
}

export const holdings: Holding[] = [
  { ticker: "AAPL", name: "Apple Inc.", shares: 45, avgCost: 142, price: 198.42, assetClass: "Stocks", sector: "Technology", region: "US", marketCap: 3040, pe: 32.1, forwardPe: 28.4, eps: 6.18, revenueGrowth: 5.4, roe: 156.1, debtEquity: 1.95, dividendYield: 0.48 },
  { ticker: "MSFT", name: "Microsoft Corp.", shares: 28, avgCost: 285, price: 421.13, assetClass: "Stocks", sector: "Technology", region: "US", marketCap: 3130, pe: 36.5, forwardPe: 31.2, eps: 11.54, revenueGrowth: 15.7, roe: 39.1, debtEquity: 0.31, dividendYield: 0.72 },
  { ticker: "NVDA", name: "NVIDIA Corp.", shares: 22, avgCost: 412, price: 875.4, assetClass: "Stocks", sector: "Technology", region: "US", marketCap: 2160, pe: 71.2, forwardPe: 38.5, eps: 12.29, revenueGrowth: 122.4, roe: 91.5, debtEquity: 0.22, dividendYield: 0.02 },
  { ticker: "GOOGL", name: "Alphabet Inc.", shares: 35, avgCost: 124, price: 175.6, assetClass: "Stocks", sector: "Technology", region: "US", marketCap: 2180, pe: 27.4, forwardPe: 22.1, eps: 6.4, revenueGrowth: 13.6, roe: 30.9, debtEquity: 0.1, dividendYield: 0.45 },
  { ticker: "JPM", name: "JPMorgan Chase", shares: 30, avgCost: 148, price: 202.55, assetClass: "Stocks", sector: "Financials", region: "US", marketCap: 580, pe: 12.1, forwardPe: 11.5, eps: 16.74, revenueGrowth: 8.2, roe: 17.0, debtEquity: 1.21, dividendYield: 2.25 },
  { ticker: "JNJ", name: "Johnson & Johnson", shares: 40, avgCost: 162, price: 154.32, assetClass: "Stocks", sector: "Healthcare", region: "US", marketCap: 380, pe: 24.6, forwardPe: 14.8, eps: 6.27, revenueGrowth: 4.1, roe: 23.4, debtEquity: 0.42, dividendYield: 3.21 },
  { ticker: "ASML", name: "ASML Holding", shares: 12, avgCost: 620, price: 945.2, assetClass: "Stocks", sector: "Technology", region: "Europe", marketCap: 380, pe: 38.4, forwardPe: 32.1, eps: 24.6, revenueGrowth: 30.2, roe: 70.5, debtEquity: 0.39, dividendYield: 0.65 },
  { ticker: "NESN.SW", name: "Nestlé SA", shares: 50, avgCost: 102, price: 96.4, assetClass: "Stocks", sector: "Consumer", region: "Europe", marketCap: 285, pe: 21.8, forwardPe: 18.6, eps: 4.42, revenueGrowth: 2.1, roe: 31.7, debtEquity: 0.78, dividendYield: 3.15 },
  { ticker: "VWCE", name: "Vanguard FTSE All-World", shares: 180, avgCost: 102, price: 122.18, assetClass: "ETFs", sector: "Technology", region: "US", marketCap: 0, pe: 0, forwardPe: 0, eps: 0, revenueGrowth: 0, roe: 0, debtEquity: 0, dividendYield: 1.85 },
  { ticker: "IUSQ", name: "iShares MSCI World", shares: 220, avgCost: 75, price: 88.45, assetClass: "ETFs", sector: "Industrials", region: "US", marketCap: 0, pe: 0, forwardPe: 0, eps: 0, revenueGrowth: 0, roe: 0, debtEquity: 0, dividendYield: 1.6 },
  { ticker: "EUNL", name: "iShares Core MSCI World", shares: 95, avgCost: 78, price: 92.6, assetClass: "ETFs", sector: "Industrials", region: "Europe", marketCap: 0, pe: 0, forwardPe: 0, eps: 0, revenueGrowth: 0, roe: 0, debtEquity: 0, dividendYield: 1.55 },
  { ticker: "VFEM", name: "Vanguard EM ETF", shares: 140, avgCost: 52, price: 58.92, assetClass: "ETFs", sector: "Financials", region: "Emerging Markets", marketCap: 0, pe: 0, forwardPe: 0, eps: 0, revenueGrowth: 0, roe: 0, debtEquity: 0, dividendYield: 2.4 },
  { ticker: "TSM", name: "Taiwan Semi", shares: 18, avgCost: 95, price: 174.8, assetClass: "Stocks", sector: "Technology", region: "Asia", marketCap: 905, pe: 30.2, forwardPe: 24.1, eps: 5.79, revenueGrowth: 36.6, roe: 26.2, debtEquity: 0.24, dividendYield: 1.42 },
  { ticker: "XOM", name: "Exxon Mobil", shares: 25, avgCost: 105, price: 118.3, assetClass: "Stocks", sector: "Energy", region: "US", marketCap: 470, pe: 13.8, forwardPe: 12.4, eps: 8.57, revenueGrowth: -3.2, roe: 17.1, debtEquity: 0.18, dividendYield: 3.32 },
  { ticker: "BTC", name: "Bitcoin", shares: 0.18, avgCost: 42000, price: 68500, assetClass: "Crypto", sector: "Technology", region: "US", marketCap: 1350, pe: 0, forwardPe: 0, eps: 0, revenueGrowth: 0, roe: 0, debtEquity: 0, dividendYield: 0 },
  { ticker: "AGG", name: "iShares US Aggregate Bond", shares: 60, avgCost: 99, price: 98.4, assetClass: "Bonds", sector: "Financials", region: "US", marketCap: 0, pe: 0, forwardPe: 0, eps: 0, revenueGrowth: 0, roe: 0, debtEquity: 0, dividendYield: 4.18 },
];

export const cashPosition = 4300;

export function holdingValue(h: Holding) { return h.shares * h.price; }
export function holdingCost(h: Holding) { return h.shares * h.avgCost; }
export function holdingGain(h: Holding) { return holdingValue(h) - holdingCost(h); }
export function holdingGainPct(h: Holding) {
  const cost = holdingCost(h);
  return cost > 0 ? (holdingGain(h) / cost) * 100 : 0;
}

export const totalInvested = holdings.reduce((s, h) => s + holdingCost(h), 0);
export const totalValue = holdings.reduce((s, h) => s + holdingValue(h), 0) + cashPosition;
export const totalGain = holdings.reduce((s, h) => s + holdingGain(h), 0);
export const totalReturnPct = (totalGain / (totalValue - totalGain)) * 100;
export const dailyPL = 850.42;
export const dailyPLPct = (dailyPL / totalValue) * 100;
export const annualizedReturn = 11.8;

export function getHolding(ticker: string) {
  return holdings.find((h) => h.ticker.toLowerCase() === ticker.toLowerCase());
}

// Aggregations
function aggregateBy<K extends string>(getKey: (h: Holding) => K) {
  const map = new Map<K, number>();
  for (const h of holdings) {
    map.set(getKey(h), (map.get(getKey(h)) ?? 0) + holdingValue(h));
  }
  return Array.from(map.entries()).map(([name, value]) => ({ name, value }));
}

export const allocationByAssetClass = [
  ...aggregateBy((h) => h.assetClass),
  { name: "Cash" as AssetClass, value: cashPosition },
].reduce<{ name: AssetClass; value: number }[]>((acc, item) => {
  const found = acc.find((a) => a.name === item.name);
  if (found) found.value += item.value;
  else acc.push(item);
  return acc;
}, []);

export const allocationBySector = aggregateBy((h) => h.sector);
export const allocationByRegion = aggregateBy((h) => h.region);

// Equity curve (24 months back, deterministic)
export function generateEquityCurve(months: number, start: number, end: number, seed = 7) {
  const points: { date: string; value: number; benchmark: number }[] = [];
  const now = new Date();
  let rng = seed;
  const rand = () => {
    rng = (rng * 9301 + 49297) % 233280;
    return rng / 233280;
  };
  for (let i = months; i >= 0; i--) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    const t = (months - i) / months;
    const drift = start + (end - start) * t;
    const noise = (rand() - 0.5) * (end - start) * 0.06;
    const value = Math.round(drift + noise);
    const benchmark = Math.round(start + (end - start) * 0.78 * t + (rand() - 0.5) * (end - start) * 0.05);
    points.push({
      date: d.toLocaleDateString("en-US", { month: "short", year: "2-digit" }),
      value,
      benchmark,
    });
  }
  return points;
}

export const equityCurve = generateEquityCurve(24, 100000, Math.round(totalValue));

export function rangeFilter(
  data: { date: string; value: number; benchmark: number }[],
  range: "1M" | "3M" | "6M" | "YTD" | "1Y" | "5Y" | "Max",
) {
  const map: Record<typeof range, number> = { "1M": 1, "3M": 3, "6M": 6, YTD: new Date().getMonth() + 1, "1Y": 12, "5Y": 60, Max: data.length } as const;
  const n = Math.min(map[range], data.length);
  return data.slice(-n);
}

// Risk metrics (computed off the equity curve)
function returns(values: number[]) {
  const r: number[] = [];
  for (let i = 1; i < values.length; i++) r.push((values[i] - values[i - 1]) / values[i - 1]);
  return r;
}
function mean(a: number[]) { return a.reduce((s, x) => s + x, 0) / a.length; }
function std(a: number[]) {
  const m = mean(a);
  return Math.sqrt(a.reduce((s, x) => s + (x - m) ** 2, 0) / (a.length - 1));
}

const vals = equityCurve.map((p) => p.value);
const benchVals = equityCurve.map((p) => p.benchmark);
const rets = returns(vals);
const benchRets = returns(benchVals);

export const portfolioVolatility = std(rets) * Math.sqrt(12) * 100; // annualized %
export const sharpeRatio = (mean(rets) * 12 - 0.02) / (std(rets) * Math.sqrt(12));

let peak = vals[0];
let maxDD = 0;
for (const v of vals) {
  if (v > peak) peak = v;
  maxDD = Math.min(maxDD, (v - peak) / peak);
}
export const maxDrawdown = maxDD * 100;

// Beta
const covar = mean(rets.map((r, i) => (r - mean(rets)) * (benchRets[i] - mean(benchRets))));
const varB = mean(benchRets.map((r) => (r - mean(benchRets)) ** 2));
export const beta = covar / varB;

// Historical VaR (95%)
const sortedRets = [...rets].sort((a, b) => a - b);
const varIdx = Math.floor(sortedRets.length * 0.05);
export const valueAtRisk95 = Math.abs(sortedRets[varIdx]) * 100;

// Drawdown series
export const drawdownSeries = (() => {
  let p = vals[0];
  return equityCurve.map((pt) => {
    p = Math.max(p, pt.value);
    return { date: pt.date, dd: ((pt.value - p) / p) * 100 };
  });
})();

// Rolling 6-month volatility
export const rollingVol = equityCurve.map((pt, i) => {
  if (i < 6) return { date: pt.date, vol: 0 };
  const slice = vals.slice(i - 6, i + 1);
  return { date: pt.date, vol: std(returns(slice)) * Math.sqrt(12) * 100 };
});

// Transactions
export type TxType = "Buy" | "Sell" | "Dividend" | "Deposit" | "Withdrawal";
export interface Transaction {
  id: string;
  date: string;
  type: TxType;
  ticker?: string;
  shares?: number;
  price?: number;
  amount: number;
}

export const transactions: Transaction[] = [
  { id: "t1", date: "2026-05-28", type: "Buy", ticker: "NVDA", shares: 5, price: 862.3, amount: 4311.5 },
  { id: "t2", date: "2026-05-15", type: "Dividend", ticker: "AAPL", amount: 10.8 },
  { id: "t3", date: "2026-05-02", type: "Deposit", amount: 2500 },
  { id: "t4", date: "2026-04-22", type: "Sell", ticker: "XOM", shares: 10, price: 116.4, amount: 1164 },
  { id: "t5", date: "2026-04-10", type: "Buy", ticker: "ASML", shares: 3, price: 920.5, amount: 2761.5 },
  { id: "t6", date: "2026-03-28", type: "Dividend", ticker: "JNJ", amount: 31.4 },
  { id: "t7", date: "2026-03-15", type: "Buy", ticker: "MSFT", shares: 6, price: 408.1, amount: 2448.6 },
  { id: "t8", date: "2026-02-22", type: "Withdrawal", amount: 1000 },
  { id: "t9", date: "2026-02-08", type: "Buy", ticker: "BTC", shares: 0.05, price: 62100, amount: 3105 },
  { id: "t10", date: "2026-01-18", type: "Dividend", ticker: "JPM", amount: 16.9 },
];

// Watchlist
export interface WatchItem {
  ticker: string;
  name: string;
  price: number;
  targetPrice: number;
  analystRating: "Strong Buy" | "Buy" | "Hold" | "Sell";
  pe: number;
  pegRatio: number;
  dividendYield: number;
}
export const watchlist: WatchItem[] = [
  { ticker: "AMZN", name: "Amazon.com", price: 186.4, targetPrice: 215, analystRating: "Strong Buy", pe: 48.2, pegRatio: 1.8, dividendYield: 0 },
  { ticker: "META", name: "Meta Platforms", price: 502.1, targetPrice: 560, analystRating: "Buy", pe: 26.4, pegRatio: 1.2, dividendYield: 0.4 },
  { ticker: "BRK.B", name: "Berkshire Hathaway", price: 412.8, targetPrice: 445, analystRating: "Buy", pe: 9.8, pegRatio: 1.6, dividendYield: 0 },
  { ticker: "V", name: "Visa Inc.", price: 275.4, targetPrice: 305, analystRating: "Strong Buy", pe: 30.1, pegRatio: 2.1, dividendYield: 0.78 },
  { ticker: "DIS", name: "Walt Disney", price: 102.5, targetPrice: 115, analystRating: "Hold", pe: 38.6, pegRatio: 1.4, dividendYield: 0.45 },
  { ticker: "ADBE", name: "Adobe Inc.", price: 482.3, targetPrice: 540, analystRating: "Buy", pe: 41.2, pegRatio: 2.3, dividendYield: 0 },
];

// Price history for a single stock (deterministic)
export function priceHistory(ticker: string, days: number) {
  const h = getHolding(ticker) ?? watchlist.find((w) => w.ticker === ticker);
  const endPrice = h?.price ?? 100;
  const startPrice = endPrice * (0.7 + ((ticker.charCodeAt(0) % 5) * 0.05));
  let rng = ticker.split("").reduce((a, c) => a + c.charCodeAt(0), 0);
  const rand = () => { rng = (rng * 9301 + 49297) % 233280; return rng / 233280; };
  const pts: { date: string; price: number }[] = [];
  for (let i = days; i >= 0; i--) {
    const t = (days - i) / days;
    const drift = startPrice + (endPrice - startPrice) * t;
    const noise = (rand() - 0.5) * endPrice * 0.04;
    const d = new Date();
    d.setDate(d.getDate() - i);
    pts.push({ date: d.toLocaleDateString("en-US", { month: "short", day: "numeric" }), price: Math.max(1, drift + noise) });
  }
  return pts;
}

// Fundamentals series for stock detail
export function fundamentals(ticker: string) {
  const h = getHolding(ticker);
  const base = h?.marketCap ? h.marketCap * 0.25 : 50;
  let rng = ticker.length * 13;
  const rand = () => { rng = (rng * 9301 + 49297) % 233280; return rng / 233280; };
  return Array.from({ length: 5 }, (_, i) => {
    const year = new Date().getFullYear() - 4 + i;
    const growth = 1 + (rand() * 0.2 - 0.02);
    const revenue = base * Math.pow(growth, i + 1);
    return {
      year: String(year),
      revenue: Math.round(revenue),
      netIncome: Math.round(revenue * (0.15 + rand() * 0.1)),
      fcf: Math.round(revenue * (0.12 + rand() * 0.08)),
    };
  });
}

export function formatCurrency(n: number, currency = "EUR") {
  return new Intl.NumberFormat("en-US", { style: "currency", currency, maximumFractionDigits: 0 }).format(n);
}
export function formatCurrencyPrecise(n: number, currency = "EUR") {
  return new Intl.NumberFormat("en-US", { style: "currency", currency, maximumFractionDigits: 2 }).format(n);
}
export function formatPercent(n: number, digits = 2) {
  return `${n >= 0 ? "+" : ""}${n.toFixed(digits)}%`;
}
