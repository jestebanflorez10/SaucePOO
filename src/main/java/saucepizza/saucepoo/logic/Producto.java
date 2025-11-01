package saucepizza.saucepoo.logic;
import java.io.Serializable;

public class Producto implements Serializable{
    private String nombre;
    private double precio;
    private int cantidad;
    private int id;
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
    
}
