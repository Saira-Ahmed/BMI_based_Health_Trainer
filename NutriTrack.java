import java.io.*;
import java.util.*;

public class NutriTrack {
    static Scanner input = new Scanner(System.in);
    static String bmiCategory = "";
    static final String USER_FILE = "users.txt";

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n-----------------/// WELCOME TO NUTRITRACK ///---------------");
            System.out.println("1. Login\n2. Signup\n3. Exit\nEnter your choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    String username = login();
                    if (username != null) {
                        inputChoice(username);
                    }
                    break;
                case "2":
                    signup();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    static String login() {
        List<String> users = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(USER_FILE))) {
            while (fileScanner.hasNextLine()) {
                users.add(fileScanner.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("User file not found. Please sign up first.");
            return null;
        }

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter username: ");
            String username = input.nextLine().trim();
            System.out.print("Enter password: ");
            String password = input.nextLine().trim();

            for (String user : users) {
                String[] parts = user.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    System.out.println("Login successful!\n");
                    return username;
                }
            }

            attempts++;
            System.out.println("Login failed. Attempts left: " + (3 - attempts));
        }

        System.out.println("Too many failed attempts. Access denied.\n");
        return null;
    }

    static void signup() {
        System.out.print("Choose a username: ");
        String uname = input.nextLine();
        System.out.print("Choose a password: ");
        String pass = input.nextLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            bw.write(uname + "," + pass);
            bw.newLine();
            System.out.println("Signup successful!");
        } catch (IOException e) {
            System.out.println("Error writing to users file.");
        }
    }

    static void inputChoice(String username) {
        while (true) {
            System.out.println("What do you want to check:\n" +
                    "1. Body Mass Index\n" +
                    "2. Exercises\n" +
                    "3. Nutrition Plan\n" +
                    "4. View History\n" +
                    "5. Return to the login page\n");

            String choose = input.nextLine();

            switch (choose) {
                case "1": {
                    double[] userData = getUserInput();
                    double bmi = calculateBMI(userData[0], userData[1]);
                    System.out.printf("Your BMI is: %.2f (%s)\n\n", bmi, bmiCategory);
                    saveUserHistory(username, userData[0], userData[1], bmi, bmiCategory);
                    break;
                }
                case "2":
                    if (bmiCategory.isEmpty()) {
                        System.out.println("⚠ Please calculate your BMI first!\n");
                        break;
                    }
                    System.out.println("Let me give you some exercises:\n");
                    displaySectionFromCombinedFile(bmiCategory, "Exercises");
                    break;
                case "3":
                    if (bmiCategory.isEmpty()) {
                        System.out.println("⚠ Please calculate your BMI first!\n");
                        break;
                    }
                    System.out.println("Let me give you a nutrition plan:\n");
                    displaySectionFromCombinedFile(bmiCategory, "Nutrition");
                    break;
                case "4":
                    displayUserHistory(username);
                    break;
                case "5":
                    System.out.println("Returning to the login page...\n");
                    return;
                default:
                    System.out.println("Invalid option. Please choose a number between 1 and 5.\n");
            }
        }
    }


    static double[] getUserInput() {
        double weight = 0, heightMeters = 0;
        while (true) {
            System.out.println("Choose unit system:\n1. US (lbs, feet/inches)\n2. Metric (kg, cm)");
            String unit = input.nextLine();

            try {
                if (unit.equals("1")) {
                    System.out.print("Weight (lbs): ");
                    weight = Double.parseDouble(input.nextLine()) * 0.453592;
                    System.out.print("Height (feet): ");
                    double feet = Double.parseDouble(input.nextLine());
                    System.out.print("Height (inches): ");
                    double inches = Double.parseDouble(input.nextLine());
                    heightMeters = ((feet * 12 + inches) * 0.0254);
                    break;
                } else if (unit.equals("2")) {
                    System.out.print("Weight (kg): ");
                    weight = Double.parseDouble(input.nextLine());
                    System.out.print("Height (cm): ");
                    heightMeters = Double.parseDouble(input.nextLine()) / 100.0;
                    break;
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, try again.");
            }
        }
        return new double[]{weight, heightMeters};
    }

    static double calculateBMI(double weightKg, double heightMeters) {
        double bmi = weightKg / (heightMeters * heightMeters);
        if (bmi < 18.5) bmiCategory = "underweight";
        else if (bmi < 25) bmiCategory = "normal";
        else if (bmi < 30) bmiCategory = "overweight";
        else bmiCategory = "obese";
        return bmi;
    }

    static void saveUserHistory(String username, double weight, double heightMeters, double bmi, String category) {
        String historyFile = "history.txt";
        String record = String.format("%s - %s, Weight: %.2f kg, Height: %.2f m, BMI: %.2f, Category: %s",
                username, new java.util.Date(), weight, heightMeters, bmi, category);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(historyFile, true))) {
            bw.write(record);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Failed to save user history.");
        }
    }


    static void displayUserHistory(String username) {
        String historyFile = "history.txt";
        System.out.println("\nYour BMI and Weight History:\n");
        try (BufferedReader br = new BufferedReader(new FileReader(historyFile))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(username + " -")) {
                    System.out.println(line);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No history found for this user.");
            }
        } catch (IOException e) {
            System.out.println("No history file found. Calculate your BMI to start tracking.");
        }
        System.out.println();
    }


    static void displaySectionFromCombinedFile(String category, String section) {
        String fileName = category + ".txt";
        String sectionHeader = "[" + section + "]";
        boolean inSection = false;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            System.out.println("\n" + section + ":");
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Check if it's the start of the section
                if (line.equalsIgnoreCase(sectionHeader)) {
                    inSection = true;
                    continue;
                }

                // If inside the section, read content until another section starts
                if (inSection) {
                    if (line.startsWith("[") && line.endsWith("]")) {
                        break; // next section starts
                    }
                    if (!line.isEmpty()) {
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load " + section.toLowerCase() + " for " + category + ".");
        }
        System.out.println();
    }

}
