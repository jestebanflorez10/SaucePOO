
package saucepizza.saucepoo;
import saucepizza.saucepoo.igu.SplashScreen;
//import saucepizza.saucepoo.igu.UtilidadesPedidos;
import saucepizza.saucepoo.logic.Producto;
//import saucepizza.saucepoo.logic.Ventas;
import saucepizza.saucepoo.logic.Controladora;
import saucepizza.saucepoo.logic.Mesa;

public class SaucePOO {
    public static String pizzeria = "Pizzeria Demo";
    public static void main(String[] args) {
        //Empresa pizzeria = new Empresa("Pizzeria Demo",1);
        
        Producto prod1 = new Producto("Pepperoni", 1000, 1, 0);
        Producto prod2= new Producto("Queso", 1500, 1, 1);
        Producto prod3 = new Producto("Carne", 1700, 1, 2);
        Producto prod4= new Producto("Soda", 500, 1, 3);
        Controladora helper = new Controladora();
        try {
            helper.getProductoServicio().Inv_inicializarBase();
            int id = helper.getProductoServicio().Inv_crear(prod4);  // Retorna ID si existe o inserta si no
            Mesa mesa = new Mesa(2, "Libre", 2250); //temporal
    
            /*Object[] mesas = {"mesa 1", "mesa 2", "mesa 3", "mesa 4", "mesa 5", "mesa 6"};
            JComboBox combo = new JComboBox(mesas);*/
            //combo.setSelectedIndex(1);
            //JOptionPane.showMessageDialog(null, combo, "Escoje una mesa", JOptionPane.PLAIN_MESSAGE);
            
            helper.getMesasServicio().Crear(mesa);
            helper.getMesasServicio().actualizar(mesa);
            prod4.setId(id);
            prod4.setCantidad(4);
            helper.getProductoServicio().Inv_actualizar(prod4);  // Actualiza la cantidad del registro existente
            } catch(Exception e) {
             System.out.println(e.getMessage());
            }
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
