import { useCallback, useEffect, useState } from "react";
import { ArrowDownLeft, ArrowUpRight, Coins, Receipt, Trash2, Wallet } from "lucide-react";
import { PageHeader } from "../components/PageHeader";
import { KpiCard } from "../components/KpiCard";
import { TransactionDialog } from "../components/TransactionDialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";
import { cn } from "../lib/utils";
import { formatCurrencyPrecise } from "../lib/portfolio-data";
import { API_BASE_TRANSACTIONS as API_BASE } from "./routes";
import { apiFetch } from "../lib/api-client";
import type { TransactionType, TransactionDto, TransactionMetricsDto } from "../api/generated/types.gen";

const TYPE_CONFIG: Record<TransactionType, { badgeStyle: string; isInflow: boolean }> = {
  BUY: { badgeStyle: "bg-accent/15 text-accent", isInflow: false },
  SELL: { badgeStyle: "bg-warning/20 text-warning-foreground", isInflow: true },
  DIVIDEND: { badgeStyle: "bg-success/15 text-success", isInflow: true },
  DEPOSIT: { badgeStyle: "bg-primary/10 text-primary", isInflow: true },
  WITHDRAWAL: { badgeStyle: "bg-destructive/15 text-destructive", isInflow: false },
};

const TRANSACTION_TYPES = Object.keys(TYPE_CONFIG) as TransactionType[];

async function fetchTransactions(typeFilter?: string): Promise<TransactionDto[]> {
  const url = typeFilter && typeFilter !== "all" 
    ? `${API_BASE}?type=${encodeURIComponent(typeFilter)}` 
    : API_BASE;
  const res = await apiFetch(url);
  if (!res.ok) throw new Error("Failed to fetch transactions");
  return res.json();
}

async function fetchMetrics(period = "ytd"): Promise<TransactionMetricsDto> {
  const res = await apiFetch(`${API_BASE}/metrics?period=${period}`);
  if (!res.ok) throw new Error("Failed to fetch transaction metrics");
  return res.json();
}

