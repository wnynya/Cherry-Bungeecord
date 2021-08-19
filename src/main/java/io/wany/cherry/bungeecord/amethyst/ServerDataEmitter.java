package io.wany.cherry.bungeecord.amethyst;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.wany.cherry.bungeecord.Cherry;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.concurrent.TimeUnit;

public class ServerDataEmitter {

  public static void sendServerData(ServerInfo s) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();

    out.writeUTF( "ServerData" );
    out.writeUTF( s.getName() );
    s.ping((serverPing, throwable) -> {
      if (serverPing == null) {
        out.writeUTF( "false" );
      }
      else {
        out.writeUTF( "true" );
        out.writeUTF( serverPing.getPlayers().getOnline() + "");
        out.writeUTF( serverPing.getPlayers().getMax() + "");
      }
      for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
        if (server.getPlayers().size() < 1) { continue; }
        server.sendData( "cherry:channel", out.toByteArray() );
      }
    });
  }

  public static void emitServerData() {
    if (ProxyServer.getInstance().getPlayers().size() < 1) { return; }
    for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
      sendServerData(server);
    }
  }

  public static void loop() {

    ProxyServer.getInstance().getScheduler().schedule(Cherry.PLUGIN, new Runnable() {
      @Override
      public void run() {
        emitServerData();
      }
    }, 0, 10, TimeUnit.SECONDS);

  }

  public static void enable() {
    loop();
  }

}
