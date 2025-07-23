package io.github.duckulus.synsniff.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.duckulus.synsniff.SynSniff;
import io.github.duckulus.synsniff.api.impl.LocalFingerprintService;
import io.github.duckulus.synsniff.config.SynSniffConfig;
import io.github.duckulus.synsniff.misc.FileUtil;
import io.github.duckulus.synsniff.sniffing.handler.CachedPayloadHandler;
import io.github.duckulus.synsniff.velocity.listeners.ConnectionListener;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class SynSniffVelocityPlugin {

  private static final String CONFIG_FILE_NAME = "config.yml";

  private SynSniff synSniff;

  private final ProxyServer server;
  private final Logger logger;
  private final Path dataDirectory;

  @Inject
  public SynSniffVelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
    this.server = server;
    this.logger = logger;
    this.dataDirectory = dataDirectory;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    saveDefaultConfig();
    SynSniffConfig config;
    try {
      config = loadConfig();
    } catch (ConfigurateException e) {
      logger.error("Error loading SynSniff config", e);
      return;
    }

    synSniff = SynSniff.initialize(config);
    LocalFingerprintService fingerprintService = synSniff.fingerprintService();
    CachedPayloadHandler payloadHandler = synSniff.payloadHandler();

    server.getEventManager().register(this, new ConnectionListener(payloadHandler, fingerprintService));
  }

  @Subscribe
  public void onShutdown(ProxyShutdownEvent event) {
    synSniff.close();
  }

  private void saveDefaultConfig() {
    try {
      FileUtil.saveResource(CONFIG_FILE_NAME, dataDirectory.resolve(CONFIG_FILE_NAME));
    } catch (IOException e) {
      logger.error("There was an error saving the default config", e);
    }
  }

  private SynSniffConfig loadConfig() throws ConfigurateException {
    CommentedConfigurationNode root = YamlConfigurationLoader.builder()
            .path(dataDirectory.resolve(CONFIG_FILE_NAME))
            .build().load();

    String interfaceName = root.node(SynSniffConfig.INTERFACE_NAME_NODE).getString("");
    int port = server.getBoundAddress().getPort();
    return new SynSniffConfig(interfaceName, port);
  }

}
