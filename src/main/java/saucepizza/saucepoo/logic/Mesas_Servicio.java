package saucepizza.saucepoo.logic;
import saucepizza.saucepoo.persistencia.ControladoraPersistencia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    public ArrayList<Mesa> obtener(String estado){
       Iterator<Mesa> it1 = controlP.getMesaFile().obtenerTodos().iterator();
       ArrayList<Mesa> retorno = new ArrayList<Mesa>();
       while (it1.hasNext()){
           Mesa m = it1.next();
           if(m.getEstado().equals(estado)){           
               retorno.add(m);
           }          
           
       }
       return retorno;
      }
    public HashMap<Integer,Mesa> obtenerTodosH(){
        ArrayList<Mesa> mesas = controlP.getMesaFile().obtenerTodos();
        HashMap<Integer,Mesa> retorno = new HashMap<>();
        Iterator<Mesa> it = mesas.iterator();
        while(it.hasNext()){
            Mesa m = it.next();
            retorno.put(m.getNumero(), m);
        }
        return retorno;
    }
    }