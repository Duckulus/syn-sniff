package de.duckulus.synsniff.service.impl;

import de.duckulus.synsniff.core.SynFingerprint;
import de.duckulus.synsniff.core.os.OS;
import de.duckulus.synsniff.service.FingerprintService;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractFingerprintservice implements FingerprintService {

  @Override
  public Map<OS, Double> predictOperatingSystem(Player player) {
    EnumMap<OS, Double> confidence = new EnumMap<>(OS.class);
    SynFingerprint fp = getFingerprint(player);
    for (OS os : OS.values()) {
      confidence.put(os, os.getReferenceFingerprint().matchScore(fp));
    }
    return confidence;
  }

}
