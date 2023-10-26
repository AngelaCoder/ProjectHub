
package depinherit;

import business.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Angela Kim
 */
public class Depreciation {

    static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        String choice, filenm;
        double cost = 0, salv = 0;
        int life = 0;
        Asset a = null;
        NumberFormat curr = NumberFormat.getCurrencyInstance();
        NumberFormat pct = NumberFormat.getPercentInstance();
        pct.setMaximumFractionDigits(3);
        pct.setMinimumFractionDigits(3);
        System.out.println("Welcome to the Depreciation Calculator");
        
        System.out.print("Asset by <i>nput, <f>ile, or <q>uit: ");
        choice = sc.nextLine();
        while (!choice.isEmpty() && !choice.toUpperCase().startsWith("Q")) {
            if (choice.toUpperCase().startsWith("F")) {
                filenm = getFile();
                a = new AssetSL(filenm);
                if (a.getErrorMsg().isEmpty()) {
                    cost = a.getCost();
                    salv = a.getSalvage();
                    life = a.getLife();
                    System.out.println("Asset Values for " + filenm +": ");
                    System.out.println("Cost: " + curr.format(a.getCost()));
                    System.out.println("Salvage: " + 
                            curr.format(a.getSalvage()));
                    System.out.println("Life: " + a.getLife());
                }
            } else {
                cost = getDoubleValue("Cost: ");
                salv = getDoubleValue("Salvage: " );
                life = getIntValue("Life (years): ");
            }
            for (int i=1; i<=3; i++) {
                switch (i) {
                    case 1:
                        a = new AssetSL(cost,salv,life);
                        break ;
                    case 2:
                        a = new AssetDDL(cost,salv,life);
                        break;
                    case 3:
                        a = new Asset15DL(cost,salv,life);
                        break;

                }
                if (!a.getErrorMsg().isEmpty()) {
                    System.out.println(a.getDepName() + " Error: " + a.getErrorMsg());
                } else {
                    System.out.println("The " + a.getDepName() + " first year depreciation is: " +
                                           curr.format(a.getAnnDep(1)));
                }
            }
            do {
                System.out.print("Schedule <S>L, <D>DL, <15>DL, <SY>D, <N>one (S/D/15/SY/N): ");
                choice = sc.nextLine();
                if (!choice.toUpperCase().startsWith("N")&&!choice.toUpperCase().startsWith("SY")) { 
                   if (choice.toUpperCase().startsWith("S")) {
                       a = new AssetSL(cost,salv,life);
                   } else if (choice.toUpperCase().startsWith("D")) {
                       a = new AssetDDL(cost,salv,life);
                   } else if (choice.toUpperCase().startsWith("15")) {
                       a = new Asset15DL(cost,salv,life);
                   } 
                   System.out.println(
                            "\n         " + a.getDepName() + " Depreciation");
                   System.out.print(
                            "Year      Beg.Bal.    Ann.Dep.    End.Bal.        ");
                   if (a instanceof AssetSL) {
                       System.out.println();
                   } else {
                       System.out.println("Dep. Pct.");
                   }
                   for(int yr=1; yr <= a.getLife(); yr++) {
                       System.out.printf("%3d  %12s %12s %12s  ", 
                                          yr,
                                          curr.format(a.getBegBal(yr)),
                                          curr.format(a.getAnnDep(yr)),
                                          curr.format(a.getEndBal(yr)));
                       if (a instanceof AssetSL) {
                           System.out.println();
                       } else if (a instanceof AssetDDL) {
                           //downcast the asset 'a' to underlying subclass
                           AssetDDL ddl = (AssetDDL)a;
                           System.out.printf("%12s \n", pct.format( ddl.getAnnDepPct(yr)) );
                       } else if (a instanceof Asset15DL) {
                           System.out.printf("%12s \n", pct.format( ((Asset15DL)a).getAnnDepPct(yr)) );
                       } else if (a instanceof AssetSYD) {
                    	   
                    	   AssetSYD syd = (AssetSYD)a;
                    	   System.out.printf("%12s \n", pct.format( syd.getAnnDep(yr)) );
                       }
                   }//end of for
                }
                else if(!choice.toUpperCase().startsWith("N")) {
                	if(choice.toUpperCase().startsWith("SY")) {
                		
                		a = new AssetSYD(cost,salv,life);
                	}
                	double formattedPercentage = 0; ///
                			
                	
                	  System.out.println(
                              "\n         " + a.getDepName() );
                     System.out.print(
                              "Year      life remaining.    SYD.    Depreciation Rate.        Annual Depreciation");
                     if (a instanceof AssetSYD) {
                         System.out.println();
                     } else {
                         System.out.println("Dep. Pct.");
                     }
                     
                     int i = 0;
                     int lifeRemaining = 0;
                     double totalDepreciationRate = 0;
                     double totalAnnualDepreciation = 0;
                     int sumOfYearsDigits = (a.getLife() * (a.getLife() + 1)) / 2;
                     for(int yr=1; yr <= a.getLife(); yr++) {
                    	 
                    	 formattedPercentage = (a.getLife() - i) / (double) sumOfYearsDigits * 100;

                    	 String formattedPercentageStr = (a instanceof AssetSYD) ? String.format("%.2f%%", formattedPercentage) : String.format("%12s", "");

                    	 System.out.printf("%3d  %13d %14s %12s %30s ", 
                                            yr,
                                            a.getLife()-i,
                                            String.valueOf(a.getLife()-i + "/" + sumOfYearsDigits),
                                            formattedPercentageStr,          
                                            curr.format(a.getAnnDep(yr)));
                    	 i++;
                    	    System.out.println();
                    	    
                    	    lifeRemaining += (a.getLife() - i);
                    	    totalDepreciationRate += formattedPercentage;
                    	    totalAnnualDepreciation += a.getAnnDep(yr);
                    	}
                     
                     
                     System.out.printf("%3s  %11d %14s %10.2f%% %30s \n",
                    	        "Totals",
                    	        sumOfYearsDigits,
                    	        "    ",
                    	        totalDepreciationRate,
                    	        curr.format(totalAnnualDepreciation));	
                    		 	 
   
                }
           } while (!choice.toUpperCase().startsWith("N"));                   
           System.out.print("Asset by <i>nput, <f>ile, or <q>uit: ");
           choice = sc.nextLine();
            }//end of outer while
        
