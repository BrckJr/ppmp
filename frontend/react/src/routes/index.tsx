import { useEffect } from "react";
import { Wallet, TrendingUp, Activity, PiggyBank, Coins, Percent } from "lucide-react";
import { PieChart, Pie, Cell, ResponsiveContainer, BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid, AreaChart, Area, Legend } from "recharts";
import { KpiCard } from "../components/KpiCard";
import { PageHeader } from "../components/PageHeader";
import {
  allocationByAssetClass,
  allocationByRegion,
  allocationBySector,
  annualizedReturn,
  cashPosition,
  dailyPL,
  dailyPLPct,
  equityCurve,
  formatCurrency,
  formatPercent,
  totalGain,
  totalInvested,
  totalReturnPct,
  totalValue,
} from "../lib/portfolio-data";

const CHART_COLORS = ["oklch(0.28 0.08 260)", "oklch(0.45 0.12 250)", "oklch(0.58 0.12 245)", "oklch(0.72 0.1 235)", "oklch(0.85 0.06 230)"];

export default function DashboardPage() {
  // Update document metadata dynamically upon client mount
  useEffect(() => {
    document.title = "Dashboard — Meridian";
  }, []);

  return (
    <div className="mx-auto max-w-7xl">
      <PageHeader
        title="How are you doing today?"
        subtitle={new Date().toLocaleDateString("en-US", { weekday: "long", month: "long", day: "numeric", year: "numeric" })}
      />

      <div className="grid grid-cols-2 gap-4 md:grid-cols-3 lg:grid-cols-6">
        <KpiCard label="Portfolio Value" value={formatCurrency(totalValue)} delta={formatPercent(totalReturnPct)} trend="up" icon={Wallet} />
        <KpiCard label="Daily P/L" value={formatCurrency(dailyPL)} delta={formatPercent(dailyPLPct)} trend={dailyPL >= 0 ? "up" : "down"} icon={Activity} />
        <KpiCard label="Total Return" value={formatCurrency(totalGain)} delta={formatPercent(totalReturnPct)} trend="up" icon={TrendingUp} />
        <KpiCard label="Annualized" value={`${annualizedReturn.toFixed(1)}%`} hint="since inception" icon={Percent} />
        <KpiCard label="Cash" value={formatCurrency(cashPosition)} hint={`${((cashPosition / totalValue) * 100).toFixed(1)}% of total`} icon={PiggyBank} />
        <KpiCard label="Invested" value={formatCurrency(totalInvested)} hint="capital deployed" icon={Coins} />
      </div>

      <div className="mt-6 grid gap-4 lg:grid-cols-3">
        <div className="rounded-lg border bg-card p-5 lg:col-span-2">
          <h2 className="font-display text-base font-semibold">Portfolio Value</h2>
          <p className="text-xs text-muted-foreground">Last 24 months</p>
          <div className="mt-4 h-72">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={equityCurve}>
                <defs>
                  <linearGradient id="g1" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="oklch(0.58 0.12 245)" stopOpacity={0.4} />
                    <stop offset="100%" stopColor="oklch(0.58 0.12 245)" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.9 0.015 245)" vertical={false} />
                <XAxis dataKey="date" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" />
                <YAxis tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" tickFormatter={(v) => `€${(v / 1000).toFixed(0)}k`} />
                <Tooltip contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} formatter={(v) => formatCurrency(Number(v))} />
                <Area type="monotone" dataKey="value" stroke="oklch(0.28 0.08 260)" strokeWidth={2} fill="url(#g1)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Asset Allocation</h2>
          <p className="text-xs text-muted-foreground">Breakdown by class</p>
          <div className="mt-4 h-72">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={allocationByAssetClass} dataKey="value" nameKey="name" innerRadius={50} outerRadius={90} paddingAngle={2}>
                  {allocationByAssetClass.map((_, i) => <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />)}
                </Pie>
                <Tooltip contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} formatter={(v) => formatCurrency(Number(v))} />
                <Legend wrapperStyle={{ fontSize: 11 }} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      <div className="mt-6 grid gap-4 lg:grid-cols-2">
        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Sector Allocation</h2>
          <p className="text-xs text-muted-foreground">Across all equity holdings</p>
          <div className="mt-4 h-64">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={allocationBySector} layout="vertical" margin={{ left: 20 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.9 0.015 245)" horizontal={false} />
                <XAxis type="number" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" tickFormatter={(v) => `€${(v / 1000).toFixed(0)}k`} />
                <YAxis type="category" dataKey="name" tick={{ fontSize: 11 }} stroke="oklch(0.5 0.03 255)" width={90} />
                <Tooltip contentStyle={{ borderRadius: 8, border: "1px solid oklch(0.9 0.015 245)", fontSize: 12 }} formatter={(v) => formatCurrency(Number(v))} />
                <Bar dataKey="value" fill="oklch(0.58 0.12 245)" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Geographic Allocation</h2>
          <p className="text-xs text-muted-foreground">Exposure by region</p>
          <div className="mt-4 space-y-3">
            {allocationByRegion.map((r) => {
              const pct = (r.value / allocationByRegion.reduce((s, x) => s + x.value, 0)) * 100;
              return (
                <div key={r.name}>
                  <div className="mb-1 flex items-center justify-between text-sm">
                    <span className="font-medium">{r.name}</span>
                    <span className="tabular-nums text-muted-foreground">{formatCurrency(r.value)} <span className="ml-2 text-xs">{pct.toFixed(1)}%</span></span>
                  </div>
                  <div className="h-2 overflow-hidden rounded-full bg-muted">
                    <div className="h-full rounded-full bg-accent transition-all" style={{ width: `${pct}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}