import { client } from "../api/generated/client.gen";

export const UNAUTHORIZED_EVENT = "ppmp:unauthorized";

// The session lives in an HttpOnly cookie, so requests only need to stay same-origin (Vite proxies /api).
client.setConfig({ baseUrl: "", credentials: "same-origin" });

function notifyUnauthorized() {
  window.dispatchEvent(new Event(UNAUTHORIZED_EVENT));
}

client.interceptors.response.use((response) => {
  if (response.status === 401) notifyUnauthorized();
  return response;
});

/** fetch() for the API that signals an expired or missing session to the app. */
export async function apiFetch(input: string, init?: RequestInit): Promise<Response> {
  const response = await fetch(input, { credentials: "same-origin", ...init });
  if (response.status === 401) notifyUnauthorized();
  return response;
}
