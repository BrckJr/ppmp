import { useEffect, useMemo, useState } from "react";
import { ArrowDownLeft, ArrowUpRight, Coins, Wallet } from "lucide-react";
import { PageHeader } from "../components/PageHeader";
import { KpiCard } from "../components/KpiCard";
import { TransactionDialog } from "../components/TransactionDialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";
import { cn } from "../lib/utils";
import { formatCurrencyPrecise, totalGain, transactions as initialTransactions, type Transaction, type TxType } from "../lib/portfolio-data";

const TYPE_BADGE: Record<TxType, string> = {
  Buy: "bg-accent/15 text-accent",
  Sell: "bg-warning/20 text-warning-foreground",
  Dividend: "bg-success/15 text-success",
  Deposit: "bg-primary/10 text-primary",
  Withdrawal: "bg-destructive/15 text-destructive",
};

export default function TransactionsPage() {
  const [filter, setFilter] = useState<"all" | TxType>("all");
  const [txs, setTxs] = useState<Transaction[]>(initialTransactions);
  
  // Set window tab title on client mount
  useEffect(() => {
    document.title = "Transactions — Meridian";
  }, []);

  const rows = useMemo(() => txs.filter((t) => filter === "all" || t.type === filter), [filter, txs]);

  const realized = txs.filter((t) => t.type === "Sell").reduce((s, t) => s + t.amount * 0.08, 0);
  const dividends = txs.filter((t) => t.type === "Dividend").reduce((s, t) => s + t.amount, 0);

  return (
    <div className="mx-auto max-w-7xl">
      <PageHeader 
        title="Transactions" 
        subtitle="All activity, realized P/L and tax lots" 
        actions={<TransactionDialog onCreate={(transaction) => setTxs((current) => [transaction, ...current])} />} 
      />

      <div className="grid grid-cols-2 gap-4 md:grid-cols-3">
        <KpiCard label="Realized Gains (YTD)" value={formatCurrencyPrecise(realized)} trend="up" icon={Coins} />
        <KpiCard label="Unrealized Gains" value={formatCurrencyPrecise(totalGain)} trend="up" icon={Wallet} />
        <KpiCard label="Dividends (YTD)" value={formatCurrencyPrecise(dividends)} trend="up" icon={ArrowDownLeft} />
      </div>

      <div className="mt-6 mb-4 flex items-center gap-3">
        <Select value={filter} onValueChange={(v) => setFilter(v as typeof filter)}>
          <SelectTrigger className="w-48"><SelectValue /></SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All types</SelectItem>
            <SelectItem value="Buy">Buy</SelectItem>
            <SelectItem value="Sell">Sell</SelectItem>
            <SelectItem value="Dividend">Dividend</SelectItem>
            <SelectItem value="Deposit">Deposit</SelectItem>
            <SelectItem value="Withdrawal">Withdrawal</SelectItem>
          </SelectContent>
        </Select>
        <span className="text-sm text-muted-foreground">{rows.length} entries</span>
      </div>

      <div className="overflow-hidden rounded-lg border bg-card">
        <table className="w-full text-sm">
          <thead className="border-b bg-muted/40 text-xs uppercase tracking-wider text-muted-foreground">
            <tr>
              <th className="px-4 py-3 text-left font-medium">Date</th>
              <th className="px-4 py-3 text-left font-medium">Type</th>
              <th className="px-4 py-3 text-left font-medium">Asset</th>
              <th className="px-4 py-3 text-right font-medium">Shares</th>
              <th className="px-4 py-3 text-right font-medium">Price</th>
              <th className="px-4 py-3 text-right font-medium">Amount</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((t) => (
              <tr key={t.id} className="border-b transition-colors last:border-0 hover:bg-muted/30">
                <td className="px-4 py-3 tabular-nums text-muted-foreground">{t.date}</td>
                <td className="px-4 py-3">
                  <span className={cn("inline-flex items-center gap-1 rounded-md px-2 py-0.5 text-xs font-medium", TYPE_BADGE[t.type])}>
                    {t.type === "Buy" || t.type === "Deposit" ? <ArrowDownLeft className="h-3 w-3" /> : <ArrowUpRight className="h-3 w-3" />}
                    {t.type}
                  </span>
                </td>
                <td className="px-4 py-3">{t.ticker ? <span className="font-mono font-semibold">{t.ticker}</span> : <span className="text-muted-foreground">—</span>}</td>
                <td className="px-4 py-3 text-right tabular-nums">{t.shares ?? "—"}</td>
                <td className="px-4 py-3 text-right tabular-nums">{t.price ? formatCurrencyPrecise(t.price) : "—"}</td>
                <td className={cn("px-4 py-3 text-right font-medium tabular-nums", t.amount >= 0 ? "text-success" : "text-foreground")}>
                  {t.amount >= 0 ? "+" : ""}{formatCurrencyPrecise(t.amount)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
