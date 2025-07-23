package io.github.duckulus.synsniff.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import io.github.duckulus.synsniff.api.impl.LocalFingerprintService;
import io.github.duckulus.synsniff.core.SynFingerprint;
import io.github.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;
import io.github.duckulus.synsniff.sniffing.net.ConnectionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

public class ConnectionListener {

  private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);

  private final CachedPayloadHandler payloadHandler;
  private final LocalFingerprintService localFingerprintService;

  public ConnectionListener(CachedPayloadHandler payloadHandler, LocalFingerprintService localFingerprintService) {
    this.payloadHandler = payloadHandler;
    this.localFingerprintService = localFingerprintService;
  }

  @Subscribe(priority = 10_000)
  public void onLogin(LoginEvent event) {
    InetSocketAddress socketAddr = event.getPlayer().getRemoteAddress();
    if (!(socketAddr.getAddress() instanceof Inet4Address addr)) {
      log.warn("Player tried logging in using IPv6");
      return;
    }
    ConnectionId cid = new ConnectionId(addr, socketAddr.getPort());
    System.out.println(cid);
    Optional<SynFingerprint> fp = payloadHandler.getCachedFingerprint(cid);

    String username = event.getPlayer().getUsername();
    UUID userId = event.getPlayer().getUniqueId();
    if (fp.isEmpty()) {
      log.warn("Could not find fingerprint for {} ({})", username, userId);
      return;
    }
    localFingerprintService.addFingerprint(userId, fp.get());
  }

  @Subscribe
  public void onLogout(DisconnectEvent event) {
    localFingerprintService.removeFingerprint(event.getPlayer().getUniqueId());
  }

}
