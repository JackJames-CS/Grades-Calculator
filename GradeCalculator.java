import java.util.ArrayList;
import java.util.Scanner;

public class GradeCalculator {

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Module> modules = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   College Module Grade Calculator");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Add a module");
            System.out.println("2. View all modules");
            System.out.println("3. View overall average (QCA)");
            System.out.println("4. Remove a module");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addModule();
                case "2" -> viewModules();
                case "3" -> viewOverallAverage();
                case "4" -> removeModule();
                case "5" -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("\nGoodbye!");
    }

    static void addModule() {
        System.out.print("\nModule name: ");
        String name = scanner.nextLine().trim();

        int credits = readInt("Credits (e.g. 5, 10, 15): ", 1, 30);
        double examWeight = readDouble("Exam weight % (e.g. 60): ", 0, 100);
        double caWeight = 100 - examWeight;
        System.out.printf("CA weight automatically set to %.0f%%\n", caWeight);

        double examMark = readDouble("Exam mark (0-100): ", 0, 100);
        double caMark = readDouble("CA mark (0-100): ", 0, 100);

        Module module = new Module(name, credits, examWeight, caWeight, examMark, caMark);
        modules.add(module);

        System.out.println("\n--- Module Added ---");
        printModule(module);
    }

    static void viewModules() {
        if (modules.isEmpty()) {
            System.out.println("\nNo modules added yet.");
            return;
        }

        System.out.println("\n==========================================");
        System.out.printf("%-25s %7s %6s %6s %8s %6s\n",
                "Module", "Credits", "Exam", "CA", "Overall", "Grade");
        System.out.println("------------------------------------------");

        for (Module m : modules) {
            System.out.printf("%-25s %7d %5.1f%% %5.1f%% %7.1f%% %6s\n",
                    truncate(m.name, 25),
                    m.credits,
                    m.examMark,
                    m.caMark,
                    m.getOverallMark(),
                    m.getGrade());
        }
        System.out.println("==========================================");
    }

    static void viewOverallAverage() {
        if (modules.isEmpty()) {
            System.out.println("\nNo modules added yet.");
            return;
        }

        double totalWeighted = 0;
        int totalCredits = 0;

        for (Module m : modules) {
            totalWeighted += m.getOverallMark() * m.credits;
            totalCredits += m.credits;
        }

        double qca = totalWeighted / totalCredits;

        System.out.println("\n==========================================");
        System.out.printf("  Total Credits: %d\n", totalCredits);
        System.out.printf("  Weighted Average (QCA): %.2f%%\n", qca);
        System.out.printf("  Overall Grade: %s\n", getGradeFromMark(qca));
        System.out.println("==========================================");

        // Pass/fail summary
        int passed = 0, failed = 0;
        for (Module m : modules) {
            if (m.getOverallMark() >= 40) passed++;
            else failed++;
        }
        System.out.printf("  Modules Passed: %d / %d\n", passed, modules.size());
        if (failed > 0) {
            System.out.printf("  ** %d module(s) below 40%% **\n", failed);
        }
    }

    static void removeModule() {
        if (modules.isEmpty()) {
            System.out.println("\nNo modules to remove.");
            return;
        }

        System.out.println("\nSelect a module to remove:");
        for (int i = 0; i < modules.size(); i++) {
            System.out.printf("  %d. %s\n", i + 1, modules.get(i).name);
        }
        int index = readInt("Module number: ", 1, modules.size()) - 1;
        String removed = modules.remove(index).name;
        System.out.println("Removed: " + removed);
    }

    static void printModule(Module m) {
        System.out.printf("  Module:   %s\n", m.name);
        System.out.printf("  Credits:  %d\n", m.credits);
        System.out.printf("  Exam:     %.1f%% (weight: %.0f%%)\n", m.examMark, m.examWeight);
        System.out.printf("  CA:       %.1f%% (weight: %.0f%%)\n", m.caMark, m.caWeight);
        System.out.printf("  Overall:  %.1f%%\n", m.getOverallMark());
        System.out.printf("  Grade:    %s\n", m.getGrade());
    }

    static String getGradeFromMark(double mark) {
        if (mark >= 70) return "1st (First)";
        if (mark >= 60) return "2.1 (Upper Second)";
        if (mark >= 50) return "2.2 (Lower Second)";
        if (mark >= 40) return "Pass";
        return "Fail";
    }

    static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 2) + "..";
    }

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("Please enter a number between %.0f and %.0f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
}

class Module {
    String name;
    int credits;
    double examWeight;
    double caWeight;
    double examMark;
    double caMark;

    Module(String name, int credits, double examWeight, double caWeight, double examMark, double caMark) {
        this.name = name;
        this.credits = credits;
        this.examWeight = examWeight;
        this.caWeight = caWeight;
        this.examMark = examMark;
        this.caMark = caMark;
    }

    double getOverallMark() {
        return (examMark * examWeight / 100) + (caMark * caWeight / 100);
    }

    String getGrade() {
        double overall = getOverallMark();
        if (overall >= 70) return "1st";
        if (overall >= 60) return "2.1";
        if (overall >= 50) return "2.2";
        if (overall >= 40) return "Pass";
        return "Fail";
    }
}
