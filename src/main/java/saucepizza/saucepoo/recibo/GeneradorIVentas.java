package saucepizza.saucepoo.recibo;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartUtils;
import saucepizza.saucepoo.logic.Ventas;
import saucepizza.saucepoo.logic.Producto;

public class GeneradorIVentas {
    
    public void ImprimirPDF(Ventas venta) {
        try {
            if (venta == null) {
                JOptionPane.showMessageDialog(null, 
                    "Error: Venta nula", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (venta.getCantidadVendida() == null || venta.getCantidadVendida().isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No hay datos de venta", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File carpetaReportesD = new File("reportes/dia");
            if (!carpetaReportesD.exists()) {
                carpetaReportesD.mkdirs();
            }
            
            String rutaPDFrelativa = "reportes/dia/informe_" + venta.getFecha() + ".pdf";
            File archivoPDF = new File(rutaPDFrelativa);
            String rutaPDFabsoluta = archivoPDF.getAbsolutePath();
            
            URL jasperURL = getClass().getClassLoader()
                .getResource("saucepizza/saucepoo/reportes/IVentas.jasper");
            if (jasperURL == null) {
                String msg = "No se encuentra IVentas.jasper en el classpath.";
                JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            generarPDF(venta, rutaPDFrelativa);
            
            JOptionPane.showMessageDialog(
                null,
                "Pedido registrado e informe generada:\n" + rutaPDFabsoluta,
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

    private void generarPDF(Ventas ventaGenerar, String ruta) {
        try {
            // 1. CREAR DATASET PARA EL GRÁFICO
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Producto producto : ventaGenerar.getCantidadVendida()) {
                dataset.addValue(producto.getCantidad(), "Cantidad", producto.getNombre());
            }
            
            // 2. CREAR GRÁFICO CON JFREECHART
            JFreeChart chart = ChartFactory.createBarChart(
                null,
                "Productos",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
            );
            
            // 3. CONVERTIR GRÁFICO A IMAGEN
            BufferedImage chartImage = chart.createBufferedImage(700, 300);
            
            // 4. PREPARAR PARÁMETROS
            Map<String, Object> textoMostrar = new HashMap<>();
            textoMostrar.put("P_FECHA", ventaGenerar.getFecha() != null ? 
                ventaGenerar.getFecha() : "N/A");
            textoMostrar.put("P_TOTAL", String.valueOf(ventaGenerar.getTotal()));
            textoMostrar.put("CHART_IMAGE", chartImage);
            
            // 5. CARGAR Y LLENAR REPORTE
            List<?> datos = ventaGenerar.getCantidadVendida();
            JRBeanCollectionDataSource fuenteDatos = new JRBeanCollectionDataSource(datos);
            
            InputStream reporteStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("saucepizza/saucepoo/reportes/IVentas.jasper");
            
            if (reporteStream == null) {
                JOptionPane.showMessageDialog(null, "No se encuentra IVentas.jasper");
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