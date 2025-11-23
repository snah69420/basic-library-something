package library;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PasswordManager {
	public static void main (String[] args) {
		Scanner input = new Scanner(System.in);
		ArrayList<String> user = new ArrayList<>();
		ArrayList<String> password = new ArrayList<>();
		ArrayList<String> site = new ArrayList<>();
		String db = "database.txt";
		// load the database
		loadDB(db, user, password, site);
		
		// Main part
		while (true) {
		System.out.println("---Welcome to our Password Manager!---");
		System.out.println("1. Add Credentials");
		System.out.println("2. View Credentials");
		System.out.println("3. Delete Credentials");
		System.out.println("4. Exit");
		System.out.print("Choice: ");

		String choice = input.nextLine();
		
		// Add Credentials
		if (choice.equals("1")) {
			System.out.println("---Add Credentials---");
			System.out.print("Enter Username/Email: ");
			user.add(input.nextLine());
			
			System.out.print("Enter Password: ");
			password.add(input.nextLine());
			
			System.out.print("Website?: ");
			site.add(input.nextLine());
			
			System.out.println("Credentials added!");
			saveData(db, user, password, site);

		// VIEW
		} else if (choice.equals("2")) {
			System.out.println("---View Credentials---");
			if(user.isEmpty()) {
				System.out.println("There are no credentials added, yet...");
			 } else {
               System.out.println("--- Websites ---");
               for (int i = 0; i < user.size(); i++) {
                   System.out.println((i + 1) + ". " + site.get(i));
               }
               // Query
                   System.out.println("Select a website: ");
                   int query_site = input.nextInt();
                   int site_index = query_site - 1;
                   input.nextLine();
                   System.out.println("--- Credentials on " + site.get(site_index) + " ---");
                   System.out.println("Username: " + user.get(site_index));
                   System.out.println("Password: " + password.get(site_index));
                   
                   System.out.println("Press enter to return to menu...");
                   input.nextLine();
                   
                   
			 }
            //       System.out.println("   Username: " + user.get(i));
            //       System.out.println("   Password: " + password.get(i));
            //       System.out.println("-------------------------");
         
			// Remove Credentials
		} else if (choice.equals("3")) {
			if (user.isEmpty()) {
				System.out.println("There are no credentials added, yet...");
			} else {
				System.out.println("---Delete Credentials---");
				for (int i = 0; i < user.size(); i++) {
					System.out.println((i + 1) + ". " + user.get(i));
				}
				System.out.print("Enter a number to delete: ");
				int delIndex = Integer.parseInt(input.nextLine()) - 1;

				if (delIndex < 0 || delIndex >= user.size()) {
					System.out.println("Invalid number.");
				} else {
					user.remove(delIndex);
					password.remove(delIndex);
					site.remove(delIndex);
					System.out.println("Book deleted successfully");
				} 
				
				}
			} else if (choice.equals("4")) {
			System.out.println("Exiting.");
			break;
		}
		}
	}
	// save to txt thingy
	public static void saveData(String db, ArrayList<String> user, ArrayList<String> password,
			ArrayList<String> site) {
		try {
			File file = new File(db);
			File parent = file.getParentFile();
			if (parent != null) {
			    parent.mkdirs();
			}
			try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
				for (int i = 0; i < user.size(); i++) {
					String u = user.get(i).replace("|", "/");
					String p = password.get(i).replace("|", "/");
					String s = site.get(i).replace("|", "/");

					pw.println(u + "|" + p + "|" + s);
				}
			}

			System.out.println("Database saved successfully.");

		} catch (Exception e) {
			System.out.println("Error saving database.");
		}
	}
	// loadDB object
	public static void loadDB(String db, ArrayList<String> user, ArrayList<String> password, ArrayList<String> site) {
		try {
		File file = new File(db);
    	if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        	String line;
        	while ((line = br.readLine()) != null) {
            	String[] parts = line.split("\\|");
            	if (parts.length == 3) {
            		user.add(parts[0]);
                	password.add(parts[1]);
                	site.add(parts[2]);
                }
            }
        }
	} catch (Exception e) {
		System.out.println("Error loading database.");
    }
	}
}
