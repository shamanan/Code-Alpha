package trading;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum Type { BUY, SELL }

    private final Type          type;
    private final String        symbol;
    private final int           quantity;
    private final double        price;
    private final double        total;
    private final LocalDateTime timestamp;

    public Transaction(Type type, String symbol, int quantity, double price) {
        this.type      = type;
        this.symbol    = symbol;
        this.quantity  = quantity;
        this.price     = price;
        this.total     = quantity * price;
        this.timestamp = LocalDateTime.now();
    }

    // private constructor for deserialization
    private Transaction(Type type, String symbol, int quantity,
                        double price, LocalDateTime ts) {
        this.type      = type;
        this.symbol    = symbol;
        this.quantity  = quantity;
        this.price     = price;
        this.total     = quantity * price;
        this.timestamp = ts;
    }

    public Type   getType()     { return type; }
    public String getSymbol()   { return symbol; }
    public int    getQuantity() { return quantity; }
    public double getPrice()    { return price; }
    public double getTotal()    { return total; }

    /** TYPE|SYMBOL|qty|price|timestamp */
    public String toSerialString() {
        return type + "|" + symbol + "|" + quantity + "|" +
               String.format("%.4f", price) + "|" + timestamp.format(FMT);
    }

    public static Transaction fromSerialString(String line) {
        String[] p = line.split("\\|");
        return new Transaction(
            Type.valueOf(p[0]), p[1],
            Integer.parseInt(p[2]),
            Double.parseDouble(p[3]),
            LocalDateTime.parse(p[4], FMT)
        );
    }

    @Override
    public String toString() {
        return String.format("[%s] %-4s %-6s  %4d shares @ $%8.2f  = $%10.2f",
                timestamp.format(FMT), type, symbol, quantity, price, total);
    }
}
