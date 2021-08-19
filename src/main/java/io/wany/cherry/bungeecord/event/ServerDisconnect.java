package io.wany.cherry.bungeecord.event;

import io.wany.cherry.bungeecord.proxy.Proxy;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerDisconnect implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onServerDisconnect(final ServerDisconnectEvent event) {

    Proxy.onServerDisconnect(event);

  }

}
