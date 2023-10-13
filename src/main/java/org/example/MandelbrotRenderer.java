package org.example;

import javax.swing.*;


public class MandelbrotRenderer extends JFrame {
    public MandelbrotRenderer() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        add(new MandelbrotPanel());
        setLocationRelativeTo(null);
    }
}