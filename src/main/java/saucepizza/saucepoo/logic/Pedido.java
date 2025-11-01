package saucepizza.saucepoo.logic;
import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable{
    private String fecha;
    private int id;
    private String nombreEmpresa;
    private String nombreCliente;
    private double subTotal;
    private double impuestos;
    private double total;
    private double cambio;
    private double efectivo;
    private ArrayList<Producto> listaProductos; //lista de datos dinamica

    public Pedido(String fecha, int id, String nombreEmpresa, String nombreCliente) {
        this.fecha = fecha;
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreCliente = nombreCliente;
        this.subTotal = 0;
        this.impuestos = 0;
        this.total = 0;
        this.listaProductos = new ArrayList<>();
    }

    public String getFecha() {
        return this.fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return this.nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreCliente() {
        return this.nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getSubTotal() {
        return this.subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getImpuestos() {
        return this.impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
    public void agregarProducto(Producto nuevoProducto) {
        for (Producto p : listaProductos) {
            if (p.getId() == nuevoProducto.getId()) {
                p.setCantidad(p.getCantidad() + nuevoProducto.getCantidad());
                return;
            }
        }
        listaProductos.add(nuevoProducto);
    }

    public double getCambio() {
        return this.cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }
    public double getEfectivo() {
        return this.efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }
    
}
