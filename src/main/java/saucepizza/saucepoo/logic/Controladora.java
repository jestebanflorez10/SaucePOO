package saucepizza.saucepoo.logic;

public class Controladora {
    // controladora persistencia
  private Usuario_Servicio usuarioService = new Usuario_Servicio();

    public Usuario_Servicio getUsuarioService() {return this.usuarioService;}

    public void setUsuarioService(Usuario_Servicio usuarioService) {this.usuarioService = usuarioService;}
    
}
