
package saucepizza.saucepoo.recibo;
//Para el archivo
import java.awt.Desktop;
import java.io.File;
//Para el JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//Manejo de archivos
import java.io.InputStream;
import java.net.URL;
//Para los datos
import java.util.HashMap;
import java.util.Map;
//Dialogos de error
import javax.swing.JOptionPane;
//Dato a mostrar
import saucepizza.saucepoo.logic.Ventas;

public class GeneradorIVentas {
    public void ImprimirPDF(Ventas venta){
    try{
        //Directorio de archivos reportesDiario
        File carpetaReportesD = new File("reportes/dia");
        if(!carpetaReportesD.exists()){
            carpetaReportesD.mkdirs();
        }
        String rutaPDFrelativa = "reportes/dia/informe_" + venta.getFecha()+ ".pdf";
        File archivoPDF = new File(rutaPDFrelativa);
        String rutaPDFabsoluta = archivoPDF.getAbsolutePath();
        URL jasperURL = getClass().getClassLoader()
            .getResource("saucepizza/saucepoo/reportes/IVentas.jasper");
        if (jasperURL == null) {
            String msg = "No se encuentra IVentas.jasper en el classpath.\n" +
                         "No se generó la factura.";
            System.out.println(msg);
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String rutaJasper = jasperURL.getPath();
        generarPDF(venta, rutaPDFrelativa);

        // Mensajes de confirmación
        System.out.println("Ruta absoluta PDF: " + rutaPDFabsoluta);
        System.out.println("Ruta archivo Jasper: " + rutaJasper);
        JOptionPane.showMessageDialog(
            null,
            "Pedido registrado e informe generada:\n" +
            rutaPDFabsoluta + "\nArchivo Jasper: " + rutaJasper,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        }catch (Exception ex) {
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
    private void generarPDF(Ventas ventaGenerar, String ruta){
        try{
            //Fuentes de datos diferente de tablas
            Map<String, Object> textoMostrar =new HashMap<>();
            textoMostrar.put("P_FECHA", ventaGenerar.getFecha());
            textoMostrar.put("P_TOTAL", ventaGenerar.getTotal());
            //Fuente de Tablas
            JRBeanCollectionDataSource fuenteDatos = new JRBeanCollectionDataSource(ventaGenerar.getCantidadVendida());
            
            //Conseguir el Jasper
            InputStream testJasper = Thread.currentThread()
              .getContextClassLoader()
                .getResourceAsStream("saucepizza/saucepoo/reportes/IVentas.jasper");
            System.out.println("¿Se encontró el jasper? " + (testJasper != null));

            InputStream reporteStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("saucepizza/saucepoo/reportes/IVentas.jasper");
            if (reporteStream == null) {
                JOptionPane.showMessageDialog(null,
                    "No se encuentra IVentas.jasper en el classpath.");
                return;                
            }
            //Uso: Informacion del jasper + Map del contenido a mostrar + Fuente de tabla de datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporteStream, textoMostrar, fuenteDatos);

            // Tambien puede dar JRException
            JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);

            JOptionPane.showMessageDialog(null,
                "Factura generada correctamente:\n" + ruta); 
            try {
            File archivoPDF = new File(ruta);
                 if (archivoPDF.exists()) {
                Desktop.getDesktop().open(archivoPDF);
             }
            } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el PDF: " + e.getMessage());
            }            
        
        }catch(JRException e){
            JOptionPane.showMessageDialog(null,
                "Error al generar factura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
