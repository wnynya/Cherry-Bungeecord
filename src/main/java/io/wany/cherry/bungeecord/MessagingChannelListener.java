package io.wany.cherry.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class MessagingChannelListener implements Listener {

  @EventHandler
  public void onPluginMessage(final PluginMessageEvent event) {

    if ( ProxyServer.getInstance().getPlayers().isEmpty()) { return; }

    ServerInfo server = null;

    for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
      if (s.getSocketAddress().equals(event.getSender().getSocketAddress())) {
        server = s;
      }
    }

    if (server == null) { return; }

    try {
      ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

      String subChannel = in.readUTF();

      switch (subChannel) {

        case "ChannelChat": {
          ByteArrayDataOutput out = ByteStreams.newDataOutput();

          out.writeUTF(subChannel);
          String channel = in.readUTF();
          String playerName = in.readUTF();
          String msg = in.readUTF();
          String fromServer = server.getName();

          out.writeUTF(channel);
          out.writeUTF(playerName);
          out.writeUTF(msg);
          out.writeUTF(fromServer);
          for (Map.Entry<String, ServerInfo> map : ProxyServer.getInstance().getServers().entrySet()) {
            if (map.getValue().getSocketAddress().equals(event.getSender().getSocketAddress())) {
              continue;
            }
            if (map.getValue().getPlayers().size() < 1) {
              continue;
            }
            map.getValue().sendData("cherry:channel", out.toByteArray());
          }
          break;
        }

        case "VaultEconomySync": {
          ByteArrayDataOutput out = ByteStreams.newDataOutput();
          out.writeUTF(subChannel);
          out.writeUTF(in.readUTF());
          out.writeDouble(in.readDouble());
          for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
            if (s.getName().equals(server.getName())) {
              continue;
            }
            else {
              MessagingChannel.send(Cherry.CHANNEL, out);
            }
          }
          break;
        }

      }

    } catch (Exception ignored) {}
  }

}