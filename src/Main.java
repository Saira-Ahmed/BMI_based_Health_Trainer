package com.team;
import java.io.*;
import java.util.*;


public class calculateBMI {

    static Scanner input = new Scanner(System.in);


    static void signUp() {

        ArrayList<String> users = new ArrayList<>();
        // String filePath = "D:\\Abuhurera data\\Courses\\JAVA\\bmiCalculator\\users.text";
        try {
            Scanner fileScanner = new Scanner(new File("D:\\Abuhurera data\\Courses\\JAVA\\bmiCalculator\\users.text"));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                users.add(line);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("User file not found.");
            return;
        }

        int attempts = 0;
        boolean loggedIn = false;
        while (attempts < 3 && !loggedIn) {
            System.out.println("=== BMI Health Trainer Login ===");
            System.out.println("Enter username: ");
            String username = input.nextLine();
            System.out.println("Enter password: ");
            String password = input.nextLine();

            loggedIn = login(users, username, password);

            if (!loggedIn) {
                attempts++;
                System.out.println("Login failed. Try again. Attempts left: " + (3 - attempts) + " \n");
            }
        }
        if (loggedIn) {
            System.out.println("Login successful! Access granted.");
        } else {
            System.out.println("Too many failed attempts. Access denied.");

        }
    }

