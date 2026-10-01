"use client";

import { useState } from "react";
import { Plus } from "lucide-react";
import { Button } from "./ui/button";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { formatCurrencyPrecise } from "../lib/portfolio-data";
import { postApiTransactions } from "../api/generated";
import type { Currency, TransactionDto, TransactionType } from "../api/generated/types.gen";

type TransactionDraft = {
  type: TransactionType;
  date: string;
  ticker: string;
  shares: string;
  price: string;
  amount: string;
  currency: Currency;
};

const TYPE_LABELS: Record<TransactionType, string> = {
  BUY: "Buy",
  SELL: "Sell",
  DIVIDEND: "Dividend",
  DEPOSIT: "Deposit",
  WITHDRAWAL: "Withdrawal",
};

function todayIsoDate() {
  return new Date().toISOString().slice(0, 10);
}

function createDraft(type: TransactionType = "BUY"): TransactionDraft {
  return {
    type,
    date: todayIsoDate(),
    ticker: "",
    shares: "",
    price: "",
    amount: "",
    currency: "USD",
  };
}

function parseNumber(value: string) {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : 0;
}

function isSecurityTransaction(type: TransactionType) {
  return type === "BUY" || type === "SELL" || type === "DIVIDEND";
}

function isTrade(type: TransactionType) {
  return type === "BUY" || type === "SELL";
}

export function TransactionDialog({ onCreate }: { onCreate: () => void }) {
  const [open, setOpen] = useState(false);
  const [draft, setDraft] = useState<TransactionDraft>(() => createDraft());
  const [isSubmitting, setIsSubmitting] = useState(false);

  const tradeAmount = parseNumber(draft.shares) * parseNumber(draft.price);
  const signedTradeAmount = draft.type === "BUY" ? -tradeAmount : tradeAmount;
  const normalizedCashAmount = draft.type === "WITHDRAWAL" ? -Math.abs(parseNumber(draft.amount)) : Math.abs(parseNumber(draft.amount));

  const canSubmit =
    Boolean(draft.date) &&
    (!isSecurityTransaction(draft.type) || draft.ticker.trim().length > 0) &&
    (!isTrade(draft.type)
      ? parseNumber(draft.amount) > 0
      : parseNumber(draft.shares) > 0 && parseNumber(draft.price) > 0);

  function openDialog() {
    setDraft(createDraft());
    setOpen(true);
  }

  function updateType(type: TransactionType) {
    setDraft((prev) => ({
      ...createDraft(type),
      date: prev.date,
    }));
  }

  async function handleSubmit() {
    if (!canSubmit || isSubmitting) return;

    const payload: TransactionDto = {
      timestamp: new Date(draft.date).toISOString(),
      type: draft.type,
      currency: draft.currency,
      grossAmount: isTrade(draft.type) ? Math.abs(signedTradeAmount) : Math.abs(normalizedCashAmount),
    };

    if (isSecurityTransaction(draft.type)) {
      payload.ticker = draft.ticker.trim().toUpperCase();
    }

    if (isTrade(draft.type)) {
      payload.quantity = parseNumber(draft.shares);
      payload.unitPrice = parseNumber(draft.price);
    }

    setIsSubmitting(true);
    try {
      const { error } = await postApiTransactions({ body: payload });
      if (error) throw new Error("Failed to create transaction");
      onCreate();
      setOpen(false);
    } catch (err) {
      alert("Failed to create transaction. Please try again.");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <Button size="sm" onClick={openDialog}>
        <Plus className="mr-1 h-4 w-4" />
        New transaction
      </Button>

      <DialogContent className="sm:max-w-2xl">
        <DialogHeader>
          <DialogTitle>New transaction</DialogTitle>
          <DialogDescription>
            Choose the transaction type first. The form below updates to show only the relevant fields.
          </DialogDescription>
        </DialogHeader>

        <div className="grid gap-5 py-2">
          <div className="grid gap-2">
            <Label htmlFor="tx-type">Transaction type</Label>
            <Select value={draft.type} onValueChange={(value) => updateType(value as TransactionType)}>
              <SelectTrigger id="tx-type">
                <SelectValue placeholder="Select a transaction type" />
              </SelectTrigger>
              <SelectContent>
                {(Object.keys(TYPE_LABELS) as TransactionType[]).map((type) => (
                  <SelectItem key={type} value={type}>
                    {TYPE_LABELS[type]}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="grid gap-2 sm:max-w-xs">
            <Label htmlFor="tx-date">Date</Label>
            <Input
              id="tx-date"
              type="date"
              value={draft.date}
              onChange={(event) => setDraft((prev) => ({ ...prev, date: event.target.value }))}
            />
          </div>

          {isSecurityTransaction(draft.type) && (
            <div className="grid gap-2">
              <Label htmlFor="tx-ticker">Ticker</Label>
              <Input
                id="tx-ticker"
                placeholder="e.g. AAPL"
                value={draft.ticker}
                onChange={(event) =>
                  setDraft((prev) => ({ ...prev, ticker: event.target.value.toUpperCase() }))
                }
              />
            </div>
          )}

          {isTrade(draft.type) ? (
            <div className="grid gap-4 sm:grid-cols-2">
              <div className="grid gap-2">
                <Label htmlFor="tx-shares">Shares</Label>
                <Input
                  id="tx-shares"
                  type="number"
                  min="0"
                  step="any"
                  placeholder="0"
                  value={draft.shares}
                  onChange={(event) => setDraft((prev) => ({ ...prev, shares: event.target.value }))}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="tx-price">Price per share</Label>
                <Input
                  id="tx-price"
                  type="number"
                  min="0"
                  step="any"
                  placeholder="0.00"
                  value={draft.price}
                  onChange={(event) => setDraft((prev) => ({ ...prev, price: event.target.value }))}
                />
              </div>
              <div className="grid gap-2 sm:col-span-2">
                <Label>Calculated amount</Label>
                <div className="rounded-md border bg-muted/40 px-3 py-2 text-sm tabular-nums text-muted-foreground">
                  {formatCurrencyPrecise(signedTradeAmount)}
                </div>
              </div>
            </div>
          ) : (
            <div className="grid gap-2 sm:max-w-xs">
              <Label htmlFor="tx-amount">
                {draft.type === "DIVIDEND" ? "Cash dividend" : "Amount"}
              </Label>
              <Input
                id="tx-amount"
                type="number"
                min="0"
                step="any"
                placeholder="0.00"
                value={draft.amount}
                onChange={(event) => setDraft((prev) => ({ ...prev, amount: event.target.value }))}
              />
              {draft.type === "WITHDRAWAL" && (
                <p className="text-xs text-muted-foreground">The stored amount will be negative for withdrawals.</p>
              )}
            </div>
          )}
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => setOpen(false)} disabled={isSubmitting}>
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={!canSubmit || isSubmitting}>
            {isSubmitting ? "Creating…" : "Create transaction"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}