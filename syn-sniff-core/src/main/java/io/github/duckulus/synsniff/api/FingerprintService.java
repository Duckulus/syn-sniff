package io.github.duckulus.synsniff.api;

import io.github.duckulus.synsniff.core.SynFingerprint;
import io.github.duckulus.synsniff.core.os.OS;

import java.util.Map;
import java.util.UUID;

public interface FingerprintService {

  /**
   * Returns the fingerprint of the specified player
   *
   * @param playerId The uniqueId of a player which is currently online on the server
   * @return The fingerprint
   */
  SynFingerprint getFingerprint(UUID playerId);

  /**
   * Analyses the players fingerprint in order to guess which operating system he currently uses
   *
   * @param playerId The uniqueId of a player which is currently online on the server
   * @return A map from each Operating System to a confidence value between 0 and 1
   */
  Map<OS, Double> getOperatingSystemConfidenceMap(UUID playerId);

  /**
   * Analyses the players fingerprint in order to guess which operating system he currently uses
   *
   * @param playerId The uniqueId of a player which is currently online on the server
   * @return The Operating System the player most likely uses based on the analysis
   */
  default OS getPredictedOperatingSystem(UUID playerId) {
    return getOperatingSystemConfidenceMap(playerId).entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow(
            () -> new IllegalStateException("OS Analysis gave empty Map")
    ).getKey();

  }

}
