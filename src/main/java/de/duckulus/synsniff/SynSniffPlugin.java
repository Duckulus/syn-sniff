package de.duckulus.synsniff;

import de.duckulus.synsniff.listener.ConnectionListener;
import de.duckulus.synsniff.sniffing.SynPacketSniffer;
import de.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class SynSniffPlugin extends JavaPlugin {

  private SynPacketSniffer sniffer;
  private CachedPayloadHandler payloadHandler;

  @Override
  public void onEnable() {
    sniffer = SynPacketSniffer.run("enp34s0", getServer().getPort());
    payloadHandler = CachedPayloadHandler.withExpiry(Duration.ofSeconds(20));
    sniffer.registerPayloadHandler(payloadHandler);

    getServer().getPluginManager().registerEvents(new ConnectionListener(payloadHandler), this);
  }

  @Override
  public void onDisable() {
    sniffer.close();
  }
}
