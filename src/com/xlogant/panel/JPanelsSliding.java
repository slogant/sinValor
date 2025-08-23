/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.panel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author oscar
 */
public class JPanelsSliding extends JPanel {

    public enum direct {

        Left,
        Right,
        up,
        Dowun
    };

    public JPanelsSliding() {
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        var size = new Dimension(this.getWidth(), this.getHeight());
    }

    public void nextSlidPanel(Component ShowPanel, direct DirectionMove) {
        nextSlidPanel(10, 40, ShowPanel, DirectionMove);
    }

    public void nextSlidPanel(int SpeedPanel, Component ShowPanel, direct DirectionMove) {
        nextSlidPanel(SpeedPanel, 40, ShowPanel, DirectionMove);
    }

    public void nextSlidPanel(int SpeedPanel, int TimeSpeed, Component ShowPanel, direct DirectionMove) {
        if (!ShowPanel.getName().equals(getCurrentComponentShow(this))) {
            var currentComp = getCurrentComponent(this);
            ShowPanel.setVisible(true);
            var sl = new JPanelSlidingListener(SpeedPanel, currentComp, ShowPanel, DirectionMove);
            var t = new Timer(TimeSpeed, sl);
            sl.timer = t;
            t.start();
        }
        refresh();
    }

    public void previous(direct direct) {
        var currentComp = getCurrentComponent(this);
        var previousComp = getPreviousComponent(this);
        var b = currentComp.getBounds();
        previousComp.setVisible(true);
        var sl = new JPanelSlidingListener(10, currentComp, previousComp, direct);
        var t = new Timer(40, sl);
        sl.timer = t;
        t.start();
        refresh();
    }

    public void next(direct direct) {
        var currentComp = getCurrentComponent(this);
        var nextComp = getNextComponent(this);
        var b = currentComp.getBounds();
        nextComp.setVisible(true);
        var sl = new JPanelSlidingListener(10, currentComp, nextComp, direct);
        var t = new Timer(40, sl);
        sl.timer = t;
        t.start();
        refresh();
    }

    public Component getCurrentComponent(Container parent) {
        Component comp = null;
        var n = parent.getComponentCount();
        for (var i = 0; i < n; i++) {
            comp = parent.getComponent(i);
            if (comp.isVisible()) {
                return comp;
            }
        }
        return comp;
    }

    public Component getNextComponent(Container parent) {
        var n = parent.getComponentCount();
        for (var i = 0; i < n; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                int currentCard = (i + 1) % n;
                comp = parent.getComponent(currentCard);
                return comp;
            }
        }
        return null;
    }

    public Component getPreviousComponent(Container parent) {
        var n = parent.getComponentCount();
        for (var i = 0; i < n; i++) {
            Component comp = parent.getComponent(i);

            if (comp.isVisible()) {
                int currentCard = ((i > 0) ? i - 1 : n - 1);
                comp = parent.getComponent(currentCard);
                return comp;
            }

        }
        return null;
    }

    public String getCurrentComponentShow(Container parent) {
        String PanelName = null;
        Component comp = null;
        var n = parent.getComponentCount();
        for (var i = 0; i < n; i++) {
            comp = parent.getComponent(i);
            if (comp.isVisible()) {
                PanelName = comp.getName();
                return PanelName;
            }
        }
        return PanelName;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 789, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public class JPanelSlidingListener implements ActionListener {

        Component HidePanel;
        Component ShowPanel;
        int steps;
        int step = 0;
        Timer timer;
        direct direct;

        public JPanelSlidingListener(int steps, Component HidePanel, Component ShowPanel, direct direct) {
            this.steps = steps;
            this.HidePanel = HidePanel;
            this.ShowPanel = ShowPanel;
            this.direct = direct;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            var bounds = HidePanel.getBounds();
            var shift = bounds.width / steps;
            var shiftup = bounds.height / steps;
            switch (direct) {
                case Left -> {
                    HidePanel.setLocation(bounds.x - shift, bounds.y);
                    ShowPanel.setLocation(bounds.x - shift + bounds.width, bounds.y);
                }
                case Right -> {
                    HidePanel.setLocation(bounds.x + shift, bounds.y);
                    ShowPanel.setLocation(bounds.x + shift - bounds.width, bounds.y);
                }
                case up -> {
                    HidePanel.setLocation(bounds.x, bounds.y - shiftup);
                    ShowPanel.setLocation(bounds.x, bounds.y - shiftup + bounds.height);
                }
                case Dowun -> {
                    HidePanel.setLocation(bounds.x, bounds.y + shiftup);
                    ShowPanel.setLocation(bounds.x, bounds.y + shiftup - bounds.height);
                }
            }
            repaint();
            step++;
            if (step == steps) {
                timer.stop();
                HidePanel.setVisible(false);
            }
        }
    }

    public void refresh() {
        revalidate();
        repaint();
    }
    @Serial
    private static final long serialVersionUID = 649607873435830229L;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
