package creditcard;

import business.Card;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author
 */
public class CreditCard {

	static Scanner sc = new Scanner(System.in);
	static Card cc = null;

	public static void main(String[] args) throws IOException  {
		int action = 0; // menu selection
		double amt; // transaction amount
		String cdesc; // charge desc

		do {
			if (cc != null) {
				DisplayValues();
			}

			action = getAction();
			if (action != 1 && action != 2 && cc == null) {
				System.out.println("Action requested with no open account.");

			} else if (action != 0) {
				// cc is either active or action is 'new' or existing'
				switch (action) {
				case 1: // new account
					cc = new Card(); // generates new account iwth #
					if (!cc.getErrorMsg().isEmpty()) {
						System.out.println("\n " + cc.getErrorMsg());
						cc = null;

					}
					break;

				case 2:

				    int acctno = getFile();
				    if (acctno == -1) {
				        System.out.println("Action requested with no open account.");
				    } else if (acctno > 0) {
				        cc = new Card(acctno);
				    }
				    break;
				case 3:

						if(cc != null) {
							
							System.out.println("Enter the charge amount: ");
							double chargeAmount = Double.parseDouble(sc.nextLine());
							System.out.println("Enter Charge Description: ");
							String chargeDescription = sc.nextLine();
							
							chargeOnAccount(cc,chargeAmount,chargeDescription );
						}else {
							
							 System.out.println("Please open an account before making a charge.");
							
						}
						break;
					

				
				case 4: 
					
					if(cc != null) {
					
					 System.out.println("Enter the amount you want to pay: $");
					 double paymentAmount = Double.parseDouble(sc.nextLine());
					 makePayment(cc, paymentAmount);
					 
				}else {
					
					 System.out.println("Please open an account before making a charge.");
						
				}
				break;
			
					
				 
				case 5:
					
					if(cc != null) {
						
						System.out.println("Requested credit increase: ");
						double creditIncrease = Double.parseDouble(sc.nextLine());
						creditIncrease(cc,creditIncrease);
						
						
					}else {
						
						 System.out.println("Please open an account before making a charge.");
							
					}
					break;
				case 6:
					
					if(cc != null) {
						
						System.out.println("Please enter a valid annual interest rate.");
						double annualInterestRate = Double.parseDouble(sc.nextLine());
						postInterestCharge(cc,annualInterestRate);
						
					}else {
						
						 System.out.println("Please open an account before making a charge.");
							
					}
					
					
					break;
				case 7: //getAccountHistory(cc);
					
					if(cc != null) {
						
						ArrayList<String> history = getAccountHistory(cc); 
						
				      if(history.size()>0) {
				    	  
				    	  System.out.println("Transaction History for Account: " + cc.getAcctNo());
				            history.forEach(System.out::println);
				    	  
				      }
				      else {
				            System.out.println("No transaction history found for Account: " + cc.getAcctNo());
				        }
						
						
					}else {
						
						 System.out.println("Please open an account before displaying the transaction history.");
					}
					break;
					
				default:

					System.out.println("Action " + action + " not legal or not implemented.");
					break;

				} // end of switch
				if (cc != null) {
					if (!cc.getErrorMsg().isEmpty()) {

						System.out.println("\n" + cc.getErrorMsg());
						cc = null;

					} else {
						// System.out.println("\n" + cc.getActionMsg());

					}
				}
				  System.out.println();
			}

		} while (action != 0);// end of main

	}


