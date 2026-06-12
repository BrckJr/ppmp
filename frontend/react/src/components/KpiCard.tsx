import { cn } from "../lib/utils";
import type { LucideIcon } from "lucide-react";

interface KpiCardProps {
  label: string;
  value: string;
  delta?: string;
  trend?: "up" | "down" | "neutral";
  icon?: LucideIcon;
  hint?: string;
}

export function KpiCard({ label, value, delta, trend = "neutral", icon: Icon, hint }: KpiCardProps) {
  return (
    <div className="rounded-lg border bg-card p-5 transition-shadow hover:shadow-sm">
      <div className="flex items-center justify-between">
        <span className="text-xs font-medium uppercase tracking-wider text-muted-foreground">{label}</span>
        {Icon && <Icon className="h-4 w-4 text-muted-foreground" />}
      </div>
      <div className="mt-3 font-display text-2xl font-semibold tabular-nums text-foreground">{value}</div>
      <div className="mt-1 flex items-center gap-2 text-xs">
        {delta && (
          <span
            className={cn(
              "tabular-nums font-medium",
              trend === "up" && "text-success",
              trend === "down" && "text-destructive",
              trend === "neutral" && "text-muted-foreground",
            )}
          >
            {delta}
          </span>
        )}
        {hint && <span className="text-muted-foreground">{hint}</span>}
      </div>
    </div>
  );
}
