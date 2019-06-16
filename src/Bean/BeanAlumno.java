
package Bean;

public class BeanAlumno {
  private String Nombre;
  private String Apellido;
  private String Codigo;
  private String Nivel;
  private String Turno;
  
  public BeanAlumno(){
    super();
  }

    public BeanAlumno(String Nombre, String Apellido, String Codigo, String Nivel, String Turno) {
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Codigo = Codigo;
        this.Nivel = Nivel;
        this.Turno = Turno;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getNivel() {
        return Nivel;
    }

    public void setNivel(String Nivel) {
        this.Nivel = Nivel;
    }

    public String getTurno() {
        return Turno;
    }

    public void setTurno(String Turno) {
        this.Turno = Turno;
    }
  
}
