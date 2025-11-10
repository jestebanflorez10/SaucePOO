
package saucepizza.saucepoo.logic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
public class ImagenUtilidades implements Serializable {
    public static byte[] bufferedImageToBytes(BufferedImage imagen) {
        if (imagen == null) return null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage bytesToBufferedImage(byte[] bytes) {
        if (bytes == null) {
            return null; // Retorna null si no hay bytes
        }
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

