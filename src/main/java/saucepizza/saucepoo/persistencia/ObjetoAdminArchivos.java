
package saucepizza.saucepoo.persistencia;
import saucepizza.saucepoo.logic.Producto;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
public class ObjetoAdminArchivos<T>{
    private String CARPETA;
    private String tipo;
    private Class<T> categoria;
    public ObjetoAdminArchivos(String carpeta, String tipoArchivos, Class clase) {
        this.CARPETA=carpeta;
        this.tipo=tipoArchivos;
        this.categoria=clase;
        try {
            Path path = Paths.get(this.CARPETA);
            if(!Files.exists(path)){
                Files.createDirectory(path);
            }
        } catch(IOException e) {
            System.err.println("Error al crear carpeta: "+e.getMessage());
        }
    }
    private String prodName(String custom){         
        return this.CARPETA+File.separator+this.categoria.getSimpleName()+custom+"."+this.tipo;
    }
    public boolean crear(T objeto){ // C
    String nArchivo = prodName(objeto.toString());
    try(FileOutputStream fos = new FileOutputStream(nArchivo);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {        
        oos.writeObject(objeto);
        oos.flush();
        System.out.println(objeto.getClass().getSimpleName().toLowerCase()+ " ha sido guardado en: "+ nArchivo);
        return true;
    } catch (IOException e){
        System.err.println("Ha ocurrido un error al guardar "+objeto.getClass().getSimpleName().toLowerCase()+": "+ e.getMessage());
        return false;  }         
    }
    public T leer (String parametro){ //R
     T objetoLeido;
     String nombre = prodName(parametro);
     try(FileInputStream fis = new FileInputStream(nombre);
         ObjectInputStream ois = new ObjectInputStream(fis);){         
         objetoLeido = (T) ois.readObject();
         System.out.println(categoria.getSimpleName()+" encontrado "+nombre);
         return objetoLeido;
     }
     catch(FileNotFoundException e){
         System.out.println(categoria.getSimpleName()+" no encontrado || ID correspondiente: "+parametro);
        return null;
     }
     catch(IOException | ClassNotFoundException e){
         System.err.println("Error al leer "+ categoria.getSimpleName()+" || ID correspondiente: "+parametro);
         return null;
     }
     
    }
    public T leer(int id){
        return leer(String.valueOf(id));
    }
    
    
    public boolean actualizar(T objeto){ //U
        String nombreArch = prodName(objeto.toString());
        File archivo = new File(nombreArch);
        if(!archivo.exists()){
        System.err.println("No se pudo actualizar."+objeto.getClass().getSimpleName()+" no existente || ID correspondiente: "+objeto.toString());
        return false;
        }
        archivo.delete();
        return crear(objeto);
    }
    public boolean eliminar(String objeto){    //D
        String nombreArch = prodName(objeto);
        File archivo = new File(nombreArch);
        if(archivo.exists()){
        if(archivo.delete()){
            System.out.println(this.categoria.getSimpleName()+" con ID "+objeto+" eliminado con exito");
            return true;
        } else{
            System.err.println("No se pudo eliminar"+ this.categoria.getSimpleName().toLowerCase()+" con ID "+objeto);
            return false;
        }
        } else{
        System.err.println("No existe algun producto con un ID: "+objeto);
        return false;        
        }        
    }
    public boolean eliminar(int id){
        return eliminar(String.valueOf(id));
    }
    
    public ArrayList<T> obtenerTodos(){ //R
        File carpeta = new File(this.CARPETA);
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith("."+this.tipo));
        ArrayList<T> objetos = new ArrayList<T>();
        if (archivos == null || archivos.length == 0) {
            return objetos;
        }              
        
        for (File archivo : archivos) {
            try (
                FileInputStream fis = new FileInputStream(archivo);
                ObjectInputStream ois = new ObjectInputStream(fis)) 
            {
                objetos.add((T)ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer archivo: " + archivo.getName());
            }
        }
        
        return objetos;
        
    }
    
}
