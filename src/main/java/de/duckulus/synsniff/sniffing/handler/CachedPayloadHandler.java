package de.duckulus.synsniff.sniffing.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.duckulus.synsniff.core.SynFingerprint;
import de.duckulus.synsniff.sniffing.SynPayload;
import de.duckulus.synsniff.sniffing.net.ConnectionId;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;

import java.time.Duration;
import java.util.Optional;

/**
 * This handler listens for SYN packets and transforms them into fingerprints
 * These are then cached for a specified amount of time
 */
public class CachedPayloadHandler implements PayloadHandler {

  public static CachedPayloadHandler withExpiry(Duration duration) {
    return new CachedPayloadHandler(
            Caffeine.newBuilder()
                    .expireAfterWrite(duration)
                    .build()
    );
  }

  private final Cache<ConnectionId, SynFingerprint> fingerprintCache;

  private CachedPayloadHandler(Cache<ConnectionId, SynFingerprint> fingerprintCache) {
    this.fingerprintCache = fingerprintCache;
  }

  @Override
  public void handlePayload(SynPayload payload) {
    IpV4Packet.IpV4Header iph = payload.iph();
    TcpPacket.TcpHeader tcph = payload.tcph();
    ConnectionId cid = new ConnectionId(iph.getSrcAddr(), tcph.getSrcPort().valueAsInt());
    SynFingerprint fp = new SynFingerprint(iph.getTtl(), tcph.getSrcPort().valueAsInt());
    fingerprintCache.put(cid, fp);
  }

  /**
   * Gets the fingerprint for this connectionId if it is currently cached
   */
  public Optional<SynFingerprint> getCachedFingerprint(ConnectionId cid) {
    return Optional.ofNullable(fingerprintCache.getIfPresent(cid));
  }

}
