package hotel;

import java.util.Scanner;

public class PaymentProcessor {

    public enum PaymentMethod { CREDIT_CARD, DEBIT_CARD, CASH, UPI }

    private PaymentProcessor() {}

    public static boolean processPayment(Booking booking, Scanner scanner) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("          PAYMENT GATEWAY              ");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.printf("  Amount Due  : $%.2f%n", booking.getTotalCost());
        System.out.println("\n  Select payment method:");
        System.out.println("  1. Credit Card");
        System.out.println("  2. Debit Card");
        System.out.println("  3. Cash");
        System.out.println("  4. UPI");
        System.out.print("  Choice: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid choice. Payment cancelled.");
            return false;
        }

        PaymentMethod method;
        switch (choice) {
            case 1: method = PaymentMethod.CREDIT_CARD; break;
            case 2: method = PaymentMethod.DEBIT_CARD;  break;
            case 3: method = PaymentMethod.CASH;         break;
            case 4: method = PaymentMethod.UPI;          break;
            default:
                System.out.println("  [!] Invalid choice. Payment cancelled.");
                return false;
        }

        return collectPaymentDetails(method, booking, scanner);
    }

    private static boolean collectPaymentDetails(PaymentMethod method,
                                                  Booking booking, Scanner sc) {
        System.out.println("\n  --- " + method.name().replace('_', ' ') + " ---");

        switch (method) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                System.out.print("  Card Number (16 digits): ");
                String card = sc.nextLine().trim().replaceAll("\\s+", "");
                if (!card.matches("\\d{16}")) {
                    System.out.println("  [!] Invalid card number.");
                    return false;
                }
                System.out.print("  Expiry (MM/YY): ");
                String expiry = sc.nextLine().trim();
                if (!expiry.matches("\\d{2}/\\d{2}")) {
                    System.out.println("  [!] Invalid expiry.");
                    return false;
                }
                System.out.print("  CVV: ");
                String cvv = sc.nextLine().trim();
                if (!cvv.matches("\\d{3,4}")) {
                    System.out.println("  [!] Invalid CVV.");
                    return false;
                }
                break;

            case UPI:
                System.out.print("  UPI ID: ");
                String upi = sc.nextLine().trim();
                if (!upi.contains("@")) {
                    System.out.println("  [!] Invalid UPI ID.");
                    return false;
                }
                break;

            case CASH:
                System.out.printf("  Please tender $%.2f at the front desk.%n",
                        booking.getTotalCost());
                break;
        }

        // Simulate processing delay
        System.out.println("\n  Processing payment...");
        try { Thread.sleep(1200); } catch (InterruptedException ignored) {}

        System.out.println("  ✔  Payment of $" + String.format("%.2f", booking.getTotalCost()) +
                           " successful! (Ref: PAY-" + System.currentTimeMillis() % 100000 + ")");
        return true;
    }
}
