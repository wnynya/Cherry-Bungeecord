package io.wany.cherry.bungeecord.event;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.amethyst.ServerDataEmitter;
import io.wany.cherry.bungeecord.ban.Ban;
import io.wany.cherry.bungeecord.MessagingChannel;
import io.wany.cherry.bungeecord.proxy.Proxy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Collection;

public class ServerConnect implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onServerConnect(final ServerConnectEvent event) {

    Ban.onServerConnect(event);
    Proxy.onServerConnect(event);

    Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
    if (networkPlayers == null || networkPlayers.isEmpty()) {
      return;
    }

    ServerInfo from = null;
    ServerInfo to = event.getTarget();
    if (event.getPlayer().getServer() != null) {
      from = event.getPlayer().getServer().getInfo();
    }

    String name = event.getPlayer().getName();
    for (String s : event.getPlayer().getModList().keySet()) {
      String ss = event.getPlayer().getModList().get(s);
      System.out.println("ModList: " + name + " -> " + s + " : " + ss);
    }

    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    if (from != null) {
      out.writeUTF("ServerChangeQuit");
      out.writeUTF(event.getPlayer().getName());
      out.writeUTF(from.getName());
      out.writeUTF(to.getName());
      from.sendData("cherry:channel", out.toByteArray());

      out = ByteStreams.newDataOutput();
      out.writeUTF("ServerChangeJoin");
      out.writeUTF(event.getPlayer().getName());
      out.writeUTF(from.getName());
      out.writeUTF(to.getName());
      to.sendData("cherry:channel", out.toByteArray());

      ServerDataEmitter.sendServerData(from);
      ServerDataEmitter.sendServerData(to);
    } else {
      out.writeUTF("NetworkJoin");
      out.writeUTF(event.getPlayer().getName());
      out.writeUTF(to.getName());
      MessagingChannel.send(Cherry.CHANNEL, out);
      ServerDataEmitter.sendServerData(to);
    }

  }

}
