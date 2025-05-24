import java.util.*;
import java.io.*;
public class Main
{
    public static void main(String[] args)
    {
       Scanner input = new Scanner(System.in);
       ArrayList<String> users = new ArrayList<>();
       String filePath = "D:\\users.txt";
       try
       {
         Scanner fileScanner = new Scanner(new File(filePath));
         while(fileScanner.hasNextLine())
         {
             String line = fileScanner.nextLine().trim();
             users.add(line);
         }
         fileScanner.close();
       }
       catch(FileNotFoundException e)
       {
           System.out.println("User file not found. Creating new file with default admin account...");
           try {
               FileWriter writer = new FileWriter(filePath);
               writer.write("admin,admin123\n"); // default user
               writer.close();
               users.add("admin,admin123");
           } catch (IOException ex) {
               System.out.println("Error creating user file.");
               return;
           }
       }


        int attempts = 0;
       boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\n=== BMI Health Trainer ===");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.print("Choose option (1 or 2): ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                while (attempts < 3 && !loggedIn) {
                    System.out.print("Enter username: ");
                    String username = input.nextLine();

                    System.out.print("Enter password: ");
                    String password = input.nextLine();

                    loggedIn = login(users, username, password);

                    if (!loggedIn) {
                        attempts++;
                        System.out.println("Login failed. Attempts left: " + (3 - attempts));
                    }
                }

                if (loggedIn) {
                    System.out.println("Login successful! Access granted.");
                } else {
                    System.out.println("Too many failed attempts. Access denied.");
                }

            } else if (choice.equals("2")) {
                System.out.print("Choose a username: ");
                String newUser = input.nextLine();

                System.out.print("Choose a password: ");
                String newPass = input.nextLine();

                boolean exists = false;
                for (String line : users) {
                    String[] parts = line.split(",");
                    if (parts.length == 2 && parts[0].equals(newUser)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println("Username already exists. Try another.");
                } else {
                    try {
                        FileWriter writer = new FileWriter(filePath, true);
                        writer.write(newUser + "," + newPass + "\n");
                        writer.close();
                        users.add(newUser + "," + newPass);
                        System.out.println("Signup successful! You can now login.");
                    } catch (IOException e) {
                        System.out.println("Error writing to file.");
                    }
                }

            } else {
                System.out.println("Invalid option. Please enter 1 or 2.");
            }
        }

        input.close();
    }

    static boolean login(ArrayList<String> users, String username, String password)
    {
        for(String line : users)
        {
            String[] parts = line.split(",");
            if(parts.length == 2)
            {
                String fileUser = parts[0];
                String filePass = parts[1];
                if(username.equals(fileUser) && password.equals(filePass))
                    return true;
            }
        }
        return false;
    }
}
