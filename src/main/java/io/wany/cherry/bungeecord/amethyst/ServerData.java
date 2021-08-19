package io.wany.cherry.bungeecord.amethyst;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerData {

  private static HashMap<String, ServerData> serverDataHashMap = new HashMap<>();
  public ServerInfo serverInfo;
  public ServerPing serverPing = null;
  public boolean isOnline = false;

  private ServerData(ServerInfo serverInfo) {
    this.serverInfo = serverInfo;
  }

  public void ping() {
    String name = serverInfo.getName();
    serverInfo.ping((result, error) -> {
      ServerData serverData = get(name);
      if (serverData != null) {
        serverData.serverPing = result;
        serverData.isOnline = serverData.serverPing != null;
      }
    });
  }

  private static final ExecutorService pingLoopExecutorService = Executors.newFixedThreadPool(1);
  private static final Timer pingLoopTimer = new Timer();
  public static void pingLoop() {
    pingLoopExecutorService.submit(() -> pingLoopTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        for (ServerData serverData : serverDataHashMap.values()) {
          serverData.ping();
        }
      }
    }, 0, 1 * 1000));
  }

  public static ServerData get(String name) {
    if (serverDataHashMap.containsKey(name)) {
      return serverDataHashMap.get(name);
    }
    else if (ProxyServer.getInstance().getServers().containsKey(name)) {
      ServerData serverData = new ServerData(ProxyServer.getInstance().getServers().get(name));
      serverDataHashMap.put(name, serverData);
      return serverData;
    }
    else {
      return null;
    }
  }

  public static boolean exist(String name) {
    return get(name) != null;
  }

  public static void onEnable() {
    for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
      ServerData serverData = new ServerData(ProxyServer.getInstance().getServers().get(serverInfo.getName()));
      serverDataHashMap.put(serverInfo.getName(), serverData);
    }
    pingLoop();
  }

  public static void disable() {
    pingLoopExecutorService.shutdown();
    pingLoopTimer.cancel();
    serverDataHashMap = new HashMap<>();
  }

}
