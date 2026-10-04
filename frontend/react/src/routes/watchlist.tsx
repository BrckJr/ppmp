import { useEffect, useState } from "react";
import { Star, Trash2, TrendingUp } from "lucide-react";
import { PageHeader } from "../components/PageHeader";
import { AddToWatchlistDialog } from "../components/AddToWatchlistDialog";
import { DeleteWatchlistDialog } from "../components/DeleteWatchlistDialog";
import { NewWatchlistDialog } from "../components/NewWatchlistDialog";
import { Button } from "../components/ui/button";
import { Badge } from "../components/ui/badge";
import { cn } from "../lib/utils";
import { formatCurrencyPrecise } from "../lib/portfolio-data";
import {
  deleteApiWatchlistsByWatchlistIdItemsByItemId,
  getApiWatchlists,
  getApiWatchlistsByWatchlistIdItems,
} from "../api/generated";
import type { AnalystRating, WatchlistDto, WatchlistItemDto } from "../api/generated/types.gen";

const RATING_LABEL: Record<AnalystRating, string> = {
  STRONG_BUY: "Strong Buy",
  BUY: "Buy",
  HOLD: "Hold",
  SELL: "Sell",
};

const RATING_COLOR: Record<AnalystRating, string> = {
  STRONG_BUY: "bg-success/15 text-success",
  BUY: "bg-accent/15 text-accent",
  HOLD: "bg-muted text-muted-foreground",
  SELL: "bg-destructive/15 text-destructive",
};

