
package saucepizza.saucepoo.logic;
import java.util.ArrayList;
import saucepizza.saucepoo.persistencia.ControladoraPersistencia;

public class Empresa_Servicio {
    private ControladoraPersistencia control = new ControladoraPersistencia();
    public boolean crear(Empresa e){ 
    return control.getEmpresaFile().crear(e);
    }
    public Empresa leer (String nombre){ 
    return control.getEmpresaFile().leer(nombre);   
    }  
    public boolean actualizar(Empresa e){ 
       return control.getEmpresaFile().actualizar(e);
    }
    public boolean eliminar(String nombre){    
        return control.getEmpresaFile().eliminar(nombre);   
    }
    public ArrayList<Empresa> obtenerTodos(){ return control.getEmpresaFile().obtenerTodos();}
}
