package io.github.duckulus.synsniff.paper.commands;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.duckulus.synsniff.api.FingerprintService;
import io.github.duckulus.synsniff.core.os.OS;
import io.github.duckulus.synsniff.misc.Chat;
import io.github.duckulus.synsniff.misc.Permissions;
import io.github.duckulus.synsniff.misc.StringUtil;
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
            .requires(sender -> sender.getSender().hasPermission(Permissions.COMMAND_PREDICTOS.getNode()))
            .then(Commands.argument("target", ArgumentTypes.player())
                    .executes(ctx -> {
                      PlayerSelectorArgumentResolver playerSelector = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                      Player target = playerSelector.resolve(ctx.getSource()).getFirst();
                      Map<OS, Double> prediction = fingerprintService.getOperatingSystemConfidenceMap(target.getUniqueId());
                      List<OS> sorted = prediction.keySet().stream().sorted(Comparator.comparingDouble((OS os) -> prediction.get(os)).reversed()).toList();

                      ctx.getSource().getSender().sendRichMessage("%sOS prediction for <i>%s:".formatted(Chat.PREFIX, target.getName()));
                      boolean first = true;
                      for (OS os : sorted) {
                        ctx.getSource().getSender().sendRichMessage("<aqua>%s%s<reset><aqua> - %d%%"
                                .formatted(first ? "<b>" : "", StringUtil.getEnumName(os), Math.round(prediction.get(os) * 100)));
                        first = false;
                      }

                      return Command.SINGLE_SUCCESS;
                    }))
            .build();
  }

}
