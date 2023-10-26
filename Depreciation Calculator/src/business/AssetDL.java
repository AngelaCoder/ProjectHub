
package business;

/**
 * 
 * @author Angela Kim
 */
abstract public class AssetDL extends Asset {
    //extends keyword makes Asset the superclass
    //and AssetDL is the subclass and inherits the methods of the superclass
    private double[] begbal, anndep, endbal;
    private boolean built;
    private double depfactor;
    public AssetDL() {
        super();
        this.built = false;
        this.depfactor = 0;
    }
    public AssetDL(double c, double s, int lf, double depf) {
        super(c,s,lf); //calls the constructor of the supperclass "Asset"
        this.depfactor = depf;
        this.built = false;
        if (super.isValid()) {
            buildAsset();
        }
    }
    public AssetDL(String fl, double depf) {
        super(fl);
        this.depfactor = depf;
        this.built = false;
        if (super.isValid()) {
            buildAsset();
        }
    }
    
    private void buildAsset() {
        double depSL, depDDLrate;
        try {
            this.begbal = new double[super.getLife()];
            this.anndep = new double[super.getLife()];
            this.endbal = new double[super.getLife()];
            double DDLWork = 0;
            depSL = (super.getCost() - super.getSalvage()) / super.getLife();
            depDDLrate = (1.0 / super.getLife()) * this.depfactor;
            this.begbal[0] = super.getCost();
            for (int i = 0; i < super.getLife(); i++) {
                if (i > 0) {
                    this.begbal[i] = this.endbal[i-1];
                }
                DDLWork = this.begbal[i] * depDDLrate;
                if (DDLWork < depSL) {
                    DDLWork = depSL;
                }
                if (this.begbal[i] - depSL < super.getSalvage()) {
                    DDLWork = this.begbal[i] - super.getSalvage();
                }
                if (this.begbal[i] == super.getSalvage()) {
                    DDLWork = 0;
                }
                this.anndep[i] = DDLWork;
                this.endbal[i] = this.begbal[i] - DDLWork;
            }
            this.built = true;
        }
        catch (Exception e) {
            this.built = false;
            super.setErrorMsg("DL Build error: " + e.getMessage());            
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
    public double getAnnDepPct(int yr) {
        if (!this.built) {
            if (super.isValid()) {
                buildAsset();
            }
            if (!this.built) {
                return -1;
            }
        }
        if (yr < 0 || yr > super.getLife()) {
            return -1;
        }
        return (this.anndep[yr-1] / super.getCost());
    }
    @Override
    abstract public String getDepName();
}
