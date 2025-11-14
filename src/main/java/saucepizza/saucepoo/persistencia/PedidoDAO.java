package saucepizza.saucepoo.persistencia;

import saucepizza.saucepoo.logic.Pedido;
import saucepizza.saucepoo.logic.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private ObjetoAdminArchivos<Producto> productoFile;

    // Constructor que inyecta la dependencia
    public PedidoDAO(ObjetoAdminArchivos<Producto> productoFile) {
        this.productoFile = productoFile;
    }

    /** Crea las tablas pedido y pedido_producto si no existen */
    public void crearTablas() {
        String sqlPedido = """
            CREATE TABLE IF NOT EXISTS pedido (
                id INTEGER PRIMARY KEY,
                fecha TEXT NOT NULL,
                nombreEmpresa TEXT NOT NULL,
                nombreCliente TEXT NOT NULL,
                subTotal REAL NOT NULL,
                impuestos REAL NOT NULL,
                total REAL NOT NULL
            );
        """;

        String sqlDetalle = """
            CREATE TABLE IF NOT EXISTS pedido_producto (
                pedido_id INTEGER NOT NULL,
                producto_id INTEGER NOT NULL,
                cantidad INTEGER NOT NULL,
                precio_unitario REAL NOT NULL,
                PRIMARY KEY (pedido_id, producto_id),
                FOREIGN KEY (pedido_id) REFERENCES pedido(id)
            );
        """;

        try (Connection conn = ConexionSQLite.getConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlPedido);
            stmt.execute(sqlDetalle);
            System.out.println("Tablas 'pedido' y 'pedido_producto' creadas o verificadas.");
        } catch (SQLException e) {
            System.out.println("Error al crear tablas de pedido: " + e.getMessage());
        }
    }

    /** Inserta un pedido y sus productos en una transacción */
    public void insertarPedidoConProductos(Pedido pedido) {
        String sqlPedido = """
            INSERT INTO pedido
              (id, fecha, nombreEmpresa, nombreCliente, subTotal, impuestos, total)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;
        String sqlDetalle = """
            INSERT INTO pedido_producto
              (pedido_id, producto_id, cantidad, precio_unitario)
            VALUES (?, ?, ?, ?);
        """;

        try (Connection conn = ConexionSQLite.getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psPedido = conn.prepareStatement(sqlPedido)) {
                psPedido.setInt(1, pedido.getId());
                psPedido.setString(2, pedido.getFecha());
                psPedido.setString(3, pedido.getNombreEmpresa());
                psPedido.setString(4, pedido.getNombreCliente());
                psPedido.setDouble(5, pedido.getSubTotal());
                psPedido.setDouble(6, pedido.getImpuestos());
                psPedido.setDouble(7, pedido.getTotal());
                psPedido.executeUpdate();
            }

            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle)) {
                for (Producto prod : pedido.getListaProductos()) {
                    psDet.setInt(1, pedido.getId());
                    psDet.setInt(2, prod.getId());
                    psDet.setInt(3, prod.getCantidad());
                    psDet.setDouble(4, prod.getPrecioUnitario());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            conn.commit();
            System.out.println("Pedido " + pedido.getId() + " y sus productos insertados correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar pedido con productos: " + e.getMessage());
            try {
                ConexionSQLite.getConexion().rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                ConexionSQLite.getConexion().setAutoCommit(true);
            } catch (SQLException ignored) {}
        }
    }

    /** Recupera un pedido completo (cabecera + productos desde archivos) */
    public Pedido buscarPedidoPorId(int id) {
        return cargarPedidoCompleto(id);
    }

    /** Método privado que carga un pedido completo con todos sus productos */
    private Pedido cargarPedidoCompleto(int pedidoId) {
        String sqlPedido = "SELECT * FROM pedido WHERE id = ?;";
        String sqlDetalle = """
            SELECT producto_id, cantidad, precio_unitario
              FROM pedido_producto
             WHERE pedido_id = ?;
        """;
        Pedido pedido = null;

        try (Connection conn = ConexionSQLite.getConexion();
             PreparedStatement psPedido = conn.prepareStatement(sqlPedido);
             PreparedStatement psDet = conn.prepareStatement(sqlDetalle)) {

            // Cargar cabecera del pedido
            psPedido.setInt(1, pedidoId);
            try (ResultSet rs = psPedido.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Pedido con ID " + pedidoId + " no encontrado en base de datos.");
                    return null;
                }
                pedido = new Pedido(
                    rs.getString("fecha"),
                    rs.getInt("id"),
                    rs.getString("nombreEmpresa"),
                    rs.getString("nombreCliente")
                );
                pedido.setSubTotal(rs.getDouble("subTotal"));
                pedido.setImpuestos(rs.getDouble("impuestos"));
                pedido.setTotal(rs.getDouble("total"));
            }

            // Cargar detalles del pedido (IDs y cantidades desde BD)
            psDet.setInt(1, pedidoId);
            try (ResultSet rs2 = psDet.executeQuery()) {
                while (rs2.next()) {
                    int productoId = rs2.getInt("producto_id");
                    int cantidad = rs2.getInt("cantidad");
                    double precioUnitario = rs2.getDouble("precio_unitario");

                    // Cargar el producto desde archivo (HISTÓRICO, no actualizado)
                    Producto prod = productoFile.leer(productoId);

                    if (prod != null) {
                        // Usar cantidad y precio HISTÓRICOS del pedido original
                        prod.setCantidad(cantidad);
                        prod.setPrecioUnitario(precioUnitario);
                        pedido.agregarProducto(prod);
                    } else {
                        System.err.println("Advertencia: Producto con ID " + productoId 
                            + " no encontrado en archivos para el pedido " + pedidoId);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar pedido completo (ID: " + pedidoId + "): " + e.getMessage());
        }

        return pedido;
    }

    /** Lista todos los pedidos CON productos cargados */
    public List<Pedido> listarPedidos() {
        String sql = "SELECT id FROM pedido ORDER BY id DESC;";
        List<Pedido> lista = new ArrayList<>();

        try (Connection conn = ConexionSQLite.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int pedidoId = rs.getInt("id");
                // Llamar al método que carga todo (cabecera + productos)
                Pedido pedido = cargarPedidoCompleto(pedidoId);
                if (pedido != null) {
                    lista.add(pedido);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }

        return lista;
    }

    /** Lista solo cabeceras de pedidos (sin productos) - si necesitas solo el resumen */
    public List<Pedido> listarPedidosCabecera() {
        String sql = "SELECT * FROM pedido ORDER BY id DESC;";
        List<Pedido> lista = new ArrayList<>();

        try (Connection conn = ConexionSQLite.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido p = new Pedido(
                    rs.getString("fecha"),
                    rs.getInt("id"),
                    rs.getString("nombreEmpresa"),
                    rs.getString("nombreCliente")
                );
                p.setSubTotal(rs.getDouble("subTotal"));
                p.setImpuestos(rs.getDouble("impuestos"));
                p.setTotal(rs.getDouble("total"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar cabeceras de pedidos: " + e.getMessage());
        }

        return lista;
    }

    public void setProductoFile(ObjetoAdminArchivos<Producto> productoFile) {
        this.productoFile = productoFile;
    }
}
