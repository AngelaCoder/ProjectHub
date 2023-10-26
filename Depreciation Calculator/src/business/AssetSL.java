
package business;

/**
 *
 * @author Angela Kim
 */
public class AssetSL extends Asset {
    public static final String DEPNAME = "Straight Line"; 
    private double[] begbal, endbal;
    private double anndep;
    private boolean built = false;
    public AssetSL() {
        super();
    }
    public AssetSL(double c
    		, double s, int lf) {
        super(c,s,lf);
        this.built = false;
        if (super.isValid()) {
            buildAsset();
        } 
    }
    public AssetSL(String fl) {
        super(fl);
        this.built = false;
        if (super.isValid()) {
            buildAsset();
        }
    }
    private void buildAsset() {
        double depSL;
        try {
            this.begbal = new double[super.getLife()];
            this.endbal = new double[super.getLife()];
            this.anndep = (super.getCost() - super.getSalvage()) / super.getLife();
            this.begbal[0] = super.getCost();
            for (int i = 0; i < super.getLife(); i++) {
                if (i>0) {
                    this.begbal[i] = this.endbal[i-1];
                }
                this.endbal[i] = this.begbal[i] - this.anndep;
            }
            this.built = true;
        }
        catch (Exception e) {
            this.built = false;
            super.setErrorMsg("SL Build Error: " + e.getMessage());
        }
    }
    @Override
    public String getDepName() {
        return DEPNAME;
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
        return this.anndep;
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
