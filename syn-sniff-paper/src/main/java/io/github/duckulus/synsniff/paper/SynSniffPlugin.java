package io.github.duckulus.synsniff.paper;

import io.github.duckulus.synsniff.SynSniff;
import io.github.duckulus.synsniff.api.impl.LocalFingerprintService;
import io.github.duckulus.synsniff.config.SynSniffConfig;
import io.github.duckulus.synsniff.paper.commands.FingerprintCommand;
import io.github.duckulus.synsniff.paper.commands.PredictOsCommand;
import io.github.duckulus.synsniff.paper.listener.ConnectionListener;
import io.github.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SynSniffPlugin extends JavaPlugin {

  private static final String CONFIG_INTERFACE_NAME_KEY = "interface_name";

  private SynSniff synSniff;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    SynSniffConfig config = loadConfig();
    synSniff = SynSniff.initialize(config);

    LocalFingerprintService fingerprintService = synSniff.fingerprintService();
    CachedPayloadHandler payloadHandler = synSniff.payloadHandler();

    getServer().getPluginManager().registerEvents(new ConnectionListener(payloadHandler, fingerprintService), this);

    this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
      commands.registrar().register(PredictOsCommand.createCommand("predictos", fingerprintService));
      commands.registrar().register(FingerprintCommand.createCommand("fingerprint", fingerprintService));
    });
  }

  private SynSniffConfig loadConfig() {
    FileConfiguration fileConfig = getConfig();
    return new SynSniffConfig(
            fileConfig.getString(CONFIG_INTERFACE_NAME_KEY, ""),
            getServer().getPort()
    );
  }

  @Override
  public void onDisable() {
    if (synSniff != null) {
      synSniff.close();
    }

  }
}
