package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MandelbrotPanel extends JPanel {
    private BufferedImage image;
    private int numTrabajadores;
    private AtomicInteger tasksCompleted;

    public MandelbrotPanel() {
        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        numTrabajadores = 4; // Número de trabajadores para empezar
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JLabel numThreadsLabel = new JLabel("Número de trabajadores: ");
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(numTrabajadores, 1, 16, 1));
        spinner.addChangeListener(e -> numTrabajadores = (int) spinner.getValue());

        JButton renderButton = new JButton("Actualizar");
        renderButton.addActionListener(e -> renderizarMandelbrot());

        controlPanel.add(numThreadsLabel);
        controlPanel.add(spinner);
        controlPanel.add(renderButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void renderizarMandelbrot() {
        tasksCompleted = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(numTrabajadores);

        int chunkHeight = image.getHeight() / numTrabajadores;

        for (int i = 0; i < numTrabajadores; i++) {
            final int startY = i * chunkHeight;
            final int endY = (i + 1 == numTrabajadores) ? image.getHeight() : (i + 1) * chunkHeight;
            executorService.execute(() -> calcularMandelBrot(startY, endY));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        repaint();
    }

    private void calcularMandelBrot(int startY, int endY) {
        for (int y = startY; y < endY; y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                double zx = 0;
                double zy = 0;
                double cX = (x - 400) / 200.0;
                double cY = (y - 300) / 200.0;
                int iter = 0;
                double tmp;
                while (zx * zx + zy * zy < 4 && iter < 100) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter++;
                }
                image.setRGB(x, y, iter | (iter << 8));
            }
        }

        tasksCompleted.incrementAndGet();

        if (tasksCompleted.get() == numTrabajadores) {
            notifyRenderingComplete();
        }
    }

    private synchronized void notifyRenderingComplete() {

        //Aquí repinta cada vez que se modifica el numero de trabajadores al pulsa actualizar
        repaint();

        System.out.println("Mandelbrot actualizado con " + numTrabajadores + " trabajadores");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);

    }
}
