package saucepizza.saucepoo.persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import saucepizza.saucepoo.logic.Producto;
import saucepizza.saucepoo.logic.Ventas;
import saucepizza.saucepoo.logic.Mesa;

public class ControladoraPersistencia {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private InventarioDAO inventarioDAO = new InventarioDAO(); //Depende mucho de ProductoFile
    //private ProductoAdminArchivos productoFile = new ProductoAdminArchivos();
    //public ObjetoAdminArchivos(String carpeta, String tipoArchivos, Class clase)
    private ObjetoAdminArchivos<Producto> productoFile= new ObjetoAdminArchivos<>("productos","productos",Producto.class);
    private ObjetoAdminArchivos<Ventas> ventasFile= new ObjetoAdminArchivos<>("ventas","ventas",Ventas.class);
    private ObjetoAdminArchivos<Mesa> mesaFile = new ObjetoAdminArchivos<>("mesa", "mesa", Mesa.class);
    private PedidoDAO pedidoDAO = new PedidoDAO(this.productoFile);
  public int obtenerNuevoIdPedido() {
    int nuevoId = 1; // valor por defecto
    String sql = "SELECT MAX(id) AS max_id FROM pedido";

    try (Connection conn = ConexionSQLite.getConexion();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        if (rs.next()) {
            nuevoId = rs.getInt("max_id") + 1;
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener nuevo ID pedido: " + e.getMessage());
    }
    return nuevoId;}

    public UsuarioDAO getUsuarioDAO() {return usuarioDAO;}
    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {this.usuarioDAO = usuarioDAO;}
    public PedidoDAO getPedidoDAO() {return pedidoDAO;}
    public void setPedidoDAO(PedidoDAO pedidoDAO) {this.pedidoDAO = pedidoDAO;}
    public ObjetoAdminArchivos<Producto> getProductoFile() {return this.productoFile;}
    public void setProductoFile(ObjetoAdminArchivos<Producto> productoFile) {this.productoFile = productoFile;}  
    /*public ProductoAdminArchivos getProductoFile() {return this.productoFile;}
    public void setProductoFile(ProductoAdminArchivos productoFile) {this.productoFile = productoFile;}*/

    public ObjetoAdminArchivos<Ventas> getVentasFile() {return ventasFile;}
    public void setVentasFile(ObjetoAdminArchivos<Ventas> ventasFile) {this.ventasFile = ventasFile;}
    
    public ObjetoAdminArchivos<Mesa> getMesaFile() { return this.mesaFile; } public void setMesaFile(ObjetoAdminArchivos<Mesa> mesaFile) { this.mesaFile = mesaFile; }

    public InventarioDAO getInventarioDAO() {return inventarioDAO;}
    public void setInventarioDAO(InventarioDAO inventarioDAO) {this.inventarioDAO = inventarioDAO;} 
}