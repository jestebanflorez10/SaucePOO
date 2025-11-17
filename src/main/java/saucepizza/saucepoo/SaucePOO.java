
package saucepizza.saucepoo;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
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
        try{
            FileReader frp = new FileReader("productos/producto0.producto");//buscar si minimo existe un producto
            FileReader frm = new FileReader("mesa/mesa0.mesa");//buscar si minimo existe un producto            
        }catch(FileNotFoundException ep){
            System.out.println("Asuma que los archivos desaparecieron");
            System.out.println("Tome aire y sientase feliz");
            //Cree una mesa y un producto "demo"
            try{
            BufferedImage imagen1 = ImageIO.read(SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demo0.png"));
            BufferedImage mesaimagen = ImageIO.read(SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demomesa0.png"));
            Producto pizza = new Producto("Pizza", 1000, 1, 0);
            Mesa mesa1 = new Mesa(0, "Libre", 0); //temporal 0: sin pedido asignado
            mesa1.setImagen(mesaimagen);
            pizza.setImagen(imagen1);
            
            //llame a la controladora
            Controladora control = new Controladora();
            control.getMesasServicio().Crear(mesa1);
            control.getProductoServicio().Inv_inicializarBase();
            int id = control.getProductoServicio().Inv_crear(pizza);
            pizza.setId(id);
            control.getProductoServicio().crear(pizza);
            
            //Configuraciones perzonalizadas
            pizza.setCantidad(1);
            control.getProductoServicio().Inv_actualizar(pizza);
            }catch(Exception e){
                System.out.println("Definitivamente algo salio mal");
                System.err.println(e.getMessage());
            }
        } finally{
            inicio_exitoso();
        }
    }
    public static void inicio_exitoso(){
            
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
        System.out.println("2025-II by Sauce Team");
        SplashScreen inicio = new SplashScreen();
        inicio.setLocationRelativeTo(null);
        inicio.setVisible(true);
    }
}
