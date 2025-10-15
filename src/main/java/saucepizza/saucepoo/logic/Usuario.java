package saucepizza.saucepoo.logic;

public class Usuario {
    private int User_id;
    private String Alias;
    private String contrasena;

    public int getUser_id() {
        return this.User_id;
    }

    public void setUser_id(int User_id) {
        this.User_id = User_id;
    }

    public String getAlias() {
        return this.Alias;
    }

    public void setAlias(String Alias) {
        this.Alias = Alias;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario(int User_id, String Alias, String contrasena) {
        this.User_id = User_id;
        this.Alias = Alias;
        this.contrasena = contrasena;
    }
}
