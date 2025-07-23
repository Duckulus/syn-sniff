package io.github.duckulus.synsniff.commands;

import io.github.duckulus.synsniff.SynSniff;
import io.github.duckulus.synsniff.core.os.OS;
import io.github.duckulus.synsniff.misc.Chat;
import io.github.duckulus.synsniff.misc.StringUtil;
import net.kyori.adventure.audience.Audience;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PredictOsExecutor {

  public static final String COMMAND_NAME = "predictos";

  /**
   * Executed the OS prediction command
   *
   * @param sender     the sender of the command
   * @param targetName name of online target
   * @param targetId   uuid of online target
   */
  public static void execute(Audience sender, String targetName, UUID targetId) {
    Map<OS, Double> prediction = SynSniff.getApi().getFingerprintService().getOperatingSystemConfidenceMap(targetId);
    List<OS> sorted = prediction.keySet().stream().sorted(Comparator.comparingDouble((OS os) -> prediction.get(os)).reversed()).toList();

    Chat.mm(sender, "%sOS prediction for <i>%s:".formatted(Chat.PREFIX, targetName));
    boolean first = true;
    for (OS os : sorted) {
      Chat.mm(sender, "<aqua>%s%s<reset><aqua> - %d%%"
              .formatted(first ? "<b>" : "", StringUtil.getEnumName(os), Math.round(prediction.get(os) * 100)));
      first = false;
    }

  }

}
