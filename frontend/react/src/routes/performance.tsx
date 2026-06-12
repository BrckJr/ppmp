import { useState, useEffect } from "react";
import { LineChart, Line, ResponsiveContainer, XAxis, YAxis, Tooltip, CartesianGrid, AreaChart, Area, Legend } from "recharts";
import { PageHeader } from "../components/PageHeader";
import { drawdownSeries, equityCurve, formatCurrency, rangeFilter, rollingVol } from "../lib/portfolio-data";

const RANGES = ["1M", "3M", "6M", "YTD", "1Y", "5Y", "Max"] as const;
const BENCHMARKS = ["S&P 500", "MSCI World", "NASDAQ 100"] as const;

export default function PerformancePage() {
  const [range, setRange] = useState<(typeof RANGES)[number]>("1Y");
  const [bench, setBench] = useState<(typeof BENCHMARKS)[number]>("S&P 500");
  const data = rangeFilter(equityCurve, range);

  // Set the browser tab title when the component mounts
  useEffect(() => {
    document.title = "Performance — Meridian";
  }, []);

  return (
    <div className="mx-auto max-w-7xl">
      <PageHeader title="Performance" subtitle="Equity curve, benchmark comparison, and risk over time" />

      <div className="rounded-lg border bg-card p-5">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h2 className="font-display text-base font-semibold">Equity Curve vs {bench}</h2>
            <p className="text-xs text-muted-foreground">Cumulative portfolio value</p>
          </div>
          <div className="flex flex-wrap gap-2">
            <div className="flex rounded-md border bg-background p-0.5">
              {RANGES.map((r) => (
                <button
                  key={r}
                  onClick={() => setRange(r)}
                  className={`rounded px-2.5 py-1 text-xs font-medium transition ${range === r ? "bg-primary text-primary-foreground" : "text-muted-foreground hover:text-foreground"}`}
                >
                  {r}
                </button>
              ))}
            </div>
            <div className="flex rounded-md border bg-background p-0.5">
              {BENCHMARKS.map((b) => (
                <button
                  key={b}
                  onClick={() => setBench(b)}
                  className={`rounded px-2.5 py-1 text-xs font-medium transition ${bench === b ? "bg-accent text-accent-foreground" : "text-muted-foreground hover:text-foreground"}`}
                >
                  {b}
                </button>
              ))}
            </div>
          </div>
        </div>
        <div className="mt-4 h-80">
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={data}>
              <defs>
                <linearGradient id="pv" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="oklch(0.28 0.08 260)" stopOpacity={0.35} />
                  <stop offset="100%" stopColor="oklch(0.28 0.08 260)" stopOpacity={0} />
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.9 0.015 245)" vertical={false} />
              <XAxis dataKey="date" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" />
              <YAxis tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" tickFormatter={(v) => `€${(v / 1000).toFixed(0)}k`} />
              <Tooltip 
                contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} 
                formatter={(v) => formatCurrency(Number(v))} 
              />
              <Legend wrapperStyle={{ fontSize: 11 }} />
              <Area type="monotone" dataKey="value" name="Portfolio" stroke="oklch(0.28 0.08 260)" strokeWidth={2} fill="url(#pv)" />
              <Line type="monotone" dataKey="benchmark" name={bench} stroke="oklch(0.72 0.1 235)" strokeWidth={2} strokeDasharray="4 4" dot={false} />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="mt-6 grid gap-4 lg:grid-cols-2">
        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Drawdowns</h2>
          <p className="text-xs text-muted-foreground">Decline from recent peak</p>
          <div className="mt-4 h-64">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={drawdownSeries}>
                <defs>
                  <linearGradient id="dd" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="oklch(0.58 0.22 25)" stopOpacity={0.05} />
                    <stop offset="100%" stopColor="oklch(0.58 0.22 25)" stopOpacity={0.4} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.9 0.015 245)" vertical={false} />
                <XAxis dataKey="date" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" />
                <YAxis tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" tickFormatter={(v) => `${v.toFixed(0)}%`} />
                <Tooltip contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} formatter={(v) => typeof v === "number" ? `${v.toFixed(2)}%` : ""} />                
                <Area type="monotone" dataKey="dd" stroke="oklch(0.58 0.22 25)" strokeWidth={2} fill="url(#dd)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Rolling Volatility</h2>
          <p className="text-xs text-muted-foreground">6-month annualized</p>
          <div className="mt-4 h-64">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={rollingVol}>
                <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.9 0.015 245)" vertical={false} />
                <XAxis dataKey="date" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" />
                <YAxis tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" tickFormatter={(v) => `${v.toFixed(0)}%`} />
                <Tooltip contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} formatter={(v) => typeof v === "number" ? `${v.toFixed(2)}%` : ""} />
                <Line type="monotone" dataKey="vol" stroke="oklch(0.45 0.12 250)" strokeWidth={2} dot={false} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}