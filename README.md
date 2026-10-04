# ppmp

Personal Portfolio Management Platform

## Authentication & data isolation

- Users register / sign in via `/api/auth/register` and `/api/auth/login` (username or email + password, bcrypt).
- A successful login sets a signed JWT in an **HttpOnly, SameSite=Strict** cookie (`ppmp_session`, 8h). It is never
  readable by JavaScript. Every other `/api/*` endpoint requires a valid session (HTTP permission policy, deny by
  default).
- The acting user is **only** taken from the verified token (`CurrentUser` in the app layer) and passed to the domain
  services. No endpoint accepts a user id, and every repository query / ownership check is filtered by it. Foreign
  resources are answered with `404`.
- Failed logins are rate limited per account (5 per 15 minutes, in memory).

Configuration (backend):

| Property / env var           | Purpose                                                                                                           |
|------------------------------|-------------------------------------------------------------------------------------------------------------------|
| `PPMP_JWT_SECRET`            | **Required outside dev/test.** Base64url key, >= 32 bytes: `openssl rand -base64 48 \| tr '+/' '-_' \| tr -d '='` |
| `ppmp.auth.cookie-secure`    | `true` by default (HTTPS only); `false` in the dev profile                                                        |
| `ppmp.auth.session-duration` | Session lifetime, default `PT8H`                                                                                  |
