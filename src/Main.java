import java.util.*;
import java.io.*;
public class Main
{
    public static void main(String[] args)
    {
       Scanner input = new Scanner(System.in);
       ArrayList<String> users = new ArrayList<>();
       //String filePath = "D:\\users.txt";
       try
       {
         Scanner fileScanner = new Scanner(new File("D:\\users.txt"));
         while(fileScanner.hasNextLine())
         {
             String line = fileScanner.nextLine().trim();
             users.add(line);
         }
         fileScanner.close();
       }
       catch(FileNotFoundException e)
       {
           System.out.println("User file not found.");
           return;
       }

       int attempts = 0;
       boolean loggedIn = false;
       while(attempts < 3 && !loggedIn)
       {
           System.out.println("=== BMI Health Trainer Login ===");
           System.out.println("Enter username: ");
           String username = input.nextLine();
           System.out.println("Enter password: ");
           String password = input.nextLine();

           loggedIn = login(users, username, password);

           if(!loggedIn)
           {
               attempts++;
               System.out.println("Login failed. Try again. Attempts left: " + (3 - attempts) + " \n");
           }
       }
       if(loggedIn)
       {
           System.out.println("Login successful! Access granted.");
       }
       else
           System.out.println("Too many failed attempts. Access denied.");

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