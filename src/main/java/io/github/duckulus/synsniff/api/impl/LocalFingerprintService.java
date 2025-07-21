package io.github.duckulus.synsniff.api.impl;

import io.github.duckulus.synsniff.core.SynFingerprint;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LocalFingerprintService extends AbstractFingerprintservice {

  /**
   * Stores fingerprints of online players
   */
  private final Map<UUID, SynFingerprint> fingerprints = new ConcurrentHashMap<>();

  @Override
  public SynFingerprint getFingerprint(Player player) {
    return fingerprints.getOrDefault(player.getUniqueId(), new SynFingerprint(Map.of()));
  }

  public void addFingerprint(UUID uuid, SynFingerprint fingerprint) {
    fingerprints.put(uuid, fingerprint);
  }

  public void removeFingerprint(UUID uuid) {
    fingerprints.remove(uuid);
  }

}
