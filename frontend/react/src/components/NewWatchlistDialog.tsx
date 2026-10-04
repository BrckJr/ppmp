"use client";

import { useState } from "react";
import { FolderPlus } from "lucide-react";
import { Button } from "./ui/button";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Textarea } from "./ui/textarea";
import { postApiWatchlists } from "../api/generated";
import type { WatchlistDto } from "../api/generated/types.gen";

const NAME_MAX = 100;
const DESCRIPTION_MAX = 255;

export function NewWatchlistDialog({ onCreate }: { onCreate: (watchlist: WatchlistDto) => void }) {
  const [open, setOpen] = useState(false);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const canSubmit = name.trim().length > 0;

  function openDialog() {
    setName("");
    setDescription("");
    setError(null);
    setOpen(true);
  }

  async function handleSubmit() {
    if (!canSubmit || isSubmitting) return;

    setIsSubmitting(true);
    setError(null);
    try {
      const { data, error: apiError } = await postApiWatchlists({
        body: { name: name.trim(), description: description.trim() || undefined },
      });
      if (apiError || !data) {
        setError(extractMessage(apiError) ?? "Failed to create watchlist. Is the name already in use?");
        return;
      }
      onCreate(data);
      setOpen(false);
    } catch (err) {
      console.error(err);
      setError("Failed to create watchlist. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <Button size="sm" variant="outline" onClick={openDialog}>
        <FolderPlus className="mr-1 h-4 w-4" />
        New watchlist
      </Button>

      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle>New watchlist</DialogTitle>
          <DialogDescription>Create an additional watchlist to group the instruments you are tracking.</DialogDescription>
        </DialogHeader>

        <div className="grid gap-5 py-2">
          <div className="grid gap-2">
            <Label htmlFor="wl-name">Name</Label>
            <Input
              id="wl-name"
              placeholder="e.g. Tech stocks"
              maxLength={NAME_MAX}
              value={name}
              onChange={(event) => setName(event.target.value)}
              onKeyDown={(event) => event.key === "Enter" && handleSubmit()}
              autoFocus
            />
          </div>
          <div className="grid gap-2">
            <Label htmlFor="wl-description">Description</Label>
            <Textarea
              id="wl-description"
              placeholder="Short description (optional)"
              maxLength={DESCRIPTION_MAX}
              value={description}
              onChange={(event) => setDescription(event.target.value)}
            />
          </div>
          {error && <p className="text-sm text-destructive">{error}</p>}
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => setOpen(false)} disabled={isSubmitting}>
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={!canSubmit || isSubmitting}>
            {isSubmitting ? "Creating…" : "Create watchlist"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// Backend errors are either a plain text message or a bean validation violation list.
function extractMessage(error: unknown): string | null {
  if (typeof error === "string" && error.trim()) return error;
  if (error && typeof error === "object" && "violations" in error) {
    const violations = (error as { violations?: { message?: string }[] }).violations;
    return violations?.[0]?.message ?? null;
  }
  return null;
}
