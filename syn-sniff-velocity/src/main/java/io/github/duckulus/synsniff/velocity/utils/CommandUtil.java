package io.github.duckulus.synsniff.velocity.utils;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;

public class CommandUtil {

  public static SuggestionProvider<CommandSource> suggestOnlinePlayers(ProxyServer server) {
    return (ctx, builder) -> {
      server.getAllPlayers().forEach(player -> builder.suggest(player.getUsername()));
      return builder.buildFuture();
    };
  }

}
