package saucepizza.saucepoo.logic;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String tipo; // "Admin" o "Cajero"
    private boolean activo; // true = activo, false = desactivado

    public Usuario(int id, String username, String password, String tipo, boolean activo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
        this.activo = activo;
    }

    // Getters
    public int getId() {return this.id;}
    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public String getTipo() {return this.tipo;}
    public boolean isActivo() {return this.activo;}
    // Setters
    public void setId(int id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    // Métodos de utilidad
    public boolean esAdmin() {
        return "Admin".equalsIgnoreCase(this.tipo);
    }

    public boolean esCajero() {
        return "Cajero".equalsIgnoreCase(this.tipo);
    }
}
