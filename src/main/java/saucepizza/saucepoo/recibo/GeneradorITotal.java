package saucepizza.saucepoo.recibo;


import java.awt.Color;
import java.awt.Desktop; //Acciones del escritorio
import java.awt.Font;
import java.awt.RenderingHints;

import java.io.File; //Manejo de archivos
import java.io.IOException; //Input and Output Exception
import java.io.InputStream; //Flujo de entrada
import java.net.URL; // Direcciones

//Datos
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane; //Para mostrar mensajes en pantalla

//JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/*import org.jfree.chart.ChartFactory; //Contiene metodos para crear tablas
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;*/


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import saucepizza.saucepoo.logic.Ventas;
import saucepizza.saucepoo.SaucePOO;
import saucepizza.saucepoo.igu.UtilidadesPedidos;
import saucepizza.saucepoo.logic.Comparador;
import saucepizza.saucepoo.logic.Producto;
import saucepizza.saucepoo.logic.Controladora;;

public class GeneradorITotal {
    
    public void ImprimirPDF(Controladora control) {
        try {
            if (control == null) {
                JOptionPane.showMessageDialog(null, 
                    "Error: Venta nula", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (control.getVentasServicio().obtenerTodos() == null || control.getVentasServicio().obtenerTodos().isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No hay datos de venta para generar informe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File carpetaReportesD = new File("reportes/total");
            if (!carpetaReportesD.exists()) {
                carpetaReportesD.mkdirs();
            }
            
            String rutaPDFrelativa = "reportes/total/informe_" + UtilidadesPedidos.obtenerFecha() + ".pdf";
            File archivoPDF = new File(rutaPDFrelativa);
            String rutaPDFabsoluta = archivoPDF.getAbsolutePath();
            
            URL jasperURL = getClass().getClassLoader()
                .getResource("saucepizza/saucepoo/reportes/InformeTotal.jasper");
            if (jasperURL == null) {
                String msg = "No se encuentra InformeTotal.jasper en el classpath.";
                JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            generarPDF(control, rutaPDFrelativa);
            
            JOptionPane.showMessageDialog(
                null,
                "Informe generado:\n" + rutaPDFabsoluta,
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
        }
    }

    private void generarPDF(Controladora control, String ruta) {
        try {
            
            BufferedImage imagen = null;
            try {
            imagen = ImageIO.read(
                getClass().getResourceAsStream("/saucepizza/saucepoo/igu/images/business.png")
                );
             } catch (IOException e) {
            e.printStackTrace();
            }
            
            // 4. PREPARAR PARÁMETROS
            Map<String, Object> textoMostrar = new HashMap<>();
            textoMostrar.put("LOGO_IMAGE", imagen);
            textoMostrar.put("P_EMPRESA",SaucePOO.pizzeria);
            textoMostrar.put("P_FECHAH", UtilidadesPedidos.obtenerFecha());
            // 5. CARGAR Y LLENAR REPORTE
            List<Ventas> datos = control.getVentasServicio().obtenerTodos();
            datos.sort(Comparator.comparing(Ventas::getFecha,new Comparador()));
            JRBeanCollectionDataSource fuenteDatos = new JRBeanCollectionDataSource(datos);
            
            InputStream reporteStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("saucepizza/saucepoo/reportes/InformeTotal.jasper");
            
            if (reporteStream == null) {
                JOptionPane.showMessageDialog(null, "No se encuentra InformeTotal.jasper");
                return;                
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                reporteStream, textoMostrar, fuenteDatos
            );
            
            // 6. EXPORTAR A PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
            
            System.out.println("PDF generado en: " + ruta);
            
            // 7. ABRIR PDF
            try {
                File archivoPDF = new File(ruta);
                if (archivoPDF.exists()) {
                    Desktop.getDesktop().open(archivoPDF);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "No se pudo abrir el PDF: " + e.getMessage());
            }            
        
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error al generar factura: " + e.getMessage());
        }
    }
}