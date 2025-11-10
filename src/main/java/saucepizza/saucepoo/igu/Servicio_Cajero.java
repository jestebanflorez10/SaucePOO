package saucepizza.saucepoo.igu;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import saucepizza.saucepoo.logic.Mesa;
import saucepizza.saucepoo.logic.Pedido;
import saucepizza.saucepoo.logic.Producto;
import saucepizza.saucepoo.logic.Producto_Servicio;
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
    private Pedido pedidoActual; private Ventas ventaActual; 
    private HashMap<Integer,Producto> Inventario;
    private String pizzaname;
    private Stack<Integer> historialProductoIds = new Stack<>();
    public Servicio_Cajero() {          
        this.pizzaname=SaucePOO.pizzeria;
        control.getPedidoServicio().crearTablasPedidos();
        Inventario=control.getProductoServicio().Inv_obtenerTodos();
        iniciarNuevoPedido();        
        initComponents();
        cargarImagenes();
        asignarProductosABotones();
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
        // Mapa global para asociar botón a id de producto
    private Map<JButton, Integer> botonIdMap = new HashMap<>();

    // Método para asignar productos a botones y ocultar los que no tengan producto
    private void asignarProductosABotones() {
        JButton[] botones = new JButton[] {
            btn_prod0, btn_prod1, btn_prod2, btn_prod3, btn_prod4, btn_prod5,
            btn_prod6, btn_prod7, btn_prod8, btn_prod9, btn_prod10, btn_prod11,
            btn_prod12, btn_prod13, btn_prod14
        };

        // Obtener productos completos con cantidad desde Inventario y archivo
        Producto_Servicio servicio = control.getProductoServicio();
        HashMap<Integer, Producto> productos = servicio.Inv_obtenerTodos();

        int i = 0;
        for (JButton boton : botones) {
            if (i < productos.size()) {
                // Obtener producto por orden pero sin asumir IDs consecutivos
                Integer idProducto = (Integer) productos.keySet().toArray()[i];
                Producto producto = productos.get(idProducto);

                if (producto != null && producto.getCantidad() > 0) {
                    // Guardar la asociación botón → id
                    botonIdMap.put(boton, producto.getId());
                    boton.setVisible(true);
                } else {
                    botonIdMap.remove(boton);
                    boton.setVisible(false);
                }
            } else {
                // No más productos disponibles, ocultar botones sobrantes
                botonIdMap.remove(boton);
                boton.setVisible(false);
            }
            i++;
        }
    }

    // Método que se llama en cada botón actionPerformed generico
    private void registrarProductoDesdeBoton(JButton boton) {
        Integer idProducto = botonIdMap.get(boton);
        if (idProducto != null) {
            registrarProducto(idProducto, 1);
        } else {
            // Opcional: mensaje o ignorar si botón sin ID
            System.out.println("Botón sin producto asignado");
        }
    }

    private void cargarImagenes() {
    JButton[] seleccion = new JButton[]{
        btn_prod0, btn_prod1, btn_prod2, btn_prod3, btn_prod4, btn_prod5, 
        btn_prod6, btn_prod7, btn_prod8, btn_prod9, btn_prod10, btn_prod11,
        btn_prod12, btn_prod13, btn_prod14
    };
    
    Iterator<Producto> it = Inventario.values().iterator();

        for (JButton boton : seleccion) {
            if (it.hasNext()) {
                Producto producto = it.next();
                if (producto != null) { // Validar que producto no sea null
                    BufferedImage img = producto.getImagen();

                    if (img != null) {
                        boton.setIcon(new ImageIcon(img));
                        boton.setVisible(true);
                    } else {
                        boton.setVisible(false);
                    }
                } else {
                    boton.setVisible(false);
                }
            } else {
                boton.setVisible(false);
            }
        }
    }


    private void registrarProducto(int id, int cantidad){        
        try{
        Producto prod = control.getProductoServicio().leer(id); //El producto de referencia
        //Inventario.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
        Producto inv = Inventario.get(id); //El inventario del producto obteenido a traves de una llave
        if(inv.getCantidad()>0){
        historialProductoIds.push(prod.getId());
        control.getPedidoServicio().agregarProductoAlPedido(pedidoActual, prod);        
        //inv.setCantidad(inv.getCantidad()-prod.getCantidad());
        //control.getProductoServicio().Inv_actualizar(inv);
        for (Producto p : Inventario.values()) {
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
        for (Producto p : Inventario.values()) {
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
        btn_prod0 = new javax.swing.JButton();
        btn_prod1 = new javax.swing.JButton();
        btn_prod8 = new javax.swing.JButton();
        btn_prod4 = new javax.swing.JButton();
        btn_prod2 = new javax.swing.JButton();
        btn_prod3 = new javax.swing.JButton();
        btn_prod5 = new javax.swing.JButton();
        btn_prod6 = new javax.swing.JButton();
        btn_prod7 = new javax.swing.JButton();
        btn_prod9 = new javax.swing.JButton();
        btn_prod10 = new javax.swing.JButton();
        btn_prod11 = new javax.swing.JButton();
        btn_prod12 = new javax.swing.JButton();
        btn_prod13 = new javax.swing.JButton();
        btn_prod14 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        btnLimpiarOrden = new javax.swing.JButton();
        btnQuitarUltimo = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedido = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

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
        jPanel3.setMaximumSize(new java.awt.Dimension(566, 503));
        jPanel3.setMinimumSize(new java.awt.Dimension(566, 503));

        btn_prod0.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod0.setText("Producto 1");
        btn_prod0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod0ActionPerformed(evt);
            }
        });

        btn_prod1.setBackground(new java.awt.Color(244, 240, 240));
        btn_prod1.setText("Producto 2");
        btn_prod1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod1ActionPerformed(evt);
            }
        });

        btn_prod8.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod8.setText("Producto 9");
        btn_prod8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod8ActionPerformed(evt);
            }
        });

        btn_prod4.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod4.setText("Producto 5");
        btn_prod4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod4ActionPerformed(evt);
            }
        });

        btn_prod2.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod2.setText("Producto 3");
        btn_prod2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod2ActionPerformed(evt);
            }
        });

        btn_prod3.setBackground(new java.awt.Color(244, 240, 240));
        btn_prod3.setText("Producto 4");
        btn_prod3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod3ActionPerformed(evt);
            }
        });

        btn_prod5.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod5.setText("Producto 6");
        btn_prod5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod5ActionPerformed(evt);
            }
        });

        btn_prod6.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod6.setText("Producto 7");
        btn_prod6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod6ActionPerformed(evt);
            }
        });

        btn_prod7.setBackground(new java.awt.Color(244, 240, 240));
        btn_prod7.setText("Producto 8");
        btn_prod7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod7ActionPerformed(evt);
            }
        });

        btn_prod9.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod9.setText("Producto 10");
        btn_prod9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod9ActionPerformed(evt);
            }
        });

        btn_prod10.setBackground(new java.awt.Color(244, 240, 240));
        btn_prod10.setText("Producto 11");
        btn_prod10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod10ActionPerformed(evt);
            }
        });

        btn_prod11.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod11.setText("Producto 12");
        btn_prod11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod11ActionPerformed(evt);
            }
        });

        btn_prod12.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod12.setText("Producto 13");
        btn_prod12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod12ActionPerformed(evt);
            }
        });

        btn_prod13.setBackground(new java.awt.Color(244, 240, 240));
        btn_prod13.setText("Producto 14");
        btn_prod13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod13ActionPerformed(evt);
            }
        });

        btn_prod14.setBackground(new java.awt.Color(240, 240, 240));
        btn_prod14.setText("Producto 15");
        btn_prod14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prod14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_prod0, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_prod1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_prod4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_prod2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_prod3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_prod5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btn_prod9, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_prod10, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_prod11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_prod12, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_prod13, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_prod14, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(btn_prod6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_prod7, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_prod8, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(35, 35, 35))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_prod0, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_prod2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_prod6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_prod9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_prod12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod13, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_prod14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Servicio");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(220, 220, 220)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnQuitarUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnLimpiarOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(436, 436, 436)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(280, 280, 280))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnQuitarUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(126, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel3)
                        .addGap(31, 31, 31)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(58, 58, 58))
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

    private void btn_prod0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod0ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod0ActionPerformed

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
        Iterator<Producto> it3 = Inventario.values().iterator();        
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
        Iterator<Producto> it3 = Inventario.values().iterator();        
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

    private void btn_prod1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod1ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod1ActionPerformed
     
    private void btn_prod8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod8ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod8ActionPerformed

    private void btn_prod4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod4ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod4ActionPerformed

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

    private void btn_prod2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod2ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod2ActionPerformed

    private void btn_prod3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod3ActionPerformed
       registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod3ActionPerformed

    private void btn_prod5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod5ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod5ActionPerformed

    private void btn_prod6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod6ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod6ActionPerformed

    private void btn_prod7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod7ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod7ActionPerformed

    private void btn_prod9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod9ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod9ActionPerformed

    private void btn_prod10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod10ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod10ActionPerformed

    private void btn_prod11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod11ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod11ActionPerformed

    private void btn_prod12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod12ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod12ActionPerformed

    private void btn_prod13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod13ActionPerformed
        registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod13ActionPerformed

    private void btn_prod14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prod14ActionPerformed
       registrarProductoDesdeBoton((JButton) evt.getSource());
    }//GEN-LAST:event_btn_prod14ActionPerformed

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
    private javax.swing.JButton btn_prod0;
    private javax.swing.JButton btn_prod1;
    private javax.swing.JButton btn_prod10;
    private javax.swing.JButton btn_prod11;
    private javax.swing.JButton btn_prod12;
    private javax.swing.JButton btn_prod13;
    private javax.swing.JButton btn_prod14;
    private javax.swing.JButton btn_prod2;
    private javax.swing.JButton btn_prod3;
    private javax.swing.JButton btn_prod4;
    private javax.swing.JButton btn_prod5;
    private javax.swing.JButton btn_prod6;
    private javax.swing.JButton btn_prod7;
    private javax.swing.JButton btn_prod8;
    private javax.swing.JButton btn_prod9;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaPedido;
    // End of variables declaration//GEN-END:variables
}
