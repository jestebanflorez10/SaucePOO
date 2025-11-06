package saucepizza.saucepoo.logic;
import java.io.Serializable;
public class Mesa implements Serializable{
    
    private int numero;
    private String estado;
    private int idPedido;
    
    
    public String ObtenerNombre(){
        return "Mesa "+ String.valueOf(this.numero);
    }

    public Mesa(int numero, String estado, int idPedido) {
        this.numero = numero;
        this.estado = estado;
        this.idPedido = idPedido;
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdPedido() {
        return this.idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    
    @Override
    public String toString(){
        return String.valueOf(this.numero);
    }
}
