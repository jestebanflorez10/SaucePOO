package saucepizza.saucepoo.logic;
import java.util.ArrayList;

public class Pedido {
    private String fecha;
    private int id;
    private String nombreEmpresa;
    private String nombreCliente;
    private double subTotal;
    private double impuestos;
    private double total;
    private ArrayList<Producto> listaProductos; //lista de datos dinamica

    public Pedido(String fecha, int id, String nombreEmpresa, String nombreCliente, double subTotal, double impuestos, double total) {
        this.fecha = fecha;
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreCliente = nombreCliente;
        this.subTotal = subTotal;
        this.impuestos = impuestos;
        this.total = total;
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
}
