package saucepizza.saucepoo.persistencia;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import saucepizza.saucepoo.logic.Usuario;

public class UsuarioDAO {

    private static final String DB_FOLDER = "database";
    private static final String DB_FILE = "usuarios.db";

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

    public void crearTablaUsuarios() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                tipo TEXT NOT NULL,
                activo BOOLEAN NOT NULL DEFAULT 1
            );
        """;

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'usuarios' creada o ya existe.");
        }
    }

    public void agregarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios(username, password, tipo, activo) VALUES (?, ?, ?, ?)";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getTipo());
            pstmt.setBoolean(4, usuario.isActivo());
            pstmt.executeUpdate();
            System.out.println("Usuario agregado: " + usuario.getUsername());
        }
    }

    public Usuario obtenerUsuarioPorUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("tipo"),
                        rs.getBoolean("activo")
                    );
                }
            }
        }
        return null;
    }

    public List<Usuario> obtenerTodosLosUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("tipo"),
                    rs.getBoolean("activo")
                ));
            }
        }
        return usuarios;
    }

    public Usuario login(String username, String password) throws SQLException {
        Usuario usuario = obtenerUsuarioPorUsername(username);
        if (usuario == null) {
            return null;
        }
        if (!usuario.getPassword().equals(password)) {
            return null;
        }
        if (usuario.esCajero() && !usuario.isActivo()) {
            throw new IllegalStateException("Usuario cajero desactivado. Contacte al administrador.");
        }
        return usuario;
    }

    public void actualizarEstadoUsuario(int id, boolean nuevoEstado) throws SQLException {
        String sql = "UPDATE usuarios SET activo = ? WHERE id = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, nuevoEstado);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Estado del usuario actualizado.");
        }
    }

    public void actualizarPassword(int id, String nuevaPassword) throws SQLException {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevaPassword);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Contraseña actualizada.");
        }
    }

    public void eliminarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Usuario eliminado.");
        }
    }
}
