package org.example.ui;

import org.example.services.ImageService;
import org.example.services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Login extends JPanel {

    private static final int MAX_WIDTH = 450;
    private static final int MAX_HEIGHT = 440;

    private final JFrame frame;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JLabel errorLabel;

    private final UserService userService;
    private final ImageService imageService;

    public Login(JFrame frame, UserService userService, ImageService imageService) {
        this.frame = frame;
        this.userService = userService;
        this.imageService = imageService;

        UiUtilities.setVerticalLayout(this);
        add(Box.createVerticalGlue());
        add(createMainPanel());
        add(Box.createVerticalGlue());
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        UiUtilities.setVerticalLayout(panel);
        UiUtilities.centerElement(panel);
        panel.setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(UiUtilities.WINDOW_BORDER_SIZE, UiUtilities.WINDOW_BORDER_SIZE, UiUtilities.WINDOW_BORDER_SIZE, UiUtilities.WINDOW_BORDER_SIZE));

        JLabel title = new JLabel("Login", JLabel.CENTER);
        UiUtilities.setFont(title, 30, Font.BOLD);
        UiUtilities.centerElement(title);
        panel.add(title);

        panel.add(Box.createVerticalStrut(30));

        panel.add(createFormPanel());

        panel.add(Box.createVerticalStrut(40));

        loginButton = new JButton("Enter");
        UiUtilities.setFont(loginButton, 20);
        UiUtilities.centerElement(loginButton);
        loginButton.setMaximumSize(new Dimension(200, 35));
        loginButton.addActionListener(this::onLoginClicked);
        panel.add(loginButton);

        panel.add(Box.createVerticalStrut(10));

        errorLabel = new JLabel("", JLabel.CENTER);
        UiUtilities.setFont(errorLabel, 15);
        UiUtilities.centerElement(errorLabel);
        errorLabel.setForeground(Color.RED);
        hideError();
        panel.add(errorLabel);

        return panel;
    }

    private void onLoginClicked(ActionEvent event) {
        if(userService.login(username.getText(), new String(password.getPassword()))) {
            UiUtilities.switchMainPanel(frame, new ImageBrowser(frame, userService, imageService));
        } else {
            showError("Cannot log in with these credentials!");
        }
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        UiUtilities.setVerticalLayout(formPanel);
        UiUtilities.centerElement(formPanel);

        JLabel usernameLabel = new JLabel("Username:");
        UiUtilities.setFont(usernameLabel, 15);
        formPanel.add(usernameLabel);

        username = new JTextField();
        UiUtilities.setFont(username, 20);
        formPanel.add(username);

        formPanel.add(Box.createVerticalStrut(25));

        JLabel passwordLabel = new JLabel("Password:");
        UiUtilities.setFont(passwordLabel, 15);
        formPanel.add(passwordLabel);

        password = new JPasswordField();
        UiUtilities.setFont(password, 20);
        formPanel.add(password);

        return formPanel;
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void hideError() {
        errorLabel.setVisible(false);
    }

}
