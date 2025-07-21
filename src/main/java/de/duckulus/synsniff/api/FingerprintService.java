package de.duckulus.synsniff.api;

import de.duckulus.synsniff.core.SynFingerprint;
import de.duckulus.synsniff.core.os.OS;
import org.bukkit.entity.Player;

import java.util.Map;

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
  Map<OS, Double> getOperatingSystemConfidenceMap(Player player);

  /**
   * Analyses the players fingerprint in order to guess which operating system he currently uses
   *
   * @param player A player which is currently online on the server
   * @return The Operating System the player most likely uses based on the analysis
   */
  default OS getPredictedOperatingSystem(Player player) {
    return getOperatingSystemConfidenceMap(player).entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow(
            () -> new IllegalStateException("OS Analysis gave empty Map")
    ).getKey();

  }

}
