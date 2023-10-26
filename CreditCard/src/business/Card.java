package business;
 
 

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;

/**
 *
 * @author 
 */
public class Card {
    
    private int acctno = 0;
    private double climit, cbal, avcr;
    private String errmsg, actionmsg;
    
    public Card(){ //constructor to create new account
        this.climit = 0;
        this.cbal = 0;
        this.acctno = 0;
        this.errmsg = "";
        this.actionmsg = "";
        
        //generate a random account no and check to see if it already exists.
        
        while(this.acctno == 0) {
            this.acctno = (int)(Math.random() * 1000000);
            try {
                BufferedReader in = new BufferedReader(new FileReader("cc" + this.acctno + ".txt"));
                //'bad' result : account number already exists.
                in.close();
                this.acctno = 0;
            }
            catch (IOException e){
                //'good' result : account no. does not exist
                this.climit = 1000;
                if(writeStatus()){
                    
                    actionmsg = "Account " + this.acctno + " opened.";
                    writeLog(this.actionmsg);
                    
                } else {
                    
                    this.acctno = 0;
                    
                }
                
            } // end of try/catch
            
        }//end of while
        
    }//end of constructor
    
    public Card(int a) {
        //overloaded constructor to receive account number and 'open' the account
        
    	this.acctno = a;
        this.errmsg = "";
        this.actionmsg = "";
    	
    	 try {
             BufferedReader in = new BufferedReader(new FileReader("cc" + this.acctno + ".txt"));
             //'bad' result : account number already exists.
             this.climit = Double.parseDouble(in.readLine()) ;
             this.cbal = Double.parseDouble(in.readLine()) ;
             in.close();
              
         }
         catch (FileNotFoundException e){
             //'good' result : account no. does not exist
             this.climit = 1000;
             if(writeStatus()){
                 
                 actionmsg = "Account " + this.acctno + " opened.";
                 writeLog(this.actionmsg);
                 
             }
             else {
                 
            	this.errmsg = "Couldn't find this account file";
                 
             }
         }  catch (IOException e) {
             // Handle other IOExceptions (e.g., read/write errors)
             this.errmsg = "Error accessing account file for Account " + this.acctno;
         }
 
    	
    }
    
    private boolean writeStatus(){
        boolean result = true;
        try{
            PrintWriter out = new PrintWriter(
             new FileWriter("cc" + this.acctno +".txt")); // do not append
            
            out.println(this.climit);
            out.println(this.cbal);
            out.close();
            
        }catch(IOException e){
             this.errmsg = "Error writing status of Account " + this.acctno;
             result = false;
            
        }
        return result;
        
    } //end of writeStatus
    
    private void writeLog (String desc){
        
        try{ 
            //'true' in FileWriter constructor = append mode 
            PrintWriter out = new PrintWriter(new FileWriter ("CCL" + this.acctno +".txt", true)) ;
            Calendar cal = Calendar.getInstance();
            DateFormat df = DateFormat.getInstance();
            String timestamp = df.format(cal.getTime());
            out.println(desc + "\t" + timestamp);
            out.close();
            
        }catch (IOException e){
            
            this.errmsg = "Failed to update log for Account " + this.acctno;
            
        }
        
    }// end of writeLog
    public int getAcctNo(){
        
        return this.acctno;
        
    }
    
    public double getCritLimit(){
        
        return this.climit;
        
    }
    
    
    public void setCritLimit(double climit){
        
        this.climit = climit ;
        writeStatus();
    }
    
    public double getCbal(){
        
        return this.cbal;
    }
    
    public void setCbal(double cbal){
        
          this.cbal = cbal;
          writeStatus();
    }
    
    
    
    public String getErrorMsg(){
        
        return this.errmsg;
    }
    
    public String getActionMsg(){
        
        return this.actionmsg;
        
    }
    
    public void setActionMsg(String msg){
        
        this.actionmsg = msg;
        writeLog(this.actionmsg);
    }
    
     
    public double getAvailCr(){
        
        return this.climit - this.cbal;
        
    }
    
	public void setAvailCr(){
        
        this.avcr = this.climit - this.cbal;
         
    }
    
    
}
