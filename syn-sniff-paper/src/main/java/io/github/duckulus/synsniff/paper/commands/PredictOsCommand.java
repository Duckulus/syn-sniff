package io.github.duckulus.synsniff.paper.commands;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.duckulus.synsniff.commands.PredictOsExecutor;
import io.github.duckulus.synsniff.misc.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

public class PredictOsCommand {

  public static LiteralCommandNode<CommandSourceStack> createCommand() {
    return Commands.literal(PredictOsExecutor.COMMAND_NAME)
            .requires(sender -> sender.getSender().hasPermission(Permissions.COMMAND_PREDICTOS.getNode()))
            .then(Commands.argument("target", ArgumentTypes.player())
                    .executes(ctx -> {
                      PlayerSelectorArgumentResolver playerSelector = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                      Player target = playerSelector.resolve(ctx.getSource()).getFirst();

                      PredictOsExecutor.execute(ctx.getSource().getSender(), target.getName(), target.getUniqueId());

                      return Command.SINGLE_SUCCESS;
                    }))
            .build();
  }

}
