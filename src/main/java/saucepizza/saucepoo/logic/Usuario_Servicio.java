package saucepizza.saucepoo.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public Usuario obtenerUsuarioPorUsername (String usuario) throws SQLException{
        return controlP.getUsuarioDAO().obtenerUsuarioPorUsername(usuario);
    }
    public void actualizarUsuario (Usuario u)throws SQLException{
        controlP.getUsuarioDAO().agregarUsuario(u);
            }
    public Usuario guardarUsuario(Usuario u) throws SQLException {
     return controlP.getUsuarioDAO().guardarUsuario(u);
    }
    public void actualizarUsuarioPorId(Usuario u) throws SQLException{
        controlP.getUsuarioDAO().actualizarUsuarioPorId(u);
    }
    public void agregarUsuario(Usuario u)  throws SQLException {
        controlP.getUsuarioDAO().agregarUsuario(u);
    }
    public boolean usernameYaExiste(String username, int idActual) throws SQLException {
        return controlP.getUsuarioDAO().usernameYaExiste(username, idActual);
    }
    /**
 * Cuenta cuántos usuarios están activos en la base de datos
 * @return cantidad de usuarios activos
 */
    public int contarUsuariosActivos() throws SQLException {
        List<Usuario> todosLosUsuarios = controlP.getUsuarioDAO().obtenerTodosLosUsuarios();
        int contador = 0;

        for (Usuario usuario : todosLosUsuarios) {
            if (usuario.isActivo()) {
                contador++;
            }
        }

        return contador;
    }
    public List<Usuario> obtenerTodos() throws SQLException{
        return controlP.getUsuarioDAO().obtenerTodosLosUsuarios();
    }

}
