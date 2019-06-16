package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class clsBD {
    public final static int ODBC = 1;
    public final static int SQLServer = 2;
    public final static int MySQL = 3;
    public final static int Oracle = 4;
    
    String _BD, _Usuario="", _Contraseña="";
    int _TipoBD;
    PreparedStatement ps = null;

    public clsBD( String strBD, int TipoBD ) {
        this._BD = strBD;
        this._TipoBD = TipoBD;
    }

    public clsBD( String strBD, int TipoBD, String strUsuario, String strContraseña ) {
        this._BD = strBD;
        this._TipoBD = TipoBD;
        this._Usuario = strUsuario;
        this._Contraseña = strContraseña;
    }

    private Connection getConexion( ) {
        String strDriver="", strUrl="";
        Connection cn = null;
        
        switch ( _TipoBD ) {
            case 1 : strDriver = "sun.jdbc.odbc.JdbcOdbcDriver"; 
                     strUrl = "jdbc:odbc:" + _BD; break;
            case 2 : strDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                     strUrl = "jdbc:sqlserver://localhost; database=" + _BD + ";integratedSecurity=true"; break;
            case 3 : strDriver = "com.mysql.jdbc.Driver";
                     strUrl = "jdbc:mysql://localhost:3306/" + _BD + "?autoReconnect=true&useSSL=false"; break;
            case 4 : strDriver = "oracle.jdbc.OracleDriver";
                     strUrl = "jdbc:oracle:thin:@localhost:1521:orcl"; break;
        }
        try {
            Class.forName( strDriver );
            cn = DriverManager.getConnection( strUrl, _Usuario, _Contraseña );
        } catch ( ClassNotFoundException | SQLException ex ) {  System.out.println( ex.getMessage() ); }
        
        return cn;
    }
    
    public void SentenciaSQL( String strSQL ) {
        if ( strSQL.indexOf("usp") == 0 && _TipoBD >= MySQL ) {
            int posBlanco = strSQL.indexOf(" ");
            if ( posBlanco > -1 )
                strSQL = strSQL.substring(0, posBlanco) + "(" + strSQL.substring(posBlanco + 1) + ")";
            strSQL = ( _TipoBD == MySQL ? "call " : "execute " ) + strSQL + ";";
        }
        try {
            ps = getConexion().prepareStatement( strSQL );
        } catch ( SQLException ex ) { System.out.println( ex.getMessage() ); }
    }
    
    public void ParametrosSQL( String[] parametros ) {
        int i = 0;
        for ( String valor : parametros )
            try {
                ps.setString( ++i, valor );
            } catch ( SQLException ex ) { System.out.println( ex.getMessage() ); }
    }

    public void ParametrosSQL( String[] parametros, byte[] imagen ) {
        ParametrosSQL(parametros);
        try {
            ps.setBytes( parametros.length + 1, imagen );
        } catch ( SQLException ex ) { System.out.println( ex.getMessage() ); }
    }
    
    public int EjecutarSQL() {
        try {
            return ps.executeUpdate();
        } catch ( SQLException ex ) { System.out.println( ex.getMessage() ); }
        return -1;
    }
    
    public String[] getRegistro() {
        try {
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmt = rs.getMetaData();
            String aRegistro[] = new String[rsmt.getColumnCount()];
            while ( rs.next() ) 
              for (int i=0; i < aRegistro.length; i++ ) 
                 aRegistro[i] = rs.getString(i+1);
            rs.close();
            return aRegistro;
        } catch ( SQLException ex ) { System.out.println( ex.getMessage() ); }
        return null;
    }
    
    public DefaultTableModel getTabla() {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmt = rs.getMetaData();
            Object objRegistro[] = new Object[rsmt.getColumnCount()];
            for (int i=1; i <= rsmt.getColumnCount(); i++ ) modelo.addColumn(rsmt.getColumnName(i));
            while ( rs.next() ) {
             for (int i=0; i < objRegistro.length; i++ ) 
                 objRegistro[i] = rs.getString(i+1);
             modelo.addRow(objRegistro);
            }
            rs.close();
            return modelo;
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }
        
        return null;
    }

    public String getCampo () {
        try {
            ResultSet rs = ps.executeQuery();
            if ( rs.next() )
               return rs.getString(1);
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }
        return null;
    }

    
    public byte[] getImagen () {
        try {
            ResultSet  rs = ps.executeQuery();
            rs.next();
            return rs.getBytes(1);
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }
        return null;
    }
     
    public void getArraySQL( ArrayList aCodigos ) {
        getArraySQL( aCodigos,1 );
    }
     
    public void getArraySQL( ArrayList aCodigos, int columna ) {
        aCodigos.clear();
        try {
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) 
                aCodigos.add( rs.getString( columna ) );
            rs.close();
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }
    }

    public void getComboSQL( JComboBox cboCombo ) {
        cboCombo.removeAllItems();
        cboCombo.addItem("Seleccionar");
        try {
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) 
                cboCombo.addItem( rs.getString(2) );
            rs.close();
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }
    }
    
    public void getComboSQL( ArrayList aCodigos, JComboBox cboCombo ) {
        aCodigos.clear();
        cboCombo.removeAllItems();
        aCodigos.add("");
        cboCombo.addItem("Seleccionar");
        try {
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                aCodigos.add( rs.getString(1) );
                cboCombo.addItem( rs.getString(2) );
            }
            rs.close();
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }
    }

    public String[] getColumna() {
        return getColumna(1);
    }
    
    public String[] getColumna( int columna ) {
        try {
            ResultSet rs = ps.executeQuery();
            rs.last();
            if  ( rs.getRow() > 0 ) {
                String arreglo[] = new String[ rs.getRow() ];
                rs = ps.executeQuery();
                int fila = -1;
                while ( rs.next() ) 
                    arreglo[ ++fila ] = rs.getString( columna );
                return arreglo;    
            }
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }

        return null;
    }
    
    public String getComboHTML ( String strName, String strClass,  String strOnChange, String idSelected, boolean bolOnOff, String strSeleccionar ) {
        String strCombo = "<select name=" + strName + ( strClass.equals("") ? "" : " class=" + strClass )  + ( strOnChange.equals("") ? "" : " onChange=" + strOnChange ) + ( bolOnOff ? "" : " disabled" ) +  " >";
        strCombo += "<option value = 0 selected >" + strSeleccionar + "</option>";
        try {
            ResultSet rs = ps.executeQuery();
            String id;
            while ( rs.next() ) {
                id = rs.getString(1);
                strCombo += "<option value=" + id + ( id.equals(idSelected) ? " selected" : "" ) +  " >" + rs.getString(2).trim() + "</option>";
            }
            rs.close();
            return strCombo + "</select>";
        } catch (SQLException ex) { System.out.println( ex.getMessage() ); }
        return "";
    }
 
    public String getListView ( String strCarpetaImagenes, String url, int TotalFilas, int TotalColumnas ) {
        try {
            ResultSet rs = ps.executeQuery();
            int TotalColumnasTabla = rs.getMetaData().getColumnCount();
            String strTabla = "<table><tr>";
            String strCodigo = "";
            int fila=1, columna = 1;
            while ( rs.next() && fila <= TotalFilas ) {
                strCodigo = rs.getString(1);
                strTabla += "<td><table><tr><td><img src=" + strCarpetaImagenes + "/" + strCodigo + ".jpg" + " width=150 height=200></td></tr>";
                for ( int i=2; i <= TotalColumnasTabla; i++ )
                    strTabla += "<tr><td>" + rs.getString(i) + "</td></tr>";
                strTabla += "<tr><td><a href=" + url + strCodigo + " >Ver detalles</a></td></tr>";
                strTabla += "</table></td>";
                if ( ++columna > TotalColumnas ) {
                    fila++; columna = 1;
                    strTabla += "</tr><tr>";
                }
            }
            strTabla += "</tr></table>";
            rs.close();
            return strTabla;
        } catch (SQLException ex) { }
        return "";
    }

    public String getListViewHTML( String strHTML, int TotalFilas, int TotalColumnas ) {
        String strSalida="", strItem = "", strFilaItem = "";
        int items=0, columna = 1;
        try {
            ResultSet rs = ps.executeQuery();
            int TotalColumnasTabla = rs.getMetaData().getColumnCount();
            while ( rs.next() && ++items <= ( TotalFilas * TotalColumnas )  ) {
                strItem = strHTML;
                for ( int i=1; i <= TotalColumnasTabla; i++ ) 
                    strItem = BuscarReemplazar( strItem, "@" + i, rs.getString(i) );
                strFilaItem += strItem;
                if ( ++columna > TotalColumnas ) {
                    columna = 1;
                    strSalida += "<div>" + strFilaItem + "</div><br >";
                    strFilaItem = "";
                }
            }
        } catch (SQLException ex) { }
        return strSalida;
    }

    private String BuscarReemplazar( String strTexto, String strBuscar, String strReemplazar) {
        int pos = strTexto.indexOf(strBuscar);
        if ( pos > -1 )
            strTexto = strTexto.substring(0, pos) + strReemplazar + strTexto.substring( pos + strBuscar.length() );
        return strTexto;
    }
    
}
