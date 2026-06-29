package hotel;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int roomNumber;
    private final RoomType type;
    private boolean available;
    private final int capacity;
    private final String amenities;

    public Room(int roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.available = true;
        switch (type) {
            case STANDARD:
                this.capacity = 2;
                this.amenities = "WiFi, TV, AC";
                break;
            case DELUXE:
                this.capacity = 3;
                this.amenities = "WiFi, TV, AC, Mini-bar, City View";
                break;
            case SUITE:
                this.capacity = 4;
                this.amenities = "WiFi, TV, AC, Mini-bar, Jacuzzi, Ocean View, Living Room";
                break;
            default:
                this.capacity = 2;
                this.amenities = "WiFi, TV";
        }
    }

    public int getRoomNumber()      { return roomNumber; }
    public RoomType getType()       { return type; }
    public boolean isAvailable()    { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getCapacity()        { return capacity; }
    public String getAmenities()    { return amenities; }
    public double getPricePerNight(){ return type.getPricePerNight(); }

    public String toSerialString() {
        return roomNumber + "," + type.name() + "," + available;
    }

    public static Room fromSerialString(String line) {
        String[] p = line.split(",");
        Room r = new Room(Integer.parseInt(p[0].trim()), RoomType.fromString(p[1].trim()));
        r.setAvailable(Boolean.parseBoolean(p[2].trim()));
        return r;
    }

    @Override
    public String toString() {
        return String.format("Room %-4d | %-8s | $%6.2f/night | Capacity: %d | %s | %s",
                roomNumber, type, type.getPricePerNight(), capacity, amenities,
                available ? "AVAILABLE" : "BOOKED");
    }
}
