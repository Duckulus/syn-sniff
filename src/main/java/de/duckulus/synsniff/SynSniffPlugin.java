package de.duckulus.synsniff;

import de.duckulus.synsniff.config.SynSniffConfig;
import de.duckulus.synsniff.listener.ConnectionListener;
import de.duckulus.synsniff.service.impl.LocalFingerprintService;
import de.duckulus.synsniff.sniffing.SynPacketSniffer;
import de.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class SynSniffPlugin extends JavaPlugin {

  private final LocalFingerprintService fingerprintService = new LocalFingerprintService();

  private SynPacketSniffer sniffer;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    SynSniffConfig config = SynSniffConfig.fromConfig(getConfig());

    sniffer = SynPacketSniffer.run(config.interfaceName(), getServer().getPort());
    CachedPayloadHandler payloadHandler = CachedPayloadHandler.withExpiry(Duration.ofSeconds(20));
    sniffer.registerPayloadHandler(payloadHandler);

    getServer().getPluginManager().registerEvents(new ConnectionListener(payloadHandler, fingerprintService), this);
  }

  @Override
  public void onDisable() {
    sniffer.close();
  }
}
