import { Link } from "react-router";
import { useMemo, useState, useEffect } from "react";
import { ArrowUpDown, Search } from "lucide-react";
import { PageHeader } from "../components/PageHeader";
import { Input } from "../components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";
import { Badge } from "../components/ui/badge";
import { cn } from "../lib/utils";
import { formatCurrencyPrecise, formatPercent, holdings, holdingGain, holdingGainPct, holdingValue } from "../lib/portfolio-data";

type SortKey = "ticker" | "value" | "gain" | "gainPct";

export default function HoldingsPage() {
  const [query, setQuery] = useState("");
  const [assetFilter, setAssetFilter] = useState<string>("all");
  const [sortKey, setSortKey] = useState<SortKey>("value");
  const [dir, setDir] = useState<"asc" | "desc">("desc");

  // Since we are in a clean SPA, we handle document titles directly inside useEffect
  useEffect(() => {
    document.title = "Holdings — Meridian";
  }, []);

  const rows = useMemo(() => {
    const filtered = holdings.filter((h) => {
      if (assetFilter !== "all" && h.assetClass !== assetFilter) return false;
      if (!query) return true;
      const q = query.toLowerCase();
      return h.ticker.toLowerCase().includes(q) || h.name.toLowerCase().includes(q);
    });
    return filtered.sort((a, b) => {
      const mult = dir === "asc" ? 1 : -1;
      switch (sortKey) {
        case "ticker": return a.ticker.localeCompare(b.ticker) * mult;
        case "value": return (holdingValue(a) - holdingValue(b)) * mult;
        case "gain": return (holdingGain(a) - holdingGain(b)) * mult;
        case "gainPct": return (holdingGainPct(a) - holdingGainPct(b)) * mult;
      }
    });
  }, [query, assetFilter, sortKey, dir]);

  function toggleSort(k: SortKey) {
    if (sortKey === k) setDir(dir === "asc" ? "desc" : "asc");
    else { setSortKey(k); setDir("desc"); }
  }

  return (
    <div className="mx-auto max-w-7xl">
      <PageHeader title="Holdings" subtitle={`${holdings.length} positions`} />

      <div className="mb-4 flex flex-wrap items-center gap-3">
        <div className="relative flex-1 min-w-64">
          <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input placeholder="Search ticker or company..." value={query} onChange={(e) => setQuery(e.target.value)} className="pl-9" />
        </div>
        <Select value={assetFilter} onValueChange={setAssetFilter}>
          <SelectTrigger className="w-40"><SelectValue /></SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All asset classes</SelectItem>
            <SelectItem value="Stocks">Stocks</SelectItem>
            <SelectItem value="ETFs">ETFs</SelectItem>
            <SelectItem value="Bonds">Bonds</SelectItem>
            <SelectItem value="Crypto">Crypto</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div className="overflow-hidden rounded-lg border bg-card">
        <table className="w-full text-sm">
          <thead className="border-b bg-muted/40 text-xs uppercase tracking-wider text-muted-foreground">
            <tr>
              <Th onClick={() => toggleSort("ticker")} active={sortKey === "ticker"}>Ticker</Th>
              <th className="px-4 py-3 text-left font-medium">Name</th>
              <th className="px-4 py-3 text-right font-medium">Shares</th>
              <th className="px-4 py-3 text-right font-medium">Price</th>
              <Th onClick={() => toggleSort("value")} active={sortKey === "value"} align="right">Value</Th>
              <Th onClick={() => toggleSort("gain")} active={sortKey === "gain"} align="right">Gain</Th>
              <Th onClick={() => toggleSort("gainPct")} active={sortKey === "gainPct"} align="right">%</Th>
            </tr>
          </thead>
          <tbody>
            {rows.map((h) => {
              const gain = holdingGain(h);
              const pct = holdingGainPct(h);
              return (
                <tr key={h.ticker} className="border-b transition-colors last:border-0 hover:bg-muted/30">
                  <td className="px-4 py-3">
                    {/* CHANGED: Swapped TanStack params syntax for React Router string template */}
                    <Link to={`/holdings/${h.ticker}`} className="font-mono font-semibold text-foreground hover:text-accent">
                      {h.ticker}
                    </Link>
                  </td>
                  <td className="px-4 py-3">
                    <div className="font-medium">{h.name}</div>
                    <Badge variant="secondary" className="mt-0.5 text-[10px]">{h.assetClass}</Badge>
                  </td>
                  <td className="px-4 py-3 text-right tabular-nums">{h.shares}</td>
                  <td className="px-4 py-3 text-right tabular-nums">{formatCurrencyPrecise(h.price)}</td>
                  <td className="px-4 py-3 text-right font-medium tabular-nums">{formatCurrencyPrecise(holdingValue(h))}</td>
                  <td className={cn("px-4 py-3 text-right tabular-nums", gain >= 0 ? "text-success" : "text-destructive")}>{gain >= 0 ? "+" : ""}{formatCurrencyPrecise(gain)}</td>
                  <td className={cn("px-4 py-3 text-right font-medium tabular-nums", pct >= 0 ? "text-success" : "text-destructive")}>{formatPercent(pct)}</td>
                </tr>
              );
            })}
            {rows.length === 0 && (
              <tr><td colSpan={7} className="px-4 py-8 text-center text-muted-foreground">No matches</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function Th({ children, onClick, active, align = "left" }: { children: React.ReactNode; onClick: () => void; active: boolean; align?: "left" | "right" }) {
  return (
    <th className={cn("px-4 py-3 font-medium", align === "right" ? "text-right" : "text-left")}>
      <button onClick={onClick} className={cn("inline-flex items-center gap-1 hover:text-foreground", active && "text-foreground")}>
        {children}
        <ArrowUpDown className="h-3 w-3" />
      </button>
    </th>
  );
}