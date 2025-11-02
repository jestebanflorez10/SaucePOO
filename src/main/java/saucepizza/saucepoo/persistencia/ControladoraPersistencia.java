package saucepizza.saucepoo.persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import saucepizza.saucepoo.logic.Empresa;
import saucepizza.saucepoo.logic.Producto;
import saucepizza.saucepoo.logic.Ventas;

public class ControladoraPersistencia {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    //private ProductoAdminArchivos productoFile = new ProductoAdminArchivos();
    //public ObjetoAdminArchivos(String carpeta, String tipoArchivos, Class clase)
    private ObjetoAdminArchivos<Producto> productoFile= new ObjetoAdminArchivos<>("productos","productos",Producto.class);
    private ObjetoAdminArchivos<Ventas> ventasFile= new ObjetoAdminArchivos<>("ventas","ventas",Ventas.class);
    private ObjetoAdminArchivos<Empresa> empresaFile= new ObjetoAdminArchivos<>("empresa","pizzeria",Empresa.class);
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
    
    public ObjetoAdminArchivos<Empresa> getEmpresaFile() {return empresaFile;}
    public void setEmpresaFile(ObjetoAdminArchivos<Empresa> empresaFile) {this.empresaFile = empresaFile;}
  
  
}