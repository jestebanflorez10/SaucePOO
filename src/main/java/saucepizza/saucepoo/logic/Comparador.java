package saucepizza.saucepoo.logic;

import java.util.Comparator;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Comparador implements Comparator<String>, Serializable {
    private static final long serialVersionUID = 1L;

    // Declarar como transient para evitar error de serialización
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public int compare(String f1, String f2) {        
        LocalDate d1 = LocalDate.parse(f1, formatter);
        LocalDate d2 = LocalDate.parse(f2, formatter);                
        return d1.compareTo(d2);
    }
    
    // Método que se invoca después de deserializar para reinicializar atributos transient
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
}