        System.out.println("Thanks for using the Depreciation Calculator");
    } //end of main
    
    public static String getFile() {
        String fl="";
        String[] files;
        ArrayList<String> astfiles = new ArrayList<>();
        
        try {
            System.out.println("\nAsset Files in Default Directory: ");
            File f = new File(".");
            files = f.list();
        
            int i = 0;
            for (String pathname : files) {
                if (pathname.toLowerCase().endsWith(".ast")) {
                    i++;
                    System.out.println(i + ": " + pathname);
                    astfiles.add(pathname);
                }
            }
            if (i == 0) {
                System.out.println("No Files found.");
                return "";
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "";
        }
        
        int flno = 0;
        do {
            try {
                System.out.print("File #: ");
                flno = sc.nextInt();
                if (flno < 1 || flno > astfiles.size()) {
                    System.out.println("Selection is out of range. 1-" 
                            + astfiles.size() + " only.");
                }
            } catch (Exception e) {
                System.out.println("Please enter an integer between 1-" 
                        + astfiles.size() + ".");
                sc.nextLine();
                flno = 0;
            }
        } while (flno < 1 || flno > astfiles.size());
        sc.nextLine();
        return astfiles.get(flno-1);
    }
    public static double getDoubleValue(String prompt) {
        double val=0;
        boolean goodVal = false;
        do {
            try {
                System.out.print(prompt);
                val = sc.nextDouble();
                goodVal = true;
            } catch (Exception e) {
                System.out.println("Illegal input: must be numeric");
                sc.nextLine();
                val = 0;
            }
        } while (!goodVal);
        sc.nextLine();
        return val;
    }
    public static int getIntValue(String prompt) {
        int val=0;
        boolean goodVal = false;
        do {
            try {
                System.out.print(prompt);
                val = sc.nextInt();
                goodVal = true;
            } catch (Exception e) {
                System.out.println("Illegal input: must be numeric integer");
                sc.nextLine();
                val = 0;
            }
        } while (!goodVal);
        sc.nextLine();
        return val;
    }
}
