package hotel;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {
    private static final String DATA_DIR      = "data";
    private static final String ROOMS_FILE    = DATA_DIR + "/rooms.txt";
    private static final String BOOKINGS_FILE = DATA_DIR + "/bookings.txt";

    public FileManager() {
        new File(DATA_DIR).mkdirs();
    }

    // ── Rooms ──────────────────────────────────────────────────────────────────

    public void saveRooms(List<Room> rooms) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            for (Room r : rooms) pw.println(r.toSerialString());
        } catch (IOException e) {
            System.err.println("[FileManager] Could not save rooms: " + e.getMessage());
        }
    }

    public List<Room> loadRooms() {
        List<Room> rooms = new ArrayList<>();
        File f = new File(ROOMS_FILE);
        if (!f.exists()) return rooms;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) rooms.add(Room.fromSerialString(line));
            }
        } catch (IOException e) {
            System.err.println("[FileManager] Could not load rooms: " + e.getMessage());
        }
        return rooms;
    }

    // ── Bookings ───────────────────────────────────────────────────────────────

    public void saveBookings(List<Booking> bookings) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) pw.println(b.toSerialString());
        } catch (IOException e) {
            System.err.println("[FileManager] Could not save bookings: " + e.getMessage());
        }
    }

    public List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        File f = new File(BOOKINGS_FILE);
        if (!f.exists()) return bookings;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        bookings.add(Booking.fromSerialString(line));
                    } catch (Exception e) {
                        System.err.println("[FileManager] Skipping corrupt record: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[FileManager] Could not load bookings: " + e.getMessage());
        }
        return bookings;
    }
}
