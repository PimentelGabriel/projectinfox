/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import br.com.infox.dal.ModuloConexao;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 *
 * @author pc4sala05
 */
public class TelaCliente extends javax.swing.JFrame {

    ArrayList<JTextField> campos = new ArrayList<JTextField>();

    //framewrok do pacote java.sql
    Connection conexao = null;
    //Var especial de apoio à conexão com o BD
    PreparedStatement pst = null;
    // Obj matriz que recebe o resultado do cmd SQL
    ResultSet rs = null;

    /**
     * Creates new form TelaUsuario
     */
    public TelaCliente() {
        initComponents();
        this.setIconImage(new ImageIcon(getClass().getResource("/br/com/infox/icon/icotela/user.png")).getImage());
        conexao = ModuloConexao.conector();
    }
    
    //Limpa o JTextFiled para cinza
    public void cleanBorda(){
        txtCliId.setBorder(new LineBorder(Color.GRAY));
        txtUsuNome.setBorder(new LineBorder(Color.GRAY));
        txtCliEndereco.setBorder(new LineBorder(Color.GRAY));
        txtClifone.setBorder(new LineBorder(Color.GRAY));
        txtCliEmail.setBorder(new LineBorder(Color.GRAY));
    }

    //Metodo para validar os campos
    public int validar(JTextField jTextFieldCamp) {
        if (jTextFieldCamp.getText().isEmpty()
                || (jTextFieldCamp.getText().indexOf(" ") <= 1
                && jTextFieldCamp.getText().indexOf(" ") >= 0)) {
            javax.swing.SwingUtilities.invokeLater(
                    new Runnable() {
                public void run() {
                    jTextFieldCamp.requestFocusInWindow();
                    jTextFieldCamp.setBorder(new LineBorder(Color.RED));
                }
            }
            );
            return -1;
        }
        return 0;
    }

    //Metodo para conbsultar usuário
    private void consultar() {
        cleanBorda();
        String sql = "SELECT * FROM tbclientes WHERE idcli=?";

        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtCliId.getText());

            rs = pst.executeQuery();

            if (validar(txtCliId) == 0) {
                if (rs.next()) {
                    txtUsuNome.setText(rs.getString(2));
                    txtCliEndereco.setText(rs.getString(3));
                    txtClifone.setText(rs.getString(4));
                    txtCliEmail.setText(rs.getString(5));
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário não cadastrado ou Campo em Branco");

                    txtUsuNome.setText(null);
                    txtCliEndereco.setText(null);
                    txtClifone.setText(null);
                    txtCliEmail.setText(null);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO:\n" + e);
        }
    }

