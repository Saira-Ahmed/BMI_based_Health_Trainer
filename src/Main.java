import java.util.*;
import java.io.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (!login()) return;

        int unit = getUnit();
        double weight = getPositiveInput("Enter your weight:");
        double height = getPositiveInput(unit == 1 ? "Enter your height (feet):" : "Enter your height (cm):");
        double inches = 0;

        if (unit == 1) {
            inches = getInches();
            height = (height * 12 + inches) * 0.0254;
            weight *= 0.453592;
        } else {
            height /= 100.0;
        }

        double bmi = calculateBMI(weight, height);
        System.out.printf("Your BMI is: %.2f\n", bmi);
        String category = getBMICategory(bmi);
        System.out.println("Category: " + category);

        displayFromFile("exercises.txt", category, "Exercises");
        displayFromFile("suggestions.txt", category, "Suggestions");
    }

    static boolean login() {
        System.out.print("Enter username: ");
        String user = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(user) && parts[1].equals(pass)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file.");
        }
        System.out.println("Invalid login.");
        return false;
    }

    static int getUnit() {
        while (true) {
            try {
                System.out.println("Choose system: 1 - US (lbs/feet), 2 - Metric (kg/cm)");
                int unit = scanner.nextInt();
                if (unit == 1 || unit == 2) return unit;
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
            System.out.println("Invalid input. Try again.");
        }
    }

    static double getPositiveInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double val = scanner.nextDouble();
                if (val > 0) return val;
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
            System.out.println("Invalid input. Try again.");
        }
    }

    static double getInches() {
        while (true) {
            try {
                System.out.print("Enter inches (0-11): ");
                double val = scanner.nextDouble();
                if (val >= 0 && val < 12) return val;
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
            System.out.println("Invalid input. Try again.");
        }
    }

    static double calculateBMI(double weightKg, double heightMeters) {
        return weightKg / (heightMeters * heightMeters);
    }

    static String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }

    static void displayFromFile(String filename, String category, String title) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(category + ":")) {
                    System.out.println("\n" + title + " for " + category + ":");
                    System.out.println(line.split(":", 2)[1].trim());
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + filename);
        }
    }
}
