package com.minigame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public class FileWriterUtil {

	String userName = "";
	String userID = "";
	String password = "";
	String errmsg = "";

	public FileWriterUtil(String userID) {

		this.userID = userID;

	}

	public void createFile(String userName, String userID, String password) {

		this.userName = userName;
		this.userID = userID;
		this.password = password;

		try {
			PrintWriter out = new PrintWriter(new FileWriter(userID + ".txt"));

			out.println("User Name: " + userName);
			out.println("User ID: " + userID);
			out.println("Password: " + password);
			out.close();
			System.out.println("User file created successfully.");

		} catch (Exception e) {

			System.out.println("An exception occurred: " + e.getMessage());
		}

	}

	public void writeLog(String description) {

		try {

			PrintWriter userLogAppender = new PrintWriter(new FileWriter(this.userID + ".txt", true));
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = now.format(formatter);
			userLogAppender.println(formattedDateTime + " \t" + description);
			userLogAppender.close();
		}

		catch (IOException e) {

			this.errmsg = "Failed to update log for " + userID;
			System.out.println(this.errmsg);

		}

	}

	public void updateUserData(String userID, int userHP, int userMoney, int userSteps) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(userID + ".txt"));
			StringBuilder updatedData = new StringBuilder();

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("User HP:")) {
					updatedData.append("User HP:" + userHP + "\n");
				} else if (line.startsWith("User Money:")) {
					updatedData.append("User Money:" + userMoney + "\n");
				} else if (line.startsWith("User Steps:")) {
					updatedData.append("User Steps:" + userSteps + "\n");
				} else {
					updatedData.append(line + "\n");
				}
			}
			reader.close();

			PrintWriter writer = new PrintWriter(new FileWriter(userID + ".txt"));
			writer.print(updatedData);
			writer.close();
		} catch (IOException e) {
			System.err.println("An error occurred while updating user data.");
			e.printStackTrace();
		}
	}

}
