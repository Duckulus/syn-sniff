package de.duckulus.synsniff.misc;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtil {

  public static String getEnumName(Enum<?> enumValue) {
    return Arrays.stream(enumValue.name().split("_"))
            .map(String::toLowerCase)
            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
            .collect(Collectors.joining(" "));
  }

}
