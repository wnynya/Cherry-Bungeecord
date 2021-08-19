package io.wany.cherry.bungeecord.ban;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.Console;
import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ban {

  public static String COLOR = "#FF3C4B;";
  public static String PREFIX = COLOR + "&l[Ban]:&r ";

  public static class BannedPlayer {

    public UUID uuid;
    public String name;
    public long expire;
    public String reason;
    public long executeTime;
    public String executor;

    public BannedPlayer(UUID uuid, String name, long expire, String reason, long time, String executor) {
      this.uuid = uuid;
      this.name = name;
      this.expire = expire;
      this.reason = reason;
      this.executeTime = time;
      this.executor = executor;
    }

  }

  public static List<BannedPlayer> bannedPlayers = new ArrayList<>();

  public static File file;

  public static void save(){
    JsonArray array = new JsonArray();
    for (BannedPlayer bannedPlayer : bannedPlayers) {
      JsonObject object = new JsonObject();
      object.addProperty("uuid", bannedPlayer.uuid.toString());
      object.addProperty("name", bannedPlayer.name);
      object.addProperty("expire", bannedPlayer.expire);
      object.addProperty("reason", bannedPlayer.reason);
      object.addProperty("create", bannedPlayer.executeTime);
      object.addProperty("executor", bannedPlayer.executor);
      array.add(object);
    }
    String jst = array.toString();
    try {
      Files.writeString(file.toPath(), jst);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean check(ProxiedPlayer player) {
    UUID uuid = player.getUniqueId();
    for (BannedPlayer b : bannedPlayers) {
      if (b.uuid.equals(uuid)) {
        return true;
      }
    }
    return false;
  }

  public static BannedPlayer get (ProxiedPlayer player) {
    UUID uuid = player.getUniqueId();
    return get(uuid);
  }

  public static BannedPlayer get (String name) {
    for (BannedPlayer b : bannedPlayers) {
      if (b.name.equals(name)) {
        return b;
      }
    }
    return null;
  }

  public static BannedPlayer get (UUID uuid) {
    for (BannedPlayer b : bannedPlayers) {
      if (b.uuid.equals(uuid)) {
        return b;
      }
    }
    return null;
  }

  public static void onServerConnect(ServerConnectEvent event) {
    if (Ban.check(event.getPlayer())) {
      event.setCancelled(true);
      event.getPlayer().disconnect(Message.parse(Message.effect(Ban.get(event.getPlayer()).reason)));
    }
  }

  public static void onEnable() {

    if (Cherry.CONFIG.getConfig().getBoolean("ban.enable")) {
      Console.log(PREFIX + "Ban enabled");
    }
    else {
      Console.log(PREFIX + "Ban disabled");
      return;
    }

    Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdBan());
    Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdUnban());
    Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdBanlist());

    file = new File(Cherry.PLUGIN.getDataFolder().getAbsoluteFile().getParentFile().getParentFile() + "/banned-players.json");
    if (!file.exists()) {
      try {
        file.createNewFile();
        JsonArray a = new JsonArray();
        Files.writeString(file.toPath(), a.toString());
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
    }

    JsonElement sourceObject;
    try {
      String con = Files.readString(file.toPath());
      sourceObject = new JsonParser().parse(con);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    JsonArray sourceArray = sourceObject.getAsJsonArray();
    for (int n = 0; n < sourceArray.size(); n++) {
      JsonElement el = sourceArray.get(n);
      JsonObject obj = el.getAsJsonObject();
      BannedPlayer bannedPlayer = new BannedPlayer(
        UUID.fromString(obj.get("uuid").getAsString()),
        obj.get("name").getAsString(),
        obj.get("expire").getAsLong(),
        obj.get("reason").getAsString(),
        obj.get("create").getAsLong(),
        obj.get("executor").getAsString()
      );
      bannedPlayers.add(bannedPlayer);
    }

    Console.log(PREFIX + "Load banned players list. (" + bannedPlayers.size() + ")");

  }

  public static void onDisable() {
    save();
  }
}