    static boolean login(ArrayList<String> users, String username, String password) {
        for (String line : users) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String fileUser = parts[0];
                String filePass = parts[1];
                if (username.equals(fileUser) && password.equals(filePass))
                    return true;
            }
        }
        return false;
    }


    static void inputChoice() {

        System.out.println("What Do you want to check:\n" +
                "1.Body Mass Index\n" +
                "2.Exercises\n" +
                "3.Nutrition-Plan\n" +
                "4.Exit\n"
        );

        System.out.println("Select an option between 1 to 4\n");
        String choose = input.nextLine();

        switch (choose) {
            case "1":
                System.out.println("You want to check your BMI");
                bmiCal();
                break;

            case "2":
                System.out.println(" Let me give you some exercises:");
                break;

            case "3":
                System.out.println(" Let me give you a nutrition plan according to your BMI.");
                break;

            case "4":
                System.out.println(" Exiting the program. Goodbye!");
                return; // Exit the method (and loop)

            default:
                System.out.println(" Invalid option. Please choose a number between 1 and 4.");
        }
    }

    static void bmiCal() {

        System.out.println("Which unit type you want to choose: \n" +
                "1.United States" + " " + "2. Metric Unit" + " " + "3.Exit\n"
        );

        String choice = input.nextLine();

        try {
            switch (choice) {

                case "1":
                    System.out.println("You are selecting || United State || units");

                    System.out.println("Enter your | age || Gender || Height in (feet) and (inches) || Weight in pounds |\n");

                    System.out.println("Age");
                    int age = input.nextInt();
                    input.nextLine();

                    System.out.println("Gender");
                    String gender = input.nextLine();

                    System.out.println("Height ((feet))");
                    int feet = input.nextInt();
                    input.nextLine();

                    System.out.println(("Height ((inches))"));
                    int inches = input.nextInt();
                    input.nextLine();

                    int totalInches = feet * 12 + inches;  //Converted the feet into inches
                    // 1 feet = 12 inches

                    double heightInMeters = totalInches * 0.0254; //Converted inches in meter
                    // 1 inch = 0.0254S

                    System.out.println("Weight ((Pounds))");
                    int weight = input.nextInt();
                    input.nextLine();


                    double weightInKg = weight * 0.453592;
                    // 1pound = 0.453592 kg

                    double BMI = weightInKg / (heightInMeters * heightInMeters);
                    // bmi = weight(Kg) / height(m^2)
                    System.out.println("Your bmi is: " + BMI + "\n");


                    if (BMI < 18.5) {
                        System.out.println("You are underweight.\n");
                        choice();
                        lowWeight();
                    } else if (BMI < 24.9) {
                        System.out.println("You are in the normal weight range.\n");
                        choice();
                        normWeight();
                    } else if (BMI < 29.9) {
                        System.out.println("You are overweight.\n");
                        choice();
                        highWeight();
                    } else {
                        System.out.println("You are in the obese range.");
                        choice();
                        highWeight();
                        System.out.println("Weight ((Pounds))");
                        int weight = input.nextInt();
                        input.nextLine();

                    }

                    break;

                case "2":

                    System.out.println("You are selecting || Metric || units");

                    System.out.println("Enter your | age || Gender || Height in (cm)  || Weight in kg |\n");

                    System.out.println("Age");
                    int age2 = input.nextInt();
                    input.nextLine();

                    System.out.println("Gender");
                    String gender2 = input.nextLine();

                    System.out.println("Height ((cm))");
                    int height = input.nextInt();
                    input.nextLine();

                    double heightInMeter = height * 0.01; //Converted cm in meter
                    // 1 m = 100 cm / 1 cm = 1/100m

                    System.out.println("Weight ((Kg))");
                    int weight2 = input.nextInt();
                    input.nextLine();


                    double BMI2 = (double) weight2 / (heightInMeter * heightInMeter);
                    // bmi = weight(Kg) / height(m^2)
                    System.out.println("Your bmi is: " + BMI2);


                    if (BMI2 < 18.5) {
                        System.out.println("You are underweight.\n");
                        choice();
                        lowWeight();

                    } else if (BMI2 < 24.9) {
                        System.out.println("You are in the normal weight range.\n");
                        choice();
                        normWeight();
                    } else if (BMI2 < 29.9) {
                        System.out.println("You are overweight.\n");
                        choice();
                        highWeight();
                    } else {
                        System.out.println("You are in the obese range.");
                        choice();
                        highWeight();
                        //
                    }


                    break;

                case "3":
                    System.out.println("You are exiting the program");
                    break;

                default:
                    System.out.println("Choose an option between 1 to 4");
            }
        }
        catch(InputMismatchException e){
            System.out.println("Invalid input. Please enter correct numeric values.");
            input.nextLine();
        }




    }

    static void choice() {

        try {
            System.out.println("What should I give to you: " +

                    "1. Purpose Exercise: \n" +
                    "2. Make a balanced diet plan for me: \n" +
                    "3. Exit the program"
            );

            String choice = input.nextLine();

            if (choice.equals("1")) {
                System.out.println("Let me purpose best exercise for you: ");

            } else if (choice.equals("2")) {
                System.out.println("Let me give you the balanced nutrition plan: ");
            } else if (choice.equals("3")) {
                System.out.println("Exiting the program");
            } else {
                System.out.println("Choose the value between ((1 and 3))");
            }


        } catch (Exception e) {
            System.out.println("Something went wrong while selecting options.");
        }
    }

    static void lowWeight() {

        String[] underweight = new String[]{
                "1. Weight Training (Focus on strength & muscle gain)",
                "2. Compound Lifts (Squats, Deadlifts, Bench Press)",
                "3. Push-ups and Pull-ups",
                "4. Yoga (For digestion and appetite stimulation)",
                "5. Walking (Low-intensity cardio)",
                "6. Pilates (For core and body strength)",
                "7. Eat more frequently with high-protein diet"
        };

        System.out.println("===============Recomended Exercise for the underweight================");

        for (int i = 0; i < underweight.length; i++) {
            System.out.println(underweight[i] + " ");
        }
    }


    static void normWeight () {
        String[] normal = new String[]{
                "1. Brisk Walking (30 mins daily)",
                "2. Jogging or Light Running (20-30 mins)",
                "3. Bodyweight Strength Training (Push-ups, Squats, Lunges)",
                "4. Yoga (Flexibility & Stress relief)",
                "5. Cycling (20-30 mins)",
                "6. Light Dumbbell Workouts (2-3 days/week)",
                "7. Recreational Sports (Badminton, Swimming)"
        };

        System.out.println("===============Recomended Exercise for the normalweight================");

        for (int i = 0; i < normal.length; i++) {
            System.out.println(normal[i] + " ");


        }
    }


    static void highWeight () {

        String[] overweight = new String[]{
                "1. Walking (Start slow, 30–45 mins)",
                "2. Swimming (Full-body workout, low joint stress)",
                "3. Cycling (Stationary or Outdoor)",
                "4. Low-Impact Aerobics or Dance",
                "5. Seated or Chair Exercises (for beginners)",
                "6. Resistance Band Workouts",
                "7. Yoga or Tai Chi (to improve flexibility and calmness)"
        };

        System.out.println("===============Recomended Exercise for the Overweight================");

        for (int i = 0; i < overweight.length; i++) {
            System.out.println(overweight[i] + " ");

        }

    }


    public static void main(String [] args){
        System.out.println("-----------------///WELCOME TO NUTRITRACK///---------------");
        signUp();
        inputChoice();
    }
}

