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
		
		// ANSI colors for the error stuff
		String RED = "\u001B[31m";
		String RESET = "\u001B[0m";


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
					for (int i = 0; i < loginUser.size(); i++) { 					// this for loop loops through all registered users, it will try until it matches the userinput (loginUserRegistered/login
						String storedUser = loginUser.get(i); 						//	<</ This part gets the username of
						String storedPass = loginPass.get(i); 						//	<<\ the (i)th registered user in the database.txt
						if (loginUserRegistered.equalsIgnoreCase(storedUser) && loginPassRegistered.equals(storedPass)) {
							clearScreen();
							System.out.println("Successfully logged in, Welcome!");
							currentUserID = userID.get(i); 	 						// store the logged in user's userID to the memory
							loggedIn = true;
							found = true;	
							break; 													// for loop stop
						}
					}
					if (!found) {													// not found fallback
						clearScreen();
						System.out.println(RED + "Username or Password does not match, try again!" + RESET);
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
						System.out.println(RED + "User already exists, try again!" + RESET);
						continue; // return
					}
					// if password does not match
					if (!registerPass.equals(registerPassConfirm)) {
						System.out.println(RED + "Passwords do not match, try again!" + RESET);
						continue; 
					// if user/pass is empty
					} else if (registerPass.isEmpty() || registerUser.isEmpty()) {
						System.out.println(RED + "Username/Password is empty, try again!" + RESET);
						continue;
					}

					// add to database
					// creates a unique ID for the newly registered user
					String regUserID = "U" + (userID.size() + 1);


					loginUser.add(registerUser);
					loginPass.add(registerPass);
					userID.add(regUserID);
					
					System.out.println("Successfully registered!");
					System.out.println("Your userID is: " + regUserID); 		// feel free to comment to remove this part, this was added for debugging reasons

					saveData(db, user, password, site, credentialUserID, loginUser, loginPass, userID);

					currentUserID = regUserID;
					loggedIn = true;
					registered = true;
				}
				
			} else {
				System.out.println(RED + "Invalid choice, try again!" + RESET);
				continue;
			}

			// END OF LOGIN/REGISTRATION BLOCK

			// -----------------------------------------------------------MAIN MENU!!!!----------------------------------------------------------------------------
			while (true) {
				// Currently logged in user
				int currentIndex = loginUser.indexOf(getCurrentUser(loginUser, userID, currentUserID));
				
				titleArt();
				
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

					clearScreen();
					System.out.println("Credentials added!");
					saveData(db, user, password, site, credentialUserID, loginUser, loginPass, userID);

				// view credentials, IDK I LET GEMINI EXPLAIN TO ME HOW TO LIST THE SITES WTHOUT DUPING
				} else if (choice.equals("2")) {
					clearScreen();
					System.out.println("--- View Credentials by Site ---");

					// list of credentals belonging to the current user
					ArrayList<Integer> userCredentialsIndices = new ArrayList<>();
					for (int i = 0; i < user.size(); i++) {
						if (credentialUserID.get(i).equals(currentUserID)) {
							userCredentialsIndices.add(i);
						}
					}

					if (userCredentialsIndices.isEmpty()) {
						System.out.println("There are no credentials added, yet...");
						System.out.print("Press enter to return to main menu...");
						input.nextLine();
						clearScreen();

					} else {
						// find all the unique site names
						ArrayList<String> uniqueSites = new ArrayList<>();
						for (int index : userCredentialsIndices) {
							String currentSite = site.get(index);
							// Only add the site name if it hasn't been added yet
							if (!uniqueSites.contains(currentSite)) {
								uniqueSites.add(currentSite);
							}
						}

						// site list main menu
						System.out.println("--- Websites ---");
						for (int i = 0; i < uniqueSites.size(); i++) {
							String siteName = uniqueSites.get(i);

							// count how many credentials belong to this site for the display
							int count = 0;
							for (int index : userCredentialsIndices) {
								if (site.get(index).equals(siteName)) {
									count++;
								}
							}
							// spit out the site and how many credentials are there
							System.out.println((i + 1) + ". " + siteName + " (" + count + " credential" + (count > 1 ? "s" : "") + ")");
						}

						System.out.print("Select a website number: ");


						int querySiteChoice = input.nextInt();
						input.nextLine();

						if (querySiteChoice < 1 || querySiteChoice > uniqueSites.size()) {
							System.out.println(RED + "Invalid choice, try again!" + RED);

						} else {
							clearScreen();
							String selectedSite = uniqueSites.get(querySiteChoice - 1);

							System.out.println("--- Credentials for " + selectedSite + " ---");

							// loop through all user's credentials thingy
							int credentialCount = 0;
							for (int actualIndex : userCredentialsIndices) {
								if (site.get(actualIndex).equals(selectedSite)) {
									credentialCount++;
									System.out.println("Credential " + credentialCount + ":");
									System.out.println("  Username/Email: " + user.get(actualIndex));
									System.out.println("  Password: " + password.get(actualIndex));
									System.out.println("-------------------------");
								}
							}

							System.out.println("Press enter to return to menu...");
							input.nextLine();
							clearScreen();
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
							System.out.println((i + 1) + ". " + user.get(idx) + " " + "(" + site.get(idx) + ")");
						} // list credentials
						System.out.print("Enter a number to delete: ");
						int delIndex = Integer.parseInt(input.nextLine()) - 1;
						if (delIndex < 0 || delIndex >= userCredentials.size()) {
							System.out.println(RED + "Invalid number." + RESET);
						} else {
							int actualIndex = userCredentials.get(delIndex);
							user.remove(actualIndex);
							password.remove(actualIndex);
							site.remove(actualIndex);
							credentialUserID.remove(actualIndex);
							
							clearScreen();
							System.out.println("Credential deleted successfully!");
							
							saveData(db, user, password, site, credentialUserID, loginUser, loginPass, userID);
						}
					}
				} else if (choice.equals("4")) {
					
					clearScreen();
					
					System.out.println("Are you sure you want to delete your account?");

					System.out.print("Enter your password to confirm: ");
					String confirmDelete = input.nextLine();

					System.out.println(RED + "ARE YOU SURE??? THIS WILL DELETE ALL DATA ASSOCIATED TO YOUR USER. TYPE 'CONFIRM' TO CONFIRM DELETION." + RESET);

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
						// remove current session's entire credentials
						loginUser.remove(currentlyLoggedIn);
						loginPass.remove(currentlyLoggedIn);
						userID.remove(currentlyLoggedIn);

						saveData(db, user, password, site, credentialUserID, loginUser, loginPass, userID);

						System.out.println("Account successfully deleted.");
						System.out.println("I am not responsible for bricked devices, dead SD cards, * thermonuclear war, or you getting fired because the alarm app failed.");
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
					System.out.println(RED + "Invalid choice, try again!" + RESET);
					continue; // return to main menu :P
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
		return loginUser.get(0); //return to first username, definitely wont happen but this will just prevent the app from crashing when it happens :P
	}

	// saveData method, this will print the provided input to database.txt
	public static void saveData(String db, ArrayList<String> user, ArrayList<String> password, ArrayList<String> site,
			ArrayList<String> credentialUserID, ArrayList<String> loginUser, ArrayList<String> loginPass, ArrayList<String> userID) {
		try {
			File file = new File(db); 				// change the directory if needed, this is tied to the variable at initialization
			File parent = file.getParentFile();
			if (parent != null) parent.mkdirs();	// mkdir when does not exist

			try (PrintWriter pw = new PrintWriter(new FileWriter(db))) {
				// login data save to txt
				for (int i = 0; i < loginUser.size(); i++) {
					String lu = loginUser.get(i).replace("|", "/");
					String lp = loginPass.get(i).replace("|", "/");
					String luID = userID.get(i).replace("|", "/");
					pw.println("LOGIN|" + lu + "|" + lp + "|" + luID);
				}
				// credential data save to txt
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

	// loadDB method, loads the db on execution, i honestly forgot how this worked
	public static void loadDB(String db, ArrayList<String> user, ArrayList<String> password, ArrayList<String> site,
			ArrayList<String> loginUser, ArrayList<String> loginPass, ArrayList<String> userID, ArrayList<String> credentialUserID) {
		try {
			File file = new File(db);
			if (!file.exists()) return;

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] parts = line.split("\\|");
					// load loginData
					if (parts[0].equals("LOGIN")) {
						loginUser.add(parts[1]);
						loginPass.add(parts[2]);
						userID.add(parts[3]);
					// load Credentials
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

	// cherry on top, floods the thing with blank lines 100 times
	public static void clearScreen() {
		for (int i = 0; i < 100; i++)
			System.out.println();
	}
	// ASCII ART!!! very kewl XD
	public static void titleArt() {
		System.out.println();
		System.out.println(" ▄▄▄▄▄                                                █         ▄    ▄                                          ");
		System.out.println(" █   ▀█  ▄▄▄    ▄▄▄    ▄▄▄  ▄     ▄  ▄▄▄    ▄ ▄▄   ▄▄▄█         ██  ██  ▄▄▄   ▄ ▄▄    ▄▄▄    ▄▄▄▄   ▄▄▄    ▄ ▄▄ ");
		System.out.println(" █▄▄▄█▀ ▀   █  █   ▀  █   ▀ ▀▄ ▄ ▄▀ █▀ ▀█   █▀  ▀ █▀ ▀█         █ ██ █ ▀   █  █▀  █  ▀   █  █▀ ▀█  █▀  █   █▀  ▀");
		System.out.println(" █      ▄▀▀▀█   ▀▀▀▄   ▀▀▀▄  █▄█▄█  █   █   █     █   █         █ ▀▀ █ ▄▀▀▀█  █   █  ▄▀▀▀█  █   █  █▀▀▀▀   █    ");
		System.out.println(" █      ▀▄▄▀█  ▀▄▄▄▀  ▀▄▄▄▀   █ █   ▀█▄█▀   █     ▀█▄██         █    █ ▀▄▄▀█  █   █  ▀▄▄▀█  ▀█▄▀█  ▀█▄▄▀   █    ");
		System.out.println("                                                                                             ▄  █               ");
		System.out.println("                                                                                              ▀▀                ");
	}
}