	public static int getFile() {
		// we need an array to store a list of all the files.
		String[] files;
		// Step 1) Create an ArrayList<Integer> for holding the account numbers.
		ArrayList<Integer> accountNumbers = new ArrayList<>();
		// We need to create a File object, that points to our project directory.
		File f = new File(".");
		// and then we get a list of all the files in our project directory.
		files = f.list();
 
		boolean existingAccounts = false;
		
		 
		// How do we loop through the files and print them out?
		int index = 1; // <-- anything inside a loop will be done over and over.
						// since we only want to do this once, it should be done outside the loop.
		for (int i = 0; i < files.length; i++) {
			// Example account file:
			// indexes: 01234567890123
			// "CCL210568 .txt"
			if (files[i].startsWith("CC") && files[i].endsWith(".txt")) {
				// Step 2) Add our account number (converted to an integer) to our arraylist.
				
	            if (!existingAccounts) {
	                System.out.println("Available Account Numbers: ");
	                existingAccounts = true;
	            }
				System.out.println(index + ". " + files[i].substring(0, 9));
				accountNumbers.add(Integer.parseInt(files[i].substring(3, 9)));
				index++;//
				existingAccounts = true;
			}
		}
      
		 if (!existingAccounts) {
	        System.out.println("No existing accounts.");
	        return -1;   // No existing accounts
	    }
		
		
         int userChoice = 0;
   		 boolean validChoice = false;
      
	 	 do {
        try {
            System.out.println("Please choose an account number: ");
            userChoice = Integer.parseInt(sc.nextLine());
            		 
            int chosenAccountNumber = accountNumbers.get(userChoice - 1);
            System.out.println("Account " + chosenAccountNumber + " re-opened.");
            validChoice = true;

            // Assuming you want to return the chosen account number
            return chosenAccountNumber;

        } catch (Exception e) {
            System.out.println("The selected option is not available.");
            System.out.println("Please choose the right account number.");
        }
   	 } while (!validChoice);
		
		// DisplayValues();
		return -1; // <-- since getFile() returns an int, we're forced to return something.
		
		/*
		 * so if technically we could return 0, 
		 * but like i said 0 is a very common number in coding for being useful 
		 * such as index 0 in an array or when we use a compare method it will return 0 
		 * but -1 is not used in your code for users
		 * So you can use -1 all over to signify an error
		 * 0 is more like meh, there might be an error
		 * but in your application -1 means something went wrong!
		 */
		
	}

	public static void chargeOnAccount(Card cc, double chargeAmount,  String chargeDescription) {

		String msg = "Charge denied.";
		double currentBalance = cc.getCbal();
		double totalBal;
		
		if(chargeAmount > 0) {
		if(currentBalance + chargeAmount <= cc.getCritLimit()) {
			
			totalBal = currentBalance + chargeAmount;
			cc.setCbal(totalBal);
			
			System.out.println("Charge of $" + chargeAmount + " for " + chargeDescription + " posted.");
	            msg = "Charge of $" + chargeAmount + " for " + chargeDescription + " posted.";
			
		} else {
			
			  System.out.println("Charge declined: The charge amount exceeds the available credit.");
	            msg = "Charge of $" + chargeAmount + " for " + chargeDescription + " declined. It exceeds the account holder's available credit.";
			
		}
		}
		
		else {
	        System.out.println("Charge denied: Please enter a valid positive charge amount.");
	        msg = "Charge denied.";
			
		}
	 
		cc.setActionMsg(msg);

	}
	
	public static void makePayment(Card cc, double paymentAmount) {
  
		 double currentBalance ;
		 double availableCredit = cc.getAvailCr();
		 String msg = "Payment denied";; 
		 
		 if(paymentAmount > 0) {
			 
			 currentBalance = cc.getCbal() - paymentAmount;
			 cc.setCbal(currentBalance);
			 availableCredit = cc.getAvailCr() + paymentAmount;
			 System.out.println("Payment of $"+paymentAmount+" processed successfully.");
			  
			 msg = "Payment of $"+paymentAmount+" processed successfully.";
		 
			 if(currentBalance < 0) {
				 
				  System.out.println("Warning: Your payment of $"+ paymentAmount+" has resulted in a negative balance.");
				  msg = "$"+ paymentAmount+" processed successfully. It has resulted in a negative balance.";
				 
			 }
			 
		 }else {
			 
			 System.out.println("Payment denied: Please enter a valid positive payment amount to pay off your current balance.");
			 msg ="Payment denied";
			 
		 }
		
		  cc.setActionMsg(msg);
	
	
	}
	

