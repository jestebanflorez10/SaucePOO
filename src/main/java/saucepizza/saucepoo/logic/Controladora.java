package saucepizza.saucepoo.logic;

public class Controladora {
    // controladora persistencia
  private Usuario_Servicio usuarioService = new Usuario_Servicio();
  private Pedido_Servicio pedidoServicio = new Pedido_Servicio();
  private Producto_Servicio productoServicio = new Producto_Servicio();
  private Venta_Servicio ventasServicio = new Venta_Servicio();


    public Usuario_Servicio getUsuarioService() {return this.usuarioService;}

    public void setUsuarioService(Usuario_Servicio usuarioService) {this.usuarioService = usuarioService;}

    public Pedido_Servicio getPedidoServicio() { return this.pedidoServicio;}

    public void setPedidoServicio(Pedido_Servicio pedidoServicio) {this.pedidoServicio = pedidoServicio;}

    public Producto_Servicio getProductoServicio() {return this.productoServicio;}

    public void setProductoServicio(Producto_Servicio productoServicio) {this.productoServicio = productoServicio;}

    public Venta_Servicio getVentasServicio() {return ventasServicio;}
    
    public void setVentasServicio(Venta_Servicio ventasServicio) {this.ventasServicio = ventasServicio;}
    
}
