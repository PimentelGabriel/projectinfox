/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import br.com.infox.dal.ModuloConexao;
import br.com.infox.valida.Valida;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 *
 * @author pc4sala05
 */
public class TelaOs extends javax.swing.JFrame {

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
    public TelaOs() {
        initComponents();
        this.setIconImage(new ImageIcon(getClass().getResource("/br/com/infox/icon/icotela/user.png")).getImage());
        conexao = ModuloConexao.conector();
    }
    
    public void queryCli(){
        new Thread( new Runnable(){
            @Override
            public void run(){
                String sql = "SELECT * FROM tbclientes WHERE idcli=?";

                try {
                    pst = conexao.prepareStatement(sql);

                    pst.setString(1, txtOsCliId.getText());

                    rs = pst.executeQuery();
                    
                    if (rs.next()) {
                        txtCliNome.setText(rs.getString(2));
                        txtCliEndereco.setText(rs.getString(3));
                        txtClifone.setText(rs.getString(4));
                        txtCliEmail.setText(rs.getString(5));
                    } else {
                        txtCliNome.setText(null);
                        txtCliEndereco.setText(null);
                        txtClifone.setText(null);
                        txtCliEmail.setText(null);
                    }
                } catch (Exception e) {
                    txtCliNome.setText("Usuário Inexistente");
                }
            }
        }).start();
    }
    
    public void limpaCampoCliente(){
        txtCliEmail.setText(null);
        txtCliEndereco.setText(null);
        txtCliNome.setText(null);
        txtClifone.setText(null);
    }
    
    //Limpa o JTextFiled para cinza
    public void cleanBorda(){
        txtOSId.setBorder(new LineBorder(Color.GRAY));
        txtOsEquipamento.setBorder(new LineBorder(Color.GRAY));
        txtOsServico.setBorder(new LineBorder(Color.GRAY));
        txtOsDefeito.setBorder(new LineBorder(Color.GRAY));
        txtOsCliId.setBorder(new LineBorder(Color.GRAY));
        txtOsData.setBorder(new LineBorder(Color.GRAY));
        txtOsTecnico.setBorder(new LineBorder(Color.GRAY));
        txtOsValor.setBorder(new LineBorder(Color.GRAY));
    }

    //Metodo para validar os campos
//    public int validar(JTextField jTextFieldCamp) {
//        if (jTextFieldCamp.getText().isEmpty()
//                || (jTextFieldCamp.getText().indexOf(" ") <= 1
//                && jTextFieldCamp.getText().indexOf(" ") >= 0)) {
//            javax.swing.SwingUtilities.invokeLater( new Runnable() {
//                                                        public void run() {
//                                                            JOptionPane.showMessageDialog(null, "Campo obrigatório para realizar a ação");
//                                                            jTextFieldCamp.requestFocusInWindow();
//                                                            jTextFieldCamp.setBorder(new LineBorder(Color.RED));
//                                                        }
//                                                    });
//            
//            return -1;
//        }
//        return 0;
//    }

    //Metodo para conbsultar usuário
    private void consultar() {
        cleanBorda();
        String sql = "SELECT *FROM tbos, tbclientes WHERE os=? AND tbos.idcli = tbclientes.idcli";
        
        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtOSId.getText());

            rs = pst.executeQuery();

            if (Valida.go(txtOSId) == 0) {
                if (rs.next()) {
                    txtOsData.setText(rs.getString(2));
                    txtOsEquipamento.setText(rs.getString(3));
                    txtOsDefeito.setText(rs.getString(4));
                    txtOsServico.setText(rs.getString(5));
                    txtOsTecnico.setText(rs.getString(6));
                    txtOsValor.setText(rs.getString(7));
                    txtOsCliId.setText(rs.getString(8));
                    //txtOsCliId.setText(rs.getString(9));
                    txtCliNome.setText(rs.getString(10));
                    txtCliEndereco.setText(rs.getString(11));
                    txtClifone.setText(rs.getString(12));
                    txtCliEmail.setText(rs.getString(13));
                    
                } else {
                    JOptionPane.showMessageDialog(null, "OS inexistente ou Campo em Branco");

                    txtOsData.setText(null);
                    txtOsEquipamento.setText(null);
                    txtOsDefeito.setText(null);
                    txtOsServico.setText(null);
                    txtOsTecnico.setText(null);
                    txtOsValor.setText(null);
                    txtOsCliId.setText(null);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO:\n" + e);
        }
    }

