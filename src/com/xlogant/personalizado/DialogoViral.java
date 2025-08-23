/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.personalizado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static java.awt.GridBagConstraints.*;
import static javax.swing.BorderFactory.createLineBorder;

/**
 *
 * @author oscar
 */
public class DialogoViral extends JDialog implements ActionListener {

    /**
     * Constructor de clase
     *
     * @param frame
     * @param modal
     * @param message
     */
    public DialogoViral(JFrame frame, boolean modal, String message) {
        DialogoViral.this.setPreferredSize(new Dimension(400, 60));
        DialogoViral.this.setUndecorated(true);
        GridBagConstraints gridBagConstraints;

        JPanel myPanel = new JPanel();
        myPanel.setPreferredSize(new Dimension(500, 100));
        myPanel.setBorder(createLineBorder(new Color(119, 232, 228), 2));
        myPanel.setBackground(new Color(0, 0, 0));
        myPanel.setLayout(new GridBagLayout());

        DialogoViral.this.getContentPane().add(myPanel);

        var lbMsg = new JLabel(message);
        lbMsg.setForeground(new Color(255, 255, 255));
        lbMsg.setOpaque(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        myPanel.add(lbMsg, gridBagConstraints);

        okButton = new BotonViral();
        okButton.setText("OK");
        okButton.setPreferredSize(new Dimension(80, 34));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = WEST;
        myPanel.add(okButton, gridBagConstraints);

        //listener
        DialogoViral.this.okButton.addActionListener(DialogoViral.this);

        DialogoViral.this.pack();
        DialogoViral.this.setLocationRelativeTo(frame);
        DialogoViral.this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (okButton == e.getSource()) {
            answer = true;
            setVisible(false);
        }
    }

    public boolean getAnswer() {
        return answer;
    }

    @Serial
    private static final long serialVersionUID = -8882840318811609532L;
    private BotonViral okButton = null;
    private boolean answer = false;
}
