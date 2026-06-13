import { Link, useParams } from "react-router";
import { useState, useEffect } from "react";
import { ArrowLeft } from "lucide-react";
import { LineChart, Line, ResponsiveContainer, XAxis, YAxis, Tooltip, CartesianGrid, BarChart, Bar, Legend } from "recharts";
import { PageHeader } from "../components/PageHeader";
import { KpiCard } from "../components/KpiCard";
import { Badge } from "../components/ui/badge";
import { formatCurrencyPrecise, formatPercent, fundamentals, getHolding, holdingGain, holdingGainPct, holdingValue, priceHistory } from "../lib/portfolio-data";

// 1. Clean, inline Not Found fallback component
function NotFoundComponent() {
  return (
    <div className="mx-auto max-w-7xl pt-6">
      <PageHeader title="Holding not found" />
      <Link to="/holdings" className="text-accent hover:underline">← Back to holdings</Link>
    </div>
  );
}

const RANGES = ["1D", "5D", "1M", "6M", "1Y", "5Y"] as const;
const RANGE_DAYS: Record<(typeof RANGES)[number], number> = { "1D": 1, "5D": 5, "1M": 30, "6M": 180, "1Y": 365, "5Y": 1825 };

export default function HoldingDetailPage() {
  // 2. Extract the dynamic segment matching :ticker from our React Router route tree
  const { ticker } = useParams<{ ticker: string }>();
  
  const [range, setRange] = useState<(typeof RANGES)[number]>("1M");

  // Validate that the parameter exists and resolves to valid data
  const h = ticker ? getHolding(ticker) : null;

  // 3. Document dynamic metadata title injection hook
  useEffect(() => {
    if (h) {
      document.title = `${h.ticker} — Meridian`;
    }
  }, [h]);

  // Guard routing context explicitly inside the functional component lifecycle
  if (!h || !ticker) {
    return <NotFoundComponent />;
  }

  const prices = priceHistory(ticker, RANGE_DAYS[range]);
  const f = fundamentals(ticker);
  const gain = holdingGain(h);
  const gainPct = holdingGainPct(h);

  return (
    <div className="mx-auto max-w-7xl">
      <Link to="/holdings" className="mb-4 inline-flex items-center gap-1 text-sm text-muted-foreground hover:text-foreground">
        <ArrowLeft className="h-3.5 w-3.5" /> Holdings
      </Link>
      <PageHeader
        title={`${h.ticker} — ${h.name}`}
        subtitle={`${h.assetClass} · ${h.sector} · ${h.region}`}
        actions={<Badge variant="secondary">{h.shares} shares @ avg {formatCurrencyPrecise(h.avgCost)}</Badge>}
      />

      <div className="grid grid-cols-2 gap-4 md:grid-cols-4 lg:grid-cols-4">
        <KpiCard label="Price" value={formatCurrencyPrecise(h.price)} />
        <KpiCard label="Market Value" value={formatCurrencyPrecise(holdingValue(h))} />
        <KpiCard label="Unrealized" value={formatCurrencyPrecise(gain)} delta={formatPercent(gainPct)} trend={gain >= 0 ? "up" : "down"} />
        <KpiCard label="Market Cap" value={h.marketCap ? `€${h.marketCap.toFixed(0)}B` : "—"} />
      </div>

      <div className="mt-6 rounded-lg border bg-card p-5">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <h2 className="font-display text-base font-semibold">Price History</h2>
          <div className="flex rounded-md border bg-background p-0.5">
            {RANGES.map((r) => (
              <button key={r} onClick={() => setRange(r)} className={`rounded px-2.5 py-1 text-xs font-medium ${range === r ? "bg-primary text-primary-foreground" : "text-muted-foreground hover:text-foreground"}`}>{r}</button>
            ))}
          </div>
        </div>
        <div className="mt-4 h-72">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={prices}>
              <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.9 0.015 245)" vertical={false} />
              <XAxis dataKey="date" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" minTickGap={30} />
              <YAxis tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" domain={["auto", "auto"]} tickFormatter={(v) => `€${v.toFixed(0)}`} />
              <Tooltip contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} formatter={(v: number) => formatCurrencyPrecise(v)} />
              <Line type="monotone" dataKey="price" stroke="oklch(0.28 0.08 260)" strokeWidth={2} dot={false} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="mt-6 grid gap-4 lg:grid-cols-2">
        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Key Stats</h2>
          <dl className="mt-4 grid grid-cols-2 gap-x-6 gap-y-3 text-sm">
            <Stat label="P/E" value={h.pe ? h.pe.toFixed(1) : "—"} />
            <Stat label="Forward P/E" value={h.forwardPe ? h.forwardPe.toFixed(1) : "—"} />
            <Stat label="EPS" value={h.eps ? `€${h.eps.toFixed(2)}` : "—"} />
            <Stat label="Revenue Growth" value={h.revenueGrowth ? `${h.revenueGrowth.toFixed(1)}%` : "—"} />
            <Stat label="ROE" value={h.roe ? `${h.roe.toFixed(1)}%` : "—"} />
            <Stat label="Debt / Equity" value={h.debtEquity ? h.debtEquity.toFixed(2) : "—"} />
            <Stat label="Dividend Yield" value={`${h.dividendYield.toFixed(2)}%`} />
            <Stat label="Market Cap" value={h.marketCap ? `€${h.marketCap.toFixed(0)}B` : "—"} />
          </dl>
        </div>

        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Fundamentals</h2>
          <p className="text-xs text-muted-foreground">5-year trend (€M)</p>
          <div className="mt-4 h-64">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={f}>
                <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.9 0.015 245)" vertical={false} />
                <XAxis dataKey="year" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" />
                <YAxis tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" />
                <Tooltip contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} />
                <Legend wrapperStyle={{ fontSize: 11 }} />
                <Bar dataKey="revenue" name="Revenue" fill="oklch(0.28 0.08 260)" radius={[4, 4, 0, 0]} />
                <Bar dataKey="netIncome" name="Net Income" fill="oklch(0.58 0.12 245)" radius={[4, 4, 0, 0]} />
                <Bar dataKey="fcf" name="FCF" fill="oklch(0.72 0.1 235)" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}

function Stat({ label, value }: { label: string; value: string }) {
  return (
    <>
      <dt className="text-muted-foreground">{label}</dt>
      <dd className="text-right font-medium tabular-nums">{value}</dd>
    </>
  );
}