    private void adicionar() {
        cleanBorda();
        String sql = "INSERT INTO tbos(equipamento, defeito, servico, tecnico, valor, idcli) VALUES(?, ?, ?, ? ,? ,?)";

        campos.add(txtOsEquipamento);
        campos.add(txtOsDefeito);
        campos.add(txtOsServico);
        campos.add(txtOsTecnico);
        campos.add(txtOsValor);
        campos.add(txtOsCliId);

        try {
            pst = conexao.prepareStatement(sql);

            //Add a string do formulário (da tela) para o comando SQL
//            pst.setString(1, txtOsEquipamento.getText());
//            pst.setString(2, txtOsServico.getText());
//            pst.setString(3, txtOsDefeito.getText());
//            pst.setString(4, txtUsuSenha.getText());
//          

            //Add a string do formulário (da tela) para o comando SQL
            for(int i = 0; i < campos.size() ;i++){
                pst.setString(i+1, campos.get(i).getText());
            }

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
                if(0 != Valida.go(campo)){
                    err = Valida.go(campo);
                    break;
                }
            }

            if (err == 0) {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de Serviço adicionado com sucesso");

                    //Deixa todos os campos null
                    campos.forEach( campo -> campo.setText(null));
                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO:\n" + e);
            System.out.println(e);
        }finally{
            campos.clear();
            limpaCampoCliente();
        }
    }

