package saucepizza.saucepoo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import saucepizza.saucepoo.igu.SplashScreen;
import saucepizza.saucepoo.logic.Producto;
import saucepizza.saucepoo.logic.Controladora;
import saucepizza.saucepoo.logic.Mesa;

public class SaucePOO {
    public static String pizzeria = "Pizzeria Demo";
    private static Controladora control = new Controladora();

    public static void main(String[] args) {
        try {
            control.getProductoServicio().Inv_inicializarBase();

            crearProductoPorDefecto();
            crearMesaPorDefecto();

        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            inicio_exitoso();
        }
    }

        private static void crearProductoPorDefecto() {
            try {
            ArrayList<Producto> productos = control.getProductoServicio().obtenerTodos();
            if (productos.isEmpty()) {
                System.out.println("Creando producto por defecto...");

                BufferedImage imagen = ImageIO.read(
                    SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demo0.png")
                );

                Producto pizza = new Producto("Pizza Demo", 1000, 1, 0);
                pizza.setImagen(imagen);

                int idProducto = control.getProductoServicio().Inv_crear(pizza);
                pizza.setId(idProducto);
                control.getProductoServicio().crear(pizza);

                pizza.setCantidad(1);
                control.getProductoServicio().Inv_actualizar(pizza);

                System.out.println("Producto por defecto creado con ID: " + idProducto);
            } else {
                System.out.println("Base de datos ya contiene " + productos.size() + " producto(s). Modo demostración desactivado.");
            }
        } catch (Exception e) {
            System.err.println("Error al crear producto por defecto: " + e.getMessage());
        }
    }

    private static void crearMesaPorDefecto() {
        try {
            ArrayList<Mesa> mesas = control.getMesasServicio().obtenerTodos();

            if (mesas.isEmpty()) {
                System.out.println("Creando mesas por defecto...");

                BufferedImage imagenMesa = ImageIO.read(
                    SaucePOO.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demomesa0.png")
                );

                Mesa[] mesasDemo = new Mesa[6];
                for (int i = 0; i < 6; i++) {
                    mesasDemo[i] = new Mesa(i, "Libre", 0);
                    mesasDemo[i].setImagen(imagenMesa);
                    control.getMesasServicio().Crear(mesasDemo[i]);
                }
                System.out.println("Mesas por defecto creadas");
            } else {
                System.out.println("Base de datos ya contiene " + mesas.size() + " mesa(s). Modo demostración desactivado.");
            }
        } catch (Exception e) {
            System.err.println("Error al crear mesa por defecto: " + e.getMessage());
        }
        }

        public static void inicio_exitoso() {
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
            System.out.println("Desarrollado por Juan Esteban Florez, Andres Felipe Pilonieta, Maria Camila Giraldo y Eric Samuel Vargas");
            System.out.println("Hecho en Java con la libreria Swing y el gestor de proyectos Maven");
            System.out.println("2025-II by Sauce Team");

            SplashScreen inicio = new SplashScreen();
            inicio.setLocationRelativeTo(null);
            inicio.setVisible(true);
        }
}
