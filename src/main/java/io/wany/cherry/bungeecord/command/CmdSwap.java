package io.wany.cherry.bungeecord.command;

import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.Message;
import io.wany.cherry.bungeecord.ban.Ban;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdSwap extends Command implements TabExecutor {

  public CmdSwap() {
    super("bswap", "cherry.bungeecord.swap", "gswap");
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

    if (!player.hasPermission("cherry.bungeecord.swap")) {
      return;
    }

    if (args.length < 1) {
      Message.info(player, "&c스왑할 플래이어를 지정하십시오");
      return;
    }

    ProxiedPlayer target1;
    ProxiedPlayer target2;

    target1 = ProxyServer.getInstance().getPlayer(args[0]);
    if (target1 == null) {
      Message.info(player, "&c플래이어를 찾을 수 없습니다");
      return;
    }

    if (args.length > 1) {
      target2 = ProxyServer.getInstance().getPlayer(args[1]);
      if (target2 == null) {
        Message.info(player, "&c플래이어를 찾을 수 없습니다");
        return;
      }
    }
    else {
      target2 = player;
      if (target1.equals(player)) {
        Message.info(player, "&c자기 자신과는 스왑할 수 없습니다");
        return;
      }
    }

    if (target1.equals(target2)) {
      Message.info(player, "&c서로 같은 플래이어는 스왑할 수 없습니다");
      return;
    }

    ServerInfo server1 = target1.getServer().getInfo();
    ServerInfo server2 = target2.getServer().getInfo();

    if (server1.equals(server2)) {
      Message.info(player, "&c서로 같은 서버에 접속한 플래이어는 스왑할 수 없습니다");
      return;
    }

    target1.connect(server2);
    target2.connect(server1);

  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

    if (args.length == 0) {
      return Collections.emptyList();
    }

    else if (args.length == 1) {
      List<String> unbannedPlayers = new ArrayList<>();
      for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
        if (Ban.get(p) == null) {
          unbannedPlayers.add(p.getName());
        }
      }
      return unbannedPlayers;
    }

    else if (args.length == 2) {
      List<String> unbannedPlayers = new ArrayList<>();
      for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
        if (Ban.get(p) == null) {
          unbannedPlayers.add(p.getName());
        }
      }
      return unbannedPlayers;
    }

    else {
      return Collections.emptyList();
    }

  }

  public static void onEnable() {
    Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdSwap());
  }

}

