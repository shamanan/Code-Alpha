package trading;

import java.util.*;
import java.util.stream.Collectors;

public class StockMarket {
    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    public StockMarket() {
        seed();
    }

    private void seed() {
        // Tech
        add("AAPL",  "Apple Inc.",             182.52, "Technology",  0.018);
        add("MSFT",  "Microsoft Corp.",         415.30, "Technology",  0.015);
        add("GOOGL", "Alphabet Inc.",           175.40, "Technology",  0.020);
        add("META",  "Meta Platforms Inc.",     505.10, "Technology",  0.025);
        add("NVDA",  "NVIDIA Corporation",      875.40, "Technology",  0.035);
        add("TSLA",  "Tesla Inc.",              175.20, "Technology",  0.040);
        // Finance
        add("JPM",   "JPMorgan Chase & Co.",   198.60, "Finance",     0.014);
        add("BAC",   "Bank of America Corp.",   36.80, "Finance",     0.016);
        add("GS",    "Goldman Sachs Group.",   460.90, "Finance",     0.018);
        // Healthcare
        add("JNJ",   "Johnson & Johnson",       155.30, "Healthcare",  0.012);
        add("PFE",   "Pfizer Inc.",              28.40, "Healthcare",  0.020);
        add("UNH",   "UnitedHealth Group",      520.80, "Healthcare",  0.015);
        // Consumer
        add("AMZN",  "Amazon.com Inc.",         185.30, "Consumer",    0.022);
        add("WMT",   "Walmart Inc.",             67.40, "Consumer",    0.010);
        add("KO",    "The Coca-Cola Co.",        61.20, "Consumer",    0.008);
        // Energy
        add("XOM",   "ExxonMobil Corp.",        112.30, "Energy",      0.020);
        add("CVX",   "Chevron Corporation",     157.60, "Energy",      0.018);
        // Index ETFs
        add("SPY",   "S&P 500 ETF Trust",       515.80, "ETF",         0.010);
        add("QQQ",   "Invesco QQQ Trust",       437.90, "ETF",         0.015);
        add("DIA",   "SPDR Dow Jones ETF",      386.40, "ETF",         0.008);
    }

    private void add(String sym, String name, double price, String sector, double vol) {
        stocks.put(sym, new Stock(sym, name, price, sector, vol));
    }

    /** Simulate one market tick — all stocks move. */
    public void tick() {
        stocks.values().forEach(Stock::simulatePriceChange);
    }

    /** Simulate N ticks. */
    public void tick(int n) {
        for (int i = 0; i < n; i++) tick();
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public Collection<Stock> getAllStocks() {
        return Collections.unmodifiableCollection(stocks.values());
    }

    public List<Stock> getByVolume() {
        return stocks.values().stream()
            .sorted(Comparator.comparingDouble(s -> Math.abs(s.getChangePercent())))
            .collect(Collectors.toList());
    }

    public List<Stock> getTopGainers(int n) {
        return stocks.values().stream()
            .sorted((a, b) -> Double.compare(b.getChangePercent(), a.getChangePercent()))
            .limit(n).collect(Collectors.toList());
    }

    public List<Stock> getTopLosers(int n) {
        return stocks.values().stream()
            .sorted(Comparator.comparingDouble(Stock::getChangePercent))
            .limit(n).collect(Collectors.toList());
    }

    public Map<String, Stock> getStocksMap() { return Collections.unmodifiableMap(stocks); }
}
