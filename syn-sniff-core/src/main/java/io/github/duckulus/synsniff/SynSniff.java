package io.github.duckulus.synsniff;

import io.github.duckulus.synsniff.api.SynSniffApi;
import io.github.duckulus.synsniff.api.impl.LocalFingerprintService;
import io.github.duckulus.synsniff.api.impl.SynSniffApiImpl;
import io.github.duckulus.synsniff.config.SynSniffConfig;
import io.github.duckulus.synsniff.exception.SynSniffException;
import io.github.duckulus.synsniff.sniffing.SynPacketSniffer;
import io.github.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;

import java.time.Duration;

public final class SynSniff {

  private static SynSniff INSTANCE;

  public static SynSniff get() {
    return INSTANCE;
  }

  private final SynSniffApiImpl api;
  private final SynPacketSniffer sniffer;
  private final CachedPayloadHandler payloadHandler;

  public static SynSniff initialize(SynSniffConfig config) {
    if (INSTANCE != null) {
      throw new SynSniffException("Already initialized");
    }
    INSTANCE = new SynSniff(config);
    return INSTANCE;
  }

  private SynSniff(SynSniffConfig config) {
    sniffer = SynPacketSniffer.run(config.interfaceName(), config.port());
    payloadHandler = CachedPayloadHandler.withExpiry(Duration.ofSeconds(20));
    sniffer.registerPayloadHandler(payloadHandler);

    api = new SynSniffApiImpl();
  }

  public SynSniffApi api() {
    return api;
  }

  public LocalFingerprintService fingerprintService() {
    return api.getFingerprintService();
  }

  public CachedPayloadHandler payloadHandler() {
    return payloadHandler;
  }

  public void close() {
    sniffer.close();
  }

  public static SynSniffApi getApi() {
    return INSTANCE.api;
  }

}
