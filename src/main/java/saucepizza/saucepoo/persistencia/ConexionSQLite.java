package saucepizza.saucepoo.persistencia;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {
    private static final String DB_FOLDER = "database"; // Carpeta para db
    private static final String DB_FILE_NAME = "usuarios.db";
    
    private static final String DB_PATH;

    static {
        // Crear carpeta si no existe
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
             System.out.println("Carpeta creada en: " + folder.getAbsolutePath());
        }
        File dbFile = new File(folder, DB_FILE_NAME);
        DB_PATH = dbFile.getPath();
    System.out.println("Base de datos SQLite en: " + dbFile.getAbsolutePath());
    }

    public static Connection getConexion() throws SQLException {
        String url = "jdbc:sqlite:" + DB_PATH;
        return DriverManager.getConnection(url);
    }
}
