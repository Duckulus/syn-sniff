package de.duckulus.synsniff.misc;

public enum Permissions {

  COMMAND_FINGERPRINT, COMMAND_PREDICTOS;

  public String getNode() {
    return "synsniff." + name().toLowerCase().replace("_", "-");
  }

}
