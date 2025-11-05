package saucepizza.saucepoo.logic;
import saucepizza.saucepoo.persistencia.ControladoraPersistencia;
import java.util.ArrayList;

public class Mesas_Servicio {
    private ControladoraPersistencia controlP = new ControladoraPersistencia();
    
    /*private int numero;
    private String estado;
    private Pedido pedido;*/
    
    public boolean Crear(Mesa mesa){
        return controlP.getMesaFile().crear(mesa);
    }
    public Mesa leer (int numero){
        return controlP.getMesaFile().leer(numero);
    }
    public boolean actualizar(Mesa mesa){
        return controlP.getMesaFile().actualizar(mesa);
    }
    public boolean eliminar (int numero){
        return controlP.getMesaFile().eliminar(numero);
    }
    
    public ArrayList<Mesa> obtenerTodos(){
        return controlP.getMesaFile().obtenerTodos();
    }
}
