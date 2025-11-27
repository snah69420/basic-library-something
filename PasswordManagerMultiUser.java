package library; 				// eclipseIDE-specific line, please remove! again, REMOVE PLEASE
import java.io.*;				// BufferedReader, File, PrintWriter, IO
import java.util.ArrayList;		// Arrays
import java.util.Scanner;		// yk what this is

public class PasswordManagerOld_MultiUser {
	public static void main (String[] args) {

		Scanner input = new Scanner(System.in);

		// registered users
		ArrayList<String> loginUser = new ArrayList<>();
		ArrayList<String> loginPass = new ArrayList<>();
		ArrayList<String> userID = new ArrayList<>();
		String currentUserID = "";	// will be used below to store currentUserID when a user logs in

		// Credentials
		ArrayList<String> user = new ArrayList<>();
		ArrayList<String> password = new ArrayList<>();
		ArrayList<String> site = new ArrayList<>();
		ArrayList<String> credentialUserID = new ArrayList<>();
		String db = "database.txt";


		// load the database
		loadDB(db, user, password, site, loginUser, loginPass, userID, credentialUserID);

		boolean loggedIn = false;	// while loop till they finna login

		// Login/Register Screen
		while (!loggedIn) {
			System.out.println("1. Login");
			System.out.println("2. New User");
			System.out.print("Select choice: ");
			String loginChoice = input.nextLine();
			clearScreen();


			// Login
			if (loginChoice.equals("1")) {
				while (!loggedIn) {
					System.out.print("Username: ");
					String loginUserRegistered = input.nextLine();

					System.out.print("Password: ");
					String loginPassRegistered = input.nextLine();

					// kowalski, analysis
					boolean found = false;
					for (int i = 0; i < loginUser.size(); i++) { 					// this for loop loops through all registered users, it will try until it matches the userinput
						String storedUser = loginUser.get(i); 						//	<</ This part gets the username of
						String storedPass = loginPass.get(i); 						//	<<\ the (i)th registered user in the database.txt
						if (loginUserRegistered.equalsIgnoreCase(storedUser) && loginPassRegistered.equals(storedPass)) {
							System.out.println("Successfully logged in, welcome!");
							currentUserID = userID.get(i); 	 						// store the logged in user's userID to the memory
							loggedIn = true;
							found = true;	
							break; 													// for loop stop
						}
					}
					if (!found) {													// not found fallback
						System.out.println("Username or Password does not match, try again!");
					}
				}
				// register

			} else if (loginChoice.equals("2")) {

				boolean registered = false;
				while (!registered) {
					System.out.println("New User");

					System.out.print("Enter Username: ");
					String registerUser = input.nextLine();

					System.out.print("Enter Password: ");
					String registerPass = input.nextLine();

					System.out.print("Confirm Password: ");
					String registerPassConfirm = input.nextLine();

					// kowalski, analysis
					boolean exists = false;
					for (String u : loginUser) {
						if (u.equals(registerUser)) {
							exists = true;
							break;
						}
					}

					if (exists) {
						System.out.println("User already exists, try again!");
						continue; // returm
					}

					if (!registerPass.equals(registerPassConfirm)) {
						System.out.println("Passwords do not match, try again!");
						continue; //
					}

					// add to database
					// creates a unique ID for the newly registered user
					String regUserID = "U" + (userID.size() + 1);


					loginUser.add(registerUser);
					loginPass.add(registerPass);
					userID.add(regUserID);

					System.out.println("Successfully registered.");
					System.out.println("Your userID is: " + regUserID); 		// feel free to comment to remove this part

					saveData(db, user, password, site, credentialUserID, loginUser, loginPass, userID);

					currentUserID = regUserID;
					loggedIn = true;
					registered = true;
				}
			} else {
				System.out.println("Invalid choice, try again!");
				continue;
			}

			// END OF LOGIN/REGISTRATION BLOCK

			// -----------------------------------------------------------MAIN MENU!!!!----------------------------------------------------------------------------
			while (true) {
				int currentIndex = loginUser.indexOf(getCurrentUser(loginUser, userID, currentUserID));
				System.out.println("---Welcome to our Password Manager, " + loginUser.get(currentIndex) + "!---");
				System.out.println("1. Add Credentials");
				System.out.println("2. View Credentials");
				System.out.println("3. Delete Credentials");
				System.out.println("4. Delete Account" + " (" + loginUser.get(currentIndex) + " " + userID.get(currentIndex) + ")");
				System.out.println("5. Exit");
				System.out.print("Choice: ");

				String choice = input.nextLine();

				// Add Credentials
				if (choice.equals("1")) {
					clearScreen();
					System.out.println("---Add Credentials---");
					System.out.print("Enter Username/Email: ");
					user.add(input.nextLine());

					System.out.print("Enter Password: ");
					password.add(input.nextLine());

					System.out.print("Website?: ");
					site.add(input.nextLine());
					credentialUserID.add(currentUserID);

					System.out.println("Credentials added!");
					saveData(db, user, password, site, credentialUserID, loginUser, loginPass, userID);

					// view credentials
				} else if (choice.equals("2")) {
					clearScreen();
					System.out.println("---View Credentials---");
					ArrayList<Integer> userCredentials = new ArrayList<>(); 		// lists down the credentials that belong to currentUserID
					for (int i = 0; i < user.size(); i++) {							// list all credentials
						if (credentialUserID.get(i).equals(currentUserID)) userCredentials.add(i);
					}

					if (userCredentials.isEmpty()) {
						System.out.println("There are no credentials added, yet...");
					} else {
						System.out.println("--- Websites ---");
						for (int i = 0; i < userCredentials.size(); i++) {			// list all credentials of the currentUserID
							int index = userCredentials.get(i);
							System.out.println((i + 1) + ". " + site.get(index));	// site name
						}

						System.out.println("Select a website: ");
						int query_site = input.nextInt();
						input.nextLine();

						if (query_site < 1 || query_site > userCredentials.size()) {
							System.out.println("Invalid choice, try again!");

						} else {
							int site_index = userCredentials.get(query_site - 1);
							System.out.println("--- Credentials on " + site.get(site_index) + " ---");
							System.out.println("Username: " + user.get(site_index));
							System.out.println("Password: " + password.get(site_index));
							System.out.println("Press enter to return to menu...");
							input.nextLine();
						}
					}
					// delete credentials
				} else if (choice.equals("3")) {
					clearScreen();
					ArrayList<Integer> userCredentials = new ArrayList<>();
					for (int i = 0; i < user.size(); i++) {
						if (credentialUserID.get(i).equals(currentUserID)) userCredentials.add(i);
					}

					if (userCredentials.isEmpty()) {
						System.out.println("There are no credentials added, yet...");
					} else {
						System.out.println("---Delete Credentials---");
						for (int i = 0; i < userCredentials.size(); i++) {
							int idx = userCredentials.get(i);
							System.out.println((i + 1) + ". " + site.get(idx));
						}
						System.out.print("Enter a number to delete: ");
						int delIndex = Integer.parseInt(input.nextLine()) - 1;
						if (delIndex < 0 || delIndex >= userCredentials.size()) {
							System.out.println("Invalid number.");
						} else {
							int actualIndex = userCredentials.get(delIndex);
							user.remove(actualIndex);
							password.remove(actualIndex);
							site.remove(actualIndex);
							credentialUserID.remove(actualIndex);
							System.out.println("Credential deleted successfully");
						}
					}
				} else if (choice.equals("4")) {
					clearScreen();
					System.out.println("Are you sure you want to delete your account?");

					System.out.print("Enter your password to confirm: ");
					String confirmDelete = input.nextLine();

					System.out.println("ARE YOU SURE??? THIS WILL DELETE EVERYTHING YOU HAD IN YOUR LIFE. TYPE 'CONFIRM' TO CONFIRM DELETION.");

					System.out.print("Confirm: ");
					String againConfirm = input.nextLine();

					int currentlyLoggedIn = loginUser.indexOf(getCurrentUser(loginUser, userID, currentUserID));
					String deletion = loginPass.get(currentlyLoggedIn);

					if (!againConfirm.equals("CONFIRM")) {
						clearScreen();
						System.out.println("You did not type that correctly, try again!");
						continue;
					} else if (confirmDelete.equals(deletion)) {
						for (int i = credentialUserID.size() - 1; i >= 0; i--) {
							if (credentialUserID.get(i).equals(currentUserID)) {
								credentialUserID.remove(i);
								user.remove(i);
								password.remove(i);
								site.remove(i);
							}
						}
						// remove current session credentials
						loginUser.remove(currentlyLoggedIn);
						loginPass.remove(currentlyLoggedIn);
						userID.remove(currentlyLoggedIn);

						saveData(db, user, password, site, credentialUserID, loginUser, loginPass, userID);

						System.out.println("Account successfully deleted.");
						return;

					} else {
						System.out.println("Password does not match, try again!");
						return;
					}


					// interrupt
				} else if (choice.equals("5")) {
					clearScreen();
					System.out.println("Exiting.");
					break;
				} else {
					clearScreen();
					System.out.println("Invalid choice, try again!");
					continue;
				}
			}
		}
	}

