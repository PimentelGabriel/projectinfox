/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.err;

/**
 *
 * @author PC02-SALA05
 */
public class txtNull extends Exception {
    private final String msg;
    public txtNull(String msg){
        super(msg);
        this.msg = msg;
    }

    @Override
    public String getMessage(){
        return msg;
    }
}
