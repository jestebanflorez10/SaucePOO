
package saucepizza.saucepoo.persistencia;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import saucepizza.saucepoo.logic.Producto;

public class InventarioDAO {
    
    private static final String DB_FOLDER = "database";
    private static final String DB_FILE = "usuarios.db";
    
    public void crearTablaInventario() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS inventario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                cantidad INTEGER NOT NULL
            );
        """; //Creas un formato de tabla

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'usuarios' creada o ya existe.");
        }
    }

    private Connection abrirConexion() throws SQLException {
        // Asegura que exista la carpeta
        File carpeta = new File(DB_FOLDER);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
            System.out.println("Carpeta creada: " + carpeta.getAbsolutePath());
        }
        // Ruta al archivo de base de datos
        File db = new File(carpeta, DB_FILE);
        System.out.println("Conectando a la base: " + db.getAbsolutePath());
        String url = "jdbc:sqlite:" + db.getPath();
        return DriverManager.getConnection(url);
    }

      public Producto obtenerInventarioPorNombre(String producto) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE nombre = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ControladoraPersistencia helper = new ControladoraPersistencia();
                    Producto r = helper.getProductoFile().leer(rs.getInt("id"));
                    //    public Producto(String nombre, double precio, int cantidad, int id) 
                    return new Producto(                        
                        rs.getString("nombre"),
                        r.getPrecioUnitario(),
                        rs.getInt("cantidad"),
                        rs.getInt("id")
                    );
                }
            }
        }
        return null;
    }
    public List<Producto> obtenerTodasCantidades() throws SQLException {
        List<Producto> inv = new ArrayList<>();
        String sql = "SELECT * FROM inventario";

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ControladoraPersistencia helper = new ControladoraPersistencia();
                Producto r = helper.getProductoFile().leer(rs.getInt("id"));
                //    public Producto(String nombre, double precio, int cantidad, int id)
                inv.add(new Producto(                    
                    rs.getString("nombre"),
                    r.getPrecioUnitario(),
                    rs.getInt("cantidad"),
                    rs.getInt("id")
                ));
            }
        }
        return inv;
    }
        public int agregarInventario(Producto producto) throws SQLException {        
        // Primero verifica si el producto ya existe
        Producto existente = obtenerInventarioPorNombre(producto.getNombre());
        if (existente != null) {
            System.out.println("Producto ya existe con ID: " + existente.getId());
            return existente.getId();
        }

        // Si no existe, entonces inserta
        String sql = "INSERT INTO inventario(nombre, cantidad) VALUES (?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setInt(2, producto.getCantidad());
            pstmt.executeUpdate();

            // Obtener el ID generado automáticamente
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    System.out.println("Inventario agregado con ID: " + idGenerado);
                    return idGenerado;
                }
            }
        }
        return -1;
    }

    public void actualizarInventario(Producto producto) throws SQLException {
        String sql = "UPDATE inventario SET cantidad = ? WHERE id = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, producto.getCantidad());
            pstmt.setInt(2, producto.getId());
            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Producto en el inventario actualizado: " + producto.getNombre());
            } else {
                System.out.println("No se encontró producto con ID: " + producto.getId());
            }
        }
    }


    public void eliminarInventario(int id) throws SQLException {
        String sql = "DELETE FROM inventario WHERE id = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Registro de inventario eliminado.");
        }
    }
    
}
