
package saucepizza.saucepoo.logic;
import java.util.HashMap;
public class Empresa {
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
}
