package io.github.duckulus.synsniff.misc;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Chat {

  public static final String WARNING = "<red>[Warning] ";
  public static final String PREFIX = "<gradient:#14f5fc:#19fc61:0.2>[SynSniff] <aqua>";


  public static void mm(Audience audience, String message) {
    audience.sendMessage(MiniMessage.miniMessage().deserialize(message, audience));
  }

}
