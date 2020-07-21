package com.astraia.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtil {

    public static final String PROCESSED_FILES = "processed.txt";

    private FileUtil() {
    }

    public static List<Path> getFilesList(String directorPath, String extension) throws IOException {
        return Files.list(Paths.get(directorPath))
                .filter(path -> path.toString().endsWith(extension))
                .collect(Collectors.toList());
    }

    public static void markFileProcessed(String inputDirectory, List<String> files) throws IOException {
        Path path = Paths.get(inputDirectory, PROCESSED_FILES);
        if(!path.toFile().exists())
            Files.createFile(path);
        String content = String.join("\n", files);
        content ="\n"+content;
        Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
    }

    public static Set<String> getProcessedFiles(String inputDirectory) throws IOException {
        Path path = Paths.get(inputDirectory, PROCESSED_FILES);
        if(!path.toFile().exists()){
            return new HashSet<>();
        }
        String content = new String(Files.readAllBytes(path));
        return Stream.of(content.split("\n"))
                .map(String::trim)
                .filter(v -> !v.isEmpty())
                .collect(Collectors.toSet());
    }

    public static void saveFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }


}
