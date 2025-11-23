package org.example.ui;

import org.example.services.ImageService;
import org.example.services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ALoggedInPage extends JPanel {

    protected final JFrame frame;
    protected final UserService userService;
    protected final ImageService imageService;

    protected ALoggedInPage(JFrame frame, UserService userService, ImageService imageService) {
        this.frame = frame;
        this.userService = userService;
        this.imageService = imageService;

        UiUtilities.setVerticalLayout(this);

        addWelcomeLabel();
    }

    protected void addUserActionsBar(SelectedPage selectedPage) {
        JPanel userActionsPanel = new JPanel();
        userActionsPanel.setLayout(new BoxLayout(userActionsPanel, BoxLayout.X_AXIS));

        if(userService.isAdmin()) {
            userActionsPanel.add(createUserActionButton("Browse images", this::onImageBrowserClicked, selectedPage != SelectedPage.IMAGE_BROWSER));
            userActionsPanel.add(createUserActionButton("Create user", this::onUserCreationClicked, selectedPage != SelectedPage.USER_CREATION));
            userActionsPanel.add(createUserActionButton("Manage permissions", this::onPermissionManagerClicked, selectedPage != SelectedPage.PERMISSION_MANAGER));
        }
        userActionsPanel.add(createUserActionButton("Logout", this::onLogoutClicked, true));

        add(Box.createVerticalStrut(30));
        add(userActionsPanel);
        add(Box.createVerticalStrut(30));
    }

    protected void addWelcomeLabel() {
        add(Box.createVerticalStrut(20));

        JLabel welcomeMessage = new JLabel(getWelcomeMessage());
        UiUtilities.setFont(welcomeMessage, 25);
        UiUtilities.centerElement(welcomeMessage);
        add(welcomeMessage);

        add(Box.createVerticalStrut(20));
    }

    private JButton createUserActionButton(String label, ActionListener actionListener, boolean enabled) {
        JButton button = new JButton(label);
        UiUtilities.setFont(button, 15);
        button.setMaximumSize(new Dimension(200, 35));
        button.setEnabled(enabled);
        button.addActionListener(actionListener);
        return button;
    }

    private void onImageBrowserClicked(ActionEvent event) {
        UiUtilities.switchMainPanel(frame, new ImageBrowser(frame, userService, imageService));
    }

    private void onUserCreationClicked(ActionEvent event) {
        UiUtilities.switchMainPanel(frame, new UserCreation(frame, userService, imageService));
    }

    private void onPermissionManagerClicked(ActionEvent event) {
        UiUtilities.switchMainPanel(frame, new PermissionManager(frame, userService, imageService));
    }

    private void onLogoutClicked(ActionEvent event) {
        userService.logout();
        UiUtilities.switchMainPanel(frame, new Login(frame, userService, imageService));
    }

    private String getWelcomeMessage() {
        StringBuilder sb = new StringBuilder("Welcome ");
        sb.append(userService.getUsername());
        sb.append("!");

        if(userService.isAdmin()) {
            sb.append(" (Admin user)");
        }

        return sb.toString();
    }

}
