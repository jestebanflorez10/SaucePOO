package saucepizza.saucepoo.igu;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import saucepizza.saucepoo.logic.Controladora;
import saucepizza.saucepoo.logic.Producto;
/*
 *
 * @author juane
 */
public class Productos_Administrador extends javax.swing.JFrame {
    private String pizzaname;
    private Controladora control = new Controladora();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Productos_Administrador.class.getName());
    private Producto retorno = null;
    public Producto getRetorno(){return this.retorno;}
    /**
     * Creates new form Gestion_Administrador
     */
    public Productos_Administrador(Producto p) {
        initComponents();
        try {
            this.setIconImage(new ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/logo.png")).getImage());
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }
        this.retorno= p;
        ftxt_precio.setText(String.valueOf(p.getPrecioUnitario()));
        txt_nombre.setText(p.getNombre());
        img_producto.setIcon(new ImageIcon(p.getImagen()));
    }
    
    public static BufferedImage iconToBufferedImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            Image image = ((ImageIcon) icon).getImage();
            if (image instanceof BufferedImage) {
                return (BufferedImage) image;
            } else {
                BufferedImage bufferedImage = new BufferedImage(
                        icon.getIconWidth(),
                        icon.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                icon.paintIcon(null, g2d, 0, 0);
                g2d.dispose();
                return bufferedImage;
            }
        } else {
            BufferedImage bufferedImage = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            icon.paintIcon(null, g2d, 0, 0);
            g2d.dispose();
            return bufferedImage;
        }
    }

        private BufferedImage imagenFinal = null;
        private void seleccionarYProcesarImagen() {
        // Crear el selector de archivos
        JFileChooser fileChooser = new JFileChooser();

        // Filtrar solo archivos de imagen
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Archivos de imagen (*.jpg, *.jpeg, *.png, *.gif, *.bmp)", 
            "jpg", "jpeg", "png", "gif", "bmp"
        );
        fileChooser.setFileFilter(imageFilter);

        // Mostrar el diálogo de selección
        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            try {
                // 1. Leer la imagen
                BufferedImage imagenOriginal = ImageIO.read(archivoSeleccionado);

                if (imagenOriginal == null) {
                    JOptionPane.showMessageDialog(null, "El archivo no es una imagen válida", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. Redimensionar a 160x80
                BufferedImage imagenRedimensionada = redimensionarImagen(imagenOriginal, 160, 80);

                // 3. Convertir a PNG (aunque BufferedImage ya lo soporta)
                imagenFinal = new BufferedImage(160, 80, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = imagenFinal.createGraphics();
                g2d.drawImage(imagenRedimensionada, 0, 0, null);
                g2d.dispose();

                // 4. (Opcional) Mostrar la imagen en un JLabel para preview
                ImageIcon icon = new ImageIcon(imagenFinal);
                img_producto.setIcon(icon);

                JOptionPane.showMessageDialog(null, "Imagen procesada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al procesar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Método para redimensionar manteniendo proporción y recortando
    private BufferedImage redimensionarImagen(BufferedImage original, int anchoDeseado, int altoDeseado) {
        // Obtener dimensiones originales
        int anchoOriginal = original.getWidth();
        int altoOriginal = original.getHeight();

        // Calcular factor de escala para que la imagen cubra todo el área 160x80
        double escalaAncho = (double) anchoDeseado / anchoOriginal;
        double escalaAlto = (double) altoDeseado / altoOriginal;
        double escala = Math.max(escalaAncho, escalaAlto); // Usar la escala mayor para cubrir todo

        // Calcular nuevas dimensiones después de la escala
        int nuevoAncho = (int) (anchoOriginal * escala);
        int nuevoAlto = (int) (altoOriginal * escala);

        // Redimensionar
        Image imagenEscalada = original.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
        BufferedImage imagenRedimensionada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenRedimensionada.createGraphics();
        g2d.drawImage(imagenEscalada, 0, 0, null);
        g2d.dispose();

        // Recortar al tamaño final 160x80 (desde el centro)
        int offsetX = (nuevoAncho - anchoDeseado) / 2;
        int offsetY = (nuevoAlto - altoDeseado) / 2;

        BufferedImage imagenRecortada = imagenRedimensionada.getSubimage(
            offsetX, offsetY, anchoDeseado, altoDeseado
        );    
      return imagenRecortada;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        img_producto = new javax.swing.JLabel();
        btn_cargarimg = new javax.swing.JButton();
        ftxt_precio = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editor de Usuarios [Administrador]");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 573));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(300, 300, 300)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Administracion de Productos");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton7.setBackground(new java.awt.Color(227, 40, 32));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Guardar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(227, 40, 32));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Limpiar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(240, 240, 240));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(227, 40, 32));
        jButton5.setText("Cerrar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Nombre");

        txt_nombre.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_nombre.setBorder(null);
        txt_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreActionPerformed(evt);
            }
        });

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Precio");

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Imagen");

        img_producto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        img_producto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/demo0.png"))); // NOI18N

        btn_cargarimg.setBackground(new java.awt.Color(240, 240, 240));
        btn_cargarimg.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_cargarimg.setForeground(new java.awt.Color(227, 40, 32));
        btn_cargarimg.setText("Cargar Imagen");
        btn_cargarimg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cargarimgActionPerformed(evt);
            }
        });

        ftxt_precio.setBorder(null);
        ftxt_precio.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ftxt_precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftxt_precioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(jSeparator4))
                        .addGap(91, 91, 91)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftxt_precio)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(img_producto)
                        .addGap(57, 57, 57)
                        .addComponent(btn_cargarimg)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftxt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_cargarimg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(img_producto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel8)
                .addGap(81, 81, 81)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(436, 436, 436)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
        // Validar que el nombre no esté vacío
        String nuevoNombre = txt_nombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto no puede estar vacío", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que el precio sea válido
        String precioText = ftxt_precio.getText().trim();
        if (precioText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El precio no puede estar vacío", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio = Double.parseDouble(precioText);
        if (precio < 0) {
            JOptionPane.showMessageDialog(this, "El precio no puede ser negativo", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que haya una imagen
        if (img_producto.getIcon() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una imagen para el producto", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto inventario = control.getProductoServicio().Inv_leer(retorno.getNombre());
        int cantidad = inventario.getCantidad();

        BufferedImage bufferedImage = iconToBufferedImage(img_producto.getIcon());
        inventario.setImagen(bufferedImage);

        inventario.setPrecioUnitario(precio);
        inventario.setNombre(nuevoNombre);
        control.getProductoServicio().Inv_actualizar(inventario);

        retorno.setNombre(nuevoNombre);
        retorno.setPrecioUnitario(precio);
        retorno.setImagen(bufferedImage);
        control.getProductoServicio().actualizar(retorno);

        JOptionPane.showMessageDialog(this, "Producto actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el producto en la base de datos", "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        Producto p= this.retorno;
        ftxt_precio.setText(String.valueOf(p.getPrecioUnitario()));
        txt_nombre.setText(p.getNombre());
        img_producto.setIcon(new ImageIcon(p.getImagen()));
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btn_cargarimgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cargarimgActionPerformed
        seleccionarYProcesarImagen();
    }//GEN-LAST:event_btn_cargarimgActionPerformed

    private void ftxt_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftxt_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftxt_precioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        BufferedImage imagen1;
        try {
            imagen1 = ImageIO.read(Productos_Administrador.class.getResourceAsStream("/saucepizza/saucepoo/igu/images/demo0.png"));
            Producto pizza = new Producto("Pizza", 1000, 1, 0);
            pizza.setImagen(imagen1);
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> new Productos_Administrador(pizza).setVisible(true));
        } catch (IOException ex) {
            System.getLogger(Productos_Administrador.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cargarimg;
    private javax.swing.JFormattedTextField ftxt_precio;
    private javax.swing.JLabel img_producto;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField txt_nombre;
    // End of variables declaration//GEN-END:variables
}
