package io.wany.cherry.bungeecord.command;

import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdGotoPlayground extends Command {

  public CmdGotoPlayground() {
    super("playground", "cherry.bungeecord.goto.playground", new String[]{"play"});
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    ProxiedPlayer player = null;
    if (commandSender instanceof ProxiedPlayer) {
      player = (ProxiedPlayer) commandSender;
    }
    else {
      return;
    }

    if (!player.hasPermission("cherry.bungeecord.goto.playground")) {
      return;
    }

    if (player.getServer().getInfo().getName().equalsIgnoreCase("playground")) {
      Message.error(player, "이미 놀이터 서버에 접속한 상태입니다!");
      return;
    }

    Message.info(player, "#D2B0DD;놀이터 서버로 이동합니다...");
    ServerInfo target = ProxyServer.getInstance().getServerInfo("playground");
    player.connect(target);

  }

}
