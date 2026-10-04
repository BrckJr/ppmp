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

# Add dummy instruments to the database
curl -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Apple Inc.","ticker":"AAPL","currency":"USD","isin":"US0378331005",
  "exchange":"NASDAQ","country":"US","region":"US","sector":"TECHNOLOGY","type":"STOCK"}'

curl -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Microsoft Corp.","ticker":"MSFT","currency":"USD","isin":"US5949181045",
  "exchange":"NASDAQ","country":"US","region":"US","sector":"TECHNOLOGY","type":"STOCK"}'

curl -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"ASML Holding","ticker":"ASML","currency":"EUR","isin":"NL0010273215",
  "exchange":"Euronext Amsterdam","country":"NL","region":"EUROPE","sector":"TECHNOLOGY","type":"STOCK"}'

curl -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Vanguard FTSE All-World UCITS ETF","ticker":"VWCE","currency":"EUR","isin":"IE00BK5BQT80",
  "exchange":"XETRA","region":"GLOBAL","type":"ETF"}'

curl -X POST localhost:8080/api/instruments -H 'Content-Type: application/json' -d '{
  "name":"Bitcoin","ticker":"BTC","currency":"USD","type":"CRYPTO"}'