package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MandelbrotRenderer renderer = new MandelbrotRenderer();
            renderer.setVisible(true);
        });
    }
    //Hay que pulsar actualizar para que se pinte el dibujo del mandelbrot
}