package de.duckulus.synsniff.service.impl;

import de.duckulus.synsniff.core.SynFingerprint;
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
    return fingerprints.get(player.getUniqueId());
  }

  public void addFingerprint(UUID uuid, SynFingerprint fingerprint) {
    fingerprints.put(uuid, fingerprint);
  }

  public void removeFingerprint(UUID uuid) {
    fingerprints.remove(uuid);
  }

}
