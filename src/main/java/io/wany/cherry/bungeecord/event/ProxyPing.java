package io.wany.cherry.bungeecord.event;

import io.wany.cherry.bungeecord.proxy.Proxy;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyPing implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onProxyPing(final ProxyPingEvent event) {

    Proxy.onProxyPing(event);

  }

}
