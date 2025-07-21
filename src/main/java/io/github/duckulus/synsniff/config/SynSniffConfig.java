package io.github.duckulus.synsniff.config;

import org.bukkit.configuration.file.FileConfiguration;

public record SynSniffConfig(String interfaceName) {

  private static final String INTERFACE_NAME_KEY = "interface_name";

  public static SynSniffConfig fromConfig(FileConfiguration fileConfig) {
    return new SynSniffConfig(
            fileConfig.getString(INTERFACE_NAME_KEY, "")
    );
  }

}
