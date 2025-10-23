package saucepizza.saucepoo.igu;


import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import static saucepizza.saucepoo.igu.UtilidadesPedidos.*;
import saucepizza.saucepoo.logic.Controladora;
import saucepizza.saucepoo.logic.Pedido;
import saucepizza.saucepoo.logic.Producto;
import saucepizza.saucepoo.recibo.ImprimirFactura;
/**
 *
 * @author EQUIPO
 */
public class Servicio_Cajero extends javax.swing.JFrame {
    private javax.swing.table.DefaultTableModel modeloTablaPedido;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Servicio_Cajero.class.getName());
    private Controladora control = new Controladora();
    private Pedido pedidoActual;
    private Stack<Integer> historialProductoIds = new Stack<>();
    public Servicio_Cajero() {
        control.getPedidoServicio().crearTablasPedidos();
        iniciarNuevoPedido();        
        initComponents();
        try {
            this.setIconImage(new ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/logo.png")).getImage());
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }
        modeloTablaPedido = new javax.swing.table.DefaultTableModel(
        new Object[]{"ID", "Producto", "Cantidad", "Precio", "Importe"}, 0
        );
        tablaPedido.setModel(modeloTablaPedido);
        
    }
    private void actualizarTablaPedido() {
    modeloTablaPedido.setRowCount(0); // Limpia la tabla
    for (Producto p : pedidoActual.getListaProductos()) {
        modeloTablaPedido.addRow(new Object[]{
            p.getId(),
            p.getNombre(),
            p.getCantidad(),
            p.getPrecioUnitario(),
            p.getCantidad() * p.getPrecioUnitario()
                });
            }
        }

    private void iniciarNuevoPedido() {
        pedidoActual = control.getPedidoServicio().crearPedido(" ", // crea método para obtener fecha actual como String    // crea método para generar un ID único
        "Sauce Pizza", //Nombre del local
        "Cliente");  //Un cliente predeterminado, se asignara uno despues
        }
    

    private void actualizarVistaTotales() {
            control.getPedidoServicio().actualizarTotales(pedidoActual);
            jLabel4.setText(String.format("Total: %.2f", pedidoActual.getTotal()));
    }
    
    private void efectivo(){
    JTextField textField = new JTextField();

        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
    
    // Método auxiliar para validar que solo haya un punto decimal en la cadena resultante
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
    
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string.matches("[0-9.,]*")) {
            // Construir la cadena resultante tras insertar el nuevo texto
            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder sb = new StringBuilder(textoActual);
            sb.insert(offset, string);
            
            // Reemplazamos comas por puntos (si usas coma como decimal)
            String textoValidado = sb.toString().replace(',', '.');
            
            if (tieneMaximoUnPunto(textoValidado)) {
                super.insertString(fb, offset, string, attr);
            }
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("[0-9.,]*")) {
            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder sb = new StringBuilder(textoActual);
            sb.replace(offset, offset + length, text);
            
            String textoValidado = sb.toString().replace(',', '.');
            
            if (tieneMaximoUnPunto(textoValidado)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
     });

        int result = JOptionPane.showConfirmDialog(null, textField, "Ingrese el efectivo", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String exchange = textField.getText().replace(',', '.');
            if (exchange.isBlank()) {
                pedidoActual.setEfectivo(0);
            } else {
                pedidoActual.setEfectivo(Double.parseDouble(exchange));
            }
            System.out.println("Entrada: " + exchange);
        }
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedido = new javax.swing.JTable();
        btnQuitarUltimo = new javax.swing.JButton();
        btnLimpiarOrden = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(227, 40, 32));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/business.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/business.png"))); // NOI18N

        jButton1.setBackground(new java.awt.Color(240, 240, 240));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(227, 40, 32));
        jButton1.setText("SERVICIO");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 40, 32)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(240, 240, 240));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(227, 40, 32));
        jButton2.setText("MESAS");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(240, 240, 240));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton9.setForeground(new java.awt.Color(227, 40, 32));
        jButton9.setText("SALIR");
        jButton9.setAutoscrolls(true);
        jButton9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(18, 18, 18))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Servicio");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Total");

        jButton4.setBackground(new java.awt.Color(240, 240, 240));
        jButton4.setText("Pizza Pepperoni");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(244, 240, 240));
        jButton5.setText("Pizza Queso");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(240, 240, 240));
        jButton6.setText("Pizza Carne ");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(240, 240, 240));
        jButton7.setText("Soda");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(227, 40, 32));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Pagar");
        jButton8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        tablaPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaPedido);

        btnQuitarUltimo.setBackground(new java.awt.Color(240, 240, 240));
        btnQuitarUltimo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnQuitarUltimo.setForeground(new java.awt.Color(227, 40, 32));
        btnQuitarUltimo.setText("Deshacer");
        btnQuitarUltimo.setAutoscrolls(true);
        btnQuitarUltimo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btnQuitarUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarUltimoActionPerformed(evt);
            }
        });

        btnLimpiarOrden.setBackground(new java.awt.Color(240, 240, 240));
        btnLimpiarOrden.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLimpiarOrden.setForeground(new java.awt.Color(227, 40, 32));
        btnLimpiarOrden.setText("Limpiar");
        btnLimpiarOrden.setAutoscrolls(true);
        btnLimpiarOrden.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btnLimpiarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarOrdenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(jButton6))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(jButton5))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(jButton7))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnQuitarUltimo, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(btnLimpiarOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(213, 213, 213))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton5))
                        .addGap(62, 62, 62)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6)
                            .addComponent(jButton7))
                        .addGap(42, 42, 42)
                        .addComponent(btnQuitarUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnLimpiarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnQuitarUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarUltimoActionPerformed
        if (!historialProductoIds.isEmpty()) {
        int idUltimo = historialProductoIds.pop();
        Producto aEliminar = null;
        for (Producto p : pedidoActual.getListaProductos()) {
            if (p.getId() == idUltimo) {
                if (p.getCantidad() > 1) {
                    p.setCantidad(p.getCantidad() - 1);
                } else {
                    aEliminar = p;
                }
                break; // Es importante salir del bucle aquí
            }
        }
        if (aEliminar != null) {
            pedidoActual.getListaProductos().remove(aEliminar);
        }
        actualizarVistaTotales();
        actualizarTablaPedido();
    } else {
        JOptionPane.showMessageDialog(this, "No hay productos para eliminar.");
    }
    }//GEN-LAST:event_btnQuitarUltimoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Producto prod = control.getProductoServicio().leer(0);//Producto("Pepperoni", 1000, 1, 0);
        historialProductoIds.push(prod.getId());
        control.getPedidoServicio().agregarProductoAlPedido(pedidoActual, prod);
        actualizarVistaTotales();
        actualizarTablaPedido();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    // Actualizar fecha y solicitar nombre de cliente
    pedidoActual.setFecha(obtenerFechaHoraActual());
    do{
    efectivo();
    if(pedidoActual.getTotal()>pedidoActual.getEfectivo()){
        JOptionPane.showMessageDialog(this,"El efectivo es inferior que el total a pagar","Revise",JOptionPane.INFORMATION_MESSAGE);
        }
    }while(pedidoActual.getTotal()>pedidoActual.getEfectivo());
    if (pedidoActual.getEfectivo() != 0) {
        pedidoActual.setCambio(pedidoActual.getEfectivo()-pedidoActual.getTotal());
        } 
    else {
        pedidoActual.setEfectivo(pedidoActual.getTotal());
        pedidoActual.setCambio(0);
        JOptionPane.showMessageDialog(this,"No se entrega cambio","Aviso",JOptionPane.INFORMATION_MESSAGE);}
    //Pedir el nombre del cliente o usar uno predeterminado
    String nombreCliente = JOptionPane.showInputDialog(this,"Ingrese el nombre del cliente:","Nombre del Cliente",JOptionPane.PLAIN_MESSAGE);
    if (nombreCliente != null && !nombreCliente.trim().isEmpty()) {pedidoActual.setNombreCliente(nombreCliente.trim());
    } else {
        pedidoActual.setNombreCliente("Cliente");
        JOptionPane.showMessageDialog(this,"Se asignó nombre por defecto: Cliente","Aviso",JOptionPane.INFORMATION_MESSAGE);
    }
    //Registrar en las bases de datos
    control.getPedidoServicio().registrarPedido(pedidoActual);
    //Generar la factura
    ImprimirFactura imprime = new ImprimirFactura();
    imprime.generarfactura(pedidoActual);
    // Actualizar interfaz para un nuevo pedido
    iniciarNuevoPedido();
    actualizarVistaTotales();
    actualizarTablaPedido();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Producto prod = control.getProductoServicio().leer(1); //Producto("Queso", 1500, 1, 1);
        historialProductoIds.push(prod.getId());
        control.getPedidoServicio().agregarProductoAlPedido(pedidoActual, prod);
        actualizarVistaTotales();
        actualizarTablaPedido();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Producto prod = control.getProductoServicio().leer(2); //Producto("Carne", 1700, 1, 2);
        historialProductoIds.push(prod.getId());
        control.getPedidoServicio().agregarProductoAlPedido(pedidoActual, prod);
        actualizarVistaTotales();
        actualizarTablaPedido();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Producto prod =  control.getProductoServicio().leer(3);//Producto("Soda", 500, 1, 3);
        historialProductoIds.push(prod.getId());
        control.getPedidoServicio().agregarProductoAlPedido(pedidoActual, prod);
        actualizarVistaTotales();
        actualizarTablaPedido();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        IniciodeSesion window = new IniciodeSesion();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btnLimpiarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarOrdenActionPerformed
        pedidoActual.getListaProductos().clear(); // Borra toda la lista de productos
        historialProductoIds.clear();
        actualizarVistaTotales();
        actualizarTablaPedido();
    }//GEN-LAST:event_btnLimpiarOrdenActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Servicio_Cajero().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLimpiarOrden;
    private javax.swing.JButton btnQuitarUltimo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaPedido;
    // End of variables declaration//GEN-END:variables
}
