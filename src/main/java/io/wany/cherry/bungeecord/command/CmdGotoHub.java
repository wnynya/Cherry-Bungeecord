package io.wany.cherry.bungeecord.command;

import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdGotoHub extends Command {

  private String server;
  public CmdGotoHub() { super("goto", "cherry.bungeecord.goto.hub", "lobby"); }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    ProxiedPlayer player;

    if (commandSender instanceof ProxiedPlayer) {
      player = (ProxiedPlayer) commandSender;
    }
    else {
      return;
    }

    if (!player.hasPermission("cherry.bungeecord.goto.hub")) {
      return;
    }

    String targetServerName = Cherry.CONFIG.getString("hub.server");
    ServerInfo target = ProxyServer.getInstance().getServerInfo(targetServerName);

    if (player.getServer().getInfo().equals(target)) {
      Message.info(player, Cherry.CONFIG.getString("hub.message.already"));
      return;
    }

    Message.info(player, Cherry.CONFIG.getString("hub.message.connect"));
    player.connect(target);
  }

  public static void onEnable() {
    if (Cherry.CONFIG.getBoolean("hub.enable")) {
      Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdGotoHub());
    }
  }

}
