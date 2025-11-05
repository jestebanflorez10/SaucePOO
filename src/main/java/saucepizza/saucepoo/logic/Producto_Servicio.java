
package saucepizza.saucepoo.logic;
import java.sql.SQLException;
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
    
    //Metodos para el acceso del inventario
    
    public Producto Inv_leer(String nombre) throws SQLException {
        return control.getInventarioDAO().obtenerInventarioPorNombre(nombre);
    }
    public int Inv_crear(Producto producto) throws SQLException{
        return control.getInventarioDAO().agregarInventario(producto);    
    }
    public void Inv_eliminar(int id) throws SQLException {
        control.getInventarioDAO().eliminarInventario(id);
    }
    public void Inv_actualizar(Producto prod)throws SQLException {
        control.getInventarioDAO().actualizarInventario(prod);
    }    
    public void Inv_inicializarBase() throws SQLException {
        control.getInventarioDAO().crearTablaInventario();
    }
    public void Inv_crearTablaUsuarios() throws SQLException {
        control.getInventarioDAO().crearTablaInventario();
       }
}
