package graphic;

import world.Environment;

import javax.swing.*;

public class EnvironmentDisplayer {
    JFrame frame;

    public EnvironmentDisplayer() {
        frame = new JFrame("Mars Rover Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
    }

    public void display(Environment env) {
        Points points = new Points(env);
        frame.add(points);
        frame.setVisible(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
