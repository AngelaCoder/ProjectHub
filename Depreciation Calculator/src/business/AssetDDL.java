
package business;

/**
 *
 * @author Angela Kim
 */ 
public class AssetDDL extends AssetDL{

    public static final String DEPNAME = "Double Declining";
       
    p

        super(c,s,lf,DEPFACTOR);
    } 
    public AssetDDL(String fl) {
        super(fl,DEPFACTOR);
    }
    @Override
    public String getDepName() {
        return DEPNAME;
    }
}
