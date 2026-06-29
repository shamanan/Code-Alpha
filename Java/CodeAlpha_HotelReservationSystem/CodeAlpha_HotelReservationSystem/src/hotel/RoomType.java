package hotel;

public enum RoomType {
    STANDARD("Standard", 99.99),
    DELUXE("Deluxe", 179.99),
    SUITE("Suite", 299.99);

    private final String displayName;
    private final double pricePerNight;

    RoomType(String displayName, double pricePerNight) {
        this.displayName = displayName;
        this.pricePerNight = pricePerNight;
    }

    public String getDisplayName() { return displayName; }
    public double getPricePerNight() { return pricePerNight; }

    public static RoomType fromString(String s) {
        for (RoomType rt : values()) {
            if (rt.name().equalsIgnoreCase(s) || rt.displayName.equalsIgnoreCase(s)) return rt;
        }
        throw new IllegalArgumentException("Unknown room type: " + s);
    }

    @Override
    public String toString() { return displayName; }
}
