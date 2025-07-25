package io.github.duckulus.synsniff.paper.commands;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.duckulus.synsniff.api.FingerprintService;
import io.github.duckulus.synsniff.core.FingerprintField;
import io.github.duckulus.synsniff.core.SynFingerprint;
import io.github.duckulus.synsniff.misc.Chat;
import io.github.duckulus.synsniff.misc.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

public class FingerprintCommand {

  public static LiteralCommandNode<CommandSourceStack> createCommand(String commandName, FingerprintService fingerprintService) {
    return Commands.literal(commandName)
            .requires(sender -> sender.getSender().hasPermission(Permissions.COMMAND_FINGERPRINT.getNode()))
            .then(Commands.argument("target", ArgumentTypes.player())
                    .executes(ctx -> {
                      PlayerSelectorArgumentResolver playerSelector = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                      Player target = playerSelector.resolve(ctx.getSource()).getFirst();

                      SynFingerprint fp = fingerprintService.getFingerprint(target.getUniqueId());
                      ctx.getSource().getSender().sendRichMessage(Chat.PREFIX + "Fingerprint for <i>" + target.getName() + ":");

                      for (FingerprintField field : FingerprintField.values()) {
                        ctx.getSource().getSender().sendRichMessage("<color:#2ea1ff>%s <white>= <aqua>%s"
                                .formatted(field.prettyName(), field.get(fp.data()).map(Object::toString).orElse("<red>N/A")));
                      }
                      return Command.SINGLE_SUCCESS;
                    }))
            .build();
  }

}
