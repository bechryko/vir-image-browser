package org.example.ui;

import org.example.services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;

public class ImageBrowser extends ALoggedInPage {

    private static final String IMAGE_DIR = "img";
    private static final int IMAGE_SIZE = 200;

    public ImageBrowser(JFrame frame, UserService userService) {
        super(frame, userService);

        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        for(String imagePath : collectImagePaths()) {
            imagesPanel.add(createImageDisplayPanel(imagePath));
        }

        add(imagesPanel);

        add(Box.createVerticalStrut(20));

        addUserActionsBar(SelectedPage.IMAGE_BROWSER);

        frame.add(this);
    }

    private JPanel createImageDisplayPanel(String imagePath) {
        JPanel imageDisplayPanel = new JPanel();
        UiUtilities.setVerticalLayout(imageDisplayPanel);

        Image scaledImage = getScaledImage(imagePath);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        UiUtilities.centerElement(imageLabel);
        JLabel textLabel = new JLabel(FilenameUtils.getBaseName(imagePath));
        UiUtilities.setFont(textLabel, 15);
        UiUtilities.centerElement(textLabel);

        imageDisplayPanel.add(imageLabel);
        imageDisplayPanel.add(textLabel);

        return imageDisplayPanel;
    }

    private Image getScaledImage(String imagePath) {
        Image image = new ImageIcon(imagePath).getImage();
        ImageObserver observer = (Image img, int flags, int x, int y, int width, int height) -> false;
        int width = image.getWidth(observer);
        int height = image.getHeight(observer);

        double horizontalScaleRatio = (double) IMAGE_SIZE / width;
        double verticalScaleRatio = (double) IMAGE_SIZE / height;
        double finalScaleRatio = Math.min(horizontalScaleRatio, verticalScaleRatio);

        int finalWidth = (int) Math.floor(width * finalScaleRatio);
        int finalHeight = (int) Math.floor(height * finalScaleRatio);

        return image.getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH);
    }

    private List<String> collectImagePaths() {
        List<String> imagePaths = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(IMAGE_DIR))) {
            imagePaths = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(pathAsString -> {
                        String extension = FilenameUtils.getExtension(pathAsString);
                        return userService.hasPermission(extension);
                    })
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return imagePaths;
    }

}
