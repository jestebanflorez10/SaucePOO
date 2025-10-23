
package saucepizza.saucepoo.igu;

import java.text.SimpleDateFormat;
import java.util.Date;
public class UtilidadesPedidos {

    /**
     * Método para obtener la fecha y hora actual en formato
     * "dd/MM/yyyy - HH:mm:ss"
     */
    public static String obtenerFechaHoraActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Date ahora = new Date();
        return formato.format(ahora);
    }
    private boolean tieneMaximoUnPunto(String texto) {
        int contadorPuntos = 0;
        for (char c : texto.toCharArray()) {
            if (c == '.') {
                contadorPuntos++;
                if (contadorPuntos > 1) return false;
            }
        }
        return true;
    }
    
}

