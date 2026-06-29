package trading;

import java.util.*;

public class UserAccount {
    private final String username;
    private String       passwordHash;   // simple SHA-style hash (simulated)
    private double       cashBalance;
    private final Map<String, Holding> portfolio = new LinkedHashMap<>();
    private final List<Transaction>    history   = new ArrayList<>();
    private final String               createdAt;

    public UserAccount(String username, String password, double initialDeposit) {
        this.username     = username;
        this.passwordHash = hashPassword(password);
        this.cashBalance  = initialDeposit;
        this.createdAt    = java.time.LocalDate.now().toString();
    }

    // package-private constructor for deserialization
    UserAccount(String username, String passwordHash,
                double cash, String createdAt, boolean raw) {
        this.username     = username;
        this.passwordHash = passwordHash;
        this.cashBalance  = cash;
        this.createdAt    = createdAt;
    }

    // ── Auth ─────────────────────────────────────────────────────────────────

    public boolean checkPassword(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    private static String hashPassword(String pw) {
        // Simple deterministic hash (not cryptographic — demo only)
        int h = 0;
        for (char c : pw.toCharArray()) h = h * 31 + c;
        return Integer.toHexString(h);
    }

    // ── Trading operations ────────────────────────────────────────────────────

    public void buy(String symbol, int qty, double price) throws TradingException {
        double cost = qty * price;
        if (cost > cashBalance)
            throw new TradingException(String.format(
                "Insufficient funds. Need $%.2f, have $%.2f", cost, cashBalance));

        cashBalance -= cost;
        portfolio.compute(symbol, (k, h) -> {
            if (h == null) return new Holding(symbol, qty, price);
            h.addShares(qty, price);
            return h;
        });
        history.add(new Transaction(Transaction.Type.BUY, symbol, qty, price));
    }

    public void sell(String symbol, int qty, double price) throws TradingException {
        Holding h = portfolio.get(symbol);
        if (h == null || h.getQuantity() < qty)
            throw new TradingException(
                "Insufficient shares. You own " +
                (h == null ? 0 : h.getQuantity()) + " share(s) of " + symbol);

        h.removeShares(qty);
        if (h.getQuantity() == 0) portfolio.remove(symbol);
        cashBalance += qty * price;
        history.add(new Transaction(Transaction.Type.SELL, symbol, qty, price));
    }

    public void deposit(double amount) throws TradingException {
        if (amount <= 0) throw new TradingException("Deposit amount must be positive.");
        cashBalance += amount;
    }

    // ── Portfolio metrics ──────────────────────────────────────────────────────

    public double getPortfolioValue(StockMarket market) {
        double total = 0;
        for (Holding h : portfolio.values()) {
            Stock s = market.getStock(h.getSymbol());
            if (s != null) total += h.getMarketValue(s.getCurrentPrice());
        }
        return total;
    }

    public double getTotalPnL(StockMarket market) {
        double pnl = 0;
        for (Holding h : portfolio.values()) {
            Stock s = market.getStock(h.getSymbol());
            if (s != null) pnl += h.getPnL(s.getCurrentPrice());
        }
        return pnl;
    }

    public double getNetWorth(StockMarket market) {
        return cashBalance + getPortfolioValue(market);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public String   getUsername()              { return username; }
    public double   getCashBalance()           { return cashBalance; }
    public String   getCreatedAt()             { return createdAt; }
    public String   getPasswordHash()          { return passwordHash; }
    public Map<String, Holding> getPortfolio() { return Collections.unmodifiableMap(portfolio); }
    public List<Transaction>    getHistory()   { return Collections.unmodifiableList(history); }
    public Map<String, Holding> portfolioMutable() { return portfolio; }
    public List<Transaction>    historyMutable()   { return history; }
}
