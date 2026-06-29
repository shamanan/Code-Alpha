package trading;

import java.util.*;

public class ConsoleUI {
    private final StockMarket market      = new StockMarket();
    private final FileManager fm          = new FileManager();
    private final Scanner     sc          = new Scanner(System.in);
    private UserAccount       currentUser = null;

    // ═══════════════════════════════════════════════════════════════════════════
    //  Entry point
    // ═══════════════════════════════════════════════════════════════════════════

    public void start() {
        printBanner();
        // Do an initial market tick so prices look realistic from the start
        market.tick(3);

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = authMenu();
            } else {
                running = tradingMenu();
            }
        }
        System.out.println("\n  Thank you for using CodeAlpha Stock Trading Platform. Goodbye!\n");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  Auth menu
    // ═══════════════════════════════════════════════════════════════════════════

    private boolean authMenu() {
        System.out.println("\n┌────────────────────────────────┐");
        System.out.println("│         WELCOME MENU           │");
        System.out.println("├────────────────────────────────┤");
        System.out.println("│  1. Create Account             │");
        System.out.println("│  2. Login                      │");
        System.out.println("│  3. Exit                       │");
        System.out.println("└────────────────────────────────┘");
        System.out.print("  Choice: ");

        switch (sc.nextLine().trim()) {
            case "1": createAccount();  break;
            case "2": login();          break;
            case "3": return false;
            default:  System.out.println("  [!] Invalid option.");
        }
        return true;
    }

    private void createAccount() {
        System.out.println("\n── Create Account ─────────────────────────────────────");
        System.out.print("  Username    : ");
        String username = sc.nextLine().trim();
        if (username.isEmpty() || !username.matches("[A-Za-z0-9_]{3,20}")) {
            System.out.println("  [!] Username must be 3-20 alphanumeric chars.");
            return;
        }
        if (fm.userExists(username)) {
            System.out.println("  [!] Username '" + username + "' is already taken.");
            return;
        }
        System.out.print("  Password    : ");
        String password = sc.nextLine().trim();
        if (password.length() < 4) {
            System.out.println("  [!] Password must be at least 4 characters.");
            return;
        }
        System.out.print("  Initial Deposit ($): ");
        double deposit;
        try {
            deposit = Double.parseDouble(sc.nextLine().trim());
            if (deposit < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid deposit amount.");
            return;
        }

        UserAccount acc = new UserAccount(username, password, deposit);
        fm.saveUser(acc);
        System.out.println("\n  ✔  Account created! Welcome, " + username + "!");
        System.out.printf("     Starting balance: $%.2f%n", deposit);
        currentUser = acc;
    }

    private void login() {
        System.out.println("\n── Login ───────────────────────────────────────────────");
        System.out.print("  Username: ");
        String username = sc.nextLine().trim();
        System.out.print("  Password: ");
        String password = sc.nextLine().trim();

        Optional<UserAccount> opt = fm.loadUser(username);
        if (opt.isEmpty() || !opt.get().checkPassword(password)) {
            System.out.println("  [!] Invalid username or password.");
            return;
        }
        currentUser = opt.get();
        System.out.println("\n  ✔  Welcome back, " + currentUser.getUsername() + "!");
        // Tick the market on login (simulates time passing)
        market.tick(5);
        System.out.println("  [Market updated — prices refreshed]");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  Trading menu
    // ═══════════════════════════════════════════════════════════════════════════

    private boolean tradingMenu() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│        TRADING DASHBOARD         │");
        System.out.printf( "│  User: %-24s│%n", currentUser.getUsername());
        System.out.printf( "│  Cash: $%-23.2f│%n", currentUser.getCashBalance());
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1.  View Market (all stocks)    │");
        System.out.println("│  2.  View Market (by sector)     │");
        System.out.println("│  3.  Stock Quote                 │");
        System.out.println("│  4.  Buy Stock                   │");
        System.out.println("│  5.  Sell Stock                  │");
        System.out.println("│  6.  View Portfolio              │");
        System.out.println("│  7.  Account Balance & Net Worth │");
        System.out.println("│  8.  Transaction History         │");
        System.out.println("│  9.  Deposit Funds               │");
        System.out.println("│  10. Refresh Market Prices       │");
        System.out.println("│  11. Top Gainers & Losers        │");
        System.out.println("│  12. Logout                      │");
        System.out.println("│  13. Exit                        │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("  Choice: ");

        switch (sc.nextLine().trim()) {
            case "1":  viewMarket();          break;
            case "2":  viewMarketBySector();  break;
            case "3":  stockQuote();          break;
            case "4":  buyStock();            break;
            case "5":  sellStock();           break;
            case "6":  viewPortfolio();       break;
            case "7":  viewBalance();         break;
            case "8":  viewHistory();         break;
            case "9":  deposit();             break;
            case "10": refreshMarket();       break;
            case "11": topMovers();           break;
            case "12": logout();             break;
            case "13": return false;
            default:   System.out.println("  [!] Invalid option.");
        }
        return true;
    }

    // ── 1. View Market ────────────────────────────────────────────────────────

    private void viewMarket() {
        System.out.println("\n── Live Market ────────────────────────────────────────────────────────────");
        System.out.printf("%-6s %-28s %10s  %7s %8s  [Sector]%n",
                "SYMB", "Company", "Price", "Chg", "Chg%");
        System.out.println("─".repeat(76));
        market.getAllStocks().forEach(s -> System.out.println(s));
        System.out.println("─".repeat(76));
        System.out.println("  " + market.getAllStocks().size() + " securities listed.");
    }

    // ── 2. View by Sector ─────────────────────────────────────────────────────

    private void viewMarketBySector() {
        Map<String, List<Stock>> bySector = new LinkedHashMap<>();
        for (Stock s : market.getAllStocks()) {
            bySector.computeIfAbsent(s.getSector(), k -> new ArrayList<>()).add(s);
        }
        System.out.println("\n── Market by Sector ───────────────────────────────────────────────────────");
        bySector.forEach((sector, stocks) -> {
            System.out.println("\n  ▶ " + sector);
            System.out.println("  " + "─".repeat(72));
            stocks.forEach(s -> System.out.println("  " + s));
        });
    }

    // ── 3. Stock Quote ────────────────────────────────────────────────────────

    private void stockQuote() {
        System.out.print("\n  Enter symbol: ");
        String sym = sc.nextLine().trim().toUpperCase();
        Stock s = market.getStock(sym);
        if (s == null) { System.out.println("  [!] Symbol not found."); return; }

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.printf("  %-6s — %s%n", s.getSymbol(), s.getCompanyName());
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf("  Current Price : $%.2f%n",  s.getCurrentPrice());
        System.out.printf("  Previous      : $%.2f%n",  s.getPreviousPrice());
        System.out.printf("  Change        : %+.2f (%+.2f%%)%n",
                s.getChange(), s.getChangePercent());
        System.out.printf("  Sector        : %s%n", s.getSector());

        Holding h = currentUser.getPortfolio().get(sym);
        if (h != null) {
            System.out.println("──────────────────────────────────────────");
            System.out.printf("  Your Shares   : %d%n", h.getQuantity());
            System.out.printf("  Avg Buy Price : $%.2f%n", h.getAvgBuyPrice());
            System.out.printf("  Market Value  : $%.2f%n", h.getMarketValue(s.getCurrentPrice()));
            System.out.printf("  P&L           : %+.2f (%+.2f%%)%n",
                    h.getPnL(s.getCurrentPrice()), h.getPnLPercent(s.getCurrentPrice()));
        }
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ── 4. Buy Stock ──────────────────────────────────────────────────────────

    private void buyStock() {
        System.out.println("\n── Buy Stock ───────────────────────────────────────────");
        System.out.print("  Symbol  : ");
        String sym = sc.nextLine().trim().toUpperCase();
        Stock s = market.getStock(sym);
        if (s == null) { System.out.println("  [!] Symbol not found."); return; }

        System.out.printf("  Current price of %s: $%.2f%n", sym, s.getCurrentPrice());
        System.out.printf("  Your cash balance  : $%.2f%n", currentUser.getCashBalance());
        System.out.print("  Quantity to buy    : ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid quantity.");
            return;
        }

        double total = qty * s.getCurrentPrice();
        System.out.printf("%n  Order Summary:%n");
        System.out.printf("    Buy %d x %s @ $%.2f = $%.2f%n",
                qty, sym, s.getCurrentPrice(), total);
        System.out.print("  Confirm? (y/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("  Order cancelled.");
            return;
        }

        try {
            currentUser.buy(sym, qty, s.getCurrentPrice());
            fm.saveUser(currentUser);
            System.out.printf("  ✔  Bought %d shares of %s. Cash remaining: $%.2f%n",
                    qty, sym, currentUser.getCashBalance());
        } catch (TradingException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ── 5. Sell Stock ─────────────────────────────────────────────────────────

    private void sellStock() {
        System.out.println("\n── Sell Stock ──────────────────────────────────────────");
        if (currentUser.getPortfolio().isEmpty()) {
            System.out.println("  Your portfolio is empty.");
            return;
        }
        System.out.print("  Symbol  : ");
        String sym = sc.nextLine().trim().toUpperCase();
        Stock s = market.getStock(sym);
        if (s == null) { System.out.println("  [!] Symbol not found."); return; }

        Holding h = currentUser.getPortfolio().get(sym);
        if (h == null) {
            System.out.println("  [!] You don't own any shares of " + sym + ".");
            return;
        }

        System.out.printf("  You own %d shares of %s @ current $%.2f%n",
                h.getQuantity(), sym, s.getCurrentPrice());
        System.out.printf("  Market value: $%.2f  |  P&L: %+.2f%n",
                h.getMarketValue(s.getCurrentPrice()), h.getPnL(s.getCurrentPrice()));
        System.out.print("  Quantity to sell: ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid quantity.");
            return;
        }

        double total = qty * s.getCurrentPrice();
        System.out.printf("%n  Order Summary:%n");
        System.out.printf("    Sell %d x %s @ $%.2f = $%.2f%n",
                qty, sym, s.getCurrentPrice(), total);
        System.out.print("  Confirm? (y/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("  Order cancelled.");
            return;
        }

        try {
            currentUser.sell(sym, qty, s.getCurrentPrice());
            fm.saveUser(currentUser);
            System.out.printf("  ✔  Sold %d shares of %s. Cash balance: $%.2f%n",
                    qty, sym, currentUser.getCashBalance());
        } catch (TradingException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ── 6. Portfolio ──────────────────────────────────────────────────────────

    private void viewPortfolio() {
        System.out.println("\n── Portfolio ───────────────────────────────────────────────────────────────");
        Map<String, Holding> portfolio = currentUser.getPortfolio();
        if (portfolio.isEmpty()) {
            System.out.println("  No holdings. Buy some stocks to get started!");
            return;
        }

        System.out.printf("%-6s %-28s %5s %10s %10s %10s %8s%n",
                "SYMB", "Company", "Qty", "AvgBuy", "CurPrice", "MktValue", "P&L");
        System.out.println("─".repeat(84));

        double totalValue    = 0;
        double totalInvested = 0;
        double totalPnL      = 0;

        for (Holding h : portfolio.values()) {
            Stock s = market.getStock(h.getSymbol());
            if (s == null) continue;
            double mktVal = h.getMarketValue(s.getCurrentPrice());
            double pnl    = h.getPnL(s.getCurrentPrice());
            totalValue    += mktVal;
            totalInvested += h.getTotalInvested();
            totalPnL      += pnl;

            System.out.printf("%-6s %-28s %5d %10.2f %10.2f %10.2f %+8.2f%n",
                    h.getSymbol(),
                    truncate(s.getCompanyName(), 26),
                    h.getQuantity(),
                    h.getAvgBuyPrice(),
                    s.getCurrentPrice(),
                    mktVal,
                    pnl);
        }

        System.out.println("─".repeat(84));
        System.out.printf("  Portfolio Value   : $%.2f%n", totalValue);
        System.out.printf("  Total Invested    : $%.2f%n", totalInvested);
        System.out.printf("  Total P&L         : %+.2f (%.2f%%)%n",
                totalPnL, totalInvested > 0 ? (totalPnL / totalInvested) * 100 : 0);
        System.out.printf("  Cash Balance      : $%.2f%n", currentUser.getCashBalance());
        System.out.printf("  Net Worth         : $%.2f%n",
                currentUser.getNetWorth(market));
    }

    // ── 7. Balance ────────────────────────────────────────────────────────────

    private void viewBalance() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.printf( "  Account Owner  : %s%n", currentUser.getUsername());
        System.out.printf( "  Member Since   : %s%n", currentUser.getCreatedAt());
        System.out.println("══════════════════════════════════════════════");
        System.out.printf( "  Cash Balance   : $%.2f%n", currentUser.getCashBalance());
        System.out.printf( "  Portfolio Value: $%.2f%n",
                currentUser.getPortfolioValue(market));
        System.out.printf( "  Net Worth      : $%.2f%n",
                currentUser.getNetWorth(market));
        System.out.printf( "  Total P&L      : %+.2f%n",
                currentUser.getTotalPnL(market));
        System.out.println("╚════════════════════════════════════════════╝");
    }

    // ── 8. History ────────────────────────────────────────────────────────────

    private void viewHistory() {
        System.out.println("\n── Transaction History ─────────────────────────────────");
        List<Transaction> hist = currentUser.getHistory();
        if (hist.isEmpty()) { System.out.println("  No transactions yet."); return; }

        // Show most recent 30
        int start = Math.max(0, hist.size() - 30);
        System.out.println("  (Showing last " + Math.min(30, hist.size()) + " of " +
                hist.size() + " transactions)\n");
        for (int i = hist.size() - 1; i >= start; i--) {
            System.out.println("  " + hist.get(i));
        }
    }

    // ── 9. Deposit ────────────────────────────────────────────────────────────

    private void deposit() {
        System.out.println("\n── Deposit Funds ───────────────────────────────────────");
        System.out.printf("  Current balance: $%.2f%n", currentUser.getCashBalance());
        System.out.print("  Amount to deposit: $");
        try {
            double amount = Double.parseDouble(sc.nextLine().trim());
            currentUser.deposit(amount);
            fm.saveUser(currentUser);
            System.out.printf("  ✔  Deposited $%.2f. New balance: $%.2f%n",
                    amount, currentUser.getCashBalance());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid amount.");
        } catch (TradingException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ── 10. Refresh Market ────────────────────────────────────────────────────

    private void refreshMarket() {
        System.out.print("  Simulating market movement...");
        market.tick(10);
        System.out.println(" done! Prices updated.");
    }

    // ── 11. Top Movers ────────────────────────────────────────────────────────

    private void topMovers() {
        System.out.println("\n── Top Gainers ─────────────────────────────────────────");
        market.getTopGainers(5).forEach(s ->
            System.out.printf("  %-6s %-26s $%8.2f  ▲ %+.2f%%%n",
                    s.getSymbol(), truncate(s.getCompanyName(), 24),
                    s.getCurrentPrice(), s.getChangePercent()));

        System.out.println("\n── Top Losers ──────────────────────────────────────────");
        market.getTopLosers(5).forEach(s ->
            System.out.printf("  %-6s %-26s $%8.2f  ▼ %+.2f%%%n",
                    s.getSymbol(), truncate(s.getCompanyName(), 24),
                    s.getCurrentPrice(), s.getChangePercent()));
    }

    // ── 12. Logout ────────────────────────────────────────────────────────────

    private void logout() {
        fm.saveUser(currentUser);
        System.out.println("  ✔  Logged out. Portfolio saved.");
        currentUser = null;
        market.tick(3);     // market moves while user is away
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void printBanner() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       CODEALPHA STOCK TRADING PLATFORM  📈               ║");
        System.out.println("║    Real-time simulated market. Learn to invest wisely.   ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
