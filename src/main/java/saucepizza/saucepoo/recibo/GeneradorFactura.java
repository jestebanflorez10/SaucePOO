package saucepizza.saucepoo.recibo;

import java.awt.Desktop;
import java.io.File;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import saucepizza.saucepoo.logic.Pedido;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class GeneradorFactura {

    public void generarPDF(Pedido pedido, String rutaSalida) {
        try {
            // 1. Preparar parámetros (datos de cabecera)
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("P_ID", pedido.getId());
            parametros.put("P_FECHA", pedido.getFecha());
            parametros.put("P_EMPRESA", pedido.getNombreEmpresa());
            parametros.put("P_CLIENTE", pedido.getNombreCliente());
            parametros.put("P_SUBTOTAL", pedido.getSubTotal());
            parametros.put("P_IMPUESTOS", pedido.getImpuestos());
            parametros.put("P_TOTAL", pedido.getTotal());

            // 2. Preparar datasource (lista de productos)
            JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(pedido.getListaProductos());

            // 3. Cargar el reporte compilado (.jasper)
            InputStream testJasper = Thread.currentThread()
              .getContextClassLoader()
                .getResourceAsStream("saucepizza/saucepoo/reportes/factura_pedido.jasper");
            System.out.println("¿Se encontró el jasper? " + (testJasper != null));

            InputStream reporteStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("saucepizza/saucepoo/reportes/factura_pedido.jasper");
            if (reporteStream == null) {
                JOptionPane.showMessageDialog(null,
                    "No se encuentra factura_pedido.jasper en el classpath.");
                return;
            }

            // 4. Llenar el reporte con datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                reporteStream, parametros, dataSource);

            // 5. Exportar a PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaSalida);

            JOptionPane.showMessageDialog(null,
                "Factura generada correctamente:\n" + rutaSalida); 
            try {
            File archivoPDF = new File(rutaSalida);
                 if (archivoPDF.exists()) {
                Desktop.getDesktop().open(archivoPDF);
             }
            } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el PDF: " + e.getMessage());
            }
            
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null,
                "Error al generar factura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
