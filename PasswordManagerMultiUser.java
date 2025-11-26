package library; 				// eclipseIDE-specific line, please remove!
import java.io.*;				
import java.util.ArrayList;		
import java.util.Scanner;		

public class PasswordManagerOld_MultiUser {
	public static void main (String[] args) {

		Scanner input = new Scanner(System.in);

		// registered users
		ArrayList<String> loginUser = new ArrayList<>();
		ArrayList<String> loginPass = new ArrayList<>();
		ArrayList<String> userID = new ArrayList<>();
		String currentUserID = "";

		// Credentials
		ArrayList<String> user = new ArrayList<>();
		ArrayList<String> password = new ArrayList<>();
		ArrayList<String> site = new ArrayList<>();
		ArrayList<String> credUserID = new ArrayList<>();
		String db = "database.txt";


		// load the database
		loadDB(db, user, password, site, loginUser, loginPass, userID, credUserID);
		
		boolean loggedIn = false;
		while (!loggedIn) {
		// Login/Register Screen
		System.out.println("1. Login");
		System.out.println("2. New User");
		System.out.print("Select choice: ");
		String loginChoice = input.nextLine();


		// Login
		if (loginChoice.equals("1")) {
			while (!loggedIn) {
				System.out.print("Username: ");
				String loginUserRegistered = input.nextLine();

				System.out.print("Password: ");
				String loginPassRegistered = input.nextLine();

				// kowalski, analysis
				boolean found = false;
				for (int i = 0; i < loginUser.size(); i++) { // this for loop loops through all registered users
					String storedUser = loginUser.get(i); //	<</ This part gets the username of
					String storedPass = loginPass.get(i); //	<<\ the (i)th registered user
					if (loginUserRegistered.equals(storedUser) && loginPassRegistered.equals(storedPass)) {
						System.out.println("Successfully logged in, welcome!");
						currentUserID = userID.get(i); 	 // store the logged in user's userID to the memory
						loggedIn = true;
						found = true;	
						break; //for loop stop
					}
				}
				if (!found) {	// not found fallback
					System.out.println("Username or Password does not match, try again!");
				}
			}
			// register
		} else if (loginChoice.equals("2")) {
			System.out.println("New User");

			System.out.print("Enter Username: ");
			String registerUser = input.nextLine();

			System.out.print("Enter Password: ");
			String registerPass = input.nextLine();

			System.out.print("Confirm Password: ");
			String registerPassConfirm = input.nextLine();

			if (registerPass.equals(registerPassConfirm)) {
				System.out.println("Successfully registered.");

				String regUserID = "U" + (userID.size() + 1);

				loginUser.add(registerUser);
				loginPass.add(registerPass);
				userID.add(regUserID);
				currentUserID = regUserID;

				saveData(db, user, password, site, credUserID, loginUser, loginPass, userID);
			}
		} else {
			System.out.println("Invalid choice, try again!");
		}
		}
		// MAIN MENU!!!!
		while (true) {
			int currentIndex = loginUser.indexOf(getCurrentUser(loginUser, userID, currentUserID));
			System.out.println("---Welcome to our Password Manager, " + loginUser.get(currentIndex) + "!---");
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
				credUserID.add(currentUserID);

				System.out.println("Credentials added!");
				saveData(db, user, password, site, credUserID, loginUser, loginPass, userID);
			// view credentials
			} else if (choice.equals("2")) {
				System.out.println("---View Credentials---");
				ArrayList<Integer> userCredentials = new ArrayList<>(); // lists down the credentials that belong to currentUserID
				for (int i = 0; i < user.size(); i++) {					// list all credentials
					if (credUserID.get(i).equals(currentUserID)) userCredentials.add(i);
				}

				if (userCredentials.isEmpty()) {
					System.out.println("There are no credentials added, yet...");
				} else {
					System.out.println("--- Websites ---");
					for (int i = 0; i < userCredentials.size(); i++) {	// list all credentials of the currentUserID
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
				ArrayList<Integer> userCredentials = new ArrayList<>();
				for (int i = 0; i < user.size(); i++) {
					if (credUserID.get(i).equals(currentUserID)) userCredentials.add(i);
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
						credUserID.remove(actualIndex);
						System.out.println("Credential deleted successfully");
					}
				}
			// interrupt
			} else if (choice.equals("4")) {
				System.out.println("Exiting.");
				break;
			}
		}
	}
	// getCurrentUser, 
	private static String getCurrentUser(ArrayList<String> loginUser, ArrayList<String> userID, String currentUserID) {
		for (int i = 0; i < userID.size(); i++) {
			if (userID.get(i).equals(currentUserID)) return loginUser.get(i);
		}
		return loginUser.get(0); //return to first username, definitely wont happen but just incase the thing crashes
	}
	// saveData method, i honestly dont know how this works
	public static void saveData(String db, ArrayList<String> user, ArrayList<String> password, ArrayList<String> site,
			ArrayList<String> credUserID, ArrayList<String> loginUser, ArrayList<String> loginPass, ArrayList<String> userID) {
		try {
			File file = new File(db);
			File parent = file.getParentFile();
			if (parent != null) parent.mkdirs();

			try (PrintWriter pw = new PrintWriter(new FileWriter(db))) {
				for (int i = 0; i < loginUser.size(); i++) {
					String lu = loginUser.get(i).replace("|", "/");
					String lp = loginPass.get(i).replace("|", "/");
					String luID = userID.get(i).replace("|", "/");
					pw.println("LOGIN|" + lu + "|" + lp + "|" + luID);
				}
				for (int i = 0; i < user.size(); i++) {
					String u = user.get(i).replace("|", "/");
					String p = password.get(i).replace("|", "/");
					String s = site.get(i).replace("|", "/");
					String uid = credUserID.get(i);
					pw.println(u + "|" + p + "|" + s + "|" + uid);
				}
			}
			System.out.println("Database saved successfully.");
		} catch (Exception e) {
			System.out.println("Error saving database.");
		}
	}
	// loadDB method, i honestly dont know how this works
	public static void loadDB(String db, ArrayList<String> user, ArrayList<String> password, ArrayList<String> site,
			ArrayList<String> loginUser, ArrayList<String> loginPass, ArrayList<String> userID, ArrayList<String> credUserID) {
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
						credUserID.add(parts[3]);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error loading database.");
		}
	}
}
