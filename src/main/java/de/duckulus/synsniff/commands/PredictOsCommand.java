package de.duckulus.synsniff.commands;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.duckulus.synsniff.core.os.OS;
import de.duckulus.synsniff.misc.Chat;
import de.duckulus.synsniff.misc.StringUtil;
import de.duckulus.synsniff.service.FingerprintService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PredictOsCommand {

  public static LiteralCommandNode<CommandSourceStack> createCommand(String commandName, FingerprintService fingerprintService) {
    return Commands.literal(commandName)
            .requires(sender -> sender.getSender().hasPermission("synsniff.predictos"))
            .then(Commands.argument("target", ArgumentTypes.player())
                    .executes(ctx -> {
                      PlayerSelectorArgumentResolver playerSelector = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                      Player target = playerSelector.resolve(ctx.getSource()).getFirst();
                      Map<OS, Double> prediction = fingerprintService.predictOperatingSystem(target);
                      List<OS> sorted = prediction.keySet().stream().sorted(Comparator.comparingDouble((OS os) -> prediction.get(os)).reversed()).toList();

                      ctx.getSource().getSender().sendRichMessage(Chat.PREFIX + "OS prediction for <i>" + target.getName());
                      for (OS os : sorted) {
                        ctx.getSource().getSender().sendRichMessage("<aqua>" + StringUtil.getEnumName(os) + " - " + Math.round(prediction.get(os) * 100) + "%");
                      }

                      return Command.SINGLE_SUCCESS;
                    }))
            .build();
  }

}
