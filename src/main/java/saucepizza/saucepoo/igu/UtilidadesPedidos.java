
package saucepizza.saucepoo.igu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class UtilidadesPedidos {

    // Para generar un ID incremental único en memoria
    private static final AtomicInteger contadorId = new AtomicInteger(0);

    /**
     * Método para obtener la fecha y hora actual en formato
     * "dd/MM/yyyy - HH:mm:ss"
     */
    public static String obtenerFechaHoraActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Date ahora = new Date();
        return formato.format(ahora);
    }

    /**
     * Método para generar un ID único para un pedido.
     * Alternativamente, implementar lógica para obtener max(id) + 1 desde la BD.
     */
    public static int generarIdUnico() {
        return contadorId.incrementAndGet();
    }
}

