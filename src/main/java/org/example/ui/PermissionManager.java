package org.example.ui;

import org.example.models.UserModel;
import org.example.services.ImageService;
import org.example.services.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PermissionManager extends ALoggedInPage {

    private static final List<String> DEFAULT_EXTENSIONS = Arrays.asList("png", "jpg", "gif");

    private JButton saveButton;
    private JButton cancelButton;

    private String editedUsername;
    private final Map<String, Boolean> permissionChanges = new HashMap<>();
    private List<String> userPermissions;

    public PermissionManager(JFrame frame, UserService userService, ImageService imageService) {
        super(frame, userService, imageService);

        useUsersPanel();
        frame.add(this);
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Permission Manager", JLabel.CENTER);
        UiUtilities.setFont(title, 30, Font.BOLD);
        UiUtilities.centerElement(title);
        return title;
    }

    private void useUsersPanel() {
        removeAll();
        addWelcomeLabel();
        add(createTitle());
        add(Box.createVerticalStrut(30));

        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        for(UserModel user : userService.listUsers()) {
            usersPanel.add(createUserButton(user));
        }

        add(usersPanel);
        editedUsername = null;

        addUserActionsBar(SelectedPage.PERMISSION_MANAGER);

        frame.revalidate();
        frame.repaint();
    }

    private JButton createUserButton(UserModel user) {
        JButton button = new JButton();
        button.setText(user.username());
        UiUtilities.setFont(button, 15);
        button.setEnabled(!user.isAdmin());
        button.addActionListener(userButtonClickedListenerFactory(user.username()));
        return button;
    }

    private void usePermissionsPanel() {
        removeAll();
        addWelcomeLabel();
        add(createTitle());
        add(Box.createVerticalStrut(30));

        JPanel permissionsPanel = new JPanel();
        UiUtilities.setVerticalLayout(permissionsPanel);

        updateUserPermissionsList();

        for(String permission : getAllPermissions()) {
            permissionsPanel.add(createPermissionPanel(permission));
        }
        add(permissionsPanel);

        add(new JPanel());

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));

        saveButton = new JButton();
        saveButton.setText("Save changes");
        UiUtilities.setFont(saveButton, 15);
        saveButton.setEnabled(!permissionChanges.isEmpty());
        saveButton.addActionListener(this::onSaveButtonClicked);
        actionsPanel.add(saveButton);

        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        UiUtilities.setFont(cancelButton, 15);
        cancelButton.addActionListener(this::onCancelButtonClicked);
        actionsPanel.add(cancelButton);

        add(actionsPanel);

        addUserActionsBar(SelectedPage.PERMISSION_MANAGER);

        frame.revalidate();
        frame.repaint();
    }

    private JPanel createPermissionPanel(String permission) {
        JPanel permissionPanel = new JPanel();
        permissionPanel.setMaximumSize(new Dimension(250, 40));

        JCheckBox permissionCheckbox = new JCheckBox();
        boolean checkedState = userPermissions.contains(permission);
        permissionCheckbox.setSelected(checkedState);
        permissionCheckbox.addActionListener(permissionCheckboxCheckedListenerFactory(permission, checkedState));
        permissionPanel.add(permissionCheckbox);

        JLabel permissionLabel = new JLabel(permission);
        UiUtilities.setFont(permissionLabel, 15);
        permissionPanel.add(permissionLabel);

        return permissionPanel;
    }

    private List<String> getAllPermissions() {
        Set<String> allPermissions = new HashSet<>(DEFAULT_EXTENSIONS);
        allPermissions.addAll(imageService.collectUniqueImageExtensions());
        allPermissions.addAll(userPermissions);
        allPermissions.addAll(
                Stream.of(ImageIO.getReaderFormatNames())
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet())
        );

        ImageService.FORBIDDEN_EXTENSIONS.forEach(allPermissions::remove);

        return allPermissions.stream().sorted().toList();
    }

    private ActionListener userButtonClickedListenerFactory(String username) {
        return (ActionListener) -> {
            editedUsername = username;
            usePermissionsPanel();
        };
    }

    private ActionListener permissionCheckboxCheckedListenerFactory(String permission, boolean checkedState) {
        return (ActionListener) -> {
            if(!permissionChanges.containsKey(permission)) {
                permissionChanges.put(permission, !checkedState);
            } else {
                permissionChanges.remove(permission);
            }

            boolean areChanges = !permissionChanges.isEmpty();
            saveButton.setEnabled(areChanges);
            cancelButton.setText(areChanges ? "Discard changes" : "Cancel");
        };
    }

    private void onSaveButtonClicked(ActionEvent event) {
        for(Map.Entry<String, Boolean> change : permissionChanges.entrySet()) {
            if(change.getValue()) {
                userService.givePermission(editedUsername, change.getKey());
            } else {
                userService.revokePermission(editedUsername, change.getKey());
            }
        }

        permissionChanges.clear();
        useUsersPanel();
    }

    private void onCancelButtonClicked(ActionEvent event) {
        permissionChanges.clear();
        useUsersPanel();
    }

    private void updateUserPermissionsList() {
        userPermissions = userService.listPermissionsForUser(editedUsername);
    }

}
