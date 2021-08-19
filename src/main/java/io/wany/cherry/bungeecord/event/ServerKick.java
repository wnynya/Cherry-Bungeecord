package io.wany.cherry.bungeecord.event;

import io.wany.cherry.bungeecord.proxy.Proxy;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerKick implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onServerKick(final ServerKickEvent event) {

    Proxy.onServerKick(event);

  }

}
