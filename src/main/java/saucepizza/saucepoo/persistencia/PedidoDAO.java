package saucepizza.saucepoo.persistencia;

import saucepizza.saucepoo.logic.Pedido;
import java.sql.*;

public class PedidoDAO {

    //Crea la tabla si no existe
    public void crearTablaPedido() {
        String sql = """
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

        try (Connection conn = ConexionSQLite.getConexion();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabla 'pedido' creada o verificada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al crear la tabla 'pedido': " + e.getMessage());
        }
    }

    // Inserta un pedido nuevo
    public void insertarPedido(Pedido pedido) {
        String sql = """
            INSERT INTO pedido (id, fecha, nombreEmpresa, nombreCliente, subTotal, impuestos, total)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = ConexionSQLite.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pedido.getId());
            pstmt.setString(2, pedido.getFecha());
            pstmt.setString(3, pedido.getNombreEmpresa());
            pstmt.setString(4, pedido.getNombreCliente());
            pstmt.setDouble(5, pedido.getSubTotal());
            pstmt.setDouble(6, pedido.getImpuestos());
            pstmt.setDouble(7, pedido.getTotal());

            pstmt.executeUpdate();
            System.out.println("Pedido insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al insertar pedido: " + e.getMessage());
        }
    }

    //Buscar pedido por ID
    public Pedido buscarPedidoPorId(int id) {
        String sql = "SELECT * FROM pedido WHERE id = ?;";
        Pedido pedido = null;

        try (Connection conn = ConexionSQLite.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                pedido = new Pedido(
                    rs.getString("fecha"),
                    rs.getInt("id"),
                    rs.getString("nombreEmpresa"),
                    rs.getString("nombreCliente"),
                    rs.getDouble("subTotal"),
                    rs.getDouble("impuestos"),
                    rs.getDouble("total")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar pedido: " + e.getMessage());
        }

        return pedido;
    }

    //Listar todos los pedidos
    public void listarPedidos() {
        String sql = "SELECT * FROM pedido;";

        try (Connection conn = ConexionSQLite.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Listado de pedidos:");
            while (rs.next()) {
                System.out.println(
                    "ID: " + rs.getInt("id") +
                    " | Cliente: " + rs.getString("nombreCliente") +
                    " | Total: " + rs.getDouble("total")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }
    }
}
