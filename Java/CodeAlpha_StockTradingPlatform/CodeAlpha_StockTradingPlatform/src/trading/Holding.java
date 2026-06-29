package trading;

public class Holding {
    private final String symbol;
    private int    quantity;
    private double avgBuyPrice;
    private double totalInvested;

    public Holding(String symbol, int quantity, double buyPrice) {
        this.symbol        = symbol;
        this.quantity      = quantity;
        this.avgBuyPrice   = buyPrice;
        this.totalInvested = quantity * buyPrice;
    }

    /** Add more shares (updates weighted average). */
    public void addShares(int qty, double price) {
        totalInvested += qty * price;
        quantity      += qty;
        avgBuyPrice    = totalInvested / quantity;
    }

    /** Remove shares; returns false if insufficient. */
    public boolean removeShares(int qty) {
        if (qty > quantity) return false;
        double portionSold = (double) qty / quantity;
        totalInvested -= totalInvested * portionSold;
        quantity      -= qty;
        return true;
    }

    public double getPnL(double currentPrice) {
        return (currentPrice - avgBuyPrice) * quantity;
    }

    public double getPnLPercent(double currentPrice) {
        return ((currentPrice - avgBuyPrice) / avgBuyPrice) * 100.0;
    }

    public double getMarketValue(double currentPrice) {
        return currentPrice * quantity;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public String getSymbol()        { return symbol; }
    public int    getQuantity()      { return quantity; }
    public double getAvgBuyPrice()   { return avgBuyPrice; }
    public double getTotalInvested() { return totalInvested; }

    /** SYMBOL,qty,avgBuy,totalInvested */
    public String toSerialString() {
        return symbol + "," + quantity + "," +
               String.format("%.4f", avgBuyPrice) + "," +
               String.format("%.4f", totalInvested);
    }

    public static Holding fromSerialString(String line) {
        String[] p = line.split(",");
        Holding h = new Holding(p[0], Integer.parseInt(p[1]), Double.parseDouble(p[2]));
        h.totalInvested = Double.parseDouble(p[3]);
        return h;
    }
}
