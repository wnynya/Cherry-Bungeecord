package io.wany.cherry.bungeecord.proxy;

import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ProxyCommand extends Command {

  public ProxyCommand() {
    super("proxy", "cherry.bungeecord.proxy");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {

    if (args.length <= 0) {
      return;
    }

    switch (args[0].toLowerCase()) {

      case "reload" -> {
        if (!sender.hasPermission("cherry.bungeecord.proxy.reload")) {
          return;
        }
        Proxy.onReload();
        sender.sendMessage(Message.parse(Message.effect(Proxy.PREFIX + "Proxy Reloaded")));
      }

    }

  }

}
