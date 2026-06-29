package trading;

import java.util.Random;

public class Stock {
    private final String symbol;
    private final String companyName;
    private double currentPrice;
    private double previousPrice;
    private double openPrice;
    private final String sector;
    private final double volatility; // 0.01–0.05 typical
    private static final Random RNG = new Random();

    public Stock(String symbol, String companyName, double initialPrice,
                 String sector, double volatility) {
        this.symbol       = symbol;
        this.companyName  = companyName;
        this.currentPrice = initialPrice;
        this.previousPrice= initialPrice;
        this.openPrice    = initialPrice;
        this.sector       = sector;
        this.volatility   = volatility;
    }

    /** Simulate a random price movement (Gaussian walk). */
    public void simulatePriceChange() {
        previousPrice = currentPrice;
        double change = currentPrice * volatility * RNG.nextGaussian();
        currentPrice = Math.max(0.01, currentPrice + change);
        currentPrice = Math.round(currentPrice * 100.0) / 100.0;
    }

    public double getChange()        { return currentPrice - previousPrice; }
    public double getChangePercent() { return (getChange() / previousPrice) * 100.0; }
    public double getDayChange()     { return currentPrice - openPrice; }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getSymbol()       { return symbol; }
    public String getCompanyName()  { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public double getPreviousPrice(){ return previousPrice; }
    public String getSector()       { return sector; }

    public void setCurrentPrice(double p) {
        previousPrice = currentPrice;
        currentPrice  = Math.round(p * 100.0) / 100.0;
    }

    /** Compact save format:  SYMBOL|price|prevPrice|openPrice */
    public String toSerialString() {
        return symbol + "|" + currentPrice + "|" + previousPrice + "|" + openPrice;
    }

    /** Only restores prices; static stock metadata comes from StockMarket seed. */
    public void restorePrices(double current, double previous, double open) {
        this.currentPrice  = current;
        this.previousPrice = previous;
        this.openPrice     = open;
    }

    @Override
    public String toString() {
        String arrow = getChange() >= 0 ? "▲" : "▼";
        return String.format("%-6s %-28s $%9.2f  %s %+6.2f (%+.2f%%)  [%s]",
                symbol, companyName, currentPrice,
                arrow, getChange(), getChangePercent(), sector);
    }
}
