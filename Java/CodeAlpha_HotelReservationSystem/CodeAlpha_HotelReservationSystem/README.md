# 🏨 CodeAlpha_HotelReservationSystem

A full-featured **Java console application** for hotel room reservation management, built with OOP principles and file-based persistence.

---

## 📋 Features

| Feature | Description |
|---|---|
| 🔍 Search Rooms | Browse available rooms filtered by type |
| 📅 Book a Room | Reserve a room with guest details and date range |
| ❌ Cancel Booking | Cancel any active reservation |
| 📄 View Booking | Look up booking details by ID or guest name |
| 💳 Simulate Payment | Credit Card, Debit Card, Cash, or UPI |
| 💾 File Persistence | Bookings and room status saved to disk automatically |
| 🏷️ Room Types | Standard ($99.99), Deluxe ($179.99), Suite ($299.99/night) |

---

## 🏗️ Project Structure

```
CodeAlpha_HotelReservationSystem/
├── src/
│   └── hotel/
│       ├── Main.java              # Entry point
│       ├── ConsoleUI.java         # Menu-driven interface
│       ├── HotelManager.java      # Core business logic
│       ├── Room.java              # Room entity
│       ├── RoomType.java          # Enum: STANDARD, DELUXE, SUITE
│       ├── Guest.java             # Guest entity
│       ├── Booking.java           # Booking entity + status
│       ├── PaymentProcessor.java  # Simulated payment gateway
│       ├── FileManager.java       # File I/O persistence layer
│       └── HotelException.java    # Custom exception
├── out/                           # Compiled .class files (generated)
├── data/                          # Persistent data files (generated)
│   ├── rooms.txt
│   └── bookings.txt
├── build.sh                       # Build script (Linux/Mac)
├── run.sh                         # Run script (Linux/Mac)
├── run.bat                        # Run script (Windows)
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher (`java -version`)
- JDK with `javac` for compilation

### Compile

**Linux / macOS:**
```bash
chmod +x build.sh run.sh
./build.sh
```

**Windows:**
```cmd
mkdir out
javac -d out src\hotel\*.java
```

**Manual (any OS):**
```bash
mkdir -p out
javac -d out src/hotel/*.java
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
java -cp out hotel.Main
```

---

## 🖥️ Application Menu

```
╔══════════════════════════════════════════════════════╗
║          GRAND VISTA HOTEL RESERVATION SYSTEM        ║
║              Welcome! Your comfort, our pride.        ║
╚══════════════════════════════════════════════════════╝

┌─────────────────────────────────┐
│           MAIN MENU             │
├─────────────────────────────────┤
│  1. Search Available Rooms      │
│  2. Book a Room                 │
│  3. Cancel a Booking            │
│  4. View Booking Details        │
│  5. View All Rooms              │
│  6. List All Bookings           │
│  7. Exit                        │
└─────────────────────────────────┘
```

---

## 🏠 Room Types

| Type | Room Numbers | Price/Night | Capacity | Amenities |
|---|---|---|---|---|
| Standard | 101–105 | $99.99 | 2 | WiFi, TV, AC |
| Deluxe | 201–204 | $179.99 | 3 | WiFi, TV, AC, Mini-bar, City View |
| Suite | 301–303 | $299.99 | 4 | WiFi, TV, AC, Mini-bar, Jacuzzi, Ocean View, Living Room |

---

## 💳 Payment Methods

- **Credit Card** – 16-digit card number, expiry (MM/YY), CVV
- **Debit Card** – Same as credit card
- **Cash** – Confirmation to pay at front desk
- **UPI** – UPI ID (format: user@bank)

---

## 🗃️ Data Persistence

Booking and room data are automatically saved to the `data/` directory:

- `data/rooms.txt` — Room inventory with availability status
- `data/bookings.txt` — All bookings with full guest and room details

Data persists across sessions — bookings survive application restarts.

---

## 🧩 OOP Design

| Principle | Implementation |
|---|---|
| Encapsulation | All fields private with getters/setters |
| Abstraction | `HotelManager` hides persistence details from `ConsoleUI` |
| Enum usage | `RoomType`, `Booking.Status`, `PaymentMethod` |
| Custom Exception | `HotelException` for domain-specific errors |
| Separation of Concerns | UI, business logic, persistence in separate classes |
| Serialization | Custom `toSerialString()` / `fromSerialString()` per entity |

---

## 📌 Sample Booking Flow

1. Select **2. Book a Room**
2. Enter guest details (name, email, phone)
3. Choose an available room number
4. Enter check-in and check-out dates (`YYYY-MM-DD`)
5. Optionally proceed to payment
6. Booking ID is assigned (e.g., `BK1000`)

---

## 🛠️ Tech Stack

- **Language:** Java 11+
- **I/O:** `java.io` (BufferedReader, PrintWriter)
- **Date handling:** `java.time.LocalDate`
- **Collections:** `java.util` (List, Optional, Stream)
- **No external dependencies**

---

## 👨‍💻 Author

Developed as part of the **CodeAlpha Java Internship** program.

---

## 📄 License

This project is open-source and available under the MIT License.
