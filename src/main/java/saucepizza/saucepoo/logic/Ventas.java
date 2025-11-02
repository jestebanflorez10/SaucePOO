
package saucepizza.saucepoo.logic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
public class Ventas implements Serializable{
    private String fecha;
    private ArrayList<Producto> cantidadVendida;   
    private Double total;
    private ArrayList<Pedido> pedidosV;
    private static final long serialVersionUID = 1L;

    public Ventas(String date) {
        this.cantidadVendida = new ArrayList<Producto>();
        this.total = 0.0;
        this.pedidosV = new ArrayList<Pedido>();
        this.fecha=date;
    }

    
    public Double getTotal() {
        return this.total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    public void agregarPedido(Pedido orden){
        this.pedidosV.add(orden);        
        this.total+=orden.getTotal();
        Iterator<Producto> it1 = orden.getListaProductos().iterator();        
        while(it1.hasNext()){
            boolean encontrado=false;
            Producto producto = it1.next();
            Iterator<Producto> it2 = this.cantidadVendida.iterator();
            while(it2.hasNext()){
                Producto existente = it2.next();
                if(existente.getId()==producto.getId()){
                existente.setCantidad(existente.getCantidad()+producto.getCantidad());
                encontrado = true;
                break; 
                }
            }
            if(!encontrado){
            this.cantidadVendida.add(producto);
            
            }
        }
    
    }

    public ArrayList<Producto> getCantidadVendida() {
        return cantidadVendida;
    }
    public int getUnidadesVendidas(){
        int dato=0;
        Iterator<Producto> it1 = this.cantidadVendida.iterator();
        while(it1.hasNext()){
            dato+=it1.next().getCantidad();
        }
        return dato;
    }
    public void setCantidadVendida(ArrayList<Producto> cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public ArrayList<Pedido> getPedidosV() {
        return pedidosV;
    }

    public void setPedidosV(ArrayList<Pedido> pedidosV) {
        this.pedidosV = pedidosV;
    }

    public String getFecha() {
        return this.fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    @Override
    public String toString(){
    return this.fecha;
    }
}
