package br.com.infox.valida;

import javax.swing.JTextField;

public class Valida {
    JTextField jTextFieldCamp = null;

    public Valida(JTextField a){
        jTextFieldCamp = a;
    }
    
    public int go(){
        
    if(this.jTextFieldCamp.getText().isEmpty()){
        javax.swing.SwingUtilities.invokeLater(
            new Runnable() { 
                public void run() {
                    jTextFieldCamp.requestFocusInWindow();
                } 
            }
        );
    }else{
        return 0;
    }
    return -1;
    }
}