	public static void creditIncrease(Card cc,double creditIncrease) {

		String msg = "Credit increase request declined.";
		int randomNumber = 1+(int)(Math.random()*2);
		
	    if (creditIncrease <= 0) {
	        System.out.println("Credit Increase request must be a positive amount and in multiples of $100.");
	        msg = "Credit increase request declined.";
	    }
		
		if (!(creditIncrease % 100.0 == 0)) {

			System.out.println("Credit Increase of " + creditIncrease + " declined – not a multiple of 100.\n");

			 msg = "Credit Increase of " + creditIncrease +" declined – not a multiple of 100.";

			 

		}
		
		  else if(randomNumber == 2) {
		 
		  System.out.println("Credit Increase of "+ creditIncrease +" declined - you do not qualify at this time.\n");
		 
		  msg = "Credit Increase of "+ creditIncrease +" declined - An accountholder does not qualify at this time.";
		  
		   
		  }
		
	
		else {

			double totalCritLimit = cc.getCritLimit() + creditIncrease;
			cc.setCritLimit(totalCritLimit);
			System.out.println("Credit Limit increased to " + cc.getCritLimit());
			System.out.println();

			msg = "Credit Limit increased " + creditIncrease + " to " + cc.getCritLimit();
			 

		}

		cc.setActionMsg(msg);

	}
	
	public static void postInterestCharge(Card cc, double annualInterestRate ) {
		
		String msg = "Interest charge declined.";
 
		
		if(annualInterestRate >=0 ) {
			
			double monthlyInterestRate = annualInterestRate / 12 ;
			double interestCharge = cc.getCbal() * monthlyInterestRate;
			double newBalance = cc.getCbal() + interestCharge;
		    cc.setCbal(newBalance);
		   // System.out.println("Interest charge of $" + interestCharge + " has been posted.");
		    System.out.printf("Interest charge of $%.2f has been posted.\n", interestCharge);
		    msg = String.format("Interest charge of $%.2f has been posted.", interestCharge);
 
			
		}else {
			
			System.out.println("Please enter a valid annual interest rate.");
			msg = "Interest charge declined.";
		} 
		
	    cc.setActionMsg(msg); 
 
		
		
	}
	


	public static int getAction() {
		int a = -1; //it doesnt want that specfically, but we need to intialize it with an invalid value
		//-1 usually is safer, because arrays or indexes always start at 0
		// must be 'robust' on final version : validate 0-7, error trap
		System.out.println("Card Action: ");
		System.out.println("1. Open New Account");
		System.out.println("2. Re-Open existing Account");
		System.out.println("3. Make charge on account");
		System.out.println("4. Make payment on account");
		System.out.println("5. Request credit increase");
		System.out.println("6. Charge interest to account");
		System.out.println("7. Display Log History");
		System.out.println("Enter selection (0 to quit): ");

      	try{
		a = Integer.parseInt(sc.nextLine());
        }
      
      	catch (NumberFormatException e){
          
          System.out.println("Choose the right option.");
        }
      	return a;
	}
	

		
		public static ArrayList<String> getAccountHistory(Card cc) throws IOException {
		    ArrayList<String> AccountHistory = new ArrayList<>();

		    try {
		        File f = new File("CCL" + cc.getAcctNo() + ".txt");
		        FileReader fr = new FileReader(f);
		        BufferedReader br = new BufferedReader(fr);
		        String line;
		        
		        while ((line = br.readLine()) != null) {
		            AccountHistory.add(line);
		        }

		        br.close();
		        fr.close();
		    } catch (FileNotFoundException e) {
		        System.out.println("File not found");
		    }

		    return AccountHistory;
		}
	
	 

	public static void DisplayValues() {

		NumberFormat curr = NumberFormat.getCurrencyInstance();
		System.out.println("Status for Account: " + cc.getAcctNo() + ": ");
		System.out.printf(" Credit Limit: %12s \n", curr.format(cc.getCritLimit()));
		System.out.printf(" Current Bal: %12s \n", curr.format(cc.getCbal()));
		System.out.printf(" Avail. Cr: %12s \n", curr.format(cc.getAvailCr()));
		System.out.println();

	}

}
