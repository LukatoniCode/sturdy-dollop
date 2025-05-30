/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pokemon.test.vjeza;

import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Tr
 */
public class Uredivanje_Pokemona extends javax.swing.JFrame {

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    JFrame f = new JFrame();
    int broj = 0;
    
    public Uredivanje_Pokemona() {
        initComponents();
        conn = JavaConnect.connectDb();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Naziv", "PrimarniTip", "SekundarniTip", "Hp", "Napad", "PosebniNapad", "Obrana", "PosebnaObrana", "Brzina"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("0 od 800");

        jButton1.setText("Pretraži");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi tipovi", "Bug", "Dark", "Drago", "Electric", "Fairy", "Fighting", "Fire", "Flying", "Ghost", "Grass", "Ground", "Ice", "Normal", "Poiso", "Psychic", "Rock", "Steel", "Water" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi tipovi", "Bug", "Dark", "Drago", "Electric", "Fairy", "Fighting", "Fire", "Flying", "Ghost", "Grass", "Ground", "Ice", "Normal", "Poiso", "Psychic", "Rock", "Steel", "Water" }));

        jSlider1.setMaximum(255);
        jSlider1.setMinimum(1);
        jSlider1.setValue(1);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jSlider2.setMaximum(255);
        jSlider2.setMinimum(1);
        jSlider2.setValue(255);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jLabel2.setText("1");

        jLabel3.setText("255");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(323, 323, 323))))
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String sql;
        broj = 0;
        try{
            if(jTextField1.getText().trim().isEmpty()){
                if (jComboBox1.getSelectedIndex() == 0 && jComboBox2.getSelectedIndex() == 0){
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                            + "FROM pokemon.pokemon AS b "
                            + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                            + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                            + "where Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +"; ";
                }else if (jComboBox1.getSelectedIndex() != 0 && jComboBox2.getSelectedIndex() == 0){
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                        + "FROM pokemon.pokemon AS b "
                        + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                        + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                        + "where b.PrimarniTipId = '" + jComboBox1.getSelectedIndex() + "' AND Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +" ";
                }else if (jComboBox1.getSelectedIndex() == 0 && jComboBox2.getSelectedIndex() != 0){
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                        + "FROM pokemon.pokemon AS b "
                        + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                        + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                        + "where b.SekundarniTipId = '" + jComboBox2.getSelectedIndex() + "' AND Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +" ";
                }else{
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                        + "FROM pokemon.pokemon AS b "
                        + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                        + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                        + "where b.SekundarniTipId = '" + jComboBox2.getSelectedIndex() + "' AND b.PrimarniTipId = '" + jComboBox1.getSelectedIndex() + "' AND Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +"";
                }
            }else{
                if (jComboBox1.getSelectedIndex() == 0 && jComboBox2.getSelectedIndex() == 0){
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                        + "FROM pokemon.pokemon AS b "
                        + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                        + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                        + "where b.Naziv LIKE '%"+jTextField1.getText()+"%' AND Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +"";
                }else if (jComboBox1.getSelectedIndex() != 0 && jComboBox2.getSelectedIndex() == 0){
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                        + "FROM pokemon.pokemon AS b "
                        + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                        + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                        + "where b.PrimarniTipId = '" + jComboBox1.getSelectedIndex() + "' AND b.Naziv LIKE '%"+jTextField1.getText()+"%' AND Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +"";
                }else if (jComboBox1.getSelectedIndex() == 0 && jComboBox2.getSelectedIndex() != 0){
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                        + "FROM pokemon.pokemon AS b "
                        + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                        + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                        + "where b.SekundarniTipId = '" + jComboBox2.getSelectedIndex() + "' AND b.Naziv LIKE '%"+jTextField1.getText()+"%' AND Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +"";
                }else{
                    sql = "SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, Hp, Napad, PosebniNapad, Obrana, PosebnaObrana, Brzina "
                        + "FROM pokemon.pokemon AS b "
                        + "LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId "
                        + "LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId "
                        + "where b.SekundarniTipId = '" + jComboBox2.getSelectedIndex() + "' AND b.PrimarniTipId = '" + jComboBox1.getSelectedIndex() + "' AND b.Naziv LIKE '%"+jTextField1.getText()+"%' AND Hp BETWEEN " + jSlider1.getValue() +" AND " + jSlider2.getValue() +"";
                }
            }
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            jTable1.setModel(DbUtils.resultSetToTableModel(rs));
            
            rs.close();
            
            rs = pst.executeQuery();
            
            while(rs.next()){
                broj++;
            }
            
            pst.close();
            rs.close();
            
            jLabel1.setText(broj + " od 800");
            jTable1.setAutoCreateRowSorter(true);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(f, e);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        String broj = Integer.toString(jSlider1.getValue());
        jLabel2.setText(broj);
        
    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        String broj = Integer.toString(jSlider2.getValue());
        jLabel3.setText(broj);
    }//GEN-LAST:event_jSlider2StateChanged

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Uredivanje_Pokemona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Uredivanje_Pokemona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Uredivanje_Pokemona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Uredivanje_Pokemona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Uredivanje_Pokemona().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
