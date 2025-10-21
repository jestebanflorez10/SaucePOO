package saucepizza.saucepoo.logic;

import java.sql.SQLException;
import saucepizza.saucepoo.persistencia.ControladoraPersistencia;


public class Usuario_Servicio {
    private ControladoraPersistencia controlP = new ControladoraPersistencia();

    /** Inicializa la BD creando la tabla si no existe */
    public void inicializarBase() throws SQLException {
        controlP.getUsuarioDAO().crearTablaUsuarios();
    }
    public void crearTablaUsuarios() throws SQLException {
        controlP.getUsuarioDAO().crearTablaUsuarios();
       }

    /** Crea un usuario demo o cualquiera */
    public void crearUsuario(String username, String password, String tipo, boolean activo) throws SQLException {
        Usuario u = new Usuario(0, username, password, tipo, activo);
        controlP.getUsuarioDAO().agregarUsuario(u);
    }

    /** Valida login, lanza IllegalStateException si cajero desactivado */
    public Usuario login(String username, String password) throws SQLException, IllegalStateException {
        Usuario usuario = controlP.getUsuarioDAO().obtenerUsuarioPorUsername(username);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            return null;
        }
        if (usuario.esCajero() && !usuario.isActivo()) {
            throw new IllegalStateException("Usuario cajero desactivado");
        }
        return usuario;
    }
}
