package io.github.duckulus.synsniff.misc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

  public static void saveResource(String resourcePath, Path outputFile) throws IOException {
    try (InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
      if (in == null) {
        throw new FileNotFoundException("Resource not found: " + resourcePath);
      }

      if (Files.notExists(outputFile)) {
        Files.createDirectories(outputFile.getParent());
        Files.copy(in, outputFile);
      }
    }
  }

}
