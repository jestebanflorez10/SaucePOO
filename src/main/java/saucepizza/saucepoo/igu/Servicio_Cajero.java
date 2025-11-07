package saucepizza.saucepoo.igu;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import saucepizza.saucepoo.SaucePOO;
import static saucepizza.saucepoo.igu.UtilidadesPedidos.*;
import saucepizza.saucepoo.logic.Controladora;
import saucepizza.saucepoo.logic.Empresa;
import saucepizza.saucepoo.logic.Mesa;
import saucepizza.saucepoo.logic.Pedido;
import saucepizza.saucepoo.logic.Producto;
import saucepizza.saucepoo.logic.Ventas;
import saucepizza.saucepoo.recibo.ImprimirFactura;
/**
 *
 * @author EQUIPO
 */
public class Servicio_Cajero extends javax.swing.JFrame {
    private javax.swing.table.DefaultTableModel modeloTablaPedido;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Servicio_Cajero.class.getName());
    private Controladora control = new Controladora();
    private Pedido pedidoActual; private Ventas ventaActual; private ArrayList<Producto> Inventario;
    private String pizzaname;
    private Stack<Integer> historialProductoIds = new Stack<>();
    public Servicio_Cajero() {          
        this.pizzaname=SaucePOO.pizzeria;
        control.getPedidoServicio().crearTablasPedidos();
        Inventario=control.getProductoServicio().Inv_obtenerTodos();
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
    private void registrarProducto(int id, int cantidad){        
        try{
        Producto prod = control.getProductoServicio().leer(id); //El producto de referencia
        Inventario.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
        Producto inv = Inventario.get(id-1); //El inventario del producto
        if(inv.getCantidad()>0){
        historialProductoIds.push(prod.getId());
        control.getPedidoServicio().agregarProductoAlPedido(pedidoActual, prod);        
        //inv.setCantidad(inv.getCantidad()-prod.getCantidad());
        //control.getProductoServicio().Inv_actualizar(inv);
        for (Producto p : Inventario) {
            if (p.getId() == id) {
                p.setCantidad(p.getCantidad() - cantidad);
                break;
            }
         }        
        
        } else{
        JOptionPane.showMessageDialog(this, "Recarge el inventario del producto", "No hay stock", JOptionPane.ERROR_MESSAGE);
        }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        actualizarVistaTotales();
        actualizarTablaPedido();
    }
    private void eliminarProducto(int id, int cantidad){        
        try{
        //control.getProductoServicio().Inv_actualizar(inv);   
        for (Producto p : Inventario) {
            if (p.getId() == id) {
                p.setCantidad(p.getCantidad() + cantidad);
                break;
            }
         } 
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        actualizarVistaTotales();
        actualizarTablaPedido();
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
        Inventario = control.getProductoServicio().Inv_obtenerTodos();
        ventaActual = control.getVentasServicio().leer(UtilidadesPedidos.obtenerFecha());
        if(ventaActual==null){
        ventaActual = new Ventas(obtenerFecha());
        control.getVentasServicio().crear(ventaActual);
        }
        pedidoActual = control.getPedidoServicio().crearPedido("Fecha Predeterminada",this.pizzaname, "Cliente Predeterminado");  
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
                pedidoActual.setEfectivo(pedidoActual.getTotal());
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
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        btnQuitarUltimo = new javax.swing.JButton();
        btnLimpiarOrden = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedido = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(18, 18, 18))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Servicio");

        jButton4.setBackground(new java.awt.Color(240, 240, 240));
        jButton4.setText("Producto 1");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(244, 240, 240));
        jButton5.setText("Producto 2");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(240, 240, 240));
        jButton6.setText("Producto 9");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(240, 240, 240));
        jButton7.setText("Producto 5");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

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

        jButton10.setBackground(new java.awt.Color(240, 240, 240));
        jButton10.setText("Producto 3");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(244, 240, 240));
        jButton11.setText("Producto 4");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(240, 240, 240));
        jButton12.setText("Producto 6");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(240, 240, 240));
        jButton13.setText("Producto 7");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(244, 240, 240));
        jButton14.setText("Producto 8");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(240, 240, 240));
        jButton15.setText("Producto 10");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(244, 240, 240));
        jButton16.setText("Producto 11");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(240, 240, 240));
        jButton17.setText("Producto 12");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(240, 240, 240));
        jButton18.setText("Producto 13");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(244, 240, 240));
        jButton19.setText("Producto 14");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(240, 240, 240));
        jButton20.setText("Producto 15");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnQuitarUltimo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLimpiarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(35, 105, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3)
                .addGap(52, 52, 52)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnQuitarUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnLimpiarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

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

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Total");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    eliminarProducto(p.getId(),1);
                } else {
                    aEliminar = p;
                }
                break; // Es importante salir del bucle aquí
            }
        }
        if (aEliminar != null) {
            pedidoActual.getListaProductos().remove(aEliminar);
            eliminarProducto(aEliminar.getId(),1);
        }
        actualizarVistaTotales();
        actualizarTablaPedido();
    } else {
        JOptionPane.showMessageDialog(this, "No hay productos para eliminar.");
    }
    }//GEN-LAST:event_btnQuitarUltimoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        registrarProducto(1,1);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    if(pedidoActual.getListaProductos().isEmpty()){
        JOptionPane.showMessageDialog(this,"Agregue productos antes de realizar el pago","No se puede completar su pedido",JOptionPane.ERROR_MESSAGE);
    } else {
     // Actualizar fecha y solicitar nombre de cliente
    pedidoActual.setFecha(obtenerFechaHoraActual());
    do{
    efectivo();
    if(pedidoActual.getTotal()>pedidoActual.getEfectivo()){
        JOptionPane.showMessageDialog(this,"El efectivo es inferior que el total a pagar","Revise",JOptionPane.INFORMATION_MESSAGE);
        }
    }while(pedidoActual.getTotal()>pedidoActual.getEfectivo());
    if ((pedidoActual.getEfectivo() - pedidoActual.getTotal())!= 0) {
        pedidoActual.setCambio(pedidoActual.getEfectivo() - pedidoActual.getTotal());
        } 
    else {
        //pedidoActual.setEfectivo(pedidoActual.getTotal());
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
    
    //Registrar pedido en archivos    
    ventaActual.agregarPedido(pedidoActual);    
    control.getVentasServicio().actualizar(ventaActual);
      
    Iterator<Mesa> consulta = control.getMesasServicio().obtener("Libre").iterator();
    ArrayList<String> inspeccion = new ArrayList<>(); Object[] mesas;
    while(consulta.hasNext()){
        inspeccion.add(consulta.next().ObtenerNombre());        
    }    
    if(!inspeccion.isEmpty()){
        mesas=inspeccion.toArray();
        JComboBox combo = new JComboBox(mesas);
        combo.setSelectedIndex(0);
        JOptionPane.showMessageDialog(null, combo, "Escoje una mesa", JOptionPane.PLAIN_MESSAGE);
        Mesa mesa = control.getMesasServicio().leer(Integer.parseInt(combo.getSelectedItem().toString().replaceFirst("Mesa ", "")));
        mesa.setEstado("Pendiente");
        mesa.setIdPedido(pedidoActual.getId());
        control.getMesasServicio().actualizar(mesa); 
        try{
        Iterator<Producto> it3 = Inventario.iterator();        
        while(it3.hasNext()){
        control.getProductoServicio().Inv_actualizar(it3.next());}
        }
        catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
        }
        
        //Generar la factura
        ImprimirFactura imprime = new ImprimirFactura();
        imprime.generarfactura(pedidoActual,mesa.ObtenerNombre());}
    else {
        JOptionPane.showMessageDialog(null,"No hay mesas disponibles, la entrega sera para llevar","Advertencia: Sin mesas libres",JOptionPane.INFORMATION_MESSAGE);
        ImprimirFactura imprime = new ImprimirFactura();
        imprime.generarfactura(pedidoActual,"Para llevar");
        System.out.println(Inventario);
        try{
        Iterator<Producto> it3 = Inventario.iterator();        
        while(it3.hasNext()){
        control.getProductoServicio().Inv_actualizar(it3.next());}
        }
        catch(Exception e){
                System.out.println(e.getMessage());
        }
    }
    
    // Actualizar interfaz para un nuevo pedido
    iniciarNuevoPedido();
    actualizarVistaTotales();
    actualizarTablaPedido();
    }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        registrarProducto(2,1);
    }//GEN-LAST:event_jButton5ActionPerformed
     
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        IniciodeSesion window = new IniciodeSesion();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btnLimpiarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarOrdenActionPerformed
        Iterator<Producto> it2 = pedidoActual.getListaProductos().iterator();
        while(it2.hasNext()){
        Producto p = it2.next();
        eliminarProducto(p.getId(),p.getCantidad());
        }
        pedidoActual.getListaProductos().clear(); // Borra toda la lista de productos
        historialProductoIds.clear();
        actualizarVistaTotales();
        actualizarTablaPedido();
    }//GEN-LAST:event_btnLimpiarOrdenActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        registrarProducto(3,1);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
       registrarProducto(4,1);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

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
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
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
