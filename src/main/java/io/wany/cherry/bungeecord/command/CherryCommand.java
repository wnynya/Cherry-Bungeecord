package io.wany.cherry.bungeecord.command;

import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.Message;
import io.wany.cherry.bungeecord.proxy.Proxy;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CherryCommand extends Command {

  public CherryCommand() {
    super("cherrybungeecord", "cherry.bungeecord.cherry", "cherrybungee", "cbg");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {

    if (args.length <= 0) {
      return;
    }

    switch (args[0].toLowerCase()) {

      case "version" -> {
        if (!sender.hasPermission("cherry.bungeecord.version")) {
          return;
        }
        Message.info(sender, Cherry.PREFIX + Cherry.PLUGIN.getDescription().getVersion());
      }

      case "reload" -> {
        if (!sender.hasPermission("cherry.bungeecord.reload")) {
          return;
        }
        Cherry.CONFIG.update();
        Proxy.onReload();
        Message.info(sender, Cherry.PREFIX + "Config Reloaded");
      }

    }

  }

}
