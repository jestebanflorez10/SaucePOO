package saucepizza.saucepoo.logic;

public class Cajero extends Usuario{
    private boolean Activado;

    public boolean isActivado() {
        return this.Activado;
    }

    public void setActivado(boolean Activado) {
        this.Activado = Activado;
    }

    public Cajero(boolean Activado, int User_id, String Alias, String contrasena) {
        super(User_id, Alias, contrasena);
        this.Activado = Activado;
    }
}
