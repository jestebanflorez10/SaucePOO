package saucepizza.saucepoo.recibo;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class GestorFacturas {
    
    private static final String CARPETA_FACTURAS = "facturas";
    
    /**
     * Método para llamar desde Swing
     * Abre la factura por ID del pedido o muestra error
     * 
     * @param pedidoId ID del pedido (guardado en Mesa)
     */
    public void abrirFacturaPorId(int pedidoId) {
        File factura = obtenerFactura(pedidoId);
        
        if (factura != null && factura.exists()) {
            try {
                Desktop.getDesktop().open(factura);
                System.out.println("Factura abierta: " + factura.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error al abrir la factura:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                System.err.println("Error al abrir factura: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null,
                "No se encontró factura para el pedido #" + pedidoId + "\n\n" +
                "Ubicación esperada: " + CARPETA_FACTURAS + File.separator + "factura_" + pedidoId + ".pdf",
                "Factura no encontrada",
                JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Factura no encontrada para pedido: " + pedidoId);
        }
    }
    
    /**
     * Busca el archivo de factura
     */
    private File obtenerFactura(int pedidoId) {
        String nombreArchivo = CARPETA_FACTURAS + File.separator + "factura_" + pedidoId + ".pdf";
        return new File(nombreArchivo);
    }
}
