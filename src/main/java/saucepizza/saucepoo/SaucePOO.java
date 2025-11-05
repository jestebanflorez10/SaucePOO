
package saucepizza.saucepoo;
import saucepizza.saucepoo.igu.SplashScreen;
//import saucepizza.saucepoo.igu.UtilidadesPedidos;
import saucepizza.saucepoo.logic.Producto;
//import saucepizza.saucepoo.logic.Ventas;
import saucepizza.saucepoo.logic.Empresa;
import saucepizza.saucepoo.logic.Controladora;
public class SaucePOO {
    public static String pizzeria = "Pizzeria Demo";
    public static void main(String[] args) {
        //Empresa pizzeria = new Empresa("Pizzeria Demo",1);
        Producto prod1 = new Producto("Pepperoni", 1000, 1, 0);
        Producto prod2= new Producto("Queso", 1500, 1, 1);
        Producto prod3 = new Producto("Carne", 1700, 1, 2);
        Producto prod4= new Producto("Soda", 500, 1, 3);
        Controladora helper = new Controladora();
        /*if(helper.getEmpresaServicio().leer(String.valueOf(1))==null){helper.getEmpresaServicio().crear(pizzeria);}
        
        System.out.println(pizzeria.getNombre());
        System.out.println(helper.getEmpresaServicio().leer(String.valueOf(1)).getNombre());*/
        helper.getProductoServicio().crear(prod1);
        helper.getProductoServicio().crear(prod2);
        helper.getProductoServicio().crear(prod3);
        helper.getProductoServicio().crear(prod4);   
        //helper.getVentasServicio().crear(venta);
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
