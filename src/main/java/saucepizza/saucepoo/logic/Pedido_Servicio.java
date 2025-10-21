package saucepizza.saucepoo.logic;

import java.sql.SQLException;
import java.util.List;
import saucepizza.saucepoo.persistencia.ControladoraPersistencia;
public class Pedido_Servicio {
    private ControladoraPersistencia controlP = new ControladoraPersistencia();
    public void inicializarBase() throws SQLException {
        controlP.getPedidoDAO().crearTablas();
    }

    /** Crea las tablas de pedidos y detalle explícitamente */
    public void crearTablasPedidos() {
        controlP.getPedidoDAO().crearTablas();
    }

    /** Registra un nuevo pedido con todos sus productos */
    public void registrarPedido(Pedido pedido) {
        controlP.getPedidoDAO().insertarPedidoConProductos(pedido);
    }

    /** Busca un pedido completo (cabecera + productos) por su ID */
    public Pedido obtenerPedidoPorId(int id) {
        return controlP.getPedidoDAO().buscarPedidoPorId(id);
    }

    /** Lista todos los pedidos (solo cabeceras) */
    public List<Pedido> listarTodosLosPedidos() {
        return controlP.getPedidoDAO().listarPedidos();
    }

    /**
     * Crea un nuevo pedido con los datos básicos y lo devuelve
     * para agregar productos antes de persistirlo.
     */
    public Pedido crearPedido(String fecha, String nombreEmpresa, String nombreCliente) {   
        int id = controlP.obtenerNuevoIdPedido();
        return new Pedido(fecha, id, nombreEmpresa, nombreCliente);
    }

    /**
     * Agrega un producto al pedido en memoria.
     */
    public void agregarProductoAlPedido(Pedido pedido, Producto producto) {
        pedido.agregarProducto(producto);
    }

    /**
     * Valida si existe un pedido con el ID proporcionado.
     * @return true si existe, false en caso contrario.
     */
    public boolean existePedido(int id) {
        return controlP.getPedidoDAO().buscarPedidoPorId(id) != null;
    }

    /**
     * Calcula el total de un pedido (subTotal + impuestos) y lo asigna al pedido.
     */
    public void actualizarTotales(Pedido pedido) {
        double subTotal = pedido.getListaProductos()
                                .stream()
                                .mapToDouble(p -> p.getCantidad() * p.getPrecioUnitario())
                                .sum();
        double impuestos = subTotal * 0.19; // ejemplo 19%
        pedido.setSubTotal(subTotal);
        pedido.setImpuestos(impuestos);
        pedido.setTotal(subTotal + impuestos);
    }
}
