
package Dao;
import Bean.BeanNivel;
import Data.clsBD;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class DaoMaster {
    clsBD oclsBD = new clsBD("bd_comedor", clsBD.MySQL, "root", "A123456$");
    public ArrayList<BeanNivel> getNiveles (){
        oclsBD.SentenciaSQL("select * from mae_nivel;");
        DefaultTableModel rows = oclsBD.getTabla();
        ArrayList<BeanNivel> oListBeanNivel = new ArrayList<>();
        for (int i = 0; i < rows.getRowCount(); i++) {
            BeanNivel oBeanNivel = new BeanNivel(
                    Integer.parseInt(rows.getValueAt(i, 0).toString()),
                    Integer.parseInt(rows.getValueAt(i, 1).toString()));
            oListBeanNivel.add(oBeanNivel);
        }
        return oListBeanNivel;
    }
}
