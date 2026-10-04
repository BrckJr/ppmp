"use client";

import { useState } from "react";
import { Trash2 } from "lucide-react";
import { Button } from "./ui/button";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog";
import { deleteApiWatchlistsByWatchlistId } from "../api/generated";
import type { WatchlistDto } from "../api/generated/types.gen";

export function DeleteWatchlistDialog({ watchlist, onDeleted }: { watchlist: WatchlistDto; onDeleted: (id: string) => void }) {
  const [open, setOpen] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const itemCount = watchlist.itemCount ?? 0;

  function handleOpenChange(next: boolean) {
    if (isDeleting) return;
    setOpen(next);
    if (next) setError(null);
  }

  async function handleDelete() {
    if (!watchlist.id || isDeleting) return;
    setIsDeleting(true);
    setError(null);
    try {
      const { error: apiError } = await deleteApiWatchlistsByWatchlistId({ path: { watchlistId: watchlist.id } });
      if (apiError) {
        setError(typeof apiError === "string" && apiError ? apiError : "Failed to delete the watchlist.");
        return;
      }
      setOpen(false);
      onDeleted(watchlist.id);
    } catch (err) {
      console.error(err);
      setError("Failed to delete the watchlist. Please try again.");
    } finally {
      setIsDeleting(false);
    }
  }

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <Button size="sm" variant="outline" onClick={() => handleOpenChange(true)}>
        <Trash2 className="mr-1 h-4 w-4" />
        Delete watchlist
      </Button>

      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Delete “{watchlist.name}”?</DialogTitle>
          <DialogDescription>
            This permanently deletes the watchlist
            {itemCount > 0 ? ` and its ${itemCount} instrument${itemCount === 1 ? "" : "s"}` : ""}. The instruments
            themselves and your transactions are not affected. This cannot be undone.
          </DialogDescription>
        </DialogHeader>

        {error && <p className="text-sm text-destructive">{error}</p>}

        <DialogFooter>
          <Button variant="outline" onClick={() => handleOpenChange(false)} disabled={isDeleting}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleDelete} disabled={isDeleting}>
            {isDeleting ? "Deleting…" : "Delete"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
