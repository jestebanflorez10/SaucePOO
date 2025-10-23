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
/// # GeneradorFactura
/// 
/// Clase responsable de generar un archivo PDF de factura a partir de un pedido.
/// Utiliza JasperReports para crear, rellenar y exportar la factura.
///
/// ## Ejemplo de uso
/// 
/// ```
/// Pedido pedido = ...; // Pedido previamente configurado
/// GeneradorFactura gen = new GeneradorFactura();
/// gen.generarPDF(pedido, "salida/factura.pdf");
/// ```
///
/// ## Dependencias
/// - JasperReports (net.sf.jasperreports)
/// - BeanCollectionDataSource para la lista de productos
/// - Pedido como fuente principal de datos
///
/// ---
public class GeneradorFactura {
    /// Genera un archivo PDF de factura a partir de un pedido y lo guarda en la ruta indicada.
    ///
    /// El método realiza los siguientes pasos:
    /// 1. Prepara los parámetros de cabecera para la factura.
    /// 2. Crea el datasource con la lista de productos del pedido.
    /// 3. Carga la plantilla Jasper de la factura.
    /// 4. Rellena la plantilla con datos y exporta a PDF.
    /// 5. Intenta abrir el archivo PDF generado.
    ///
    /// Si ocurre algún error durante el proceso, se muestra un mensaje informativo al usuario.
    ///
    /// ### Ejemplo
    /// ```
    /// generador.generarPDF(pedido, "factura_001.pdf");
    /// ```
    ///@param pedido  El pedido con toda la información necesaria para generar la factura
    ///@param rutaSalida Ruta completa donde se guardará el archivo PDF generado
    ///@see Pedido
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
            parametros.put("P_CAMBIO", pedido.getCambio());
            parametros.put("P_EFECTIVO", pedido.getEfectivo());
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
