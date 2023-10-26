
package business;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** 
 *
 * @author Angela Kim
 */
abstract public class Asset {
    private String errmsg;
    double cost, salvage;
    int life;
    boolean built;
    
    public Asset() {
        this.cost = 0;
        this.salvage = 0;
        this.life = 0;
        this.errmsg = "";
        this.built = false;
    }
    
    public Asset (double c, double s, int lf) {
        this.cost = c;
        this.salvage = s;
        this.life = lf;
        this.errmsg = "";
        this.built = false;
    }
    public Asset(String fl) {
        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(fl));
            this.cost = Double.parseDouble(in.readLine() );
            this.salvage = Double.parseDouble(in.readLine() );
            this.life = Integer.parseInt(in.readLine());
            in.close();
        } catch (IOException e) {
            //account not found
            this.errmsg = "File " + fl + " not found.  No Asset created.";
        } catch (NumberFormatException e) {
            this.errmsg = "File " + fl + 
                    " did not have required start values.";
        }
        this.built = false;
    }
    protected boolean isValid() {
        this.errmsg = "";
        if (this.cost <= 0) {
            this.errmsg += "Cost must be a positive value.";
        }
        if (this.salvage < 0)  {
            this.errmsg += "Salvage cannot be negative.";
        }
        if (this.salvage >= this.cost) {
            this.errmsg += "Salvage must be less than cost.";
        }
        //return true if all items pass validations...
        if (this.errmsg.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public String getErrorMsg() {
        return this.errmsg;
    }
    protected void setErrorMsg(String m) {
        this.errmsg = m ;
    }
    public double getCost() {
        return this.cost;
    }
    public void setCost(double c) {
        this.cost = c;
    }
    public double getSalvage() {
        return this.salvage;
    }
    public void setSalvage(double s){
        this.salvage = s;
    }
    public int getLife() {
        return this.life;
    }
    public void setLife(int lf) {
        this.life = lf;
    }
    abstract public double getBegBal(int yr);
    abstract public double getAnnDep(int yr);
    abstract public double getEndBal(int yr);
    abstract public String getDepName();
}


