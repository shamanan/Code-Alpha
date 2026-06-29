package hotel;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class HotelManager {
    private final List<Room>    rooms    = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final FileManager   fm       = new FileManager();
    private int bookingCounter = 1000;

    public HotelManager() {
        load();
        if (rooms.isEmpty()) seedRooms();
    }

    // ── Seed rooms on first run ────────────────────────────────────────────────

    private void seedRooms() {
        int[] stdNums   = {101,102,103,104,105};
        int[] deluxNums = {201,202,203,204};
        int[] suiteNums = {301,302,303};
        for (int n : stdNums)   rooms.add(new Room(n, RoomType.STANDARD));
        for (int n : deluxNums) rooms.add(new Room(n, RoomType.DELUXE));
        for (int n : suiteNums) rooms.add(new Room(n, RoomType.SUITE));
        fm.saveRooms(rooms);
    }

    private void load() {
        rooms.addAll(fm.loadRooms());
        bookings.addAll(fm.loadBookings());
        // Derive highest booking counter
        for (Booking b : bookings) {
            try {
                int n = Integer.parseInt(b.getBookingId().replace("BK", ""));
                if (n >= bookingCounter) bookingCounter = n + 1;
            } catch (NumberFormatException ignored) {}
        }
        // Sync room availability from active bookings
        syncRoomAvailability();
    }

    private void syncRoomAvailability() {
        // Reset all to available, then mark booked ones
        rooms.forEach(r -> r.setAvailable(true));
        for (Booking b : bookings) {
            if (b.getStatus() == Booking.Status.CONFIRMED ||
                b.getStatus() == Booking.Status.CHECKED_IN) {
                findRoomByNumber(b.getRoom().getRoomNumber())
                    .ifPresent(r -> r.setAvailable(false));
            }
        }
    }

    // ── Room search ───────────────────────────────────────────────────────────

    public List<Room> searchAvailableRooms(RoomType type) {
        return rooms.stream()
            .filter(Room::isAvailable)
            .filter(r -> type == null || r.getType() == type)
            .collect(Collectors.toList());
    }

    public List<Room> getAllRooms() { return Collections.unmodifiableList(rooms); }

    // ── Booking ───────────────────────────────────────────────────────────────

    public Booking createBooking(Guest guest, int roomNumber,
                                  LocalDate checkIn, LocalDate checkOut)
            throws HotelException {

        if (!checkOut.isAfter(checkIn))
            throw new HotelException("Check-out must be after check-in.");

        Room room = findRoomByNumber(roomNumber)
            .orElseThrow(() -> new HotelException("Room " + roomNumber + " not found."));

        if (!room.isAvailable())
            throw new HotelException("Room " + roomNumber + " is not available.");

        String bid = "BK" + (bookingCounter++);
        Booking b  = new Booking(bid, guest, room, checkIn, checkOut);
        room.setAvailable(false);
        bookings.add(b);
        persist();
        return b;
    }

    // ── Cancel ────────────────────────────────────────────────────────────────

    public Booking cancelBooking(String bookingId) throws HotelException {
        Booking b = findBookingById(bookingId)
            .orElseThrow(() -> new HotelException("Booking " + bookingId + " not found."));

        if (b.getStatus() == Booking.Status.CANCELLED)
            throw new HotelException("Booking is already cancelled.");

        b.setStatus(Booking.Status.CANCELLED);
        findRoomByNumber(b.getRoom().getRoomNumber())
            .ifPresent(r -> r.setAvailable(true));
        persist();
        return b;
    }

    // ── Payment ───────────────────────────────────────────────────────────────

    public void markPaid(String bookingId) throws HotelException {
        Booking b = findBookingById(bookingId)
            .orElseThrow(() -> new HotelException("Booking " + bookingId + " not found."));
        b.setPaid(true);
        persist();
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public Optional<Booking> findBookingById(String id) {
        return bookings.stream().filter(b -> b.getBookingId().equalsIgnoreCase(id)).findFirst();
    }

    public List<Booking> getBookingsByGuest(String guestName) {
        String lower = guestName.toLowerCase();
        return bookings.stream()
            .filter(b -> b.getGuest().getName().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    public List<Booking> getAllBookings() { return Collections.unmodifiableList(bookings); }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Optional<Room> findRoomByNumber(int number) {
        return rooms.stream().filter(r -> r.getRoomNumber() == number).findFirst();
    }

    private void persist() {
        fm.saveRooms(rooms);
        fm.saveBookings(bookings);
    }
}
