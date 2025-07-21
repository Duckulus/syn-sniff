package de.duckulus.synsniff.service;

import de.duckulus.synsniff.core.SynFingerprint;
import de.duckulus.synsniff.core.os.OS;
import org.bukkit.entity.Player;

import java.util.EnumMap;

public interface FingerprintService {

  /**
   * Returns the fingerprint of the specified player
   *
   * @param player A player which is currently online on the server
   * @return The fingerprint
   */
  SynFingerprint getFingerprint(Player player);

  /**
   * Analyses the players fingerprint in order to guess which operating system he currently uses
   *
   * @param player A player which is currently online on the server
   * @return A map from each Operating System to a confidence value between 0 and 1
   */
  EnumMap<OS, Double> predictOperatingSystem(Player player);

}