async function deleteTransactionApi(id: string): Promise<void> {
  const res = await apiFetch(`${API_BASE}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Failed to delete transaction");
}

export default function TransactionsPage() {
  const [filter, setFilter] = useState<"all" | TransactionType>("all");
  const [txs, setTxs] = useState<TransactionDto[]>([]);
  const [metrics, setMetrics] = useState<TransactionMetricsDto>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [deletingId, setDeletingId] = useState<string | null>(null);

  useEffect(() => {
    document.title = "Transactions — Meridian";
  }, []);

  // Manual refresh trigger after creating or deleting records
  const refreshData = useCallback(async (typeFilter: string) => {
    setIsLoading(true);
    try {
      const [transactionsData, metricsData] = await Promise.all([
        fetchTransactions(typeFilter),
        fetchMetrics("ytd"),
      ]);
      setTxs(transactionsData);
      setMetrics(metricsData);
    } catch (err) {
      console.error("Error refreshing data:", err);
    } finally {
      setIsLoading(false);
    }
  }, []);

  // Fetch transactions and metrics in parallel on filter change (race-condition safe)
  useEffect(() => {
    let ignore = false;

    async function loadData() {
      setIsLoading(true);
      try {
        const [transactionsData, metricsData] = await Promise.all([
          fetchTransactions(filter),
          fetchMetrics("ytd"),
        ]);
        if (!ignore) {
          setTxs(transactionsData);
          setMetrics(metricsData);
        }
      } catch (err) {
        if (!ignore) console.error("Error loading transaction page data:", err);
      } finally {
        if (!ignore) setIsLoading(false);
      }
    }

    loadData();

    return () => {
      ignore = true;
    };
  }, [filter]);

  // Handle Delete Action
  const handleDelete = async (id?: string) => {
    if (!id) return;
    if (!window.confirm("Are you sure you want to delete this transaction?")) return;
    
    setDeletingId(id);
    try {
      await deleteTransactionApi(id);
      // Optimistic update + recalculate backend metrics
      setTxs((current) => current.filter((t) => t.id !== id));
      const updatedMetrics = await fetchMetrics("ytd");
      setMetrics(updatedMetrics);
    } catch (err) {
      alert("Failed to delete transaction. Please try again.");
      console.error(err);
    } finally {
      setDeletingId(null);
    }
  };

  const handleCreated = () => {
    refreshData(filter);
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-6">
      <PageHeader 
        title="Transactions" 
        subtitle="All activity, settlement details, and history" 
        actions={<TransactionDialog onCreate={handleCreated} />} 
      />

      {/* Top KPI Metrics Cards — Direct non-optional field access */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <KpiCard 
          label="Total Dividends (YTD)" 
          value={metrics ? formatCurrencyPrecise(metrics.totalDividends) : "—"} 
          trend="up" 
          icon={Coins} 
        />
        <KpiCard 
          label="Net Capital Inflow" 
          value={metrics ? formatCurrencyPrecise(metrics.netCapitalInflow) : "—"} 
          trend={metrics && metrics.netCapitalInflow >= 0 ? "up" : "down"} 
          icon={Wallet} 
        />
        <KpiCard 
          label="Trading Volume" 
          value={metrics ? formatCurrencyPrecise(metrics.totalVolume) : "—"} 
          trend="up" 
          icon={Receipt} 
        />
      </div>

      {/* Filter Options */}
      <div className="mt-6 mb-4 flex items-center gap-3">
        <Select value={filter} onValueChange={(v) => setFilter(v as typeof filter)}>
          <SelectTrigger className="w-48"><SelectValue placeholder="Select type" /></SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All types</SelectItem>
            {TRANSACTION_TYPES.map((type) => (
              <SelectItem key={type} value={type}>
                {type.charAt(0) + type.slice(1).toLowerCase()}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <span className="text-sm text-muted-foreground">{txs.length} entries</span>
      </div>

      {/* Transaction Table */}
      <div className="overflow-hidden rounded-lg border bg-card shadow-sm">
        <table className="w-full text-sm">
          <thead className="border-b bg-muted/40 text-xs uppercase tracking-wider text-muted-foreground">
            <tr>
              <th className="px-4 py-3 text-left font-medium">Date</th>
              <th className="px-4 py-3 text-left font-medium">Type</th>
              <th className="px-4 py-3 text-left font-medium">Asset</th>
              <th className="px-4 py-3 text-right font-medium">Quantity</th>
              <th className="px-4 py-3 text-right font-medium">Unit Price</th>
              <th className="px-4 py-3 text-right font-medium">Gross Amount</th>
              <th className="px-4 py-3 text-center font-medium w-12">Actions</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? (
              <tr>
                <td colSpan={7} className="px-4 py-8 text-center text-muted-foreground">
                  Loading transactions...
                </td>
              </tr>
            ) : txs.length === 0 ? (
              <tr>
                <td colSpan={7} className="px-4 py-8 text-center text-muted-foreground">
                  No transactions found.
                </td>
              </tr>
            ) : (
              txs.map((t, idx) => {
                const typeKey = t.type ?? "BUY";
                const config = TYPE_CONFIG[typeKey];
                
                // Format ISO timestamp string (e.g., 2022-03-10T16:15:50Z) to YYYY-MM-DD
                const formattedDate = t.timestamp ? String(t.timestamp).split("T")[0] : "—";

                return (
                  <tr key={t.id ?? idx} className="border-b transition-colors last:border-0 hover:bg-muted/30">
                    <td className="px-4 py-3 tabular-nums text-muted-foreground">{formattedDate}</td>
                    <td className="px-4 py-3">
                      <span className={cn("inline-flex items-center gap-1 rounded-md px-2 py-0.5 text-xs font-medium", config.badgeStyle)}>
                        {config.isInflow ? <ArrowDownLeft className="h-3 w-3" /> : <ArrowUpRight className="h-3 w-3" />}
                        {typeKey}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      {t.ticker ? <span className="font-mono font-semibold">{t.ticker}</span> : <span className="text-muted-foreground">—</span>}
                    </td>
                    <td className="px-4 py-3 text-right tabular-nums">{t.quantity ?? "—"}</td>
                    <td className="px-4 py-3 text-right tabular-nums">{t.unitPrice !== undefined ? formatCurrencyPrecise(t.unitPrice) : "—"}</td>
                    <td className={cn("px-4 py-3 text-right font-medium tabular-nums", config.isInflow ? "text-success" : "text-foreground")}>
                      {config.isInflow ? "+" : "-"}{formatCurrencyPrecise(t.grossAmount ?? 0)}
                    </td>
                    <td className="px-4 py-3 text-center">
                      <button
                        onClick={() => handleDelete(t.id)}
                        disabled={deletingId === t.id}
                        className="rounded p-1 text-muted-foreground hover:bg-destructive/10 hover:text-destructive transition-colors disabled:opacity-50"
                        title="Delete transaction"
                      >
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}