import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Desktop;
import java.io.File;

public class Main {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		ArrayList<String> title = new ArrayList<>();
		ArrayList<String> author = new ArrayList<>();
		ArrayList<String> category = new ArrayList<>();
		ArrayList<String> year = new ArrayList<>();
		ArrayList<String> path = new ArrayList<>();

		String fileName = "library_directory.txt";

		// saved data
		while (true) {
			System.out.println("---Welcome to the Austronesian Library!---");
			System.out.println("1. Add Book");
			System.out.println("2. Delete Book");
			System.out.println("3. List Books");
			System.out.println("4. Open Book");
			System.out.println("5. Exit");
			System.out.print("Choice: ");

			String choice = input.nextLine();

			// add a book
			if (choice.equals("1")) {
				System.out.print("Enter Title: ");
				title.add(input.nextLine());

				System.out.print("Enter Author: ");
				author.add(input.nextLine());

				System.out.print("Enter Category: ");
				category.add(input.nextLine());

				System.out.print("Enter Year: ");
				year.add(input.nextLine());

				System.out.print("Enter File Path: ");
				path.add(input.nextLine());

				System.out.println("Book added!");
				saveLibrary(fileName, title, author, category, year, path);

			// Delete Book
			} else if (choice.equals(2)) {
				if (title.isEmpty()) {
					System.out.println("There are no books to delete.");
				} else {
					System.out.println("\n--- Delete Book ---");
					for (int i = 0; i < title.size(); i++) {
						System.out.println((i + 1) + ". " + title.get(i) + 	" by " + author.get(i));
					}
					System.out.print("Enter book number to delete: ");
					int delIndex = Integer.parseInt(input.nextLine()) - 1;

					if (delIndex < 0 || delIndex >= title.size()) {
						System.out.println("Invalid number.");
					} else {
						title.remove(delIndex);
						author.remove(delIndex);
						category.remove(delIndex);
						year.remove(delIndex);
						path.remove(delIndex);
						System.out.println("Book deleted successfully");
					}
				}
			// List Books
			} else if (choice.equals("3")) {
				if (title.isEmpty()) {
					System.out.println("No books yet.");
				} else {
					System.out.println("\n=== LIST OF BOOKS ===");
					for (int i = 0; i < title.size(); i++) {
						System.out.println("\nBook #" + (i + 1));
						System.out.println("Title: " + title.get(i));
						System.out.println("Author: " + author.get(i));
						System.out.println("Category: " + category.get(i));
						System.out.println("Year: " + year.get(i));
						System.out.println("File: " + path.get(i));
					}

				}
			// open book
			} else if (choice.equals("4")) {
            if (title.isEmpty()) {
                System.out.println("No books to open.");
            } else {
                System.out.println("\n--- Open Book ---");
                for (int i = 0; i < title.size(); i++) {
                    System.out.println((i + 1) + ". " + title.get(i));
                }
                System.out.print("Enter book number to open: ");
                int openIndex = Integer.parseInt(input.nextLine()) - 1;

                if (openIndex < 0 || openIndex >= title.size()) {
                    System.out.println("Invalid number.");
                } else {
                    String filePath = path.get(openIndex);
                    File file = new File(filePath);
                    if (!file.exists()) {
                        System.out.println("File does not exist at the given path.");
                    } else {
                        try {
                            java.awt.Desktop.getDesktop().open(file);
                            System.out.println("Opening " + title.get(openIndex) + "...");
                        } catch (Exception e) {
                            System.out.println("Error.");
                        }
                    }
                }
            }
            // exit
            } else if (choice.equals("5")) {
                System.out.println("Goodbye!");
                break;
            // invalid
            } else {
            	System.out.println("Invalid.");
            }
			input.close();
		}
	}

	// save to txt thingy
	public static void saveLibrary(String fileName, ArrayList<String> title, ArrayList<String> author,
			ArrayList<String> category, ArrayList<String> year, ArrayList<String> path) {
		try {
			File file = new File(fileName);
			file.getParentFile().mkdirs();

			try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
				for (int i = 0; i < title.size(); i++) {
					String t = title.get(i).replace("|", "/");
					String a = author.get(i).replace("|", "/");
					String c = category.get(i).replace("|", "/");
					String y = year.get(i).replace("|", "/");
					String p = path.get(i).replace("|", "/");

					pw.println(t + "|" + a + "|" + c + "|" + y + "|" + p);
				}
			}

			System.out.println("Library saved successfully.");

		} catch (Exception e) {
			System.out.println("Error saving library.");
		}
	}
}
