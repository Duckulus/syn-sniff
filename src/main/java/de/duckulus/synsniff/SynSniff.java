package de.duckulus.synsniff;

import de.duckulus.synsniff.api.SynSniffApi;
import de.duckulus.synsniff.api.impl.SynSniffApiImpl;
import de.duckulus.synsniff.commands.FingerprintCommand;
import de.duckulus.synsniff.commands.PredictOsCommand;
import de.duckulus.synsniff.config.SynSniffConfig;
import de.duckulus.synsniff.listener.ConnectionListener;
import de.duckulus.synsniff.sniffing.SynPacketSniffer;
import de.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class SynSniff extends JavaPlugin {

  private static SynSniff INSTANCE;
  private SynSniffApiImpl api;

  private SynPacketSniffer sniffer;

  @Override
  public void onLoad() {
    INSTANCE = this;
  }

  @Override
  public void onEnable() {
    saveDefaultConfig();
    SynSniffConfig config = SynSniffConfig.fromConfig(getConfig());

    sniffer = SynPacketSniffer.run(config.interfaceName(), getServer().getPort());
    CachedPayloadHandler payloadHandler = CachedPayloadHandler.withExpiry(Duration.ofSeconds(20));
    sniffer.registerPayloadHandler(payloadHandler);

    api = new SynSniffApiImpl();

    this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
      commands.registrar().register(PredictOsCommand.createCommand("predictos", api.getFingerprintService()));
      commands.registrar().register(FingerprintCommand.createCommand("fingerprint", api.getFingerprintService()));
    });

    getServer().getPluginManager().registerEvents(new ConnectionListener(payloadHandler, api.getFingerprintService()), this);
  }

  @Override
  public void onDisable() {
    sniffer.close();
  }

  public static SynSniffApi getApi() {
    return INSTANCE.api;
  }

}
