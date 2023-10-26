/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

/**
 *
 * @author Angela Kim
 */ 


public class AssetSYD extends Asset {

	public static final String DEPNAME = "Sum of Year’s Digits";
	
	private double[] begbal, endbal,anndep;
	 
	private boolean built = false;
	
 

    public AssetSYD(double c
    		, double s, int lf) {
        super(c,s,lf);
        this.built = false;
        if (super.isValid()) {
            buildAsset();
        }
    }
    public AssetSYD(String fl) {
        super(fl);
        this.built = false;
        if (super.isValid()) {
            buildAsset();
        }
    }
	
    
    @Override 
    public String getDepName() {
    	
    	return DEPNAME;
    	
    }
 
    private void buildAsset() {
        try {
            double sumOfYearsDigits = (getLife() * (getLife() + 1)) / 2;
            this.begbal = new double[super.getLife()];
            this.endbal = new double[super.getLife()];
            this.anndep = new double[super.getLife()];
            this.begbal[0] = super.getCost();

            for (int i = 0; i < super.getLife(); i++) {
                if (i > 0) {
                    this.begbal[i] = this.endbal[i - 1];
                }

                double depreciationRate = (super.getLife() - i) / sumOfYearsDigits;
                double annualDepreciation = depreciationRate * (super.getCost() - super.getSalvage());

                this.anndep[i] = annualDepreciation;
                this.endbal[i] = this.begbal[i] - this.anndep[i];
            }

            this.built = true;
        } catch (Exception e) {
            this.built = false;
            super.setErrorMsg("SYD Build Error: " + e.getMessage());
        }
    }
    
    
    @Override
    public double getBegBal(int yr) {
        if (!this.built) {
            if (super.isValid()) {
                buildAsset();
            }
            if (!this.built) {
                return -1;
            }
        }
        if (yr < 1 || yr > super.getLife()) {
            return -1;
        }
        return this.begbal[yr-1];
    
    }
 
    @Override
    public double getAnnDep(int yr) { //get AnnDep for SL
        if (!this.built) {
            if (super.isValid()) {
                buildAsset();
            }
            if (!this.built) {
                return -1;
            }
        }
        if (yr < 1 || yr > super.getLife()) {
            return -1;
        }
        return this.anndep[yr-1];
    }
    @Override
    public double getEndBal(int yr) {
        if (!this.built) {
            if (super.isValid()) {
                buildAsset();
            }
            if (!this.built) {
                return -1;
            }
        }
        if (yr < 1 || yr > super.getLife()) {
            return -1;
        }
        return this.endbal[yr-1];
    
    }
 
    
    
	
}
