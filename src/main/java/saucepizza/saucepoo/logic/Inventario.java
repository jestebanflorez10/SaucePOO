package saucepizza.saucepoo.logic;

public class Inventario {
    private int item_id;
    private int capacidad;
    private String nombre;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Inventario(int item_id, int capacidad, String nombre) {
        this.item_id = item_id;
        this.capacidad = capacidad;
        this.nombre = nombre;
    }
}
