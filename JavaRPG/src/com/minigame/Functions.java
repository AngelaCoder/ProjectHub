package com.minigame;

import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Functions extends Information {

	Scanner input = new Scanner(System.in);
	Random rand = new Random();
	FileWriterUtil userFile;
	boolean askAgain = true;
	String userName = "";
	String userID = "";
	String password = "";
	String directoryPath = "."; // Set the default directory path here
	String description = "";

	public void greeting() {

		System.out.println("Welcome to a mini board game!");

		loginScreen();

	}

	public void loginScreen() {

		ArrayList<String> existingUserIDs = getListOfExistingUserIDs(directoryPath);

		System.out.println("Main Menu:");
		System.out.println("1. Log In");
		System.out.println("2. Sign Up");
		System.out.println("3. Exit the Game");
		System.out.println();

		int userChoice = -1;
		do {

			try {
				System.out.print("Please choose an option (1/2/3): ");

				userChoice = Integer.parseInt(input.nextLine());

				if (userChoice < 1 || userChoice > 4) {

					askAgain = false;
				} else if (userChoice >= 1 && userChoice <= 3) {

					askAgain = true;
				}

			} catch (Exception e) {
				System.out.println("You typed a wrong answer. Please enter a number.");
				askAgain = false;
			}

		} while (!askAgain);

		switch (userChoice) {

		case 1:
			login();
			break;

		case 2:
			signUp();
			break;

		case 3:
			System.out.println("If you ever decide to return, your adventure awaits. Farewell and take care!");
			System.exit(0);

		}

	}

	public void login() {
		// ArrayList<String> existingUserIDs = getListOfExistingUserIDs(directoryPath);
		boolean isLoginSuccessful = false;

		System.out.println("Enter your ID: ");
		userID = input.nextLine().toLowerCase();

		if (isValidUserID(userID)) {
			String fileName = userID + ".txt";
			String line;

			try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(":", 2);

					if (parts.length == 2) {
						String key = parts[0].trim();
						String value = parts[1].trim();

						if (key.equals("User password")) {
							System.out.println("Enter password: ");
							String password = input.nextLine();

							if (password.equals(value)) {
								System.out.println("Login successful. \n");
								userFile = new FileWriterUtil(this.userID);
								this.description = this.userID + " successfully logged in.";
								userFile.writeLog(this.description);
								super.setUserID(userID);

								isLoginSuccessful = true;
							} else {
								System.out.println("Invalid password.");
								break;
							}
						} else {

							// Update userInfo based on key
							switch (key) {
							case "User Name":
								super.setUserName(value);
								break;
							case "User ID":
								super.setUserID(value);
								break;
							case "User HP":
								super.setUserHP(Integer.parseInt(value));
								break;
							case "User Money":
								super.setUserMoney(Integer.parseInt(value));
								break;
							case "User Steps":
								super.setUserSteps(Integer.parseInt(value));
								break;
							}
						}
					}
				}

				if (isLoginSuccessful) {
					options(); // Call the options method with updated userInfo
				}
			} catch (IOException e) {
				System.err.println("An error occurred while reading the file: " + e.getMessage());
			}
		} else {
			System.out.println(userID + " does not exist.");
			loginScreen();

		}
	}

	public void signUp() {

		ArrayList<String> existingUserIDs = getListOfExistingUserIDs(directoryPath);

		System.out.println("Enter your name: ");
		userName = input.nextLine();
		boolean accountCreated = false;

		while (!accountCreated) {
			System.out.println("Enter your ID: ");
			userID = input.nextLine().toLowerCase();

			if (isValidUserID(userID)) {
				System.out.println("User ID is taken. Please choose another one.");
			} else {
				while (true) {
					System.out.println("Enter password");
					password = input.nextLine();
					System.out.println("Re-Enter password");
					String password2 = input.nextLine();

					if (password2.equals(password)) {
						try {
							PrintWriter userFileWriter = new PrintWriter(new FileWriter(userID + ".txt"));
							userFile = new FileWriterUtil(this.userID);
							userFileWriter.println("User Name:" + userName);
							userFileWriter.println("User ID:" + userID);
							userFileWriter.println("User password:" + password);

							userFileWriter.println("User HP:1000");
							userFileWriter.println("User Money:500");
							userFileWriter.println("User Steps:0");
							userFileWriter.close();
							this.description = "Account created successfully.";
							System.out.println(this.description);

							userFile.writeLog(this.description);

							accountCreated = true;
							break;
						} catch (IOException e) {
							System.err.println("An error occurred while creating the user account.");
							e.printStackTrace();
						}
					} else {
						System.out.println("Passwords do not match. Please try again.");
					}
				}
			}
		}
		loginScreen();
	}

	private ArrayList<String> getListOfExistingUserIDs(String directoryPath) {
		ArrayList<String> existingUserIDs = new ArrayList<>();
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		int i = 0;
		if (files != null) {
			for (File userFile : files) {
				String fileName = userFile.getName();
				if (fileName.endsWith(".txt")) {

					String userID = fileName.substring(0, fileName.lastIndexOf('.'));
					existingUserIDs.add(userID);
					i++;
				}

			}
		}
		if (i == 0) {

			System.out.println();

		}

		return existingUserIDs;
	}

	private boolean isValidUserID(String userID) {
		ArrayList<String> existingUserIDs = getListOfExistingUserIDs(directoryPath);
		if (existingUserIDs.contains(userID)) {

			return true;

		} else {

			return false;
		}
	}

	public void rollDice(Random rand, int diceNumber) {
		diceNumber = rand.nextInt(6) + 1;
		System.out.println("You rolled the dice.");
		System.out.println("You got " + diceNumber);
		userSteps += diceNumber;
		super.setUserSteps(userSteps);
		gameLevel(userSteps);

	}

	public void options() {
		int optionChoice = 0;

		while (optionChoice != 4) {
			System.out.println();
			System.out.println("1. Check User Status");
			System.out.println("2. Roll the dice");
			System.out.println("3. Restore HP");
			System.out.println("4. Exit the game");
			System.out.print("Please choose an option (1/2/3/4): ");

			try {
				String userInput = input.nextLine();
				optionChoice = Integer.parseInt(userInput);

				if (optionChoice < 1 || optionChoice > 4) {
					System.out.println("Invalid choice. Please choose a valid option (1/2/3/4):");
				} else {
					switch (optionChoice) {
					case 1:
						System.out.printf("%-10s %-7s %-7s %-7s  \n", "Name", "HP", "Money", "UserSteps");
						System.out.printf("%-10s %-7d %-6d  %-7d \n", super.getUserID(), super.getUserHP(),
								super.getUserMoney(), super.getUserSteps());
						System.out.println();
						break;
					case 2:
						int number = 0;
						rollDice(rand, number);
						break;
					case 3:
						restoreHP(super.getUserHP());
						break;
					case 4:
						System.out.println(
								"Thank you for playing our game! We hope you had a great time. See you next time!");
						System.exit(0);
						break;
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");

			}
		}
	}

	public void restoreHP(int userHP) {

		this.userHP = userHP;

		System.out.println("Recharge your HP to restore your strength! \n"
				+ "It costs 100 coins to recharge 100 HP. How much HP would you like to recharge? \n"
				+ "Please enter a number that is a multiple of 100 for HP recharge: \n" + "Type Q to quit");

		while (true) {
			try {
				System.out.print("Enter 'q' to quit or a numerical value to recharge HP: ");
				String userChoice = input.nextLine();
				if (userChoice.equalsIgnoreCase("q")) {
					options();
					break;
				}

				int hpToRecharge = Integer.parseInt(userChoice);

				if (hpToRecharge < 0) {
					System.out.println("Please enter a positive number for HP recharge.");
					continue;
				} else if (hpToRecharge == 0) {
					System.out.println("No HP added. Your HP remains unchanged.");
					continue;
				} else if (!(hpToRecharge % 100 == 0)) {
					System.out.println("Please enter a valid multiple of 100 HP for recharge.");
					continue;
				} else if (super.getUserHP() == 1000) {
					System.out.println(this.userID + "'s HP is already at its maximum capacity.");
					break;
				} else {
					int currentHP = super.getUserHP();
					int maxHP = 1000;
					int remainingHP = maxHP - currentHP;

					if (hpToRecharge > remainingHP) {
						hpToRecharge = remainingHP;
					}

					int hpRechargeCost = (hpToRecharge / 100) * 100;

					if (hpRechargeCost > super.getUserMoney()) {
						System.out.println("Insufficient coins to recharge this much HP.");
					} else {
						super.setUserHP(currentHP + hpToRecharge);
						System.out.println(this.userID + "'s HP has been successfully recharged by " + hpToRecharge
								+ " points. Strength has been restored!");
						super.setUserMoney(super.getUserMoney() - hpRechargeCost);
						System.out.println("You spent " + hpRechargeCost + " coins on HP recharge.");
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Error: Please enter 'q' or a valid numerical value.");
			}
		}

		userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
	}

	public void gameLevel(int userSteps) {

		this.userSteps = userSteps;

		switch (userSteps) {

		case 9:
		case 36:
		case 11:
		case 22:
		case 38:
		case 25:
		case 34:
			warningMessage();
			level1();
			break;

		case 2:
		case 6:
		case 10:
		case 20:
		case 37:

			warningMessage();
			level2();
			break;
		case 1:
		case 7:
		case 3:

		case 24:
			warningMessage();
			level3();
			break;

		case 4:
		case 13:
		case 19:
		case 35:

			warningMessage();
			level4();
			break;

		case 5:
		case 14:
		case 26:
		case 8:
		case 33:
		case 39:
			warningMessage();
			level5();
			break;
		case 23:
		case 29:
		case 15:
		case 12:
			warningMessage();
			level6();
			break;
		case 21:
		case 31:
		case 16:
		case 28:
			warningMessage();
			level7();
			break;

		case 17:
		case 32:
			warningMessage();
			level8();
			break;

		case 18:
		case 27:
		case 30:
			warningMessage();
			level9();
			break;

		case 40:
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
			description = "Congratulations, " + this.userID + "! You have completed the game by reaching "
					+ super.finishedSteps + " steps!";
			System.out.println(description);
			userFile.writeLog(description);
			System.exit(0);
			break;

		default:
			if (userSteps >= 40) {
				userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
				description = "Congratulations, " + this.userID + "! You have completed the game by reaching "
						+ finishedSteps + " steps!";
				System.out.println(description);
				userFile.writeLog(description);
				System.exit(0);

			}

		}

		options();

	}

	public void level1() {
		System.out.println();
		System.out.println("********************************************************");
		System.out.println("Solve the following math game");
		System.out.println("You have 5 Math questions!");
		System.out.println("Make sure to get at least 3 answers right.");
		System.out.println("Otherwise, something mysterious will happen to you...");
		System.out.println("********************************************************");

		int problems = 4;
		int i = 0;
		String userAnswer = "";
		String[] userAnswers = new String[5];
		String[] answers = { "A", "B", "C", "D" };

		while (problems > 0) {
			int operand1 = rand.nextInt(10) + 1;
			int operand2 = rand.nextInt(10) + 1;
			int operatorType = rand.nextInt(3) + 1;
			int choice1;
			int choice2;
			int choice3;

			String operator = "+";
			int result = -1;

			switch (operatorType) {
			case 1:
				operator = "+";
				result = operand1 + operand2;
				break;
			case 2:
				operator = "-";
				result = operand1 - operand2;
				break;
			case 3:
				operator = "*";
				result = operand1 * operand2;
				break;
			default:
				operator = "Invalid";
				break;
			}

			do {
				choice1 = rand.nextInt(30) + 1;
				choice2 = rand.nextInt(30) + 1;
				choice3 = rand.nextInt(30) + 1;
			} while (choice1 == choice2 || choice1 == choice3 || choice2 == choice3);

			System.out.println("What is " + operand1 + " " + operator + " " + operand2 + " ?");
			System.out.println("A: " + (i == 0 ? result : choice1));
			System.out.println("B: " + (i == 1 ? result : choice2));
			System.out.println("C: " + (i == 2 ? result : choice3));
			System.out.println("D: " + (i == 3 ? result : choice1));

			while (true) {
				System.out.print("Answer: ");
				userAnswer = input.nextLine().toUpperCase();

				if (!(userAnswer.equals("A") || userAnswer.equals("B") || userAnswer.equals("C")
						|| userAnswer.equals("D"))) {
					System.out.println("Invalid input. Please enter A, B, C, or D.");
				} else {
					break;
				}
			}

			userAnswers[i] = userAnswer;
			i++;
			problems--;
		}

		int count = 0;
		for (i = 0; i < 4; i++) {
			if (userAnswers[i].equals(answers[i])) {
				count++;
			}
		}

		System.out.println("Correct Answers: " + count);

		if (count == 4) {

			System.out.println(super.getUserID() + " earned 100 coins.");
			super.setUserMoney(super.getUserMoney() + 100);
			this.description = this.userID + " defeated spiders and earned 100 coins.";
			userFile.writeLog(this.description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());

		} else {

			System.out.println("Spiders started attacking " + super.getUserID());
			System.out.println(super.getUserID() + " lost 50 HP.");
			super.setUserHP(super.getUserHP() - 50);
			this.description = "The spider attacked " + this.userID + " and " + this.userID + " lost 50HP";
			userFile.writeLog(description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());

		}

	}

	public void level2() {
		String answer = "leave";
		String userAnswer;
		int tries = 0;
		int chances = 5;
		boolean askQuiz = true;

		System.out.println();
		System.out.println(super.getUserID() + " walked in the forest.");
		System.out.println("A mysterious looking monster appeared in front of " + super.getUserID());
		System.out.println("A goblin: Excuse me? What do you think you are doing?");
		System.out.println("This forest belongs to me! You are trespassing on my forest!");
		System.out.println(super.getUserID() + ": I am sorry! I can leave now");
		System.out.println("A goblin: Wait! You can only leave if you solve my riddle!");
		System.out.println("I will give you 5 chances... If you solve it, you can leave");
		System.out.println("If not, then... I will take your coins!");
		System.out.println("**********************************************");
		System.out.println("Riddle: Walk on the living, they don't even mumble.");
		System.out.println("Walk on the dead, they mutter and grumble. What are they?");
		System.out.println("What is it?");
		System.out.println("**********************************************");
		System.out.print("Your answer: ");

		while (askQuiz) {
			userAnswer = input.nextLine().trim().toLowerCase();

			if (userAnswer.contains("leave") || userAnswer.contains("leaves")) {
				System.out.println("A goblin: Grrrr Humans are smarter than I thought!");
				System.out.println(super.getUserID() + " gained 100 coins.");
				super.setUserMoney(super.getUserMoney() + 100);
				this.description = this.userID + " defeated a monster and earned 100 coins";
				userFile.writeLog(this.description);
				userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());

				return;

			} else if (chances <= 1) {
				System.out.println("A goblin: MUAHAHAHA! YOU FOOL!");
				System.out.println("The answer is 'leave'!");
				System.out.println("I'm going to take your 30 coins!");
				System.out.println(super.getUserID() + " lost 30 coins.");
				super.setUserMoney(super.getUserMoney() - 30);
				this.description = this.userID + " lost 30 coins after being defeated by a monster.";
				userFile.writeLog(this.description);
				userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());

				return;
			} else {
				System.out.println(userAnswer + " is a wrong answer. Try again.");
				System.out.print("Your answer: ");
				chances--;
			}
		}
	}

	public void level3() {
		System.out.println();
		System.out.println(super.getUserID() + " walked into a swamp.");
		System.out.println("Crocodile is approaching " + super.getUserID() + " fast.");
		System.out.println(super.getUserID() + " needs to solve 2 questions to run away from a crocodile fast!");

		String[] answer = { "A", "B", "C" };
		String[][] userAnswer = new String[1][3];
		String choice = "E";
		int count = 0;

		System.out.println("Which US state has the least amount of precipitation?");
		System.out.println("A: Nevada  B: Arizona  C: Colorado  D: New Mexico");

		while (true) {
			System.out.print("Your answer: ");
			choice = input.nextLine().toUpperCase().trim();

			if (choice.equals("A") || choice.equals("B") || choice.equals("C") || choice.equals("D")) {
				break;
			} else {
				System.out.println("Invalid input. Please enter A,B,C, or D.");
				continue;
			}
		}

		userAnswer[0][0] = choice;

		System.out.println("What country has the longest coastline?");
		System.out.println("A: Japan  B: Canada  C: Mexico  D: America");

		while (true) {
			System.out.print("Your answer: ");
			choice = input.nextLine().toUpperCase().trim();

			if (choice.equals("A") || choice.equals("B") || choice.equals("C") || choice.equals("D")) {
				break;
			} else {
				System.out.println("Invalid input. Please enter A,B,C, or D.");
				continue;
			}
		}
		userAnswer[0][1] = choice;

		System.out.println("What is the capital city of Missouri?");
		System.out.println("A: St. Louis  B: Springfield C: Jefferson City D: Columbia");

		while (true) {
			System.out.print("Your answer: ");
			choice = input.nextLine().toUpperCase().trim();

			if (choice.equals("A") || choice.equals("B") || choice.equals("C") || choice.equals("D")) {
				break;
			} else {
				System.out.println("Invalid input. Please enter A,B,C, or D.");
				continue;
			}
		}
		userAnswer[0][2] = choice;

		for (int row = 0; row < userAnswer.length; row++) {
			for (int column = 0; column < userAnswer[0].length; column++) {
				if (userAnswer[row][column] == answer[column]) {
					count++;
				}
			}
		}

		if (count == 4) {
			// Success condition
			System.out.println("A crocodile disappeared.");
			System.out.println(super.getUserID() + " earned 100 coins.");
			super.setUserMoney(super.getUserMoney() + 100);
			this.description = this.userID + " defeated a crocodile and earned 100 coins.";
			userFile.writeLog(this.description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
		} else {
			// Failure condition
			System.out.println("A crocodile attacked " + super.getUserID());
			System.out.println(super.getUserID() + " got hurt and lost 15 HP.");
			super.setUserHP(super.getUserHP() - 15);
			this.description = this.userID + " lost 15 HP after being attacked by a crocodile.";
			userFile.writeLog(this.description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
		}
	}

	public void level4() {
		int minRange = 1;
		int maxRange = 20;
		int targetNumber = rand.nextInt(maxRange - minRange + 1) + minRange;
		int maxAttempts = 5;
		int attempts = 0;
		boolean hasGuessedCorrectly = false;
		int userGuess = 0;

		System.out.println("You enter a dark cave and hear growling sounds.");
		System.out.println("Suddenly, a menacing number-eating monster appears!");
		System.out.println("The monster is thinking of a number between " + minRange + " and " + maxRange);
		System.out.println("You have " + maxAttempts + " chances to guess the number or become the monster's snack.");

		while (attempts < maxAttempts) {
			System.out.print("Guess the number: ");

			try {
				userGuess = Integer.parseInt(input.nextLine());
				attempts++;

				if (userGuess < minRange || userGuess > maxRange) {
					System.out.println("Please guess a number between " + minRange + " and " + maxRange + ".");
					continue;
				}

				if (userGuess < targetNumber) {
					System.out.println("The monster growls: 'Too low!'");
				} else if (userGuess > targetNumber) {
					System.out.println("The monster hisses: 'Too high!'");
				} else {
					System.out.println("The monster roars: 'You guessed correctly!'");
					System.out.println("You've defeated the monster and escaped the cave victoriously.");
					System.out.println(super.getUserID() + " earned 200 coins.");
					super.setUserMoney(super.getUserMoney() + 200);
					this.description = this.userID + " defeated the monster and earned 200 coins.";
					userFile.writeLog(this.description);
					userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());

					hasGuessedCorrectly = true;
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}

		if (!hasGuessedCorrectly) {
			System.out.println("Out of attempts! The monster's number was: " + targetNumber);
			System.out.println("The monster started attacking you.");
			System.out.println(super.getUserID() + " lost 150 HP.");
			super.setUserHP(super.getUserHP() - 150);
			this.description = "The monster attacked " + this.userID + " and " + this.userID + " lost 150 HP";
			userFile.writeLog(description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
		}

	}

	public void level5() {

		System.out.println("Walk 2 steps backwards.");
		super.setUserSteps(userSteps - 2);
		level3();
		this.description = this.userID + " walked 2 steps backwards.";
		userFile.writeLog(this.description);
		userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());//
	}

	public void level6() {

		System.out.println(super.getUserID() + " encountered a group of wolves in the forest.");
		System.out.println(super.getUserID() + " need to solve 3 questions to run away from them!");

		String[] userAnswer = new String[3];
		String[] Answers = { "honey", "pacific", "hawaii" };
		int correctAnswers = 0;

		System.out.println("What is the only editable food that never goes bad? ");
		System.out.println("Your answer: ");

		userAnswer[0] = input.nextLine().toLowerCase();

		System.out.println("What is the World's largest Ocean?");
		System.out.println("Your answer: ");
		userAnswer[1] = input.nextLine().toLowerCase();

		System.out.println("Which U.S state is the only state to grow its own coffee beans? ");
		System.out.println("Your answer: ");
		userAnswer[2] = input.nextLine().toLowerCase();

		for (int i = 0; i < userAnswer.length; i++) {

			if (userAnswer[i].equals(Answers[i])) {

				/*
				 * 
				 * 
				 * == is used mostly when we are comparing integers, doubles or other numbers
				 * such as bytes. equals() is used when comparing strings. So always remember.
				 * equals() for strings only. == for rest of the things!
				 * 
				 */

				correctAnswers++;

			}

		}

		System.out.println("You got " + correctAnswers + " answers correct!");

		if (correctAnswers == 3) {

			System.out.println("A group of wolves disappeared.");
			System.out.println(super.getUserID() + " earned 150 coins.");
			super.setUserMoney(super.getUserMoney() + 150);
			this.description = this.userID + " defeated a group of wolves and earned 150 coins.";
			userFile.writeLog(this.description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());//

		} else {

			System.out.println("A group of wolves attacked " + super.getUserID());
			System.out.println(super.getUserID() + " lost 100 HP.");
			super.setUserHP(super.getUserHP() - 100);
			this.description = this.userID + " lost 100 HP after being attacked by a group of wolves.";
			userFile.writeLog(this.description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());//

		}

	}

	public void level7() {

		String userAnswer;
		int chances = 3;

		System.out.println("A giant spider appeared.");
		System.out.println(super.getUserID() + " needs to solve one question to run away from it!");
		System.out.println(super.getUserID() + " has 3 chances.");
		System.out.println();
		System.out.println("Which country has the greatest number of natural lakes? ");
		System.out.println("If you need a hint, type hint.");
		System.out.println("Your answer: ");

		for (int i = 0; i <= chances; i--) {

			userAnswer = input.nextLine().toLowerCase();
			chances--;

			if (userAnswer.contains("hint")) {

				System.out.println("It is the second biggest country in the world.");
				System.out.println("Your answer: ");
				chances++;
			}

			else if (userAnswer.contains("canada")) {

				System.out.println("The giant spider disappeared");
				System.out.println(super.getUserID() + " earned 100 coins.");
				super.setUserMoney(super.getUserMoney() + 100);
				this.description = this.userID + " defeated a giant spider and earned 100 coins.";
				userFile.writeLog(this.description);
				userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());//
				break;

			}

			else if (chances == 0) {
				System.out.println();
				System.out.println(super.getUserID() + " failed to answer the question correctly.");
				System.out.println("The giant spider started attacking " + super.getUserID());
				System.out.println(super.getUserID() + " lost 100 HP.");
				super.setUserHP(super.getUserHP() - 100);
				this.description = this.userID + " lost 100 HP after being attacked by a giant spider.";
				userFile.writeLog(this.description);
				System.out.println();
				userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());//
				break;
			}

			else {

				System.out.println("Wrong. Try again!");
				System.out.println("Your answer: ");

			}
		}

	}

	public void level8() {

		int randomNumber = rand.nextInt(3);
		String computerMove = "none";
		String playerMove = "none";
		boolean playAgain = true;
		boolean tryAgain = true;

		System.out.println("An ork appeared!");
		System.out.println("Ork: You came to my territory!");
		System.out.println("Ork: YOu cannot leave unless you win rock, scissor, paper game!!");
		System.out.println("****************************************");

		System.out.println("Please choose (Rock/Scissor/Paper) ");

		System.out.println("****************************************");
		System.out.print("Your answer: ");

		while (playAgain) {

			playerMove = input.nextLine().toLowerCase();

			while (tryAgain) {
				if (!playerMove.equals("rock") && !playerMove.equals("scissor") && !playerMove.equals("paper")) {

					System.out.println("Please choose (Rock/Scissor/Paper) ");
					System.out.println("Your answer: ");
					playerMove = input.nextLine().toLowerCase();

				} else {
					break;

				}
			}

			if (randomNumber == 0) {

				computerMove = "rock";

			} else if (randomNumber == 1) {

				computerMove = "scissor";
			} else if (randomNumber == 2) {

				computerMove = "paper";
			}

			System.out.println("The computer chose " + computerMove + "!");

			if (playerWins(playerMove, computerMove)) {

				System.out.println(super.getUserID() + " wins!");
				System.out.println(super.getUserID() + " earned 100 coins!");
				System.out.println();
				super.setUserMoney(super.getUserMoney() + 100);
				userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());//
				playAgain = false;

			} else if (playerMove.equals(computerMove)) {

				System.out.println("It is a draw!");
				System.out.println("Play the game again!");
				System.out.println();
				System.out.println("Please choose (Rock/Scissor/Paper) ");

				System.out.println("Your answer: ");

			} else {

				System.out.println("The ork wins!");
				System.out.println("The ork attacked " + super.getUserID() + "!");
				System.out.println(super.getUserID() + " lost 100 HP");
				super.setUserHP(super.getUserHP() - 100);
				System.out.println();
				userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());//
				playAgain = false;
			}

		}

	}

	public void level9() {
		System.out.println(super.getUserID() + " is now at the secret fishing spot!");
		System.out.println("You have 20 seconds to catch 3 rare fish as possible.");
		System.out.println("Be careful, some rare fish are hard to catch!");

		int fishCaught = 0;
		int timeRemaining = 20;
		Random random = new Random();

		long startTime = System.currentTimeMillis();
		long currentTime = startTime;

		while (timeRemaining > 0) {
			currentTime = System.currentTimeMillis();
			timeRemaining = (int) (20 - (currentTime - startTime) / 1000);

			if (timeRemaining <= 0) {

				timeRemaining = 0;
				System.out.println("Time's up! You caught " + fishCaught + " rare fish.");
				break;

			}
			System.out.println("Time remaining: " + timeRemaining + " seconds");

			System.out.println("Press 'f' to cast your line and 'q' to quit: ");

			String input = this.input.nextLine();

			if (input.equalsIgnoreCase("f")) {
				int catchChance = random.nextInt(100);
				if (catchChance < 30) {
					fishCaught++;
					System.out.println("You caught a rare fish! Total rare fish caught: " + fishCaught);
				} else {
					System.out.println("You missed a rare fish!");
				}
			} else if (input.equalsIgnoreCase("q")) {
				break;
			} else if (!input.equalsIgnoreCase("f")) {
				System.out.println("Invalid input. Please type 'f' to perform the action.");
			}

		}

		if (fishCaught >= 3) {
			System.out.println("Congratulations! You are a skilled angler!");
			super.setUserMoney(super.getUserMoney() + 200);
			this.description = userID + " earned 200 coins";
			userFile.writeLog(description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
		} else {
			System.out.println("Nice try! Keep practicing your fishing skills." + this.userID + " lost 40 HP points");
			this.description = userID + " lost 40 HP";
			super.setUserHP(super.getUserHP() - 15);
			userFile.writeLog(description);
			userFile.updateUserData(userID, super.getUserHP(), super.getUserMoney(), super.getUserSteps());
		}
	}

	public void warningMessage() {

		if (super.getUserMoney() == 0) {
			System.out.println();
			System.out.println("**Warning**");
			System.out.println("Warning: " + this.userID + " has insufficient funds.");
			System.out.println();

		} else if (super.getUserMoney() == 200) {
			System.out.println();
			System.out.println("**Warning**");
			System.out.println("Warning: " + this.userID
					+ "'s funds are running low. Consider earning more money to avoid running out.");
			System.out.println();
		} else if (super.getUserHP() <= 300) {
			System.out.println();
			System.out.println("**Warning**");
			System.out.println("Warning: Your HP is low. Consider recharging your HP to avoid running out.");
			System.out.println();
		}
	}

	static boolean playerWins(String playerMove, String computerMove) {

		if (playerMove.equals("rock")) {

			return computerMove.equals("Scissors");

		} else if (playerMove.equals("Scissors")) {

			return computerMove.equals("paper");

		} else if (playerMove.equals("paper")) {
			return computerMove.equals("rock");
		} else {

			return computerMove.equals("paper");

		}

	}

}