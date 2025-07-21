package de.duckulus.synsniff.listener;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import de.duckulus.synsniff.core.os.ReferenceFingerprints;
import de.duckulus.synsniff.core.SynFingerprint;
import de.duckulus.synsniff.service.impl.LocalFingerprintService;
import de.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;
import de.duckulus.synsniff.sniffing.net.ConnectionId;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Optional;

public class ConnectionListener implements Listener {

  private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);

  private final CachedPayloadHandler payloadHandler;
  private final LocalFingerprintService localFingerprintService;

  public ConnectionListener(CachedPayloadHandler payloadHandler, LocalFingerprintService localFingerprintService) {
    this.payloadHandler = payloadHandler;
    this.localFingerprintService = localFingerprintService;
  }

  @EventHandler
  @SuppressWarnings("UnstableApiUsage")
  public void onPreLogin(AsyncPlayerPreLoginEvent event) {
    InetSocketAddress socketAddr = event.getConnection().getClientAddress();
    if (!(socketAddr.getAddress() instanceof Inet4Address addr)) {
      log.warn("Player tried logging in using IPv6");
      return;
    }
    ConnectionId cid = new ConnectionId(addr, socketAddr.getPort());
    Optional<SynFingerprint> fp = payloadHandler.getCachedFingerprint(cid);

    PlayerProfile profile = event.getPlayerProfile();
    if (fp.isEmpty()) {
      log.warn("Could not find fingerprint for {} ({})", profile.getName(), profile.getId());
      return;
    }
    localFingerprintService.addFingerprint(profile.getId(), fp.get());
    log.info("{} joined with fingerprint {}", profile.getName(), fp.get());
    log.info("Analysis: Linux ({}%), Windows ({}%), Apple ({}%)",
            ReferenceFingerprints.LINUX.matchScore(fp.get()) * 100,
            ReferenceFingerprints.WINDOWS.matchScore(fp.get()) * 100,
            ReferenceFingerprints.APPLE.matchScore(fp.get()) * 100
    );
  }

  @EventHandler
  public void onDisconnect(PlayerConnectionCloseEvent event) {
    localFingerprintService.removeFingerprint(event.getPlayerUniqueId());
  }

}