	// END OF PROGRAM, here are the methods

	// getCurrentUser, 
	private static String getCurrentUser(ArrayList<String> loginUser, ArrayList<String> userID, String currentUserID) {
		for (int i = 0; i < userID.size(); i++) {
			if (userID.get(i).equals(currentUserID)) return loginUser.get(i);
		}
		return loginUser.get(0); //return to first username, definitely wont happen but just incase the thing crashes
	}

	// saveData method, i honestly dont know how this works
	public static void saveData(String db, ArrayList<String> user, ArrayList<String> password, ArrayList<String> site,
			ArrayList<String> credentialUserID, ArrayList<String> loginUser, ArrayList<String> loginPass, ArrayList<String> userID) {
		try {
			File file = new File(db); 				// change the directory if needed, this is tied to the variable at initialization
			File parent = file.getParentFile();
			if (parent != null) parent.mkdirs();	// mkdir when does not exist

			try (PrintWriter pw = new PrintWriter(new FileWriter(db))) {
				// login data
				for (int i = 0; i < loginUser.size(); i++) {
					String lu = loginUser.get(i).replace("|", "/");
					String lp = loginPass.get(i).replace("|", "/");
					String luID = userID.get(i).replace("|", "/");
					pw.println("LOGIN|" + lu + "|" + lp + "|" + luID);
				}
				// credential data
				for (int i = 0; i < user.size(); i++) {
					String u = user.get(i).replace("|", "/");
					String p = password.get(i).replace("|", "/");
					String s = site.get(i).replace("|", "/");
					String uid = credentialUserID.get(i);
					pw.println(u + "|" + p + "|" + s + "|" + uid);
				}
			}
			System.out.println("Database saved!");
		} catch (Exception e) {
			System.out.println("Error!");
		}
	}

	// loadDB method, i honestly dont know how this works
	public static void loadDB(String db, ArrayList<String> user, ArrayList<String> password, ArrayList<String> site,
			ArrayList<String> loginUser, ArrayList<String> loginPass, ArrayList<String> userID, ArrayList<String> credentialUserID) {
		try {
			File file = new File(db);
			if (!file.exists()) return;

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] parts = line.split("\\|");
					if (parts[0].equals("LOGIN")) {
						loginUser.add(parts[1]);
						loginPass.add(parts[2]);
						userID.add(parts[3]);
					} else if(parts.length >= 4) {
						user.add(parts[0]);
						password.add(parts[1]);
						site.add(parts[2]);
						credentialUserID.add(parts[3]);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error!");
		}
	}

	// cherry on top
	public static void clearScreen() {
		for (int i = 0; i < 50; i++) 
			System.out.println();
	}
}
