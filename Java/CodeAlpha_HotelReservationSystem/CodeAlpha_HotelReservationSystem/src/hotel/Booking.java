package hotel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public enum Status { CONFIRMED, CANCELLED, CHECKED_IN, CHECKED_OUT }

    private final String bookingId;
    private final Guest  guest;
    private final Room   room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private Status status;
    private boolean paid;
    private final LocalDate bookedOn;

    public Booking(String bookingId, Guest guest, Room room,
                   LocalDate checkIn, LocalDate checkOut) {
        this.bookingId = bookingId;
        this.guest     = guest;
        this.room      = room;
        this.checkIn   = checkIn;
        this.checkOut  = checkOut;
        this.status    = Status.CONFIRMED;
        this.paid      = false;
        this.bookedOn  = LocalDate.now();
    }

    public String    getBookingId() { return bookingId; }
    public Guest     getGuest()     { return guest; }
    public Room      getRoom()      { return room; }
    public LocalDate getCheckIn()   { return checkIn; }
    public LocalDate getCheckOut()  { return checkOut; }
    public Status    getStatus()    { return status; }
    public void      setStatus(Status s) { this.status = s; }
    public boolean   isPaid()       { return paid; }
    public void      setPaid(boolean paid) { this.paid = paid; }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public double getTotalCost() {
        return getNights() * room.getPricePerNight();
    }

    /** CSV line: bookingId|guestSerial|roomSerial|checkIn|checkOut|status|paid|bookedOn */
    public String toSerialString() {
        return bookingId + "~" +
               guest.toSerialString() + "~" +
               room.toSerialString() + "~" +
               checkIn + "~" + checkOut + "~" +
               status + "~" + paid + "~" + bookedOn;
    }

    public static Booking fromSerialString(String line) {
        String[] p = line.split("~");
        String   bid      = p[0];
        Guest    g        = Guest.fromSerialString(p[1]);
        Room     r        = Room.fromSerialString(p[2]);
        LocalDate ci      = LocalDate.parse(p[3], FMT);
        LocalDate co      = LocalDate.parse(p[4], FMT);
        Booking  b        = new Booking(bid, g, r, ci, co);
        b.status          = Status.valueOf(p[5]);
        b.paid            = Boolean.parseBoolean(p[6]);
        return b;
    }

    public String getSummary() {
        return String.format(
            "╔══════════════════════════════════════════════════╗%n" +
            "  Booking ID  : %s%n" +
            "  Guest       : %s (ID: %s)%n" +
            "  Email       : %s%n" +
            "  Phone       : %s%n" +
            "  Room        : %d (%s)%n" +
            "  Check-In    : %s%n" +
            "  Check-Out   : %s%n" +
            "  Nights      : %d%n" +
            "  Rate/Night  : $%.2f%n" +
            "  Total Cost  : $%.2f%n" +
            "  Status      : %s%n" +
            "  Payment     : %s%n" +
            "╚══════════════════════════════════════════════════╝",
            bookingId,
            guest.getName(), guest.getGuestId(),
            guest.getEmail(),
            guest.getPhone(),
            room.getRoomNumber(), room.getType(),
            checkIn, checkOut,
            getNights(),
            room.getPricePerNight(),
            getTotalCost(),
            status,
            paid ? "PAID" : "PENDING"
        );
    }
}
