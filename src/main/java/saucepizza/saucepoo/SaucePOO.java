
package saucepizza.saucepoo;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import saucepizza.saucepoo.igu.SplashScreen;
//import saucepizza.saucepoo.igu.UtilidadesPedidos;
import saucepizza.saucepoo.logic.Producto;
//import saucepizza.saucepoo.logic.Ventas;
import saucepizza.saucepoo.logic.Controladora;
import saucepizza.saucepoo.logic.Mesa;

public class SaucePOO {
    public static String pizzeria = "Pizzeria Demo";
    public static void main(String[] args) {
        try(
            BufferedReader br = new BufferedReader(new FileReader(new File("demo.txt")));             
            ){
            String modo = br.readLine();
            if(modo.isBlank()||modo.isEmpty()){pizzeria = "Pizzeria Demo";}
            else{pizzeria = modo;}
            BufferedImage imagen1 = ImageIO.read(SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demo0.png"));
            BufferedImage imagen2 = ImageIO.read(SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demo1.png"));
            BufferedImage imagen3 = ImageIO.read(SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demo2.png"));
            BufferedImage imagen4 = ImageIO.read(SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demo3.png"));
            BufferedImage mesaimagen = ImageIO.read(SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demomesa0.png")); //nuevo
            Producto prod1 = new Producto("Pepperoni", 1000, 1, 0);
            Producto prod2= new Producto("Queso", 1500, 1, 1);
            Producto prod3 = new Producto("Carne", 1700, 1, 2);
            Producto prod4= new Producto("Soda", 500, 1, 3);
            prod1.setImagen(imagen1);
            prod2.setImagen(imagen2);
            prod3.setImagen(imagen3);
            prod4.setImagen(imagen4);
            Controladora helper = new Controladora();
            helper.getProductoServicio().Inv_inicializarBase();
                int id1 = helper.getProductoServicio().Inv_crear(prod1);  // Retorna ID si existe o inserta si no
                int id2 = helper.getProductoServicio().Inv_crear(prod2);
                int id3 = helper.getProductoServicio().Inv_crear(prod3);
                int id4 = helper.getProductoServicio().Inv_crear(prod4);
            Mesa mesa1 = new Mesa(2, "Libre", 0); //temporal -1: sin pedido asignado
            Mesa mesa2 = new Mesa(1, "Libre", 0); //temporal -1: sin pedido asignado
            Mesa mesa3 = new Mesa(0, "Libre", 0); //temporal -1: sin pedido asignado 
            
            mesa1.setImagen(mesaimagen);  mesa2.setImagen(mesaimagen); mesa3.setImagen(mesaimagen);          
            
            helper.getMesasServicio().Crear(mesa1);
            helper.getMesasServicio().Crear(mesa2);
            helper.getMesasServicio().Crear(mesa3);            
            
            prod1.setId(id1);
            prod2.setId(id2);
            prod3.setId(id3);
            prod4.setId(id4);
            helper.getProductoServicio().crear(prod1); //Crea los archivos
            helper.getProductoServicio().crear(prod2);
            helper.getProductoServicio().crear(prod3);
            helper.getProductoServicio().crear(prod4); 
            prod1.setCantidad(4);
            prod2.setCantidad(4);
            prod3.setCantidad(4);
            prod4.setCantidad(4);
                helper.getProductoServicio().Inv_actualizar(prod1);  // Actualiza la cantidad del registro existente
                helper.getProductoServicio().Inv_actualizar(prod2);
                helper.getProductoServicio().Inv_actualizar(prod3);
                helper.getProductoServicio().Inv_actualizar(prod4);
                }catch(Exception e){
                System.out.println("No se activa el modo demo");
                System.err.println(e.getMessage());
                System.err.println(e.getStackTrace());
                }            
        System.out.println("2025-II by Sauce Team");
        SplashScreen inicio = new SplashScreen();
        inicio.setLocationRelativeTo(null);
        inicio.setVisible(true);       
                
        String art = 
        """
                                                                                                                                                 
                                                                                                                                                 
                                                                                                                                                 
                            ########                                                                                                             
                           ##       ######                                                                                                       
                           #####         #####                                                                                                   
                           ##.########       ###                                                                                                 
                          ##         #####     ##:                                                                                               
                         ##  #####       ###    +###           :####.                                                                            
                        ##   ###    ###    ### ######       ###########                                                                          
                       ##         .#####     ########      ####      #      #######      ####    #####      #####*       ######                  
                      ##  #####    .:    -#########        #####          *##########    ####    #####   ###########   ##########                
                     ##  #####      .########               ##########           =###+   ####    #####  ####.    #    ###-     ###               
                    .#         ############                      #######   ###########   ####    #####  ####         ##############              
                    #     ################                          ####  ####    ####   ####    #####  ####     .    ###+                       
                   #################    ##                 #############  ####  .#####   #############   ###########  ###########                
                  #############  ##    ####                 +########.     ###### ####     #####*.####     #######       #######.                
                   #     ####     ##   ####                                                                                                      
                         ####    ####                                                                                                            
                         ####    ####                                                                                                            
                        ######                                                                                                                   
                        #####                                                                                                                    
                                                                                                                                                 
                                                                                                                                                 
        """;
        
        System.out.println(art);
        System.out.println("Desarollado por Juan Esteban Florez, Andres Felipe Pilonieta, Maria Camila Giraldo y Eric Samuel Vargas");
        System.out.println("Hecho en Java con la libreria Swing y el gestor de proyectos Maven");
    }
}
