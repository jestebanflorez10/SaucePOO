
package saucepizza.saucepoo.logic;
import java.util.ArrayList;
import saucepizza.saucepoo.persistencia.ControladoraPersistencia;
public class Producto_Servicio {
    private ControladoraPersistencia control = new ControladoraPersistencia();
    public boolean crear(Producto producto){ 
    return control.getProductoFile().crear(producto);
    }
    public Producto leer (int id){ 
    return control.getProductoFile().leer(id);   
    }  
    public boolean actualizar(Producto producto){ 
       return control.getProductoFile().actualizar(producto);
    }
    public boolean eliminar(int id){    
        return control.getProductoFile().eliminar(id);
        
    }
    public ArrayList<Producto> obtenerTodos(){ return control.getProductoFile().obtenerTodos();}
}
