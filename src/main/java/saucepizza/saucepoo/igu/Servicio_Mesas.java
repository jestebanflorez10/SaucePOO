package saucepizza.saucepoo.igu;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import saucepizza.saucepoo.logic.Controladora;
import saucepizza.saucepoo.logic.Mesa;
import saucepizza.saucepoo.recibo.GestorFacturas;
/**
 *
 * @author EQUIPO
 */
public class Servicio_Mesas extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Servicio_Mesas.class.getName());
    private Controladora control = new Controladora();
    private HashMap<Integer, Mesa> local;
    public Servicio_Mesas() {
        local = control.getMesasServicio().obtenerTodosH();
        initComponents();
        cargarDatos();
        try {
            this.setIconImage(new ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/logo.png")).getImage());
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }
        
    }
    
        private void cargarDatos() { //Muestra imagen, panel y actualiza estado en ComboBox
        local = control.getMesasServicio().obtenerTodosH();
        JLabel[] seleccion = new JLabel[]{
            img_mesa1, img_mesa2, img_mesa3, img_mesa4, img_mesa5, img_mesa6 };
        JPanel[] paneles = new JPanel[]{
            jpl_mesa1, jpl_mesa2, jpl_mesa3, jpl_mesa4, jpl_mesa5, jpl_mesa6 };
        JComboBox[] combos = new JComboBox[]{
            cmb_mesa1, cmb_mesa2, cmb_mesa3, cmb_mesa4, cmb_mesa5, cmb_mesa6 };
        JPanel[] estados = new JPanel[]{
            jpl_estmesa1, jpl_estmesa2, jpl_estmesa3, jpl_estmesa4, jpl_estmesa5, jpl_estmesa6 };
        JButton[] actualizar = new JButton[]{btn_actmesa1,btn_actmesa2,btn_actmesa3,btn_actmesa4,
            btn_actmesa5, btn_actmesa6};
        JButton[] obtener = new JButton[]{btn_obtmesa1,btn_obtmesa2,btn_obtmesa3,btn_obtmesa4,
            btn_obtmesa5, btn_obtmesa6};
        Iterator<Mesa> it = local.values().iterator();
        for (int i = 0; i < seleccion.length; i++) {
            if (it.hasNext()) {
                Mesa m = it.next();
                if (m != null) { // Validar que mesa no sea null
                    BufferedImage img = m.getImagen();
                    if (img != null) {
                        seleccion[i].setIcon(new ImageIcon(img));
                        seleccion[i].setVisible(true);
                        paneles[i].setVisible(true);

                        // Actualizar ComboBox con el estado de la mesa
                        String estado = m.getEstado();
                        if (estado != null) {
                            combos[i].setSelectedItem(estado);
                            if(estado.equals("Libre")){
                                estados[i].setBackground(Color.GREEN);
                                actualizar[i].setEnabled(true);
                                obtener[i].setEnabled(false);
                            } else
                            if(estado.equalsIgnoreCase("Limpiar")){
                                estados[i].setBackground(Color.GRAY);
                                actualizar[i].setEnabled(true);
                                obtener[i].setEnabled(false);
                            } else
                            if(estado.equalsIgnoreCase("Ocupado")){
                                estados[i].setBackground(Color.RED);
                                actualizar[i].setEnabled(false);
                                obtener[i].setEnabled(true);
                            }
                        }
                        combos[i].setEnabled(true);
                    } else {
                        seleccion[i].setVisible(false);
                        paneles[i].setVisible(false);
                        combos[i].setEnabled(false);
                    }
                } else {
                    seleccion[i].setVisible(false);
                    paneles[i].setVisible(false);
                    combos[i].setEnabled(false);
                }
            } else {
                seleccion[i].setVisible(false);
                paneles[i].setVisible(false);
                combos[i].setEnabled(false);
            }
        }
    }

   private void modificarEstado(int mesaid, String estado){
       Mesa m = control.getMesasServicio().leer(mesaid);
       if(!estado.equalsIgnoreCase("Ocupado")){
        m.setEstado(estado);
        control.getMesasServicio().actualizar(m);
        cargarDatos();
       } else {
            JOptionPane.showMessageDialog(null, "El software selecciona el estado ocupado de forma automatica", "No se puede modificar una mesa a ", JOptionPane.INFORMATION_MESSAGE);
       }
       
   }
   private void cambiarEstado(int id){
    JComboBox[] combos = new JComboBox[]{
            cmb_mesa1, cmb_mesa2, cmb_mesa3, cmb_mesa4, cmb_mesa5, cmb_mesa6 };
    JButton[] actualizar = new JButton[]{btn_actmesa1,btn_actmesa2,btn_actmesa3,btn_actmesa4,
            btn_actmesa5, btn_actmesa6};
    JButton[] obtener = new JButton[]{btn_obtmesa1,btn_obtmesa2,btn_obtmesa3,btn_obtmesa4,
            btn_obtmesa5, btn_obtmesa6};
   String seleccion = combos[id].getSelectedItem().toString();
        if(seleccion.equals("Ocupado")){
            actualizar[id].setEnabled(false);
            obtener[id].setEnabled(true);
        } else {
            actualizar[id].setEnabled(true);
            obtener[id].setEnabled(false);
        }
   }
   private void obtenerRecibo(int idboton) {
    Mesa m = control.getMesasServicio().leer(idboton);
    
    if (m == null) {
        JOptionPane.showMessageDialog(this,
            "Error: Mesa no encontrada",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar que la mesa NO esté Libre
    if ("Libre".equalsIgnoreCase(m.getEstado())) {
        JOptionPane.showMessageDialog(this,
            "La mesa está libre. No hay pedido asociado.",
            "Mesa Vacía",
            JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    int idPedido = m.getIdPedido();
    
    // Validar que tenga un ID de pedido válido (diferente de 0 que significa "sin pedido")
    if (idPedido == 0) {
        JOptionPane.showMessageDialog(this,
            "Error: La mesa no tiene un pedido válido asociado.",
            "Pedido No Válido",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Abrir la factura con el ID del pedido (que inicia en 1 en SQL)
    GestorFacturas imprimir = new GestorFacturas();
    imprimir.abrirFacturaPorId(idPedido);
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
        jpl_mesa1 = new javax.swing.JPanel();
        img_mesa1 = new javax.swing.JLabel();
        lbl_mesa1 = new javax.swing.JLabel();
        cmb_mesa1 = new javax.swing.JComboBox<>();
        btn_obtmesa1 = new javax.swing.JButton();
        btn_actmesa1 = new javax.swing.JButton();
        jpl_estmesa1 = new javax.swing.JPanel();
        jpl_mesa2 = new javax.swing.JPanel();
        img_mesa2 = new javax.swing.JLabel();
        lbl_mesa2 = new javax.swing.JLabel();
        cmb_mesa2 = new javax.swing.JComboBox<>();
        btn_obtmesa2 = new javax.swing.JButton();
        btn_actmesa2 = new javax.swing.JButton();
        jpl_estmesa2 = new javax.swing.JPanel();
        jpl_mesa3 = new javax.swing.JPanel();
        img_mesa3 = new javax.swing.JLabel();
        lbl_mesa3 = new javax.swing.JLabel();
        cmb_mesa3 = new javax.swing.JComboBox<>();
        btn_obtmesa3 = new javax.swing.JButton();
        btn_actmesa3 = new javax.swing.JButton();
        jpl_estmesa3 = new javax.swing.JPanel();
        jpl_mesa4 = new javax.swing.JPanel();
        img_mesa4 = new javax.swing.JLabel();
        lbl_mesa4 = new javax.swing.JLabel();
        cmb_mesa4 = new javax.swing.JComboBox<>();
        btn_obtmesa4 = new javax.swing.JButton();
        btn_actmesa4 = new javax.swing.JButton();
        jpl_estmesa4 = new javax.swing.JPanel();
        jpl_mesa6 = new javax.swing.JPanel();
        img_mesa6 = new javax.swing.JLabel();
        lbl_mesa6 = new javax.swing.JLabel();
        cmb_mesa6 = new javax.swing.JComboBox<>();
        btn_obtmesa6 = new javax.swing.JButton();
        btn_actmesa6 = new javax.swing.JButton();
        jpl_estmesa6 = new javax.swing.JPanel();
        jpl_mesa5 = new javax.swing.JPanel();
        img_mesa5 = new javax.swing.JLabel();
        lbl_mesa5 = new javax.swing.JLabel();
        cmb_mesa5 = new javax.swing.JComboBox<>();
        btn_obtmesa5 = new javax.swing.JButton();
        btn_actmesa5 = new javax.swing.JButton();
        jpl_estmesa5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(227, 40, 32));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/business.png"))); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saucepizza/saucepoo/igu/images/minilogo.png"))); // NOI18N

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
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
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
        jLabel3.setText("Mesas");

        jpl_mesa1.setBackground(new java.awt.Color(255, 255, 255));

        img_mesa1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_mesa1.setText("Imagen");

        lbl_mesa1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_mesa1.setText("Mesa 0");

        cmb_mesa1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libre", "Ocupado", "Limpiar" }));
        cmb_mesa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_mesa1ActionPerformed(evt);
            }
        });

        btn_obtmesa1.setBackground(new java.awt.Color(240, 240, 240));
        btn_obtmesa1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_obtmesa1.setForeground(new java.awt.Color(227, 40, 32));
        btn_obtmesa1.setText("Obtener Recibo");
        btn_obtmesa1.setAutoscrolls(true);
        btn_obtmesa1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_obtmesa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_obtmesa1ActionPerformed(evt);
            }
        });

        btn_actmesa1.setBackground(new java.awt.Color(227, 40, 32));
        btn_actmesa1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_actmesa1.setForeground(new java.awt.Color(255, 255, 255));
        btn_actmesa1.setText("Actualizar");
        btn_actmesa1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_actmesa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actmesa1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpl_estmesa1Layout = new javax.swing.GroupLayout(jpl_estmesa1);
        jpl_estmesa1.setLayout(jpl_estmesa1Layout);
        jpl_estmesa1Layout.setHorizontalGroup(
            jpl_estmesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 328, Short.MAX_VALUE)
        );
        jpl_estmesa1Layout.setVerticalGroup(
            jpl_estmesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpl_mesa1Layout = new javax.swing.GroupLayout(jpl_mesa1);
        jpl_mesa1.setLayout(jpl_mesa1Layout);
        jpl_mesa1Layout.setHorizontalGroup(
            jpl_mesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jpl_mesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpl_mesa1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(img_mesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jpl_mesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_obtmesa1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_actmesa1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmb_mesa1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_mesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jpl_estmesa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jpl_mesa1Layout.setVerticalGroup(
            jpl_mesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpl_mesa1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpl_mesa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpl_mesa1Layout.createSequentialGroup()
                        .addComponent(lbl_mesa1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_mesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_obtmesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_actmesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(img_mesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpl_estmesa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jpl_mesa2.setBackground(new java.awt.Color(255, 255, 255));

        img_mesa2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_mesa2.setText("Imagen");

        lbl_mesa2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_mesa2.setText("Mesa 1");

        cmb_mesa2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libre", "Ocupado", "Limpiar" }));
        cmb_mesa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_mesa2ActionPerformed(evt);
            }
        });

        btn_obtmesa2.setBackground(new java.awt.Color(240, 240, 240));
        btn_obtmesa2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_obtmesa2.setForeground(new java.awt.Color(227, 40, 32));
        btn_obtmesa2.setText("Obtener Recibo");
        btn_obtmesa2.setAutoscrolls(true);
        btn_obtmesa2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_obtmesa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_obtmesa2ActionPerformed(evt);
            }
        });

        btn_actmesa2.setBackground(new java.awt.Color(227, 40, 32));
        btn_actmesa2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_actmesa2.setForeground(new java.awt.Color(255, 255, 255));
        btn_actmesa2.setText("Actualizar");
        btn_actmesa2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_actmesa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actmesa2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpl_estmesa2Layout = new javax.swing.GroupLayout(jpl_estmesa2);
        jpl_estmesa2.setLayout(jpl_estmesa2Layout);
        jpl_estmesa2Layout.setHorizontalGroup(
            jpl_estmesa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jpl_estmesa2Layout.setVerticalGroup(
            jpl_estmesa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpl_mesa2Layout = new javax.swing.GroupLayout(jpl_mesa2);
        jpl_mesa2.setLayout(jpl_mesa2Layout);
        jpl_mesa2Layout.setHorizontalGroup(
            jpl_mesa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jpl_mesa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpl_mesa2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jpl_estmesa2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpl_mesa2Layout.createSequentialGroup()
                        .addComponent(img_mesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jpl_mesa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_obtmesa2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_actmesa2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmb_mesa2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_mesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jpl_mesa2Layout.setVerticalGroup(
            jpl_mesa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpl_mesa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpl_mesa2Layout.createSequentialGroup()
                        .addComponent(lbl_mesa2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_mesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_obtmesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_actmesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(img_mesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpl_estmesa2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jpl_mesa3.setBackground(new java.awt.Color(255, 255, 255));

        img_mesa3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_mesa3.setText("Imagen");

        lbl_mesa3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_mesa3.setText("Mesa 2");

        cmb_mesa3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libre", "Ocupado", "Limpiar" }));
        cmb_mesa3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_mesa3ActionPerformed(evt);
            }
        });

        btn_obtmesa3.setBackground(new java.awt.Color(240, 240, 240));
        btn_obtmesa3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_obtmesa3.setForeground(new java.awt.Color(227, 40, 32));
        btn_obtmesa3.setText("Obtener Recibo");
        btn_obtmesa3.setAutoscrolls(true);
        btn_obtmesa3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_obtmesa3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_obtmesa3ActionPerformed(evt);
            }
        });

        btn_actmesa3.setBackground(new java.awt.Color(227, 40, 32));
        btn_actmesa3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_actmesa3.setForeground(new java.awt.Color(255, 255, 255));
        btn_actmesa3.setText("Actualizar");
        btn_actmesa3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_actmesa3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actmesa3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpl_estmesa3Layout = new javax.swing.GroupLayout(jpl_estmesa3);
        jpl_estmesa3.setLayout(jpl_estmesa3Layout);
        jpl_estmesa3Layout.setHorizontalGroup(
            jpl_estmesa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jpl_estmesa3Layout.setVerticalGroup(
            jpl_estmesa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpl_mesa3Layout = new javax.swing.GroupLayout(jpl_mesa3);
        jpl_mesa3.setLayout(jpl_mesa3Layout);
        jpl_mesa3Layout.setHorizontalGroup(
            jpl_mesa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jpl_mesa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpl_mesa3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jpl_estmesa3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpl_mesa3Layout.createSequentialGroup()
                        .addComponent(img_mesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jpl_mesa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_obtmesa3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_actmesa3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmb_mesa3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_mesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpl_mesa3Layout.setVerticalGroup(
            jpl_mesa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpl_mesa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpl_mesa3Layout.createSequentialGroup()
                        .addComponent(lbl_mesa3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_mesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_obtmesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_actmesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(img_mesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpl_estmesa3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpl_mesa4.setBackground(new java.awt.Color(255, 255, 255));

        img_mesa4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_mesa4.setText("Imagen");

        lbl_mesa4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_mesa4.setText("Mesa 3");

        cmb_mesa4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libre", "Ocupado", "Limpiar" }));
        cmb_mesa4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_mesa4ActionPerformed(evt);
            }
        });

        btn_obtmesa4.setBackground(new java.awt.Color(240, 240, 240));
        btn_obtmesa4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_obtmesa4.setForeground(new java.awt.Color(227, 40, 32));
        btn_obtmesa4.setText("Obtener Recibo");
        btn_obtmesa4.setAutoscrolls(true);
        btn_obtmesa4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_obtmesa4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_obtmesa4ActionPerformed(evt);
            }
        });

        btn_actmesa4.setBackground(new java.awt.Color(227, 40, 32));
        btn_actmesa4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_actmesa4.setForeground(new java.awt.Color(255, 255, 255));
        btn_actmesa4.setText("Actualizar");
        btn_actmesa4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_actmesa4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actmesa4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpl_estmesa4Layout = new javax.swing.GroupLayout(jpl_estmesa4);
        jpl_estmesa4.setLayout(jpl_estmesa4Layout);
        jpl_estmesa4Layout.setHorizontalGroup(
            jpl_estmesa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jpl_estmesa4Layout.setVerticalGroup(
            jpl_estmesa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpl_mesa4Layout = new javax.swing.GroupLayout(jpl_mesa4);
        jpl_mesa4.setLayout(jpl_mesa4Layout);
        jpl_mesa4Layout.setHorizontalGroup(
            jpl_mesa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jpl_mesa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpl_mesa4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jpl_estmesa4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpl_mesa4Layout.createSequentialGroup()
                        .addComponent(img_mesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jpl_mesa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_obtmesa4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_actmesa4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmb_mesa4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_mesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jpl_mesa4Layout.setVerticalGroup(
            jpl_mesa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpl_mesa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpl_mesa4Layout.createSequentialGroup()
                        .addComponent(lbl_mesa4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_mesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_obtmesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_actmesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(img_mesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpl_estmesa4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jpl_mesa6.setBackground(new java.awt.Color(255, 255, 255));

        img_mesa6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_mesa6.setText("Imagen");

        lbl_mesa6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_mesa6.setText("Mesa 5");

        cmb_mesa6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libre", "Ocupado", "Limpiar" }));
        cmb_mesa6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_mesa6ActionPerformed(evt);
            }
        });

        btn_obtmesa6.setBackground(new java.awt.Color(240, 240, 240));
        btn_obtmesa6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_obtmesa6.setForeground(new java.awt.Color(227, 40, 32));
        btn_obtmesa6.setText("Obtener Recibo");
        btn_obtmesa6.setAutoscrolls(true);
        btn_obtmesa6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_obtmesa6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_obtmesa6ActionPerformed(evt);
            }
        });

        btn_actmesa6.setBackground(new java.awt.Color(227, 40, 32));
        btn_actmesa6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_actmesa6.setForeground(new java.awt.Color(255, 255, 255));
        btn_actmesa6.setText("Actualizar");
        btn_actmesa6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_actmesa6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actmesa6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpl_estmesa6Layout = new javax.swing.GroupLayout(jpl_estmesa6);
        jpl_estmesa6.setLayout(jpl_estmesa6Layout);
        jpl_estmesa6Layout.setHorizontalGroup(
            jpl_estmesa6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jpl_estmesa6Layout.setVerticalGroup(
            jpl_estmesa6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpl_mesa6Layout = new javax.swing.GroupLayout(jpl_mesa6);
        jpl_mesa6.setLayout(jpl_mesa6Layout);
        jpl_mesa6Layout.setHorizontalGroup(
            jpl_mesa6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jpl_mesa6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpl_estmesa6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpl_mesa6Layout.createSequentialGroup()
                        .addComponent(img_mesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jpl_mesa6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_obtmesa6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_actmesa6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmb_mesa6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_mesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jpl_mesa6Layout.setVerticalGroup(
            jpl_mesa6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpl_mesa6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpl_mesa6Layout.createSequentialGroup()
                        .addComponent(lbl_mesa6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_mesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_obtmesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_actmesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(img_mesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpl_estmesa6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jpl_mesa5.setBackground(new java.awt.Color(255, 255, 255));

        img_mesa5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_mesa5.setText("Imagen");

        lbl_mesa5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_mesa5.setText("Mesa 4");

        cmb_mesa5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libre", "Ocupado", "Limpiar" }));
        cmb_mesa5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_mesa5ActionPerformed(evt);
            }
        });

        btn_obtmesa5.setBackground(new java.awt.Color(240, 240, 240));
        btn_obtmesa5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_obtmesa5.setForeground(new java.awt.Color(227, 40, 32));
        btn_obtmesa5.setText("Obtener Recibo");
        btn_obtmesa5.setAutoscrolls(true);
        btn_obtmesa5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_obtmesa5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_obtmesa5ActionPerformed(evt);
            }
        });

        btn_actmesa5.setBackground(new java.awt.Color(227, 40, 32));
        btn_actmesa5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_actmesa5.setForeground(new java.awt.Color(255, 255, 255));
        btn_actmesa5.setText("Actualizar");
        btn_actmesa5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 40, 32), 1, true));
        btn_actmesa5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actmesa5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpl_estmesa5Layout = new javax.swing.GroupLayout(jpl_estmesa5);
        jpl_estmesa5.setLayout(jpl_estmesa5Layout);
        jpl_estmesa5Layout.setHorizontalGroup(
            jpl_estmesa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jpl_estmesa5Layout.setVerticalGroup(
            jpl_estmesa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpl_mesa5Layout = new javax.swing.GroupLayout(jpl_mesa5);
        jpl_mesa5.setLayout(jpl_mesa5Layout);
        jpl_mesa5Layout.setHorizontalGroup(
            jpl_mesa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jpl_mesa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpl_estmesa5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpl_mesa5Layout.createSequentialGroup()
                        .addComponent(img_mesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jpl_mesa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_obtmesa5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_actmesa5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmb_mesa5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_mesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jpl_mesa5Layout.setVerticalGroup(
            jpl_mesa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpl_mesa5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpl_mesa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpl_mesa5Layout.createSequentialGroup()
                        .addComponent(lbl_mesa5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_mesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_obtmesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_actmesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(img_mesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpl_estmesa5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(jpl_mesa2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpl_mesa3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpl_mesa1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpl_mesa5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpl_mesa6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpl_mesa4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(283, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpl_mesa4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpl_mesa1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpl_mesa2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpl_mesa5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpl_mesa6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpl_mesa3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
        Servicio_Cajero window = new Servicio_Cajero();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        IniciodeSesion window = new IniciodeSesion();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btn_actmesa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actmesa1ActionPerformed
        modificarEstado(0,cmb_mesa1.getSelectedItem().toString());
    }//GEN-LAST:event_btn_actmesa1ActionPerformed

    private void cmb_mesa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_mesa1ActionPerformed
        cambiarEstado(0);
    }//GEN-LAST:event_cmb_mesa1ActionPerformed

    private void btn_obtmesa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_obtmesa1ActionPerformed
        obtenerRecibo(0);
    }//GEN-LAST:event_btn_obtmesa1ActionPerformed

    private void cmb_mesa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_mesa2ActionPerformed
        cambiarEstado(1);
    }//GEN-LAST:event_cmb_mesa2ActionPerformed

    private void btn_obtmesa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_obtmesa2ActionPerformed
        obtenerRecibo(1);
    }//GEN-LAST:event_btn_obtmesa2ActionPerformed

    private void btn_actmesa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actmesa2ActionPerformed
     modificarEstado(1,cmb_mesa2.getSelectedItem().toString());
    }//GEN-LAST:event_btn_actmesa2ActionPerformed

    private void cmb_mesa3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_mesa3ActionPerformed
       cambiarEstado(2);
    }//GEN-LAST:event_cmb_mesa3ActionPerformed

    private void btn_obtmesa3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_obtmesa3ActionPerformed
        obtenerRecibo(2);
    }//GEN-LAST:event_btn_obtmesa3ActionPerformed

    private void btn_actmesa3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actmesa3ActionPerformed
        modificarEstado(2,cmb_mesa3.getSelectedItem().toString());
    }//GEN-LAST:event_btn_actmesa3ActionPerformed

    private void cmb_mesa4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_mesa4ActionPerformed
        cambiarEstado(3);
    }//GEN-LAST:event_cmb_mesa4ActionPerformed

    private void btn_obtmesa4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_obtmesa4ActionPerformed
       obtenerRecibo(3);
    }//GEN-LAST:event_btn_obtmesa4ActionPerformed

    private void btn_actmesa4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actmesa4ActionPerformed
       modificarEstado(3,cmb_mesa4.getSelectedItem().toString());
    }//GEN-LAST:event_btn_actmesa4ActionPerformed

    private void cmb_mesa5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_mesa5ActionPerformed
        cambiarEstado(4);
    }//GEN-LAST:event_cmb_mesa5ActionPerformed

    private void btn_obtmesa5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_obtmesa5ActionPerformed
        obtenerRecibo(4);
    }//GEN-LAST:event_btn_obtmesa5ActionPerformed

    private void btn_actmesa5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actmesa5ActionPerformed
       modificarEstado(4,cmb_mesa5.getSelectedItem().toString());

    }//GEN-LAST:event_btn_actmesa5ActionPerformed

    private void cmb_mesa6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_mesa6ActionPerformed
        cambiarEstado(5);
    }//GEN-LAST:event_cmb_mesa6ActionPerformed

    private void btn_obtmesa6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_obtmesa6ActionPerformed
        obtenerRecibo(5);
    }//GEN-LAST:event_btn_obtmesa6ActionPerformed

    private void btn_actmesa6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actmesa6ActionPerformed
        modificarEstado(5,cmb_mesa6.getSelectedItem().toString());
    }//GEN-LAST:event_btn_actmesa6ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Servicio_Mesas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actmesa1;
    private javax.swing.JButton btn_actmesa2;
    private javax.swing.JButton btn_actmesa3;
    private javax.swing.JButton btn_actmesa4;
    private javax.swing.JButton btn_actmesa5;
    private javax.swing.JButton btn_actmesa6;
    private javax.swing.JButton btn_obtmesa1;
    private javax.swing.JButton btn_obtmesa2;
    private javax.swing.JButton btn_obtmesa3;
    private javax.swing.JButton btn_obtmesa4;
    private javax.swing.JButton btn_obtmesa5;
    private javax.swing.JButton btn_obtmesa6;
    private javax.swing.JComboBox<String> cmb_mesa1;
    private javax.swing.JComboBox<String> cmb_mesa2;
    private javax.swing.JComboBox<String> cmb_mesa3;
    private javax.swing.JComboBox<String> cmb_mesa4;
    private javax.swing.JComboBox<String> cmb_mesa5;
    private javax.swing.JComboBox<String> cmb_mesa6;
    private javax.swing.JLabel img_mesa1;
    private javax.swing.JLabel img_mesa2;
    private javax.swing.JLabel img_mesa3;
    private javax.swing.JLabel img_mesa4;
    private javax.swing.JLabel img_mesa5;
    private javax.swing.JLabel img_mesa6;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jpl_estmesa1;
    private javax.swing.JPanel jpl_estmesa2;
    private javax.swing.JPanel jpl_estmesa3;
    private javax.swing.JPanel jpl_estmesa4;
    private javax.swing.JPanel jpl_estmesa5;
    private javax.swing.JPanel jpl_estmesa6;
    private javax.swing.JPanel jpl_mesa1;
    private javax.swing.JPanel jpl_mesa2;
    private javax.swing.JPanel jpl_mesa3;
    private javax.swing.JPanel jpl_mesa4;
    private javax.swing.JPanel jpl_mesa5;
    private javax.swing.JPanel jpl_mesa6;
    private javax.swing.JLabel lbl_mesa1;
    private javax.swing.JLabel lbl_mesa2;
    private javax.swing.JLabel lbl_mesa3;
    private javax.swing.JLabel lbl_mesa4;
    private javax.swing.JLabel lbl_mesa5;
    private javax.swing.JLabel lbl_mesa6;
    // End of variables declaration//GEN-END:variables
}
