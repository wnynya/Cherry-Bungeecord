package io.wany.cherry.bungeecord;

import com.google.common.io.ByteArrayDataOutput;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class MessagingChannel {

  public static void register(String channel) {
    Cherry.PLUGIN.getProxy().registerChannel(channel);
    Cherry.PLUGIN.getProxy().getPluginManager().registerListener(Cherry.PLUGIN, new MessagingChannelListener());
  }

  public static void send(String channel, ByteArrayDataOutput message) {
    for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
      server.sendData(channel, message.toByteArray());
    }
  }

  public static void send(String channel, ByteArrayDataOutput message, String server) {
    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server);
    if (serverInfo == null) {
      return;
    }
    send(channel, message, serverInfo);
  }

  public static void send(String channel, ByteArrayDataOutput message, ServerInfo server) {
    server.sendData(channel, message.toByteArray());
  }

  public static void send(ByteArrayDataOutput message, ServerInfo server) {
    server.sendData(Cherry.CHANNEL, message.toByteArray());
  }

}
