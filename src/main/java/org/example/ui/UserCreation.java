package org.example.ui;

import org.example.services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class UserCreation extends ALoggedInPage {

    private static final int MAX_FORM_WIDTH = 450;
    private static final int MAX_FORM_HEIGHT = 300;

    private JTextField username;
    private JPasswordField password;
    private JPasswordField confirmPassword;
    private JCheckBox isAdmin;

    public UserCreation(JFrame frame, UserService userService) {
        super(frame, userService);

        JLabel title = new JLabel("Create User", JLabel.CENTER);
        UiUtilities.setFont(title, 30, Font.BOLD);
        UiUtilities.centerElement(title);
        add(title);

        add(Box.createVerticalStrut(30));

        add(createFormPanel());

        add(Box.createVerticalStrut(40));

        JButton createButton = new JButton("Create");
        UiUtilities.setFont(createButton, 20);
        UiUtilities.centerElement(createButton);
        createButton.setMaximumSize(new Dimension(200, 35));
        createButton.addActionListener(this::onCreateClicked);
        add(createButton);

        add(new JPanel());

        addUserActionsBar(SelectedPage.USER_CREATION);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        UiUtilities.setVerticalLayout(formPanel);
        UiUtilities.centerElement(formPanel);
        formPanel.setMaximumSize(new Dimension(MAX_FORM_WIDTH, MAX_FORM_HEIGHT));

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

        formPanel.add(Box.createVerticalStrut(25));

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        UiUtilities.setFont(confirmPasswordLabel, 15);
        formPanel.add(confirmPasswordLabel);

        confirmPassword = new JPasswordField();
        UiUtilities.setFont(confirmPassword, 20);
        formPanel.add(confirmPassword);

        formPanel.add(Box.createVerticalStrut(25));

        JLabel isAdminLabel = new JLabel("Create Admin user:");
        UiUtilities.setFont(isAdminLabel, 15);
        formPanel.add(isAdminLabel);

        isAdmin = new JCheckBox();
        formPanel.add(isAdmin);

        return formPanel;
    }

    private void onCreateClicked(ActionEvent event) {
        String passwordValue = new String(password.getPassword());
        String confirmPasswordValue = new String(confirmPassword.getPassword());

        if(!Objects.equals(passwordValue, confirmPasswordValue)) {
            // TODO: error message
            return;
        }

        userService.createUser(username.getText(), passwordValue, isAdmin.isSelected());

        username.setText("");
        password.setText("");
        confirmPassword.setText("");
        isAdmin.setSelected(false);
    }
}
