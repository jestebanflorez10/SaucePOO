package saucepizza.saucepoo.persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControladoraPersistencia {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private ProductoAdminArchivos productoFile = new ProductoAdminArchivos();
    
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
    public ProductoAdminArchivos getProductoFile() {return this.productoFile;}
    public void setProductoFile(ProductoAdminArchivos productoFile) {this.productoFile = productoFile;}
  
  
  
}