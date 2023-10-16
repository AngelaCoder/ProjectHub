package com.minigame;

public class Information {

	public String userID;
	public String userName;
	public int userLevel;
	public int userMoney;
	public int userHP;
	public int userSteps;
	public FileWriterUtil userFile;
	public String description;
	public final int finishedSteps = 40;

	public Information() {

		userID = "Unknown";
		userName = "Unknown";
		userMoney = 500;
		userHP = 1000;
		userSteps = 0;
		description = "";

	}

	public Information(FileWriterUtil userFile) {
		this(); // Call the default constructor to initialize other attributes.
		this.userFile = userFile;
	}

	public Information(String userID, int userMoney, int userSteps) {

		setUserID(userID);
		setUserMoney(userMoney);

		setUserSteps(userSteps);

	}

	public String getUserName() {

		return this.userName;

	}

	public void setUserName(String userName) {

		this.userName = userName;

	}

	public String getUserID() {

		return this.userID;

	}

	public void setUserID(String userName) {
		this.userID = userName;
	}

	public int getUserLevel() {

		return this.userLevel;

	}

	public void setUserLevel(int userLevel) {

		this.userLevel = userLevel;

	}

	public int getUserMoney() {

		return this.userMoney;

	}

	public void setUserMoney(int userMoney) {

		this.userMoney = userMoney;

		if (this.userMoney < 0) {

			this.userMoney = 0;
			userFile = new FileWriterUtil(getUserID());
			System.out.println(this.userID + " has run out of money. Game over.");
			userFile.updateUserData(userID, getUserHP(), getUserMoney(), getUserSteps());
			this.description = this.userID + " has run out of money. Game over.";
			userFile.writeLog(description);
			System.exit(0);

		}

	}

	public int getUserHP() {

		return userHP;

	}

	public void setUserHP(int userHP) {

		this.userHP = userHP;

		if (this.userHP <= 0) {
			this.userHP = 0;
			System.out.println(this.userID + " has been defeated." + this.userID + "'s HP has reached 0. Game over!");
			userFile = new FileWriterUtil(getUserID());
			userFile.updateUserData(userID, 0, getUserMoney(), getUserSteps());
			description = this.userID + " has been defeated." + this.userID + "'s HP has reached 0. Game over!";
			userFile.writeLog(description);

			System.exit(0);
		}

	}

	public int getUserSteps() {

		return userSteps;

	}

	public void setUserSteps(int userSteps) {
		this.userSteps = userSteps;

	}
}
