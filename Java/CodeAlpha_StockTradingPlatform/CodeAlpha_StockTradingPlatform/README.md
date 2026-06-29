# 📈 CodeAlpha_StockTradingPlatform

A full-featured **Java console application** simulating a real stock trading platform — with live price simulation, portfolio management, and persistent user accounts.

---

## 🚀 Features

| Feature | Description |
|---|---|
| 👤 Account System | Create accounts with username, password, and initial deposit |
| 🔐 Secure Login | Password hashing; sessions persist across restarts |
| 📊 Live Market | 20 real-world stocks across 5 sectors with price simulation |
| 📉 Price Simulation | Gaussian random walk (realistic volatility per stock) |
| 💰 Buy Stocks | Purchase any quantity with real-time price and balance check |
| 💸 Sell Stocks | Sell shares; updates cash balance and P&L instantly |
| 📂 Portfolio View | Holdings with avg buy price, market value, and P&L |
| 🏦 Net Worth | Cash + portfolio value + total return breakdown |
| 📜 Trade History | Full transaction log (type, symbol, qty, price, timestamp) |
| 💳 Deposit Funds | Add cash to account at any time |
| 🔄 Market Refresh | Simulate market ticks (prices move on demand) |
| 📈 Top Movers | View top 5 gainers and losers at any time |
| 💾 File Persistence | Portfolio and transaction history saved per user |

---

## 🏗️ Project Structure

```
CodeAlpha_StockTradingPlatform/
├── src/
│   └── trading/
│       ├── Main.java              # Entry point
│       ├── ConsoleUI.java         # Menu-driven interface (13 options)
│       ├── StockMarket.java       # Market with 20 stocks; tick/gainers/losers
│       ├── Stock.java             # Stock entity with Gaussian price simulation
│       ├── Holding.java           # Portfolio position (qty, avg buy, P&L)
│       ├── Transaction.java       # Trade log entry (BUY/SELL)
│       ├── UserAccount.java       # User entity (auth, portfolio, balance)
│       ├── FileManager.java       # File I/O persistence per user
│       └── TradingException.java  # Custom domain exception
├── out/                           # Compiled .class files (generated)
├── data/
│   └── users/                     # One .txt file per user (generated)
│       └── <username>.txt
├── build.sh                       # Linux/Mac build script
├── run.sh                         # Linux/Mac run script
├── run.bat                        # Windows run script
└── README.md
```

---

## ⚙️ Getting Started

### Prerequisites
- **Java 11+** (check: `java -version`)
- **JDK** with `javac` for compilation

### Compile

**Linux / macOS:**
```bash
chmod +x build.sh run.sh
./build.sh
```

**Windows:**
```cmd
mkdir out
javac -d out src\trading\*.java
```

**Manual:**
```bash
mkdir -p out
javac -d out src/trading/*.java
```

### Run

**Linux / macOS:**
```bash
./run.sh
```

**Windows:**
```cmd
run.bat
```

**Manual:**
```bash
java -cp out trading.Main
```

---

## 🖥️ Application Flow

```
╔══════════════════════════════════════════════════════════╗
║       CODEALPHA STOCK TRADING PLATFORM  📈               ║
╚══════════════════════════════════════════════════════════╝

WELCOME MENU              TRADING DASHBOARD
─────────────             ─────────────────────────────────
1. Create Account         1.  View Market (all stocks)
2. Login                  2.  View Market (by sector)
3. Exit                   3.  Stock Quote
                          4.  Buy Stock
                          5.  Sell Stock
                          6.  View Portfolio
                          7.  Account Balance & Net Worth
                          8.  Transaction History
                          9.  Deposit Funds
                          10. Refresh Market Prices
                          11. Top Gainers & Losers
                          12. Logout
                          13. Exit
```

---

## 📊 Stock Universe (20 Securities)

| Sector | Tickers |
|---|---|
| Technology | AAPL, MSFT, GOOGL, META, NVDA, TSLA |
| Finance | JPM, BAC, GS |
| Healthcare | JNJ, PFE, UNH |
| Consumer | AMZN, WMT, KO |
| Energy | XOM, CVX |
| ETF | SPY, QQQ, DIA |

Each stock has a **volatility coefficient** tuned to real-world behavior (e.g. TSLA 4%, NVDA 3.5%, KO 0.8%).

---

## 💾 Data Persistence

User data is stored in `data/users/<username>.txt`:

```
testuser|5f4dcc3b|44092.83|2026-06-29
HOLDING:AAPL,7,188.3600,1318.5200
HOLDING:NVDA,5,917.7300,4588.6500
TXN:BUY|AAPL|10|188.3600|2026-06-29 14:22:11
TXN:SELL|AAPL|3|188.3600|2026-06-29 14:22:12
TXN:BUY|NVDA|5|917.7300|2026-06-29 14:22:13
```

Data is saved automatically after every buy, sell, deposit, and logout.

---

## 🧩 OOP Design

| Principle | Implementation |
|---|---|
| Encapsulation | All fields private; accessed through typed methods |
| Abstraction | `StockMarket` hides simulation; `FileManager` hides I/O |
| Custom Exception | `TradingException` for all domain-level errors |
| Enum | `Transaction.Type` (BUY/SELL) |
| Composition | `UserAccount` owns `Map<String, Holding>` + `List<Transaction>` |
| Single Responsibility | Each class has one clear job |

---

## 📐 Price Simulation Algorithm

Each `Stock.simulatePriceChange()` call applies a **Gaussian random walk**:

```
new_price = current_price + (current_price × volatility × N(0,1))
```

- `N(0,1)` = standard normal random value
- `volatility` = per-stock constant (0.008–0.04)
- Price is floored at `$0.01` to prevent negative values
- Rounded to 2 decimal places after each tick

---

## 🛠️ Tech Stack

- **Language:** Java 11+
- **I/O:** `java.io` (BufferedReader, PrintWriter, File)
- **Time:** `java.time.LocalDateTime`
- **RNG:** `java.util.Random` (Gaussian distribution)
- **Collections:** `java.util` (LinkedHashMap, ArrayList, Optional)
- **No external dependencies**

---

## 👨‍💻 Author

Developed as part of the **CodeAlpha Java Internship** program.

---

## 📄 License

This project is open-source and available under the MIT License.
