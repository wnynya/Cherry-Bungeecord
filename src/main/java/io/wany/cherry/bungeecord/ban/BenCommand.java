package io.wany.cherry.bungeecord.ban;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class BenCommand extends Command implements TabExecutor {

  public BenCommand() {
    super("ben", "cherry.bungeecord.ben", "gben");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {

    if (args.length == 0) {
      return;
    }

    String targetName = args[0];
    ProxiedPlayer target;

    target = ProxyServer.getInstance().getPlayer(targetName);
    if (target == null) {
      UUID uuid = UUID.fromString(targetName);
      target = ProxyServer.getInstance().getPlayer(uuid);
    }
    if (target == null) {
      Message.info(commandSender, Ban.PREFIX + "플레이어를 찾을 수 없습니다");
      return;
    }

    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo("bedserver");

    target.connect(serverInfo);

  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

    if (args.length == 0) {
      return Collections.emptyList();
    }

    else if (args.length == 1) {
      List<String> players = new ArrayList<>();
      for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
        players.add(p.getName());
      }
      return players;
    }

    else {
      return Collections.emptyList();
    }

  }

}
