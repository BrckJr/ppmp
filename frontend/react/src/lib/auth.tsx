import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { UNAUTHORIZED_EVENT } from "./api-client";

export interface AuthUser {
  id: string;
  username: string;
  email: string;
}

type AuthStatus = "loading" | "authenticated" | "anonymous";

interface AuthContextValue {
  user: AuthUser | null;
  status: AuthStatus;
  login: (identifier: string, password: string) => Promise<void>;
  register: (username: string, email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | null>(null);

async function errorMessage(response: Response, fallback: string): Promise<string> {
  try {
    const text = await response.text();
    try {
      const json = JSON.parse(text);
      const violations = Array.isArray(json?.violations) ? json.violations.map((v: { message: string }) => v.message) : [];
      if (violations.length > 0) return violations.join(", ");
    } catch {
      // not JSON
    }
    const plain = text.trim();
    if (plain && plain.length < 200 && !plain.startsWith("<")) return plain;
  } catch {
    // fall through
  }
  return fallback;
}

async function postJson(path: string, body: unknown): Promise<Response> {
  return fetch(path, {
    method: "POST",
    credentials: "same-origin",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [status, setStatus] = useState<AuthStatus>("loading");

  const clearSession = useCallback(() => {
    setUser(null);
    setStatus("anonymous");
  }, []);

  useEffect(() => {
    let cancelled = false;
    fetch("/api/auth/me", { credentials: "same-origin" })
      .then(async (response) => {
        if (cancelled) return;
        if (response.ok) {
          setUser(await response.json());
          setStatus("authenticated");
        } else {
          clearSession();
        }
      })
      .catch(() => {
        if (!cancelled) clearSession();
      });
    return () => {
      cancelled = true;
    };
  }, [clearSession]);

  useEffect(() => {
    window.addEventListener(UNAUTHORIZED_EVENT, clearSession);
    return () => window.removeEventListener(UNAUTHORIZED_EVENT, clearSession);
  }, [clearSession]);

  const authenticate = useCallback(async (path: string, body: unknown, fallback: string) => {
    const response = await postJson(path, body);
    if (!response.ok) {
      throw new Error(await errorMessage(response, fallback));
    }
    setUser(await response.json());
    setStatus("authenticated");
  }, []);

  const login = useCallback(
    (identifier: string, password: string) =>
      authenticate("/api/auth/login", { identifier, password }, "Invalid username or password"),
    [authenticate],
  );

  const register = useCallback(
    (username: string, email: string, password: string) =>
      authenticate("/api/auth/register", { username, email, password }, "Registration failed"),
    [authenticate],
  );

  const logout = useCallback(async () => {
    try {
      await postJson("/api/auth/logout", {});
    } finally {
      clearSession();
    }
  }, [clearSession]);

  const value = useMemo(() => ({ user, status, login, register, logout }), [user, status, login, register, logout]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
}
