
package Bean;

/**
 *
 * @author alex
 */
public class BeanNivel {
    private int nid_nivel;
    private int co_nivel;
    
    public BeanNivel() {
        super();
    }
    
    public BeanNivel(int nid_nivel, int co_nivel){
        this.nid_nivel = nid_nivel;
        this.co_nivel = co_nivel;
    }
    
    public int getNid_nivel() {
        return this.nid_nivel;
    }
    
    public void setNid_nivel(int nid_nivel) {
        this.nid_nivel = nid_nivel;
    }
    
    public int getCo_nivel(){
        return this.co_nivel;
    }
    
    public void setCo_nivel(int co_nivel) {
        this.co_nivel = co_nivel;
    }
}

