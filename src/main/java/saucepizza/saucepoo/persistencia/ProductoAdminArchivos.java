
package saucepizza.saucepoo.persistencia;
import saucepizza.saucepoo.logic.Producto;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
public class ProductoAdminArchivos {
    private static final String CARPETA_PRODUCTOS = "productos";

    public ProductoAdminArchivos() {
        try {
            Path path = Paths.get(CARPETA_PRODUCTOS);
            if(!Files.exists(path)){
                Files.createDirectory(path);
            }
        } catch(IOException e) {
            System.err.println("Error al crear carpeta de productos: "+e.getMessage());
        }
    }
    private String prodName(int id){
        return CARPETA_PRODUCTOS+File.separator+"pizza"+id+".productos";
    }
    public boolean crear(Producto producto){ // C
    String nArchivo = prodName(producto.getId());
    try {
        FileOutputStream fos = new FileOutputStream(nArchivo);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(producto);
        oos.flush();
        System.out.println("El producto ha sido guardado en: "+ nArchivo);
        return true;
    } catch (IOException e){
        System.err.println("Ha ocurrido un errro al guardar el producto" + e.getMessage());
        return false;  }         
    }
    public Producto leer (int id){ //R
     String nombre = prodName(id);
     try{
         FileInputStream fis = new FileInputStream(nombre);
         ObjectInputStream ois = new ObjectInputStream(fis);
         Producto productoLeido = (Producto) ois.readObject();
         System.out.println("Producto encontrado "+nombre);
         return productoLeido;
     }
     catch(FileNotFoundException e){
         System.out.println("Producto no encontrado || ID correspondiente: "+id);
        return null;
     }
     catch(IOException | ClassNotFoundException e){
         System.err.println("Error al leer producto || ID correspondiente: "+id);
         return null;
     }
     
    }
  
    public boolean actualizar(Producto producto){ //U
        String nombreArch = prodName(producto.getId());
        File archivo = new File(nombreArch);
        if(!archivo.exists()){
        System.err.println("No se pudo actualizar. Producto no existente || ID correspondiente: "+producto.getId());
        return false;
        }
        return crear(producto);
    }
    public boolean eliminar(int id){    //D
        String nombreArch = prodName(id);
        File archivo = new File(nombreArch);
        if(archivo.exists()){
        if(archivo.delete()){
            System.out.println("Producto con ID "+id+" eliminado con exito");
            return true;
        } else{
            System.err.println("No se pudo eliminar producto con ID "+id);
            return false;
        }
        } else{
        System.err.println("No existe algun producto con un ID: "+id);
        return false;        
        }
        
    }
    public ArrayList<Producto> obtenerTodos(){ //R
        File carpeta = new File(CARPETA_PRODUCTOS);
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".producto"));
        ArrayList<Producto> productos = new ArrayList<Producto>();
        if (archivos == null || archivos.length == 0) {
            return productos;
        }              
        
        for (File archivo : archivos) {
            try (
                FileInputStream fis = new FileInputStream(archivo);
                ObjectInputStream ois = new ObjectInputStream(fis)) 
            {
                productos.add((Producto)ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer archivo: " + archivo.getName());
            }
        }
        
        return productos;
        
    }
    
}
