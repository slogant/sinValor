package com.xlogant.inicio;

import com.xlogant.principal.CentroPrincipal;

import javax.swing.UIManager.LookAndFeelInfo;

import static java.awt.EventQueue.invokeLater;
import static java.lang.System.out;
import static java.util.logging.Logger.*;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author oscar
 */
public class IniciaPrincipal {

    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (var info : getInstalledLookAndFeels()) {
                out.println(info.getName());
                if ("Metal".equals(info.getName())) {
                    setLookAndFeel(info.getClassName());

                    /* Create and display the form */
                    invokeLater(() -> new CentroPrincipal().setVisible(false));
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            getLogger(CentroPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        
    }

}
