import { useEffect } from "react";
import { Plus, Star, TrendingUp } from "lucide-react";
import { PageHeader } from "../components/PageHeader";
import { Button } from "../components/ui/button";
import { Badge } from "../components/ui/badge";
import { cn } from "../lib/utils";
import { formatCurrencyPrecise, watchlist } from "../lib/portfolio-data";

const RATING_COLOR: Record<string, string> = {
  "Strong Buy": "bg-success/15 text-success",
  Buy: "bg-accent/15 text-accent",
  Hold: "bg-muted text-muted-foreground",
  Sell: "bg-destructive/15 text-destructive",
};

export default function WatchlistPage() {
  // Synchronize document metadata dynamically on client mount
  useEffect(() => {
    document.title = "Watchlist — Meridian";
  }, []);

  return (
    <div className="mx-auto max-w-7xl">
      <PageHeader title="Watchlist" subtitle="Stocks you're tracking" actions={<Button size="sm"><Plus className="mr-1 h-4 w-4" />Add to watchlist</Button>} />

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {watchlist.map((w) => {
          const upside = ((w.targetPrice - w.price) / w.price) * 100;
          return (
            <div key={w.ticker} className="rounded-lg border bg-card p-5 transition-shadow hover:shadow-md">
              <div className="flex items-start justify-between">
                <div>
                  <div className="flex items-center gap-2">
                    <Star className="h-4 w-4 text-warning" />
                    <span className="font-mono text-base font-semibold">{w.ticker}</span>
                  </div>
                  <p className="mt-1 text-sm text-muted-foreground">{w.name}</p>
                </div>
                <Badge variant="secondary" className={cn("text-xs font-medium", RATING_COLOR[w.analystRating])}>{w.analystRating}</Badge>
              </div>

              <div className="mt-4 flex items-end justify-between">
                <div>
                  <p className="text-xs text-muted-foreground">Current</p>
                  <p className="font-display text-xl font-semibold tabular-nums">{formatCurrencyPrecise(w.price)}</p>
                </div>
                <div className="text-right">
                  <p className="text-xs text-muted-foreground">Target</p>
                  <p className="font-display text-xl font-semibold tabular-nums">{formatCurrencyPrecise(w.targetPrice)}</p>
                </div>
              </div>

              <div className="mt-3 flex items-center gap-1 text-xs">
                <TrendingUp className={cn("h-3.5 w-3.5", upside >= 0 ? "text-success" : "text-destructive")} />
                <span className={cn("font-medium tabular-nums", upside >= 0 ? "text-success" : "text-destructive")}>
                  {upside >= 0 ? "+" : ""}{upside.toFixed(1)}% upside
                </span>
              </div>

              <div className="mt-4 grid grid-cols-3 gap-2 border-t pt-3 text-xs">
                <Metric label="P/E" value={w.pe.toFixed(1)} />
                <Metric label="PEG" value={w.pegRatio.toFixed(1)} />
                <Metric label="Yield" value={`${w.dividendYield.toFixed(2)}%`} />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

function Metric({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <p className="text-muted-foreground">{label}</p>
      <p className="font-medium tabular-nums text-foreground">{value}</p>
    </div>
  );
}