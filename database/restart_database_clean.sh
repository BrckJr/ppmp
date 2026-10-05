cd docker
docker compose down
docker volume rm docker_postgres_data
docker compose up -d
cd ..

# Roll out new database scheme
mvn clean compile \
-Dpostgresdb.url=jdbc:postgresql://localhost:5432/ppmp \
-Dpostgresdb.user=ppmp-bot \
-Dpostgresdb.password=secret \
-f pom.xml

# Instruments are protected by the authentication, so sign in as a dev-only seed user first.
# The backend must be running (dev profile) and already connected to the freshly migrated database.
SEED_USER="${SEED_USER:-seed}"
SEED_EMAIL="${SEED_EMAIL:-seed@ppmp.local}"
SEED_PASSWORD="${SEED_PASSWORD:-seed-password-dev-only}"
COOKIES="$(mktemp)"
trap 'rm -f "$COOKIES"' EXIT

curl -sS -o /dev/null -c "$COOKIES" -X POST localhost:8080/api/auth/register -H 'Content-Type: application/json' \
-d "{\"username\":\"$SEED_USER\",\"email\":\"$SEED_EMAIL\",\"password\":\"$SEED_PASSWORD\"}"
# Registration already signs in; fall back to login if the user exists (e.g. on a re-run without a clean database)
if ! grep -q ppmp_session "$COOKIES"; then
  curl -sS -o /dev/null -c "$COOKIES" -X POST localhost:8080/api/auth/login -H 'Content-Type: application/json' \
  -d "{\"identifier\":\"$SEED_USER\",\"password\":\"$SEED_PASSWORD\"}"
fi
grep -q ppmp_session "$COOKIES" || { echo "Could not authenticate the seed user - is the backend running?"; exit 1; }

# Add dummy instruments to the database
curl -sS -b "$COOKIES" -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Apple Inc.","ticker":"AAPL","currency":"USD","isin":"US0378331005",
  "exchange":"NASDAQ","country":"US","region":"US","sector":"TECHNOLOGY","type":"STOCK"}'

curl -sS -b "$COOKIES" -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Microsoft Corp.","ticker":"MSFT","currency":"USD","isin":"US5949181045",
  "exchange":"NASDAQ","country":"US","region":"US","sector":"TECHNOLOGY","type":"STOCK"}'

curl -sS -b "$COOKIES" -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"ASML Holding","ticker":"ASML","currency":"EUR","isin":"NL0010273215",
  "exchange":"Euronext Amsterdam","country":"NL","region":"EUROPE","sector":"TECHNOLOGY","type":"STOCK"}'

curl -sS -b "$COOKIES" -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Vanguard FTSE All-World UCITS ETF","ticker":"VWCE","currency":"EUR","isin":"IE00BK5BQT80",
  "exchange":"XETRA","region":"GLOBAL","type":"ETF"}'

curl -sS -b "$COOKIES" -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Bitcoin","ticker":"BTC","currency":"USD","type":"CRYPTO"}'