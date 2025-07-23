package io.github.duckulus.synsniff.api.impl;

import io.github.duckulus.synsniff.core.SynFingerprint;
import io.github.duckulus.synsniff.core.os.OS;
import io.github.duckulus.synsniff.api.FingerprintService;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractFingerprintservice implements FingerprintService {

  @Override
  public Map<OS, Double> getOperatingSystemConfidenceMap(UUID playerId) {
    EnumMap<OS, Double> confidence = new EnumMap<>(OS.class);
    SynFingerprint fp = getFingerprint(playerId);
    for (OS os : OS.values()) {
      confidence.put(os, os.getReferenceFingerprint().matchScore(fp));
    }
    return confidence;
  }

}
