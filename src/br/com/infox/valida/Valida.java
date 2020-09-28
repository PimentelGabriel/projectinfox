package br.com.infox.valida;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Valida{
    static public int go(JTextField jTextFieldCamp){
        if (jTextFieldCamp.getText().isEmpty()
                || (jTextFieldCamp.getText().indexOf(" ") <= 1
                && jTextFieldCamp.getText().indexOf(" ") >= 0)) {
            javax.swing.SwingUtilities.invokeLater( new Runnable() {
                                                        public void run() {
                                                            JOptionPane.showMessageDialog(null, "Campo obrigatório para realizar a ação");
                                                            jTextFieldCamp.requestFocusInWindow();
                                                            jTextFieldCamp.setBorder(new LineBorder(Color.RED));
                                                        }
                                                    });
            return -1;
        }else{
            return 0;
        }
    }
}
