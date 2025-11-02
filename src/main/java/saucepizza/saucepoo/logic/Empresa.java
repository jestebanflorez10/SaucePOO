
package saucepizza.saucepoo.logic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
public class Empresa implements Serializable{
    private int id;
    private String nombre;
    private HashMap<String, Ventas> registro;
    public Empresa(String nombre){
        this.nombre=nombre;
        this.registro=new HashMap<String, Ventas>();
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

    public HashMap<String, Ventas> getRegistro() {
        return registro;
    }

    public void setRegistro(HashMap<String, Ventas> registro) {
        this.registro = registro;
    }
     public Set<String> todasVFechas(){    
        return this.registro.keySet();    
    }
}
