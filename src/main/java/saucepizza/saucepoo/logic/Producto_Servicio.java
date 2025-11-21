package saucepizza.saucepoo.logic;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    public ArrayList<Producto> obtenerTodos(){ 
        return control.getProductoFile().obtenerTodos();
    }
    
    public int crearProductoCompleto(Producto producto) throws SQLException {
        try {
            // 1. Asegurarse de que el producto NO tiene ID en archivo
            producto.setId(0);
            
            // 2. Crear en inventario (BD) PRIMERO - obtiene el ID real
            int nuevoId = control.getInventarioDAO().agregarInventario(producto);
            if (nuevoId == -1) {
                throw new SQLException("Error al crear en inventario");
            }
            
            System.out.println("Inventario creado con ID: " + nuevoId);
            
            // 3. Asignar el ID correcto al producto
            producto.setId(nuevoId);
            
            // 4. Guardar en archivo CON el ID correcto desde el inicio
            boolean guardoEnArchivo = this.crear(producto);
            if (!guardoEnArchivo) {
                throw new SQLException("Error al guardar en archivo");
            }
            
            System.out.println("Archivo guardado con ID: " + nuevoId);
            
            return nuevoId;
            
        } catch (SQLException ex) {
            System.err.println("Error en crearProductoCompleto: " + ex.getMessage());
            throw ex;
        }
    }
    
    //Metodos para el acceso del inventario
    public Producto Inv_leer(String nombre) throws SQLException {
        Producto productoBD = control.getInventarioDAO().obtenerInventarioPorNombre(nombre);
        
        if (productoBD != null) {
            Producto productoCompleto = this.leer(productoBD.getId());
            
            if (productoCompleto != null) {
                productoCompleto.setCantidad(productoBD.getCantidad());
                return productoCompleto;
            }
        }
        return null;
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
    
    public HashMap<Integer, Producto> Inv_obtenerTodos() {
        HashMap<Integer, Producto> retorno = new HashMap<>();
        try {
            // Obtener referencias desde BD (id, nombre, cantidad)
            ArrayList<Producto> inventarioBD = (ArrayList<Producto>) control.getInventarioDAO().obtenerTodasCantidades();
            
            // Para cada uno en BD, obtener datos completos del archivo
            for (Producto p : inventarioBD) {
                Producto productoCompleto = this.leer(p.getId());
                
                if (productoCompleto != null) {
                    // Sincronizar: cantidad de BD + datos del archivo
                    productoCompleto.setCantidad(p.getCantidad());
                    retorno.put(p.getId(), productoCompleto);
                } else {
                    System.err.println("Producto ID " + p.getId() + " NO encontrado en archivo");
                }
            }
            return retorno;
        } catch (Exception e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
            return retorno;
        }
    }
    
    public Producto Inv_leerPorId(int id) throws SQLException {
        Producto productoBD = control.getInventarioDAO().obtenerInventarioPorId(id);
        
        if (productoBD != null) {
            Producto productoCompleto = this.leer(productoBD.getId());
            if (productoCompleto != null) {
                productoCompleto.setCantidad(productoBD.getCantidad());
                return productoCompleto;
            }
        }
        return null;
    }
}