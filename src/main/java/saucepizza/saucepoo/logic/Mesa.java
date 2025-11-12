package saucepizza.saucepoo.logic;
import java.awt.image.BufferedImage;
import java.io.Serializable;
public class Mesa implements Serializable{
    
    private int numero;
    private String estado;
    private int idPedido;    
    private byte[] imagenBytes;
    private static final long serialVersionUID = 1L;
    
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
    public void setImagen(BufferedImage imagen) {
        this.imagenBytes = ImagenUtilidades.bufferedImageToBytes(imagen);
    }

    public BufferedImage getImagen() {
    if (imagenBytes == null) {
        return null;
    }
    return ImagenUtilidades.bytesToBufferedImage(imagenBytes);
    }
    public byte[] getImagenBytes() {
        return imagenBytes;
        }

    public void setImagenBytes(byte[] imagenBytes) {
         this.imagenBytes = imagenBytes;
    }
}
