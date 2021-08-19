package io.wany.cherry.bungeecord.wanyfield;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.wany.cherry.bungeecord.Message;
import io.wany.cherry.bungeecord.amethyst.ServerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestartCbomb extends Command {

  public RestartCbomb() {
    super("rcb", "cherry.bungeecord.rcb");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {

    if (args.length == 1 && args[0].equals("confirm")) {
      restart();
    }

  }


  public static void restart() {
    String serverName = "cbomb";
    ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);
    Collection<ProxiedPlayer> players = server.getPlayers();
    for (ProxiedPlayer player : players) {
      player.sendMessage(new TextComponent(Message.effect("&c&l[공지]: &r잠시 후 서버가 재시작됩니다.")));
    }
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF( "ServerStop" );
        server.sendData( "cherry:channel", out.toByteArray() );

      }
    }, 1000 * 5);
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF( "ServerStop" );
        server.sendData( "cherry:channel", out.toByteArray() );

        ServerData ls = ServerData.get("lounge");
        ServerData ws = ServerData.get("wanyfield");
        ServerData ps = ServerData.get("playground");
        if (ls != null && ls.isOnline) {
          for (ProxiedPlayer player : players) {
            player.sendMessage(new TextComponent(Message.effect("&c&l[공지]: &r서버 재시작 중... 이동된 임시 서버에서 잠시만 기다려 주십시오.")));
            player.connect(ProxyServer.getInstance().getServerInfo("lounge"));
          }
        }
        else if (ws != null && ws.isOnline) {
          for (ProxiedPlayer player : players) {
            player.sendMessage(new TextComponent(Message.effect("&c&l[공지]: &r서버 재시작 중... 이동된 임시 서버에서 잠시만 기다려 주십시오.")));
            player.connect(ProxyServer.getInstance().getServerInfo("wanyfield"));
          }
        }
        else if (ps != null && ps.isOnline) {
          for (ProxiedPlayer player : players) {
            player.sendMessage(new TextComponent(Message.effect("&c&l[공지]: &r서버 재시작 중... 이동된 임시 서버에서 잠시만 기다려 주십시오.")));
            player.connect(ProxyServer.getInstance().getServerInfo("playground"));
          }
        }
        else {
          for (ProxiedPlayer player : server.getPlayers()) {
            player.disconnect(new TextComponent(Message.effect("게임 서버를 재시작합니다. 다시 접속하여 주십시오.")));
          }
        }

      }
    }, 1000 * 10);
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        for (ProxiedPlayer player : players) {
          player.connect(server);
        }
      }
    }, 1000 * 100);

  }

  private static ExecutorService executorService = Executors.newFixedThreadPool(1);
  private static Timer timer = new Timer();
  public static void onEnable() {

    executorService.submit(() -> {
      try {
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            if (c.get(Calendar.HOUR_OF_DAY) == 5 && c.get(Calendar.MINUTE) == 00) {
              restart();
            }
          }
        }, 0, 1000 * 60);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    });

  }

  public static void onDisable() {

    timer.cancel();
    executorService.shutdown();

  }

}
