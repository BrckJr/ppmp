import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import RootLayout from "./routes/__root";
import IndexPage from "./routes/index";
import WatchlistPage from "./routes/watchlist";
import TransactionsPage from "./routes/transactions";
import RiskPage from "./routes/risk";
import PerformancePage from "./routes/performance";
import HoldingsPage from "./routes/holdings";
import HoldingsTickerPage from "./routes/holdings_ticker";
import "./styles.css";

const rootElement = document.getElementById("root");
if (!rootElement) {
  throw new Error("Root element #root not found");
}

ReactDOM.createRoot(rootElement).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route element={<RootLayout />}>
          <Route path="/" element={<IndexPage />} />
          <Route path="/watchlist" element={<WatchlistPage />} />
          <Route path="/transactions" element={<TransactionsPage />} />
          <Route path="/risk" element={<RiskPage />} />
          <Route path="/performance" element={<PerformancePage />} />
          <Route path="/holdings">
            <Route index element={<HoldingsPage />} />
            <Route path=":ticker" element={<HoldingsTickerPage />} />
          </Route>
        </Route>
        <Route path="*" element={<div className="p-8">Page Not Found</div>} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);