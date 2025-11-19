package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class UiUtilities {

    public static final String FONT_NAME = "Segoe UI";
    public static final int WINDOW_BORDER_SIZE = 30;

    public static void centerElement(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public static void setFont(JComponent component, int fontSize) {
        setFont(component, fontSize, Font.PLAIN);
    }

    public static void setFont(JComponent component, int fontSize, int style) {
        component.setFont(new Font(UiUtilities.FONT_NAME, style, fontSize));
    }

    public static void setVerticalLayout(JComponent component) {
        component.setLayout(new BoxLayout(component, BoxLayout.Y_AXIS));
    }

}
