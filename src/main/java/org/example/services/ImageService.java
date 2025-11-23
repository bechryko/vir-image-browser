package org.example.services;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageService {

    public static final List<String> FORBIDDEN_EXTENSIONS = List.of("webp");
    private static final String IMAGE_DIR = "img";

    private final UserService userService;

    public ImageService(UserService userService) {
        this.userService = userService;
    }

    public List<String> collectImagePaths() {
        List<String> imagePaths = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(IMAGE_DIR))) {
            imagePaths = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(pathAsString -> {
                        String extension = FilenameUtils.getExtension(pathAsString);
                        return FORBIDDEN_EXTENSIONS.contains(extension) || userService.hasPermission(extension);
                    })
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return imagePaths;
    }

    public Set<String> collectUniqueImageExtensions() {
        return collectImagePaths().stream()
                .map(FilenameUtils::getExtension)
                .collect(Collectors.toSet());
    }

}
