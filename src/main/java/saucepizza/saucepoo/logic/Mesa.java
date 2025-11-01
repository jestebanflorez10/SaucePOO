package saucepizza.saucepoo.logic;

public class Mesa {
    private int numero;
    private String estado;
    private Pedido pedido;

    public Mesa(int numero, String estado, Pedido pedido) {
        this.numero = numero;
        this.estado = estado;
        this.pedido = pedido;
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

    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    
}
