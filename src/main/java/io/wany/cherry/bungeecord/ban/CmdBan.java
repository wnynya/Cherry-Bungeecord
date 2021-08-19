package io.wany.cherry.bungeecord.ban;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class CmdBan extends Command implements TabExecutor {

  public CmdBan() {
    super("bban", "cherry.bungeecord.ban", "gban");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {

    if (args.length == 0) {
      return;
    }

    String target = args[0];
    ProxiedPlayer player;

    UUID uuid;
    String name;
    long expires = -1;
    String reason = Cherry.CONFIG.getString("ban.default.reason");
    String executor = "#Server";


    if (commandSender instanceof ProxiedPlayer proxiedPlayer) {
      executor = proxiedPlayer.getName();
    }

    try {
      uuid = UUID.fromString(target);
      player = ProxyServer.getInstance().getPlayer(uuid);
      if (player != null) {
        name = player.getName();
      }
      else {
        JsonObject data;
        try {
          URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
          InputStream is = url.openStream();
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
          StringBuilder content = new StringBuilder();
          String line;
          while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
          }
          bufferedReader.close();
          data = (JsonObject) new JsonParser().parse(String.valueOf(content));
        } catch (Exception ex) {
          Message.info(commandSender, Ban.PREFIX + "플레이어를 찾을 수 없습니다.");
          return;
        }
        String nm = data.get("name").getAsString();
        if (nm == null) {
          Message.info(commandSender, Ban.PREFIX + "플레이어를 찾을 수 없습니다.");
          return;
        }
        name = nm;
      }
    }
    catch (Exception e) {
      player = ProxyServer.getInstance().getPlayer(target);
      if (player != null) {
        name = player.getName();
        uuid = player.getUniqueId();
      }
      else {
        JsonObject data;
        try {
          URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + target + "?at=" + System.currentTimeMillis());
          InputStream is = url.openStream();
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
          StringBuilder content = new StringBuilder();
          String line;
          while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
          }
          bufferedReader.close();
          data = (JsonObject) new JsonParser().parse(String.valueOf(content));
        } catch (Exception ex) {
          Message.info(commandSender, Ban.PREFIX + "플레이어를 찾을 수 없습니다.");
          return;
        }
        String id = data.get("id").getAsString();
        if (id == null) {
          Message.info(commandSender, Ban.PREFIX + "플레이어를 찾을 수 없습니다.");
          return;
        }
        id = id.replaceAll("([0-9a-zA-Z]{8})([0-9a-zA-Z]{4})([0-9a-zA-Z]{4})([0-9a-zA-Z]{4})([0-9a-zA-Z]{12})", "$1-$2-$3-$4-$5");
        uuid = UUID.fromString(id);
        name = target;
      }
    }

    if (Ban.get(uuid) != null) {
      Message.info(commandSender, Ban.PREFIX + "이미 서버 접속 제한 조치된 플레이어입니다.");
      return;
    }

    if (args.length >= 2) {
      String r = "";
      for (int n = 1; n < args.length; n++) {
        r += args[n] + " ";
      }
      reason = Message.effect(r.substring(0, r.length() - 1));
    }

    Ban.BannedPlayer bannedPlayer = new Ban.BannedPlayer(
      uuid,
      name,
      expires,
      reason,
      System.currentTimeMillis(),
      executor
    );

    if (player != null) {
      player.disconnect(new TextComponent(reason));
    }

    Ban.bannedPlayers.add(bannedPlayer);

    Ban.save();

    for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
      Message.info(proxiedPlayer, Ban.PREFIX + name + " (" + uuid + ") 플레이어의 서버 접속이 제한되었습니다. 사유: &7&o" + reason);
    }

    Message.info(commandSender, Ban.PREFIX + name + " (" + uuid + ")의 서버 접속 제한 조치가 완료되었습니다. 사유: &7&o" + reason);

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

    /*else if (args.length == 2) {
      return Arrays.asList("<시간(초)>", "-1");
    }*/

    else if (args.length == 2) {
      return Arrays.asList("<사유>",
        Cherry.CONFIG.getString("ban.default.reason"),
        Cherry.CONFIG.getString("ban.default.reason") + ": 핵 클라이언트 사용",
        Cherry.CONFIG.getString("ban.default.reason") + ": 공개적인 욕설 및 반사회적 발언",
        Cherry.CONFIG.getString("ban.default.reason") + ": 고의적인 서버 운영 방해",
        Cherry.CONFIG.getString("ban.default.reason") + ": 오이가 트롤링함"
      );
    }

    else {
      return Collections.emptyList();
    }

  }

}
