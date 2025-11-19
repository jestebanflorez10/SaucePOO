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
        """;

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'inventario' creada o ya existe.");
        }
    }

    private Connection abrirConexion() throws SQLException {
        File carpeta = new File(DB_FOLDER);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
            System.out.println("Carpeta creada: " + carpeta.getAbsolutePath());
        }
        File db = new File(carpeta, DB_FILE);
        System.out.println("Conectando a la base: " + db.getAbsolutePath());
        String url = "jdbc:sqlite:" + db.getPath();
        return DriverManager.getConnection(url);
    }

    public Producto obtenerInventarioPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE nombre = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Crear producto con datos de BD, obtener precio desde archivo
                    Producto producto = new Producto(
                        rs.getString("nombre"),
                        0, // Precio temporal, se actualizará desde archivo
                        rs.getInt("cantidad"),
                        rs.getInt("id")
                    );
                    return producto;
                }
            }
        }
        return null;
    }

    public List<Producto> obtenerTodasCantidades() throws SQLException {
        List<Producto> inv = new ArrayList<>();
        String sql = "SELECT id, nombre, cantidad FROM inventario";

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getString("nombre"),
                    0, // Precio temporal, se actualizará desde archivo
                    rs.getInt("cantidad"),
                    rs.getInt("id")
                );
                inv.add(producto);
            }
        }
        return inv;
    }

    public int agregarInventario(Producto producto) throws SQLException {        
    Producto existente = obtenerInventarioPorNombre(producto.getNombre());
    if (existente != null) {
        System.out.println("Producto ya existe con ID: " + existente.getId());
        return existente.getId();
    }
    String sql = "INSERT INTO inventario(nombre, cantidad) VALUES (?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setInt(2, producto.getCantidad());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = (int) generatedKeys.getLong(1);
                    System.out.println("Inventario agregado con ID: " + idGenerado);
                    return idGenerado;
                }
            }
        }
        return -1; // Error
    }

    public void actualizarInventario(Producto producto) throws SQLException {
        String sql = "UPDATE inventario SET cantidad = ?, nombre = ? WHERE id = ?";

    try (Connection conn = abrirConexion();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, producto.getCantidad());
        pstmt.setString(2, producto.getNombre());
        pstmt.setInt(3, producto.getId());
        int filasActualizadas = pstmt.executeUpdate();
        if (filasActualizadas > 0) {
            System.out.println("Producto actualizado: " + producto.getNombre());
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
            System.out.println("Registro de inventario eliminado con ID: " + id);
        }
    }
    public Producto obtenerInventarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE id = ?";
        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Crear producto con datos de BD
                    Producto producto = new Producto(
                        rs.getString("nombre"),
                        0, // Precio temporal, se actualizará desde archivo
                        rs.getInt("cantidad"),
                        rs.getInt("id")
                    );
                    return producto;
                }
            }
        }
        return null;
    }
}