    private int alterar() {
        cleanBorda();
        String sql = "UPDATE tbos SET equipamento=?, defeito=?, servico=?, tecnico=?, valor=?, idcli=? WHERE OS=?";

        campos.add(txtOsEquipamento);
        campos.add(txtOsDefeito);
        campos.add(txtOsServico);
        campos.add(txtOsTecnico);
        campos.add(txtOsValor);
        campos.add(txtOsCliId);
        campos.add(txtOSId);

        try {
            pst = conexao.prepareStatement(sql);

            //Add a string do formulário (da tela) para o comando SQL
            for(int i=1; i<=campos.size();i++){
                pst.setString(i, campos.get(i-1).getText());
                System.out.println(i+"° <= "+campos.get(i-1).getText());
            }

            //Faz um for para procurar qual campo estar vazio
            for (JTextField campo : campos) {
                if (Valida.go(campo) != 0) {
                    return -1;
                }
            }

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Ordem de Serviço alterada com sucesso");

                //Faz um forEach em dtodos os campos atribuindo null no texto
                campos.forEach((campo) -> {
                    campo.setText(null);
                });
                
                txtOsData.setText(null);
                txtOSId.setText(null);
                
            }
            return 0;

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO:\n" + e);
            return -1;
        } finally {
            limpaCampoCliente();
            campos.clear();
        }
    }
    
    private void remover(){
        cleanBorda();
        if(Valida.go(txtOSId)==0)
        if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Tem certesa que deseja deletar?", "Atenção", JOptionPane.YES_NO_OPTION)){
            String sql = "DELETE FROM tbos WHERE OS=?";
            
            try {
                pst = conexao.prepareStatement(sql);

                //Add a string do formulário (da tela) para o comando SQL
                pst.setString(1, txtOSId.getText());
                
                int apagado = pst.executeUpdate();
                
                if(apagado > 0){
                    JOptionPane.showMessageDialog(null, "Usuário apagado com sucesso");
                    
                    campos.add(txtOsEquipamento);
                    campos.add(txtOsDefeito);
                    campos.add(txtOsServico);
                    campos.add(txtOsTecnico);
                    campos.add(txtOsValor);
                    campos.add(txtOsCliId);
                    campos.add(txtOSId);
                    campos.add(txtOsData);

                    for(JTextField campo : campos){
                        campo.setText(null);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro:\n"+e);
            }finally{
                limpaCampoCliente();
                campos.clear();
            }
        }
    }

//    public void deletar() {
//        String sql = "DELETE FROM tbusuarios WHERE iduser=?";
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
        jSeparator4 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtOsDefeito = new javax.swing.JTextField();
        txtOsEquipamento = new javax.swing.JTextField();
        txtOSId = new javax.swing.JTextField();
        txtOsServico = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtOsData = new javax.swing.JTextField();
        txtOsTecnico = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtOsValor = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtOsCliId = new javax.swing.JTextField();
        txtCliEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtClifone = new javax.swing.JTextField();
        txtCliEndereco = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

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
        setTitle("Ordem de Serviço");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        setMinimumSize(new java.awt.Dimension(682, 520));
        setSize(new java.awt.Dimension(682, 520));

        jLabel1.setText("OS:");

        jLabel2.setText("Equipamento:");

        jLabel3.setText("Defeito:");

        jLabel4.setText("Serviço:");

        txtOsDefeito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsDefeitoKeyTyped(evt);
            }
        });

        txtOsEquipamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsEquipamentoKeyTyped(evt);
            }
        });

        txtOSId.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtOSIdInputMethodTextChanged(evt);
            }
        });
        txtOSId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOSIdKeyPressed(evt);
            }
        });

        txtOsServico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsServicoKeyTyped(evt);
            }
        });

        jLabel6.setText("Data:");

        txtOsData.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtOsDataInputMethodTextChanged(evt);
            }
        });
        txtOsData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOsDataKeyPressed(evt);
            }
        });

        txtOsTecnico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsTecnicoKeyTyped(evt);
            }
        });

        jLabel8.setText("Técnico");

        jLabel9.setText("Valor:");

        txtOsValor.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtOsValorInputMethodTextChanged(evt);
            }
        });
        txtOsValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOsValorKeyPressed(evt);
            }
        });

        jLabel7.setText("ID Cliente:");

        txtOsCliId.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtOsCliIdInputMethodTextChanged(evt);
            }
        });
        txtOsCliId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsCliIdKeyTyped(evt);
            }
        });

        txtCliEmail.setEditable(false);

        jLabel5.setText("Email");

        jLabel10.setText("Fone:");

        txtClifone.setEditable(false);
        txtClifone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtClifoneKeyTyped(evt);
            }
        });

        txtCliEndereco.setEditable(false);
        txtCliEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCliEnderecoKeyTyped(evt);
            }
        });

        jLabel11.setText("Endereço:");

        jLabel12.setText("Cliente:");

        txtCliNome.setEditable(false);
        txtCliNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCliNomeKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(jLabel5)
                                .addGap(31, 31, 31)
                                .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(txtClifone, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(108, 108, 108)
                                .addComponent(txtOsCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel12)
                                .addGap(34, 34, 34)
                                .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtOsCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(36, 36, 36)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtClifone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel5)
                    .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtOSId, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtOsData, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(230, 230, 230))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtOsDefeito, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtOsServico, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(txtOsTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtOsEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtOSId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtOsData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsDefeito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtOsServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(698, 675));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtOsCliIdInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtOsCliIdInputMethodTextChanged
        txtOsCliId.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_txtOsCliIdInputMethodTextChanged

    private void txtOsValorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsValorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsValorKeyPressed

    private void txtOsValorInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtOsValorInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsValorInputMethodTextChanged

    private void txtOsTecnicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsTecnicoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsTecnicoKeyTyped

    private void txtOsDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsDataKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsDataKeyPressed

    private void txtOsDataInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtOsDataInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsDataInputMethodTextChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        alterar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        remover();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        consultar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        adicionar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtOsServicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsServicoKeyTyped
        txtOsServico.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtOsServicoKeyTyped

    private void txtOSIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOSIdKeyPressed
        txtOSId.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtOSIdKeyPressed

    private void txtOSIdInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtOSIdInputMethodTextChanged
        txtOSId.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_txtOSIdInputMethodTextChanged

    private void txtOsEquipamentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsEquipamentoKeyTyped
        txtOsEquipamento.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtOsEquipamentoKeyTyped

    private void txtOsDefeitoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsDefeitoKeyTyped
        txtOsDefeito.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtOsDefeitoKeyTyped

    private void txtClifoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClifoneKeyTyped
        txtClifone.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtClifoneKeyTyped

    private void txtCliEnderecoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliEnderecoKeyTyped
        txtCliEndereco.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtCliEnderecoKeyTyped

    private void txtCliNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliNomeKeyTyped
        txtCliNome.setBorder(new LineBorder(Color.GRAY));
    }//GEN-LAST:event_txtCliNomeKeyTyped

    private void txtOsCliIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsCliIdKeyTyped
        queryCli();
    }//GEN-LAST:event_txtOsCliIdKeyTyped

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
            java.util.logging.Logger.getLogger(TelaOs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaOs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaOs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaOs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaOs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEndereco;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtClifone;
    private javax.swing.JTextField txtOSId;
    private javax.swing.JTextField txtOsCliId;
    private javax.swing.JTextField txtOsData;
    private javax.swing.JTextField txtOsDefeito;
    private javax.swing.JTextField txtOsEquipamento;
    private javax.swing.JTextField txtOsServico;
    private javax.swing.JTextField txtOsTecnico;
    private javax.swing.JTextField txtOsValor;
    // End of variables declaration//GEN-END:variables
}
