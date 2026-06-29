import java.util.ArrayList;
import java.util.Scanner;

// =========================================
//  Student class (OOP - Encapsulation)
// =========================================
class Student {
    private String name;
    private double marks;

    public Student(String name, double marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName()  { return name; }
    public double getMarks() { return marks; }

    public String getLetterGrade() {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 60) return "C";
        else if (marks >= 50) return "D";
        else return "F";
    }

    public String getStatus() {
        return marks >= 50 ? "PASS" : "FAIL";
    }

    @Override
    public String toString() {
        return String.format("  %-20s | %6.2f | %-3s | %s",
                name, marks, getLetterGrade(), getStatus());
    }
}

// =========================================
//  GradeTracker class (OOP - Logic Layer)
// =========================================
class GradeTracker {
    private ArrayList<Student> students = new ArrayList<>();

    public void addStudent(String name, double marks) {
        students.add(new Student(name, marks));
        System.out.println("  >> Student \"" + name + "\" added successfully!");
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    public double getAverage() {
        double sum = 0;
        for (Student s : students) sum += s.getMarks();
        return sum / students.size();
    }

    public Student getHighest() {
        Student top = students.get(0);
        for (Student s : students)
            if (s.getMarks() > top.getMarks()) top = s;
        return top;
    }

    public Student getLowest() {
        Student low = students.get(0);
        for (Student s : students)
            if (s.getMarks() < low.getMarks()) low = s;
        return low;
    }

    public void displayAll() {
        String line = "  " + "=".repeat(52);
        System.out.println("\n" + line);
        System.out.printf("  %-20s | %6s | %-3s | %s%n", "Name", "Marks", "Grade", "Status");
        System.out.println(line);
        for (Student s : students)
            System.out.println(s);
        System.out.println(line);
        System.out.println("  Total Students : " + students.size());
    }

    public void displaySummary() {
        if (isEmpty()) { System.out.println("  No students to display."); return; }
        String line = "  +" + "=".repeat(44) + "+";
        System.out.println("\n" + line);
        System.out.println("  |          SUMMARY REPORT                    |");
        System.out.println(line);
        System.out.printf ("  |  Total Students  : %-24d|%n", students.size());
        System.out.printf ("  |  Average Score   : %-24.2f|%n", getAverage());
        System.out.printf ("  |  Highest Score   : %.2f  (%s)%n",
                getHighest().getMarks(), getHighest().getName());
        System.out.printf ("  |  Lowest  Score   : %.2f  (%s)%n",
                getLowest().getMarks(), getLowest().getName());
        System.out.println(line);
    }
}

// =========================================
//  Main class - Console Menu
// =========================================
public class Main {

    static Scanner scanner = new Scanner(System.in);
    static GradeTracker tracker = new GradeTracker();

    public static void main(String[] args) {
        int choice;
        printBanner();
        do {
            printMenu();
            System.out.print("  Enter choice: ");
            choice = getIntInput();
            switch (choice) {
                case 1: addStudent();        break;
                case 2: viewAllStudents();   break;
                case 3: tracker.displaySummary(); break;
                case 4: viewHighestLowest(); break;
                case 5:
                    System.out.println("\n  Thank you for using Student Grade Tracker. Goodbye!\n");
                    break;
                default:
                    System.out.println("  [!] Invalid choice. Please enter 1-5.");
            }
        } while (choice != 5);
        scanner.close();
    }

    static void printBanner() {
        System.out.println();
        System.out.println("  +==========================================+");
        System.out.println("  |   STUDENT GRADE TRACKER  v1.0           |");
        System.out.println("  |       CodeAlpha Internship               |");
        System.out.println("  +==========================================+");
    }

    static void printMenu() {
        System.out.println("\n  +-----------------------------+");
        System.out.println("  |           MENU              |");
        System.out.println("  +---------+-------------------+");
        System.out.println("  |  1. Add Student             |");
        System.out.println("  |  2. View All Students       |");
        System.out.println("  |  3. Summary Report          |");
        System.out.println("  |  4. Highest & Lowest Score  |");
        System.out.println("  |  5. Exit                    |");
        System.out.println("  +-----------------------------+");
    }

    static void addStudent() {
        System.out.print("\n  Enter student name  : ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) { System.out.println("  [!] Name cannot be empty."); return; }
        System.out.print("  Enter marks (0-100) : ");
        double marks = getDoubleInput();
        if (marks < 0 || marks > 100) { System.out.println("  [!] Marks must be between 0 and 100."); return; }
        tracker.addStudent(name, marks);
    }

    static void viewAllStudents() {
        if (tracker.isEmpty()) { System.out.println("\n  No students added yet."); return; }
        tracker.displayAll();
    }

    static void viewHighestLowest() {
        if (tracker.isEmpty()) { System.out.println("\n  No students added yet."); return; }
        System.out.println("\n  [TOP]    Highest : " + tracker.getHighest().getName()
                + " - " + tracker.getHighest().getMarks());
        System.out.println("  [BOTTOM] Lowest  : " + tracker.getLowest().getName()
                + " - " + tracker.getLowest().getMarks());
    }

    static int getIntInput() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    static double getDoubleInput() {
        try { return Double.parseDouble(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }
}
