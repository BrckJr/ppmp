import { useEffect } from "react";
import { Activity, AlertTriangle, BarChart3, Gauge, ShieldAlert, TrendingDown } from "lucide-react";
import { PageHeader } from "../components/PageHeader";
import { KpiCard } from "../components/KpiCard";
import { ResponsiveContainer, RadialBarChart, RadialBar, PolarAngleAxis } from "recharts";
import { beta, maxDrawdown, portfolioVolatility, sharpeRatio, totalValue, valueAtRisk95 } from "../lib/portfolio-data";

export default function RiskPage() {
  const varAmount = (valueAtRisk95 / 100) * totalValue;

  // Sync document title to browser window dynamically
  useEffect(() => {
    document.title = "Risk Analytics — Meridian";
  }, []);

  return (
    <div className="mx-auto max-w-7xl">
      <PageHeader title="Risk Analytics" subtitle="Understand your portfolio's risk profile" />

      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
        <KpiCard label="Portfolio Volatility" value={`${portfolioVolatility.toFixed(2)}%`} hint="annualized" icon={Activity} />
        <KpiCard label="Sharpe Ratio" value={sharpeRatio.toFixed(2)} hint="risk-adjusted return" icon={Gauge} trend={sharpeRatio >= 1 ? "up" : "neutral"} />
        <KpiCard label="Max Drawdown" value={`${maxDrawdown.toFixed(2)}%`} hint="largest historical loss" icon={TrendingDown} trend="down" />
        <KpiCard label="Beta" value={beta.toFixed(2)} hint="vs benchmark" icon={BarChart3} />
        <KpiCard label="VaR (95%)" value={`${valueAtRisk95.toFixed(2)}%`} hint={`≈ €${Math.round(varAmount).toLocaleString()} monthly`} icon={ShieldAlert} trend="down" />
        <KpiCard label="Risk Score" value={scoreLabel(portfolioVolatility)} hint="composite assessment" icon={AlertTriangle} />
      </div>

      <div className="mt-6 grid gap-4 lg:grid-cols-2">
        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Risk Gauge</h2>
          <p className="text-xs text-muted-foreground">Composite portfolio risk score</p>
          <div className="mt-4 h-64">
            <ResponsiveContainer width="100%" height="100%">
              <RadialBarChart innerRadius="60%" outerRadius="100%" data={[{ name: "risk", value: Math.min(100, portfolioVolatility * 4), fill: "oklch(0.58 0.12 245)" }]} startAngle={180} endAngle={0}>
                <PolarAngleAxis type="number" domain={[0, 100]} tick={false} />
                <RadialBar background dataKey="value" cornerRadius={10} />
              </RadialBarChart>
            </ResponsiveContainer>
          </div>
          <div className="-mt-12 text-center">
            <p className="font-display text-3xl font-semibold tabular-nums">{portfolioVolatility.toFixed(1)}<span className="text-xl">%</span></p>
            <p className="text-xs text-muted-foreground">annualized volatility</p>
          </div>
        </div>

        <div className="rounded-lg border bg-card p-5">
          <h2 className="font-display text-base font-semibold">Interpretation</h2>
          <p className="text-xs text-muted-foreground">What these numbers mean</p>
          <ul className="mt-4 space-y-3 text-sm">
            <Bullet title="Sharpe Ratio" value={sharpeRatio.toFixed(2)}>
              {sharpeRatio >= 1 ? "Healthy risk-adjusted returns." : "Returns are not strongly compensating for risk taken."}
            </Bullet>
            <Bullet title="Beta" value={beta.toFixed(2)}>
              Portfolio moves {beta < 1 ? "less" : "more"} than the benchmark — a {beta.toFixed(2)}× sensitivity.
            </Bullet>
            <Bullet title="Max Drawdown" value={`${maxDrawdown.toFixed(2)}%`}>
              The worst peak-to-trough decline you've absorbed. Plan capital for at least this magnitude.
            </Bullet>
            <Bullet title="Value-at-Risk (95%)" value={`€${Math.round(varAmount).toLocaleString()}`}>
              On a bad month (worst 5% of cases), you might expect a loss of around this size.
            </Bullet>
          </ul>
        </div>
      </div>
    </div>
  );
}

function Bullet({ title, value, children }: { title: string; value: string; children: React.ReactNode }) {
  return (
    <li className="flex gap-3">
      <div className="mt-0.5 h-2 w-2 shrink-0 rounded-full bg-accent" />
      <div>
        <p className="font-medium">{title} <span className="ml-1 font-mono text-xs text-accent">{value}</span></p>
        <p className="text-muted-foreground">{children}</p>
      </div>
    </li>
  );
}

function scoreLabel(vol: number) {
  if (vol < 10) return "Conservative";
  if (vol < 18) return "Balanced";
  if (vol < 28) return "Aggressive";
  return "High";
}