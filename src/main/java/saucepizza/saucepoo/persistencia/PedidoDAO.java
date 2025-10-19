package saucepizza.saucepoo.persistencia;

import saucepizza.saucepoo.logic.Pedido;
import saucepizza.saucepoo.logic.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

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
                FOREIGN KEY (pedido_id)   REFERENCES pedido(id),
                FOREIGN KEY (producto_id) REFERENCES producto(id)
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
            System.out.println("Pedido y productos insertados correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar pedido con productos: " + e.getMessage());
            try {
                // Intentar rollback si la conexión sigue abierta
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

    /** Recupera un pedido completo (cabecera + productos) */
    public Pedido buscarPedidoPorId(int id) {
        String sqlPedido = "SELECT * FROM pedido WHERE id = ?;";
        String sqlDetalle = """
            SELECT p.producto_id, pr.nombre, d.cantidad, d.precio_unitario
              FROM pedido_producto d
              JOIN producto pr ON d.producto_id = pr.id
             WHERE d.pedido_id = ?;
        """;
        Pedido pedido = null;

        try (Connection conn = ConexionSQLite.getConexion();
             PreparedStatement psPedido = conn.prepareStatement(sqlPedido);
             PreparedStatement psDet = conn.prepareStatement(sqlDetalle)) {

            psPedido.setInt(1, id);
            try (ResultSet rs = psPedido.executeQuery()) {
                if (!rs.next()) return null;
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

            psDet.setInt(1, id);
            try (ResultSet rs2 = psDet.executeQuery()) {
                while (rs2.next()) {
                    Producto prod = new Producto(
                        rs2.getString("nombre"),
                        rs2.getDouble("precio_unitario"),
                        rs2.getInt("cantidad"),
                        rs2.getInt("producto_id")                     
                                              
                    );
                    pedido.agregarProducto(prod);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar pedido completo: " + e.getMessage());
        }

        return pedido;
    }

    /** Lista todos los pedidos (solo cabeceras) */
    public List<Pedido> listarPedidos() {
        String sql = "SELECT * FROM pedido;";
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
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }

        return lista;
    }
}