    private void adicionar() {
        cleanBorda();
        String sql = "INSERT INTO tbclientes(nomecli, endcli,fonecli, emailcli) VALUES(?, ?, ?, ?)";

        campos.add(txtUsuNome);
        campos.add(txtCliEndereco);
        campos.add(txtCliEmail);
        campos.add(txtClifone);

        try {
            pst = conexao.prepareStatement(sql);

            //Add a string do formulário (da tela) para o comando SQL
            pst.setString(1, txtUsuNome.getText());
            pst.setString(2, txtCliEndereco.getText());
            pst.setString(3, txtClifone.getText());
            pst.setString(4, txtCliEmail.getText());

//            if(txtUsuNome.getText().isEmpty()){
//                javax.swing.SwingUtilities.invokeLater(
//                            new Runnable() { 
//                                public void run() {
//                                    txtUsuNome.requestFocusInWindow();
//                                } 
//                            });
//            }
//            
//            if( txtUsuNome.getText().isEmpty()  ||
//                txtUsuFone.getText().isEmpty()  ||
//                txtUsuLogin.getText().isEmpty() ||
//                txtUsuSenha.getText().isEmpty()
//            ){
//                JOptionPane.showMessageDialog(null, "Peencha todos os campos")
            int err = 0;

            for (JTextField campo : campos) {
                err = validar(campo);
            }

            if (err == 0) {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");

                    for (JTextField campo : campos) {
                        campo.setText(null); //Deixa todos os campos null
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO:\n" + e);
        }
    }

    private int alterar() {
        cleanBorda();
        String sql = "UPDATE tbclientes SET nomecli=?, endcli=?, fonecli=?, emailcli=? WHERE idcli=?";

        campos.add(txtCliId);
        campos.add(txtUsuNome);
        campos.add(txtCliEndereco);
        campos.add(txtCliEmail);
        campos.add(txtClifone);

        try {
            pst = conexao.prepareStatement(sql);

            //Add a string do formulário (da tela) para o comando SQL
            pst.setString(1, txtUsuNome.getText());
            pst.setString(2, txtCliEndereco.getText());
            pst.setString(3, txtClifone.getText());
            pst.setString(4, txtCliEmail.getText());
            pst.setString(5, txtCliId.getText());

            //Faz um for para procurar qual campo estar vazio
            for (JTextField campo : campos) {
                if (validar(campo) == -1) {
                    JOptionPane.showMessageDialog(null, "Peencha todos os campos");
                    return -1;
                }
            }

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso");

                //Faz um for em dtodos os campos atribuindo null no texto
                for (JTextField campo : campos) {
                    campo.setText(null);
                }
            }
            return 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO:\n" + e);
            return -1;
        } finally {
            campos.clear();
        }
    }
    
    private void remover(){
        cleanBorda();
        if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Tem certesa que deseja deletar?", "Atenção", JOptionPane.YES_NO_OPTION)){
            String sql = "DELETE FROM tbclientes WHERE idcli=?";
            
            try {
                pst = conexao.prepareStatement(sql);

                //Add a string do formulário (da tela) para o comando SQL
                pst.setString(1, txtCliId.getText());
                
                int apagado = pst.executeUpdate();
                
                if(apagado > 0){
                    JOptionPane.showMessageDialog(null, "Cliente apagado com sucesso");
                    
                    campos.add(txtCliId);
                    campos.add(txtUsuNome);
                    campos.add(txtCliEndereco);
                    campos.add(txtCliEmail);
                    campos.add(txtClifone);

                    for(JTextField campo : campos){
                        campo.setText(null);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

//    public void deletar() {
//        String sql = "DELETE FROM tbclientes WHERE idcli=?";
//
//        try {
//            pst = conexao.prepareStatement(sql);
//
//            //Add a string do formulário (da tela) para o comando SQL
//            pst.setString(1, txtUsuId.getText());
//
//            if (txtUsuId.getText().isEmpty()) {
//                javax.swing.SwingUtilities.invokeLater(
//                        new Runnable() {
//                    public void run() {
//                        txtUsuNome.requestFocusInWindow();
//                    }
//                });
//            } else {
//                if (JOptionPane.showConfirmDialog(null, "Tem certesa que deseja deletar?", "Atenção", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                    int deletado = pst.executeUpdate();
//                    //pst.executeUpdate();
//                    if(deletado==0){
//                        JOptionPane.showMessageDialog(null, deletado, sql, deletado, icon);
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "ERRO:\n" + e);
//        } finally {
//            campos.clear();
//        }
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtClifone = new javax.swing.JTextField();
        txtUsuNome = new javax.swing.JTextField();
        txtCliId = new javax.swing.JTextField();
        txtCliEndereco = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        txtCliEmail = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Clientes");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        setMinimumSize(new java.awt.Dimension(682, 520));
        setSize(new java.awt.Dimension(682, 520));

        jLabel1.setText("ID:");

        jLabel2.setText("Nome:");

        jLabel3.setText("Fone:");

        jLabel4.setText("Endereço:");

        jLabel5.setText("Email");

        txtClifone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtClifoneKeyTyped(evt);
            }
        });

        txtUsuNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuNomeKeyTyped(evt);
            }
        });

        txtCliId.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtCliIdInputMethodTextChanged(evt);
            }
        });
        txtCliId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCliIdKeyPressed(evt);
            }
        });

        txtCliEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCliEnderecoKeyTyped(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icon/crud/create.png"))); // NOI18N
        jButton1.setToolTipText("Adicionar um novo usuário");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setPreferredSize(new java.awt.Dimension(80, 80));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icon/crud/read.png"))); // NOI18N
        jButton3.setToolTipText("Consultar");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setPreferredSize(new java.awt.Dimension(80, 80));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icon/crud/delete.png"))); // NOI18N
        jButton2.setToolTipText("Remover");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setPreferredSize(new java.awt.Dimension(80, 80));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icon/crud/update.png"))); // NOI18N
        jButton4.setToolTipText("Alterar");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setPreferredSize(new java.awt.Dimension(80, 80));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtClifone, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel2))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtUsuNome, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUsuNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtClifone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(698, 539));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        consultar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        adicionar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        alterar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtCliIdInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtCliIdInputMethodTextChanged
        txtCliId.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_txtCliIdInputMethodTextChanged

    private void txtCliIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliIdKeyPressed
        txtCliId.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtCliIdKeyPressed

    private void txtUsuNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuNomeKeyTyped
        txtUsuNome.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtUsuNomeKeyTyped

    private void txtCliEnderecoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliEnderecoKeyTyped
        txtCliEndereco.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtCliEnderecoKeyTyped

    private void txtClifoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClifoneKeyTyped
        txtClifone.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtClifoneKeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        remover();
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEndereco;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtClifone;
    private javax.swing.JTextField txtUsuNome;
    // End of variables declaration//GEN-END:variables
}