export default function WatchlistPage() {
  const [watchlists, setWatchlists] = useState<WatchlistDto[]>([]);
  const [selectedId, setSelectedId] = useState<string>();
  // Items are stored together with the watchlist they belong to, so stale data is never shown for another list.
  const [loaded, setLoaded] = useState<{ watchlistId: string; items: WatchlistItemDto[] } | null>(null);
  const [reloadKey, setReloadKey] = useState(0);
  const [loadError, setLoadError] = useState(false);
  const [loadedLists, setLoadedLists] = useState(false);

  useEffect(() => {
    document.title = "Watchlist — Meridian";
  }, []);

  // (Re)load all watchlists; keeps the current selection if it still exists
  useEffect(() => {
    let ignore = false;
    getApiWatchlists()
      .then(({ data }) => {
        if (ignore) return;
        const lists = data ?? [];
        setWatchlists(lists);
        setLoadedLists(true);
        setSelectedId((current) => (lists.some((w) => w.id === current) ? current : lists[0]?.id));
        setLoadError(false);
      })
      .catch((err) => {
        console.error("Error loading watchlists:", err);
        if (!ignore) setLoadError(true);
      });
    return () => {
      ignore = true;
    };
  }, [reloadKey]);

  // Load the items of the selected watchlist (race-condition safe)
  useEffect(() => {
    if (!selectedId) return;
    let ignore = false;
    getApiWatchlistsByWatchlistIdItems({ path: { watchlistId: selectedId } })
      .then(({ data }) => {
        if (!ignore) setLoaded({ watchlistId: selectedId, items: data ?? [] });
      })
      .catch((err) => {
        console.error("Error loading watchlist items:", err);
        if (!ignore) setLoadError(true);
      });
    return () => {
      ignore = true;
    };
  }, [selectedId]);

  const selected = watchlists.find((w) => w.id === selectedId);
  const items = loaded && loaded.watchlistId === selectedId ? loaded.items : [];
  const isLoading = selectedId !== undefined && loaded?.watchlistId !== selectedId;

  function handleWatchlistCreated(created: WatchlistDto) {
    setSelectedId(created.id);
    setReloadKey((key) => key + 1);
  }

  function handleWatchlistDeleted(id: string) {
    // The selection falls back to the first remaining watchlist after the reload.
    if (selectedId === id) setSelectedId(undefined);
    setReloadKey((key) => key + 1);
  }

  function handleItemAdded(watchlistId: string, item: WatchlistItemDto) {
    setLoaded((prev) => (prev?.watchlistId === watchlistId ? { watchlistId, items: [...prev.items, item] } : prev));
    setWatchlists((prev) => prev.map((w) => (w.id === watchlistId ? { ...w, itemCount: (w.itemCount ?? 0) + 1 } : w)));
  }

  async function handleRemove(item: WatchlistItemDto) {
    if (!selectedId || !item.id) return;
    try {
      const { error } = await deleteApiWatchlistsByWatchlistIdItemsByItemId({ path: { watchlistId: selectedId, itemId: item.id } });
      if (error) throw new Error("Failed to remove item");
      setLoaded((prev) => (prev ? { ...prev, items: prev.items.filter((i) => i.id !== item.id) } : prev));
      setWatchlists((prev) => prev.map((w) => (w.id === selectedId ? { ...w, itemCount: Math.max((w.itemCount ?? 1) - 1, 0) } : w)));
    } catch (err) {
      alert("Failed to remove the instrument. Please try again.");
      console.error(err);
    }
  }

  return (
    <div className="mx-auto max-w-7xl">
      <PageHeader
        title="Watchlist"
        subtitle={selected ? `${selected.name}${selected.description && selected.description !== selected.name ? ` — ${selected.description}` : ""}` : "Instruments you're tracking"}
        actions={
          <div className="flex gap-2">
            {selected?.id && (
              <DeleteWatchlistDialog key={selected.id} watchlist={selected} onDeleted={handleWatchlistDeleted} />
            )}
            <NewWatchlistDialog onCreate={handleWatchlistCreated} />
            <AddToWatchlistDialog watchlists={watchlists} defaultWatchlistId={selectedId} onAdd={handleItemAdded} />
          </div>
        }
      />

      {watchlists.length > 0 && (
        <div className="mb-5 flex flex-wrap gap-2">
          {watchlists.map((w) => (
            <Button
              key={w.id}
              size="sm"
              variant={w.id === selectedId ? "default" : "outline"}
              onClick={() => setSelectedId(w.id)}
            >
              {w.name}
              <span className="ml-2 text-xs opacity-70">{w.itemCount ?? 0}</span>
            </Button>
          ))}
        </div>
      )}

      {loadError && <p className="mb-4 text-sm text-destructive">Could not load the watchlist from the server.</p>}
      {!loadError && loadedLists && watchlists.length === 0 && (
        <p className="text-sm text-muted-foreground">
          You don't have a watchlist yet. Use “New watchlist” to create one.
        </p>
      )}
      {!isLoading && !loadError && selected && items.length === 0 && (
        <p className="text-sm text-muted-foreground">
          This watchlist is empty. Use “Add to watchlist” to search for instruments and add them.
        </p>
      )}

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {items.map((w) => {
          const price = w.price ?? 0;
          const targetPrice = w.targetPrice ?? 0;
          const upside = price > 0 ? ((targetPrice - price) / price) * 100 : 0;
          return (
            <div key={w.id} className="rounded-lg border bg-card p-5 transition-shadow hover:shadow-md">
              <div className="flex items-start justify-between">
                <div>
                  <div className="flex items-center gap-2">
                    <Star className="h-4 w-4 text-warning" />
                    <span className="font-mono text-base font-semibold">{w.ticker}</span>
                  </div>
                  <p className="mt-1 text-sm text-muted-foreground">{w.name}</p>
                </div>
                <div className="flex items-center gap-1">
                  {w.analystRating && (
                    <Badge variant="secondary" className={cn("text-xs font-medium", RATING_COLOR[w.analystRating])}>
                      {RATING_LABEL[w.analystRating]}
                    </Badge>
                  )}
                  <Button variant="ghost" size="icon" className="h-7 w-7" aria-label={`Remove ${w.ticker}`} onClick={() => handleRemove(w)}>
                    <Trash2 className="h-4 w-4 text-muted-foreground" />
                  </Button>
                </div>
              </div>

              <div className="mt-4 flex items-end justify-between">
                <div>
                  <p className="text-xs text-muted-foreground">Current</p>
                  <p className="font-display text-xl font-semibold tabular-nums">{formatCurrencyPrecise(price)}</p>
                </div>
                <div className="text-right">
                  <p className="text-xs text-muted-foreground">Target</p>
                  <p className="font-display text-xl font-semibold tabular-nums">{formatCurrencyPrecise(targetPrice)}</p>
                </div>
              </div>

              <div className="mt-3 flex items-center gap-1 text-xs">
                <TrendingUp className={cn("h-3.5 w-3.5", upside >= 0 ? "text-success" : "text-destructive")} />
                <span className={cn("font-medium tabular-nums", upside >= 0 ? "text-success" : "text-destructive")}>
                  {upside >= 0 ? "+" : ""}{upside.toFixed(1)}% upside
                </span>
              </div>

              <div className="mt-4 grid grid-cols-3 gap-2 border-t pt-3 text-xs">
                <Metric label="P/E" value={(w.pe ?? 0).toFixed(1)} />
                <Metric label="PEG" value={(w.pegRatio ?? 0).toFixed(1)} />
                <Metric label="Yield" value={`${(w.dividendYieldPct ?? 0).toFixed(2)}%`} />
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
