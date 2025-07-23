package io.github.duckulus.synsniff.config;

public record SynSniffConfig(String interfaceName, int port) {

  public static final String INTERFACE_NAME_NODE = "interface_name";

}
