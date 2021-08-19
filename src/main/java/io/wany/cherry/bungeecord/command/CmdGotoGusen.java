package io.wany.cherry.bungeecord.command;

import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdGotoGusen extends Command {

  public CmdGotoGusen() {
    super("gusen", "cherry.bungeecord.goto.gusen");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    ProxiedPlayer player;

    if (commandSender instanceof ProxiedPlayer) {
      player = (ProxiedPlayer) commandSender;
    }
    else {
      return;
    }

    if (!player.hasPermission("cherry.bungeecord.goto.gusen")) {
      return;
    }

    if (player.getServer().getInfo().getName().equalsIgnoreCase("gusen")) {
      Message.info(player, "&c이미 구센 서버에 접속한 상태입니다!");
      return;
    }

    Message.info(player, "#D2B0DD;구센 서버로 이동합니다...");
    ServerInfo target = ProxyServer.getInstance().getServerInfo("gusen");
    player.connect(target);

  }

}

