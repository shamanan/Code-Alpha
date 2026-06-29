package hotel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final HotelManager manager = new HotelManager();
    private final Scanner      sc      = new Scanner(System.in);

    // ── Entry point ───────────────────────────────────────────────────────────

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": searchRooms();   break;
                case "2": bookRoom();      break;
                case "3": cancelBooking(); break;
                case "4": viewBooking();   break;
                case "5": viewAllRooms();  break;
                case "6": listBookings();  break;
                case "7": running = false; break;
                default:  System.out.println("  [!] Invalid option. Try again.");
            }
        }
        System.out.println("\n  Thank you for using Grand Vista Hotel System. Goodbye!\n");
    }

    // ── Menus ─────────────────────────────────────────────────────────────────

    private void printBanner() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║          GRAND VISTA HOTEL RESERVATION SYSTEM        ║");
        System.out.println("║              Welcome! Your comfort, our pride.        ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
    }

    private void printMainMenu() {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│           MAIN MENU             │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  1. Search Available Rooms      │");
        System.out.println("│  2. Book a Room                 │");
        System.out.println("│  3. Cancel a Booking            │");
        System.out.println("│  4. View Booking Details        │");
        System.out.println("│  5. View All Rooms              │");
        System.out.println("│  6. List All Bookings           │");
        System.out.println("│  7. Exit                        │");
        System.out.println("└─────────────────────────────────┘");
        System.out.print("  Enter choice: ");
    }

    // ── Feature 1: Search rooms ───────────────────────────────────────────────

    private void searchRooms() {
        System.out.println("\n── Search Available Rooms ─────────────────────────────");
        System.out.println("  Filter by type (or press Enter for all):");
        System.out.println("  1. Standard ($99.99/night)");
        System.out.println("  2. Deluxe   ($179.99/night)");
        System.out.println("  3. Suite    ($299.99/night)");
        System.out.print("  Choice: ");
        String input = sc.nextLine().trim();

        RoomType filter = null;
        switch (input) {
            case "1": filter = RoomType.STANDARD; break;
            case "2": filter = RoomType.DELUXE;   break;
            case "3": filter = RoomType.SUITE;     break;
        }

        List<Room> results = manager.searchAvailableRooms(filter);
        if (results.isEmpty()) {
            System.out.println("\n  No available rooms found.");
        } else {
            System.out.println("\n  Available Rooms:");
            System.out.println("  " + "─".repeat(80));
            results.forEach(r -> System.out.println("  " + r));
            System.out.println("  " + "─".repeat(80));
            System.out.println("  Total: " + results.size() + " room(s) available.");
        }
    }

    // ── Feature 2: Book a room ────────────────────────────────────────────────

    private void bookRoom() {
        System.out.println("\n── Book a Room ────────────────────────────────────────");
        try {
            // Guest details
            System.out.print("  Guest Name  : ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) { System.out.println("  [!] Name cannot be empty."); return; }

            System.out.print("  Email       : ");
            String email = sc.nextLine().trim();

            System.out.print("  Phone       : ");
            String phone = sc.nextLine().trim();

            String guestId = "G" + System.currentTimeMillis() % 100000;
            Guest  guest   = new Guest(guestId, name, email, phone);

            // Show available rooms
            List<Room> available = manager.searchAvailableRooms(null);
            if (available.isEmpty()) {
                System.out.println("  No rooms are currently available.");
                return;
            }
            System.out.println("\n  Available Rooms:");
            available.forEach(r -> System.out.println("    " + r));

            System.out.print("\n  Room Number : ");
            int roomNum;
            try { roomNum = Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  [!] Invalid room number."); return; }

            System.out.print("  Check-In  (YYYY-MM-DD): ");
            LocalDate checkIn  = parseDate(sc.nextLine().trim());
            System.out.print("  Check-Out (YYYY-MM-DD): ");
            LocalDate checkOut = parseDate(sc.nextLine().trim());
            if (checkIn == null || checkOut == null) return;

            Booking booking = manager.createBooking(guest, roomNum, checkIn, checkOut);

            System.out.println("\n  ✔  Booking created successfully!");
            System.out.println(booking.getSummary());

            // Payment
            System.out.print("\n  Proceed to payment now? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                boolean paid = PaymentProcessor.processPayment(booking, sc);
                if (paid) {
                    manager.markPaid(booking.getBookingId());
                    System.out.println("  Booking is now fully confirmed and paid.");
                }
            } else {
                System.out.println("  Payment pending. You can pay at check-in.");
            }

        } catch (HotelException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ── Feature 3: Cancel booking ─────────────────────────────────────────────

    private void cancelBooking() {
        System.out.println("\n── Cancel a Booking ───────────────────────────────────");
        System.out.print("  Enter Booking ID (e.g. BK1001): ");
        String bid = sc.nextLine().trim();
        try {
            Booking b = manager.cancelBooking(bid);
            System.out.println("\n  ✔  Booking " + b.getBookingId() +
                               " for " + b.getGuest().getName() + " has been CANCELLED.");
        } catch (HotelException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ── Feature 4: View booking ───────────────────────────────────────────────

    private void viewBooking() {
        System.out.println("\n── View Booking Details ───────────────────────────────");
        System.out.println("  Search by:  1. Booking ID   2. Guest Name");
        System.out.print("  Choice: ");
        String choice = sc.nextLine().trim();

        if ("1".equals(choice)) {
            System.out.print("  Booking ID: ");
            String bid = sc.nextLine().trim();
            manager.findBookingById(bid).ifPresentOrElse(
                b -> System.out.println("\n" + b.getSummary()),
                () -> System.out.println("  [!] Booking not found.")
            );
        } else if ("2".equals(choice)) {
            System.out.print("  Guest Name (partial ok): ");
            String name = sc.nextLine().trim();
            List<Booking> list = manager.getBookingsByGuest(name);
            if (list.isEmpty()) {
                System.out.println("  [!] No bookings found for '" + name + "'.");
            } else {
                list.forEach(b -> System.out.println("\n" + b.getSummary()));
            }
        } else {
            System.out.println("  [!] Invalid choice.");
        }
    }

    // ── Feature 5: View all rooms ─────────────────────────────────────────────

    private void viewAllRooms() {
        System.out.println("\n── All Rooms ──────────────────────────────────────────");
        System.out.println("  " + "─".repeat(80));
        manager.getAllRooms().forEach(r -> System.out.println("  " + r));
        System.out.println("  " + "─".repeat(80));
        long avail = manager.getAllRooms().stream().filter(Room::isAvailable).count();
        System.out.println("  Available: " + avail + " / " + manager.getAllRooms().size());
    }

    // ── Feature 6: List bookings ──────────────────────────────────────────────

    private void listBookings() {
        System.out.println("\n── All Bookings ───────────────────────────────────────");
        List<Booking> all = manager.getAllBookings();
        if (all.isEmpty()) {
            System.out.println("  No bookings on record.");
            return;
        }
        System.out.printf("  %-8s %-20s %-6s %-12s %-12s %-11s %-7s%n",
                "ID", "Guest", "Room", "Check-In", "Check-Out", "Status", "Paid");
        System.out.println("  " + "─".repeat(82));
        for (Booking b : all) {
            System.out.printf("  %-8s %-20s %-6d %-12s %-12s %-11s %-7s%n",
                b.getBookingId(),
                truncate(b.getGuest().getName(), 18),
                b.getRoom().getRoomNumber(),
                b.getCheckIn(),
                b.getCheckOut(),
                b.getStatus(),
                b.isPaid() ? "YES" : "NO");
        }
        System.out.println("  " + "─".repeat(82));
        System.out.println("  Total bookings: " + all.size());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            System.out.println("  [!] Invalid date format. Use YYYY-MM-DD.");
            return null;
        }
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
