package library;				// Eclipse IDE-specific line, please remove!
import java.io.*;				// BufferedReader and read database.txt packages
import java.util.ArrayList;		// arrays
import java.util.Scanner;		// yk what this is

public class PasswordManager {
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		ArrayList<ArrayList<String>> user = new ArrayList<>();
		ArrayList<ArrayList<String>> password = new ArrayList<>();
		ArrayList<String> site = new ArrayList<>();

		ArrayList<String> loginUser = new ArrayList<>();
		ArrayList<String> loginPass = new ArrayList<>();

		String db = "database.txt";

		// loads database on execution
		loadDB(db, site, user, password, loginUser, loginPass);

		// first time signup
		if (loginUser.isEmpty()) {
			System.out.println("First-time Setup");

			System.out.print("Enter username: ");
			loginUser.add(input.nextLine());

			System.out.print("Enter password: ");
			String pass = input.nextLine();

			System.out.print("Confirm password: ");
			String confirm = input.nextLine();

			if (pass.equals(confirm)) {
				loginPass.add(pass);
				System.out.println("Setup finished!");
				saveData(db, site, user, password, loginUser, loginPass);
			} else {
				System.out.println("Passwords do not match. Restart the program.");
				return;
			}
		} else {
			// login
			System.out.println("Please login.");
			boolean loggedIn = false;

			while (!loggedIn) {
				System.out.print("Username: ");
				String u = input.nextLine();

				System.out.print("Password: ");
				String p = input.nextLine();

				if (u.equals(loginUser.get(0)) && p.equals(loginPass.get(0))) {
					System.out.println("Successfully logged in.");
					loggedIn = true;
				} else {
					System.out.println("Incorrect username or password.");
				}
			}
		}

		// Main menu!!!
		while (true) {
			System.out.println("\n---Welcome to Password Manager---");
			System.out.println("1. Add Credentials");
			System.out.println("2. View Credentials");
			System.out.println("3. Delete Credentials");
			System.out.println("4. Exit");
			System.out.print("Choice: ");
			// tried using the int datatype on this, but it just won't work
			String choice = input.nextLine();

			// add credentials
			if (choice.equals("1")) {
				System.out.println("---Add Credentials---");

				System.out.print("Website: ");
				String newSite = input.nextLine();

				System.out.print("Username/Email: ");
				String newUser = input.nextLine();

				System.out.print("Password: ");
				String newPass = input.nextLine();

				int index = site.indexOf(newSite);

				if (index != -1) {								// determines if a site already exists
					// site already exists
					user.get(index).add(newUser);
					password.get(index).add(newPass);
				} else {										// creates a new site
					site.add(newSite);

					ArrayList<String> uList = new ArrayList<>();
					ArrayList<String> pList = new ArrayList<>();
																// add new username/pass to the new list
					uList.add(newUser);
					pList.add(newPass);

					user.add(uList);
					password.add(pList);
				}

				saveData(db, site, user, password, loginUser, loginPass);
				System.out.println("Credentials added!");

			}

			// VIEW
			else if (choice.equals("2")) {
				System.out.println("---View Credentials---");
				
				if (site.isEmpty()) {							// no credentials
					System.out.println("No credentials saved.");
					continue;									// returns to main menu
				}

				System.out.println("--- Websites ---");
				for (int i = 0; i < site.size(); i++) {
					System.out.println((i + 1) + ". " + site.get(i));
				}

				System.out.print("Select a website: ");
				int index = Integer.parseInt(input.nextLine()) - 1;

				if (index < 0 || index >= site.size()) {
					System.out.println("Invalid choice.");
					continue;
				}
				// accounts for <what site?>
				System.out.println("--- Accounts for " + site.get(index) + " ---");
				ArrayList<String> uList = user.get(index);
				ArrayList<String> pList = password.get(index);
				// lists down
				for (int i = 0; i < uList.size(); i++) {
					System.out.println((i + 1) + ". Username: " + uList.get(i));
					System.out.println("   Password: " + pList.get(i));
				}

			}

			// DELETE
			else if (choice.equals("3")) {
				if (site.isEmpty()) {
					System.out.println("There are no credentials added... yet!");
					continue;
				}
				// query the websites
				System.out.println("--- Websites ---");
				for (int i = 0; i < site.size(); i++) {
					System.out.println((i + 1) + ". " + site.get(i));
				}

				System.out.print("Select a website: ");
				int sIndex = Integer.parseInt(input.nextLine()) - 1;

				if (sIndex < 0 || sIndex >= site.size()) {
					System.out.println("Invalid choice.");
					continue;
				}

				ArrayList<String> uList = user.get(sIndex);
				ArrayList<String> pList = password.get(sIndex);

				System.out.println("--- Accounts ---");
				for (int i = 0; i < uList.size(); i++) {
					System.out.println((i + 1) + ". " + uList.get(i));
				}

				System.out.print("Select account number to delete: ");
				int aIndex = Integer.parseInt(input.nextLine()) - 1;

				if (aIndex < 0 || aIndex >= uList.size()) {
					System.out.println("Invalid number, try again!");
				} else {
					uList.remove(aIndex);
					pList.remove(aIndex);

					if (uList.isEmpty()) {
						site.remove(sIndex);
						user.remove(sIndex);
						password.remove(sIndex);
					}

					saveData(db, site, user, password, loginUser, loginPass);
					System.out.println("Deleted successfully!");
				}
			}

			// 
			else if (choice.equals("4")) {
				System.out.println("Exiting...");
				break;
			}

			else {
				System.out.println("Invalid choice, try again!");
			}
		}
	}
	// END OF PROGRAM, here are the methods

	// saveData method, i still don't understand how this works
	public static void saveData(String db, ArrayList<String> site, ArrayList<ArrayList<String>> user,
			ArrayList<ArrayList<String>> password, ArrayList<String> loginUser,
			ArrayList<String> loginPass) {
		// writes the credentials into a file
		try (PrintWriter pw = new PrintWriter(new FileWriter(db))) {
			
			if (!loginUser.isEmpty()) {																// shoves the input master login to the database.txt
				pw.println("Login Credentials |" + loginUser.get(0) + "|" + loginPass.get(0));
			}
			for (int i = 0; i < site.size(); i++) {
				ArrayList<String> uList = user.get(i);
				ArrayList<String> pList = password.get(i);

				for (int j = 0; j < uList.size(); j++) {
					pw.println(site.get(i) + "|" + uList.get(j) + "|" + pList.get(j));
				}
			}

		} catch (Exception e) {
			System.out.println("Error saving credentials.");
		}
	}

	// loadDB method, idk how it worked
	public static void loadDB(String db, ArrayList<String> site, ArrayList<ArrayList<String>> user,
			ArrayList<ArrayList<String>> password, ArrayList<String> loginUser,
			ArrayList<String> loginPass) {

		try {
			File file = new File(db);
			if (!file.exists()) return;
			// reads database
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;

				while ((line = br.readLine()) != null) {
					String[] parts = line.split("\\|");

					if (parts[0].equals("LOGIN")) {
						loginUser.add(parts[1]);
						loginPass.add(parts[2]);
					} else {
						String s = parts[0];
						String u = parts[1];
						String p = parts[2];

						int index = site.indexOf(s);

						if (index != -1) {
							user.get(index).add(u);
							password.get(index).add(p);
						} else {
							site.add(s);

							ArrayList<String> uList = new ArrayList<>();
							ArrayList<String> pList = new ArrayList<>();

							uList.add(u);
							pList.add(p);

							user.add(uList);
							password.add(pList);
						}
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Error loading data.");
		}
	}
}
