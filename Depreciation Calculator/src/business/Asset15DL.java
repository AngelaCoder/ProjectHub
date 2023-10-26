package business;

/** 
 *
 * @author Angela Kim
 */
public class Asset15DL extends AssetDL {
    public static final double DEPFACTOR = 1.5;
    public static final String DEPNAME = "1.5x Declining";
    
    public Asset15DL(double c, double s, int lf) {
        super(c,s,lf,DEPFACTOR);
    }
    public Asset15DL(String fl) {
        super(fl,DEPFACTOR);
    }
    @Override
    public String getDepName() {
        return DEPNAME;
    }
}
