
package saucepizza.saucepoo.recibo;

import java.io.File;
import java.net.URL;
import javax.swing.JOptionPane;
import saucepizza.saucepoo.logic.Pedido;
///# ImprimirFactura
///
///*Clase respondable de otrogar un metodo directo de generacion de factura para la interfaz*
///Utiliza la clase *GeneradorFactura* del mismo paquete.
///Emplea la clase *File* para manejo de archivos y URL para las rutas
///
///## Ejemplo de uso
///...
///Pedido pedidoActual = ...; //Pedido creado anteriormente
///Controladora controladora = ...; // Controladora (clase del paquete _logic_) creada anteriormente
///ImprimirFactura imprimir = new Imprimir();
///imprimir.generarfactura(pedidoActual,controladora);
///...
public class ImprimirFactura {
    ///*Crea las condiciones ideales para luego generar la factura*
    ///
    /// El método realiza los siguientes pasos:
    /// 1. Si no existe, crea un directorio _facturas_ donde se ejecuta el archivo jar
    /// 2. Define el nombre del PDF de salida como _"factura_ID.pdf"_.
    /// 3. Verifica si en los archivos del programa existe factura_pedido.jasper (el molde de la factura).
    /// 4. Llama a una instancia de _GeneradorFactura_ para usar el metodo _generarPDF(pedidoActual,rutaPDFrelativa)_.
    /// 5. Muestra mensajes de confirmación si todo ha salido como se esperaba
    /// ### Ejemplo
    /// ```
    /// imprimir.generarfactura(pedidoActual,controladora);
    /// ```
    ///@param pedidoActual El pedido con toda la información necesaria para generar la factura
    public void generarfactura(Pedido pedidoActual){
    try {
        File carpetaFacturas = new File("facturas");
        if (!carpetaFacturas.exists()) {
            carpetaFacturas.mkdirs();
        }

        
        String rutaPDFrelativa = "facturas/factura_" + pedidoActual.getId() + ".pdf";
        File archivoPDF = new File(rutaPDFrelativa);
        String rutaPDFabsoluta = archivoPDF.getAbsolutePath();

        URL jasperURL = getClass().getClassLoader()
            .getResource("saucepizza/saucepoo/reportes/factura_pedido.jasper");
        if (jasperURL == null) {
            String msg = "No se encuentra factura_pedido.jasper en el classpath.\n" +
                         "No se generó la factura.";
            System.out.println(msg);
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String rutaJasper = jasperURL.getPath();

        // Generar PDF
        GeneradorFactura generador = new GeneradorFactura();
        generador.generarPDF(pedidoActual, rutaPDFrelativa);

        // Mensajes de confirmación
        System.out.println("Ruta absoluta PDF: " + rutaPDFabsoluta);
        System.out.println("Ruta archivo Jasper: " + rutaJasper);
        JOptionPane.showMessageDialog(
            null,
            "Pedido registrado y factura generada:\n" +
            rutaPDFabsoluta + "\nArchivo Jasper: " + rutaJasper,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
            null,
            "Error al generar la factura: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }
    
    }
}
