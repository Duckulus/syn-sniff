package io.github.duckulus.synsniff.velocity.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.duckulus.synsniff.commands.PredictOsExecutor;
import io.github.duckulus.synsniff.misc.Chat;
import io.github.duckulus.synsniff.misc.Permissions;
import io.github.duckulus.synsniff.velocity.utils.CommandUtil;

import java.util.Optional;

public class PredictOsCommand {

  public static BrigadierCommand createCommand(ProxyServer server) {
    LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder(PredictOsExecutor.COMMAND_NAME)
            .requires(source -> source.hasPermission(Permissions.COMMAND_PREDICTOS.getNode()))
            .then(BrigadierCommand.requiredArgumentBuilder("target", StringArgumentType.word())
                    .suggests(CommandUtil.suggestOnlinePlayers(server))
                    .executes(ctx -> {
                      String targetUsername = ctx.getArgument("target", String.class);
                      Optional<Player> target = server.getPlayer(targetUsername);
                      if (target.isEmpty()) {
                        Chat.mm(ctx.getSource(), "%sPlayer is not online".formatted(Chat.WARNING));
                        return Command.SINGLE_SUCCESS;
                      }

                      PredictOsExecutor.execute(ctx.getSource(), target.get().getUsername(), target.get().getUniqueId());

                      return Command.SINGLE_SUCCESS;
                    })
            )
            .build();
    return new BrigadierCommand(node);
  }

}
