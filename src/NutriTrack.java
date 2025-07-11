
//package com.team;

import java.io.*;
import java.util.*;

public class NutriTrack {
    static Scanner input = new Scanner(System.in);
    static String bmiCategory = "";
    static final String USER_FILE = "users.txt";
    static double lastBmi = -1;
    static String lastCategory = "";

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n-----------------/// WELCOME TO NUTRITRACK ///---------------");
            System.out.println("1. Login\n2. Signup\n3. Exit\nEnter your choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    String username = login();
                    if (username != null) {
                        loadLastBmi(username);
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
                    "4. CompareProgress\n" +
                    "5. Return to the login page\n");

            String choose = input.nextLine();

            switch (choose) {
                case "1": {
                    double[] userData = getUserInput();
                    double bmi = calculateBMI(userData[0], userData[1]);
                    System.out.printf("Your BMI is: %.2f (%s)\n\n", bmi, bmiCategory);

                    saveUserHistory(username, userData[0], userData[1], bmi, bmiCategory);
                    compareWithLastBmi(bmi, bmiCategory);
                    lastBmi = bmi;
                    lastCategory = bmiCategory;
                    break;
                }

                case "2":
                case "3":
                    if (bmiCategory.isEmpty()) {
                        System.out.print("You haven't calculated your BMI. Do you want to enter it manually? (yes/no): ");
                        String response = input.nextLine().trim().toLowerCase();
                        if (response.equals("yes")) {
                            System.out.print("Enter your BMI value: ");
                            try {
                                double manualBmi = Double.parseDouble(input.nextLine());
                                bmiCategory = getCategoryFromBMI(manualBmi);
                                System.out.println("Your BMI category is set to: " + bmiCategory);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Returning to menu.\n");
                                break;
                            }
                        } else {
                            System.out.println("⚠ Please calculate your BMI first!\n");
                            break;
                        }
                    }

                    System.out.println((choose.equals("2") ? "Let me give you some exercises:" : "Let me give you a nutrition plan:") + "\n");
                    displaySectionFromCombinedFile(bmiCategory, choose.equals("2") ? "Exercises" : "Nutrition");
                    break;

                case "4":
                    comparisonChoice(username);
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

        if (bmi < 18.5) {
            bmiCategory = "underweight";
        } else if (bmi < 25) {
            bmiCategory = "normal";
        } else if (bmi < 30) {
            bmiCategory = "overweight";
        } else {
            bmiCategory = "obese";
        }
        return bmi;
    }

    static String getCategoryFromBMI(double bmi) {
        if (bmi < 18.5) {
            return "underweight";
        }
        if (bmi < 25) {
            return "normal";
        }
        if (bmi < 30) {
            return "overweight";
        }
        return "obese";
    }

    static void saveUserHistory(String username, double weight, double heightMeters, double bmi, String category) {
        String historyFile = "history.txt";
        String record = String.format("%s - %s, Weight: %.2f kg, Height: %.2f m, BMI: %.2f, Category: %s",
                username, new Date(), weight, heightMeters, bmi, category);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(historyFile, true))) {
            bw.write(record);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Failed to save user history.");
        }
    }

    static void loadLastBmi(String username) {
        String historyFile = "history.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(historyFile))) {
            String line;
            String lastLine = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith(username + " -")) {
                    lastLine = line;
                }
            }

            if (lastLine != null) {
                String[] parts = lastLine.split("BMI: ");
                if (parts.length > 1) {
                    String[] bmiParts = parts[1].split(",");
                    lastBmi = Double.parseDouble(bmiParts[0]);
                    lastCategory = bmiParts[1].split(":")[1].trim();
                }
            }
        } catch (Exception e) {
            lastBmi = -1;
            lastCategory = "";
        }
    }

    static void compareWithLastBmi(double newBmi, String newCategory) {
        if (lastBmi == -1) return;

        System.out.printf("Previous BMI: %.2f (%s)\n", lastBmi, lastCategory);

        if (!newCategory.equalsIgnoreCase(lastCategory)) {
            System.out.println("Your BMI category changed from " + lastCategory + " to " + newCategory + "!");
        } else {
            System.out.println("Your BMI category remains the same: " + newCategory);
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

                if (line.equalsIgnoreCase(sectionHeader)) {
                    inSection = true;
                    continue;
                }

                if (inSection) {
                    if (line.startsWith("[") && line.endsWith("]")) break;
                    if (!line.isEmpty()) System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load " + section.toLowerCase() + " for " + category + ".");
        }
        System.out.println();
    }


    static void comparisonChoice(String username) {
        System.out.println("Choose how you'd like to enter BMI for comparison:\n1. Enter manually\n2. Calculate now");
        String compareChoice = input.nextLine();

        double currentBmi = -1;
        String currentCategory = "";

        switch (compareChoice) {
            case "1":
                try {
                    System.out.print("Enter your BMI: ");
                    currentBmi = Double.parseDouble(input.nextLine());
                    currentCategory = getCategoryFromBMI(currentBmi);
                    System.out.printf("Your BMI category is: %s\n", currentCategory);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid BMI entered. Returning to menu.\n");
                    return;
                }
                break;

            case "2":
                double[] userData = getUserInput();
                currentBmi = calculateBMI(userData[0], userData[1]);
                currentCategory = bmiCategory;
                System.out.printf("Your BMI is: %.2f (%s)\n", currentBmi, currentCategory);
                break;

            default:
                System.out.println("Invalid option. Returning to menu.\n");
                return;
        }

        compareWithLastBmi(currentBmi, currentCategory);
        lastBmi = currentBmi;
        lastCategory = currentCategory;
        saveUserHistory(username, 0, 0, currentBmi, currentCategory);
    }
}
