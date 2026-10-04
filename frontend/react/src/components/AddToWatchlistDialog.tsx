"use client";

import { useEffect, useState } from "react";
import { Check, Plus, Search } from "lucide-react";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { getApiInstruments, getApiWatchlistsByWatchlistIdItems, postApiWatchlistsByWatchlistIdItems } from "../api/generated";
import type { InstrumentDto, WatchlistDto, WatchlistItemDto } from "../api/generated/types.gen";

const SEARCH_DEBOUNCE_MS = 250;

type Props = {
  watchlists: WatchlistDto[];
  /** Watchlist that is preselected as the target when the dialog opens. */
  defaultWatchlistId?: string;
  onAdd: (watchlistId: string, item: WatchlistItemDto) => void;
};

export function AddToWatchlistDialog({ watchlists, defaultWatchlistId, onAdd }: Props) {
  const [open, setOpen] = useState(false);
  const [targetId, setTargetId] = useState<string>("");
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<InstrumentDto[]>([]);
  const [searchedQuery, setSearchedQuery] = useState<string | null>(null);
  const [addingId, setAddingId] = useState<string | null>(null);
  // Instruments already on the target watchlist, to mark them in the results.
  const [targetInstrumentIds, setTargetInstrumentIds] = useState<Set<string>>(new Set());
  const [error, setError] = useState<string | null>(null);

  const target = watchlists.find((w) => w.id === targetId);

  function openDialog() {
    setTargetId(defaultWatchlistId ?? watchlists[0]?.id ?? "");
    setQuery("");
    setSearchedQuery(null);
    setResults([]);
    setTargetInstrumentIds(new Set());
    setError(null);
    setOpen(true);
  }

  function changeTarget(id: string) {
    setTargetId(id);
    setTargetInstrumentIds(new Set());
    setError(null);
  }

  useEffect(() => {
    if (!open || !targetId) return;
    let ignore = false;
    getApiWatchlistsByWatchlistIdItems({ path: { watchlistId: targetId } })
      .then(({ data }) => {
        if (!ignore) setTargetInstrumentIds(new Set((data ?? []).map((item) => item.instrumentId)));
      })
      .catch((err) => console.error(err));
    return () => {
      ignore = true;
    };
  }, [open, targetId]);

  // Debounced search; `ignore` drops responses of outdated queries.
  useEffect(() => {
    if (!open) return;
    let ignore = false;
    const timer = setTimeout(async () => {
      try {
        const { data } = await getApiInstruments({ query: { q: query.trim() || undefined, limit: 25 } });
        if (!ignore) setResults(data ?? []);
      } catch (err) {
        console.error(err);
        if (!ignore) setResults([]);
      } finally {
        if (!ignore) setSearchedQuery(query);
      }
    }, SEARCH_DEBOUNCE_MS);
    return () => {
      ignore = true;
      clearTimeout(timer);
    };
  }, [open, query]);

  const isSearching = searchedQuery !== query;

  function isOnTarget(instrumentId?: string) {
    return instrumentId !== undefined && targetInstrumentIds.has(instrumentId);
  }

  async function handleAdd(instrument: InstrumentDto) {
    if (!instrument.id || !targetId || addingId) return;
    setAddingId(instrument.id);
    setError(null);
    try {
      const { data, error: apiError } = await postApiWatchlistsByWatchlistIdItems({
        path: { watchlistId: targetId },
        body: { instrumentId: instrument.id },
      });
      if (apiError || !data) {
        setError(typeof apiError === "string" && apiError ? apiError : `Could not add ${instrument.ticker}.`);
        return;
      }
      setTargetInstrumentIds((prev) => new Set(prev).add(instrument.id!));
      onAdd(targetId, data);
    } catch (err) {
      console.error(err);
      setError(`Could not add ${instrument.ticker}. Please try again.`);
    } finally {
      setAddingId(null);
    }
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <Button size="sm" onClick={openDialog} disabled={watchlists.length === 0}>
        <Plus className="mr-1 h-4 w-4" />
        Add to watchlist
      </Button>

      <DialogContent className="sm:max-w-2xl">
        <DialogHeader>
          <DialogTitle>Add to watchlist</DialogTitle>
          <DialogDescription>
            {target ? (
              <>
                Instruments will be added to <span className="font-semibold text-foreground">{target.name}</span>.
              </>
            ) : (
              "Select a watchlist first."
            )}
          </DialogDescription>
        </DialogHeader>

        <div className="grid gap-4 py-2">
          <div className="grid gap-2 sm:max-w-sm">
            <Label htmlFor="add-target">Target watchlist</Label>
            <Select value={targetId} onValueChange={changeTarget}>
              <SelectTrigger id="add-target">
                <SelectValue placeholder="Select a watchlist" />
              </SelectTrigger>
              <SelectContent>
                {watchlists.map((w) => (
                  <SelectItem key={w.id} value={w.id!}>
                    {w.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="grid gap-2">
            <Label htmlFor="add-search">Search instruments</Label>
            <div className="relative">
              <Search className="pointer-events-none absolute left-3 top-2.5 h-4 w-4 text-muted-foreground" />
              <Input
                id="add-search"
                className="pl-9"
                placeholder="Ticker, name or ISIN"
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                autoFocus
              />
            </div>
          </div>

          <div className="max-h-72 divide-y overflow-y-auto rounded-md border">
            {results.length === 0 && (
              <p className="p-4 text-sm text-muted-foreground">
                {isSearching ? "Searching…" : "No instruments found."}
              </p>
            )}
            {results.map((instrument) => {
              const added = isOnTarget(instrument.id);
              return (
                <div key={instrument.id} className="flex items-center justify-between gap-3 p-3">
                  <div className="min-w-0">
                    <div className="flex items-center gap-2">
                      <span className="font-mono text-sm font-semibold">{instrument.ticker}</span>
                      <Badge variant="secondary" className="text-xs">{instrument.type}</Badge>
                    </div>
                    <p className="truncate text-xs text-muted-foreground">
                      {[instrument.name, instrument.exchange, instrument.currency].filter(Boolean).join(" · ")}
                    </p>
                  </div>
                  {added ? (
                    <span className="flex items-center gap-1 text-xs font-medium text-success">
                      <Check className="h-4 w-4" />
                      On watchlist
                    </span>
                  ) : (
                    <Button size="sm" variant="outline" onClick={() => handleAdd(instrument)} disabled={!targetId || addingId !== null}>
                      {addingId === instrument.id ? "Adding…" : "Add"}
                    </Button>
                  )}
                </div>
              );
            })}
          </div>

          {error && <p className="text-sm text-destructive">{error}</p>}
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => setOpen(false)}>
            Done
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
