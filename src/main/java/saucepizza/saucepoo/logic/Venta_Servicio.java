
package saucepizza.saucepoo.logic;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import saucepizza.saucepoo.persistencia.ControladoraPersistencia;

public class Venta_Servicio {
    private ControladoraPersistencia control = new ControladoraPersistencia();
    public boolean crear(Ventas venta){ 
    return control.getVentasFile().crear(venta);
    }
    public Ventas leer (String fecha){ 
    return control.getVentasFile().leer(fecha);   
    }  
    public boolean actualizar(Ventas venta){ 
       return control.getVentasFile().actualizar(venta);
    }
    public boolean eliminar(int id){    
        return control.getVentasFile().eliminar(id);
        
    }
    public boolean eliminar(String id){    
        return control.getVentasFile().eliminar(id);
        
    }
    public ArrayList<Ventas> obtenerTodos(){ return control.getVentasFile().obtenerTodos();}
    
    public TreeMap<String, Ventas> segunFecha(){
    TreeMap<String, Ventas> retorno = new TreeMap<>(new Comparador());
    Iterator<Ventas> it1 = obtenerTodos().iterator();
    while(it1.hasNext()){
        Ventas v = it1.next();
        retorno.put(v.getFecha(), v);
    }
    return retorno;
    }
}
