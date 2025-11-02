
package saucepizza.saucepoo.logic;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.time.format.DateTimeFormatter;
public class Empresa implements Serializable{
    private int id;
    private String nombre;
    private TreeMap<String, Ventas> registro;
    private static final long serialVersionUID = 1L;

    public Empresa(String nombre, int id){
        
        this.nombre=nombre;
        this.id=id;
        this.registro=new TreeMap<>(new Comparador());
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void agregarVenta(String fecha, Ventas ventadia){
        this.registro.put(fecha, ventadia);        
    }
    public Ventas consultarVenta(String fecha){
        return this.registro.get(fecha);
    }
    @Override
    public String toString(){
        return String.valueOf(this.id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TreeMap<String, Ventas> getRegistro() {
        return registro;
    }

    public void setRegistro(TreeMap<String, Ventas> registro) {
        this.registro = registro;
    }
     public Set<String> todasVFechas(){    
        return this.registro.keySet();    
    }
}
