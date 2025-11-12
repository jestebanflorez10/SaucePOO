package saucepizza.saucepoo.logic;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Producto implements Serializable{
    private String nombre;
    private double precio;
    private int cantidad;
    private int id;
    private byte[] imagenBytes;
    private static final long serialVersionUID = 1L;
    public Producto(String nombre, double precio, int cantidad, int id) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.id=id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioUnitario() {
        return this.precio;
    }

    public void setPrecioUnitario(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString(){
    return String.valueOf(this.id);
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
