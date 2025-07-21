package io.github.duckulus.synsniff.api.impl;

import io.github.duckulus.synsniff.core.SynFingerprint;
import io.github.duckulus.synsniff.core.os.OS;
import io.github.duckulus.synsniff.api.FingerprintService;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractFingerprintservice implements FingerprintService {

  @Override
  public Map<OS, Double> getOperatingSystemConfidenceMap(Player player) {
    EnumMap<OS, Double> confidence = new EnumMap<>(OS.class);
    SynFingerprint fp = getFingerprint(player);
    for (OS os : OS.values()) {
      confidence.put(os, os.getReferenceFingerprint().matchScore(fp));
    }
    return confidence;
  }

}
