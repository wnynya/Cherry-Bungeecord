package io.wany.cherry.bungeecord.event;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.wany.cherry.bungeecord.proxy.Proxy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Collection;
import java.util.Map;

public class PlayerDisconnect implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerDisconnect(final PlayerDisconnectEvent event) {

    Proxy.onPlayerDisconnect(event);

    Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
    if (networkPlayers == null || networkPlayers.isEmpty()) {
      return;
    }

    ByteArrayDataOutput out = ByteStreams.newDataOutput();

    out.writeUTF("NetworkQuit");
    out.writeUTF(event.getPlayer().getName());
    for (Map.Entry<String, ServerInfo> map : ProxyServer.getInstance().getServers().entrySet()) {
      ServerInfo s = map.getValue();
      s.ping((serverPing, throwable) -> {
        if (serverPing == null) {
          return;
        }
        if (serverPing.getPlayers().getOnline() >= 1) {
          s.sendData("cherry:channel", out.toByteArray());
        }
      });
    }

  }

}
