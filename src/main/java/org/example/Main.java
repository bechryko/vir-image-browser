package org.example;

import org.example.ui.Login;
import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    public static void main() {

        JFrame frame = new JFrame("VIR Képböngésző - Kozma Kristóf, UQ13LD");
        centerJFrame(frame);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        frame.add(new Login());

        frame.setVisible(true);
    }

    private static void centerJFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        int frameWidth = 640;
        int frameHeight = 480;

        frame.setBounds(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2, frameWidth, frameHeight);
    }

}
