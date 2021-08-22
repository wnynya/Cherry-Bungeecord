package io.wany.cherry.bungeecord.proxy;

import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.Console;
import io.wany.cherry.bungeecord.Message;
import io.wany.cherry.bungeecord.amethyst.ServerData;
import io.wany.cherry.bungeecord.event.ProxyPing;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.config.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Proxy {

  public static String COLOR = "#00DCF0;";
  public static String PREFIX = COLOR + "&l[Proxy]:&r ";
  public static boolean ENABLED = false;

  public static HashMap<String, Proxy> proxies = new HashMap<>();
  public static HashMap<ProxiedPlayer, ServerInfo> playerDisconnectFrom = new HashMap<>();


  public String host;
  public String port;
  public List<String> servers;
  public ProxyPing proxyPing;
  public String srv = null;

  public Proxy(String host, String port, List<String> servers, ProxyPing proxyPing) {
    this.host = host;
    this.port = port;
    this.servers = servers;
    this.proxyPing = proxyPing;
  }

  public static class ProxyPing {
    public String protocolName;
    public String protocolVersion;
    public List<String> playersServer;
    public String description;
    public String icon;

    public ProxyPing(String protocolName, String protocolVersion, List<String> playersServer, String description, String icon) {
      this.protocolName = protocolName;
      this.protocolVersion = protocolVersion;
      this.playersServer = playersServer;
      this.description = description;
      this.icon = icon;
    }
  }

  public static String proxyPingFormatter(ProxyPingEvent event, String string) {
    // {isonline:server:[online:offline]}
    Pattern isOnlinePattern = Pattern.compile("\\{isonline:([a-zA-Z0-9]+):\\[(.*)\\|(.*)]}");
    Matcher isOnlineMatcher = isOnlinePattern.matcher(string);
    if (isOnlineMatcher.find()) {
      String server = isOnlineMatcher.group(1);
      String whenOnline = isOnlineMatcher.group(2);
      String whenOffline = isOnlineMatcher.group(3);
      if (server != null && ServerData.exist(server)) {
        ServerData serverData = ServerData.get(server);
        if (serverData != null && serverData.isOnline) {
          string = string.replace(isOnlineMatcher.group(0), whenOnline);
        } else {
          string = string.replace(isOnlineMatcher.group(0), whenOffline);
        }
      } else {
        string = string.replace(isOnlineMatcher.group(0), "");
      }
    }

    Pattern isOnlinePattern2 = Pattern.compile("\\{isonline:\\[([a-zA-Z0-9]+)\\|([a-zA-Z0-9]+)]:\\[(.*)\\|(.*)\\|(.*)]}");
    Matcher isOnlineMatcher2 = isOnlinePattern2.matcher(string);
    if (isOnlineMatcher2.find()) {
      String server1 = isOnlineMatcher2.group(1);
      String server2 = isOnlineMatcher2.group(2);
      String whenOnline1 = isOnlineMatcher2.group(3);
      String whenOnline2 = isOnlineMatcher2.group(4);
      String whenOffline = isOnlineMatcher2.group(5);
      if (server1 != null && ServerData.exist(server1)) {
        ServerData serverData = ServerData.get(server1);
        if (serverData != null && serverData.isOnline) {
          string = string.replace(isOnlineMatcher2.group(0), whenOnline1);
        } else if (server2 != null && ServerData.exist(server2)) {
          ServerData serverData2 = ServerData.get(server2);
          if (serverData2 != null && serverData2.isOnline) {
            string = string.replace(isOnlineMatcher2.group(0), whenOnline2);
          } else {
            string = string.replace(isOnlineMatcher2.group(0), whenOffline);
          }
        } else {
          string = string.replace(isOnlineMatcher2.group(0), whenOffline);
        }
      } else {
        string = string.replace(isOnlineMatcher2.group(0), "");
      }
    }

    Pattern onlinePlayersPattern = Pattern.compile("\\{onlineplayers:([a-zA-Z0-9]+)}");
    Matcher onlinePlayersMatcher = onlinePlayersPattern.matcher(string);
    if (onlinePlayersMatcher.find()) {
      String server = onlinePlayersMatcher.group(1);
      if (server.equalsIgnoreCase("{default}")) {
        string = string.replace(onlinePlayersMatcher.group(0), event.getResponse().getPlayers().getOnline() + "");
      } else {
        ServerData serverData = ServerData.get(server);
        if (serverData != null) {
          string = string.replace(onlinePlayersMatcher.group(0), serverData.serverPing.getPlayers().getOnline() + "");
        } else {
          string = string.replace(onlinePlayersMatcher.group(0), "");
        }
      }
    }

    Pattern maxPlayersPattern = Pattern.compile("\\{maxplayers:([a-zA-Z0-9]+)}");
    Matcher maxPlayersMatcher = maxPlayersPattern.matcher(string);
    if (maxPlayersMatcher.find()) {
      String server = maxPlayersMatcher.group(1);
      if (server.equalsIgnoreCase("{default}")) {
        string = string.replace(maxPlayersMatcher.group(0), event.getResponse().getPlayers().getMax() + "");
      } else {
        ServerData serverData = ServerData.get(server);
        if (serverData != null) {
          string = string.replace(maxPlayersMatcher.group(0), serverData.serverPing.getPlayers().getMax() + "");
        } else {
          string = string.replace(maxPlayersMatcher.group(0), "");
        }
      }
    }

    string = Message.effect(string);

    return string;
  }


  public static void onProxyPing(ProxyPingEvent event) {
    if (!ENABLED) {
      return;
    }

    try {
      InetSocketAddress inetSocketAddress = event.getConnection().getVirtualHost();
      String hostname = getHostName(inetSocketAddress);

      Console.log(PREFIX + "ProxyPing: [" + event.getConnection().getSocketAddress().toString() + "] <-> " + hostname);

      Proxy proxy = Proxy.proxies.get(hostname);

      if (proxy == null) {
        ServerPing.Protocol protocol = new ServerPing.Protocol("", 0);
        ServerPing.Players players = new ServerPing.Players(0, 0, null);
        Favicon icon = event.getResponse().getFaviconObject();
        event.setResponse(new ServerPing(protocol, players, Message.parse(""), icon));
        return;
      }

      ServerPing.Protocol protocol = event.getResponse().getVersion();

      String protocolName = proxy.proxyPing.protocolName;
      protocolName = proxyPingFormatter(event, protocolName);
      protocolName = protocolName.replaceAll("\\{default}", protocol.getName());
      protocol.setName(protocolName);

      String protocolVersion = proxy.proxyPing.protocolVersion;
      protocolVersion = proxyPingFormatter(event, protocolVersion);
      protocolVersion = protocolVersion.replaceAll("\\{default}", protocol.getProtocol() + "");
      int protocolVersionNumber = Integer.parseInt(protocolVersion);
      protocol.setProtocol(protocolVersionNumber);

      net.md_5.bungee.api.ServerPing.Players players = event.getResponse().getPlayers();
      for (String serverName : proxy.proxyPing.playersServer) {
        if (serverName.equalsIgnoreCase("{default}")) {
          break;
        }
        ServerData serverData = ServerData.get(serverName);
        if (serverData == null) {
          continue;
        }
        if (serverData.isOnline) {
          players = serverData.serverPing.getPlayers();
          break;
        }
      }

      String descriptionString = proxy.proxyPing.description;
      descriptionString = proxyPingFormatter(event, descriptionString);
      descriptionString = descriptionString.replaceAll("\\{default}", event.getResponse().getDescriptionComponent().toLegacyText());
      BaseComponent description = new TextComponent(TextComponent.fromLegacyText(descriptionString));

      Favicon icon = event.getResponse().getFaviconObject();
      String iconString = proxy.proxyPing.icon;
      iconString = proxyPingFormatter(event, iconString);
      Pattern filePattern = Pattern.compile("\\{file:(.*)}");
      Matcher filePatternMatcher = filePattern.matcher(iconString);
      if (filePatternMatcher.find()) {
        String filepath = filePatternMatcher.group(1);
        File file = new File(filepath);
        if (file.canRead()) {
          BufferedImage bufferedImage = ImageIO.read(file);
          icon = Favicon.create(bufferedImage);
        }
      }

      event.setResponse(new net.md_5.bungee.api.ServerPing(protocol, players, description, icon));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void onServerConnect(ServerConnectEvent event) {
    if (!ENABLED) {
      return;
    }
    ProxiedPlayer player = event.getPlayer();
    ServerConnectEvent.Reason reason = event.getReason();

    switch (reason) {
      case JOIN_PROXY -> {
        InetSocketAddress socketAddress = player.getPendingConnection().getVirtualHost();
        if (socketAddress == null) {
          player.disconnect(Message.parse(Message.effect("Connection refused")));
          event.setCancelled(true);
          return;
        }
        Proxy proxy = proxies.get(getHostName(socketAddress));
        if (proxy == null) {
          event.setCancelled(true);
          player.disconnect(Message.parse(Message.effect("Connection refused")));
          return;
        }
        for (String serverName : proxy.servers) {
          ServerData serverData = ServerData.get(serverName);
          if (serverData != null && serverData.isOnline && serverData.serverInfo.canAccess(player)) {
            Console.log(PREFIX + "ServerConnect: (" + reason.name() + ") " + player.getName() + " Set goto: " + serverData.serverInfo.getName());
            event.setTarget(serverData.serverInfo);
            return;
          }
        }
      }
      case LOBBY_FALLBACK, SERVER_DOWN_REDIRECT, KICK_REDIRECT -> {
        Proxy proxy = proxies.get(getHostName(player.getPendingConnection().getVirtualHost()));
        ServerInfo disconnectedFrom = playerDisconnectFrom.get(player);
        if (disconnectedFrom == null) {
          return;
        }
        for (String serverName : proxy.servers) {
          ServerData serverData = ServerData.get(serverName);
          if (serverData != null && !serverData.serverInfo.equals(disconnectedFrom) && serverData.isOnline && serverData.serverInfo.canAccess(player)) {
            Console.log(PREFIX + "ServerConnect: (" + reason.name() + ") " + player.getName() + " Set goto: " + serverData.serverInfo.getName());
            event.setTarget(serverData.serverInfo);
            return;
          }
        }
      }
      case PLUGIN, UNKNOWN, PLUGIN_MESSAGE, COMMAND -> {
        Console.log(PREFIX + "ServerConnect: (" + reason.name() + ") " + player.getName());
        return;
      }
    }

    event.getPlayer().disconnect();

  }

  public static void onServerKick(ServerKickEvent event) {
    if (!ENABLED) {
      return;
    }
    ProxiedPlayer player = event.getPlayer();
    playerDisconnectFrom.remove(player);
    playerDisconnectFrom.put(player, event.getKickedFrom());
  }

  public static void onServerDisconnect(ServerDisconnectEvent event) {
    if (!ENABLED) {
      return;
    }
    ProxiedPlayer player = event.getPlayer();
    Console.log(PREFIX + "ServerDisconnect: " + player.getName() + " From " + event.getTarget());
  }

  public static void onPlayerDisconnect(PlayerDisconnectEvent event) {
    if (!ENABLED) {
      return;
    }
    ProxiedPlayer player = event.getPlayer();
    playerDisconnectFrom.remove(player);
  }

  public static String getHostName(InetSocketAddress inetSocketAddress) {
    if (inetSocketAddress == null) {
      return "";
    }
    String host = inetSocketAddress.getHostName();
    int port = inetSocketAddress.getPort();
    return host.toUpperCase() + ":" + port;
  }


  public static void onEnable() {

    if (!Cherry.CONFIG.getConfig().getBoolean("proxy.enable")) {
      Console.log(PREFIX + "Proxy disabled");
      return;
    }
    Console.log(PREFIX + "Proxy enabled");
    ENABLED = true;

    for (String hostname : Cherry.CONFIG.getConfig().getSection("proxy.vhosts").getKeys()) {
      Configuration section = Cherry.CONFIG.getConfig().getSection("proxy.vhosts." + hostname);
      hostname = hostname.replaceAll(",", ".").replaceAll(";", ":");
      String host;
      String port;
      Pattern hostnamePattern = Pattern.compile("(.*):([0-9]{1,5})");
      Matcher hostnameMatcher = hostnamePattern.matcher(hostname);
      if (hostnameMatcher.find()) {
        host = hostnameMatcher.group(1);
        port = hostnameMatcher.group(2);
      } else {
        continue;
      }
      Console.log(PREFIX + "Add vhost: " + COLOR + hostname);
      List<String> server = section.getStringList("server");
      String protocolName = section.getString("ping.protocol.name");
      String protocolVersion = section.getString("ping.protocol.version");
      List<String> playersServer = section.getStringList("ping.players");
      String description = section.getString("ping.description");
      String icon = section.getString("ping.icon");
      ProxyPing serverPing = new ProxyPing(protocolName, protocolVersion, playersServer, description, icon);
      Proxy proxy = new Proxy(host, port, server, serverPing);
      proxy.srv = section.getString("srv");
      proxies.put(hostname, proxy);
    }

    Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new ProxyCommand());

  }

  public static void onReload() {

    if (!ENABLED) {
      return;
    }

    proxies.clear();

    for (String hostname : Cherry.CONFIG.getConfig().getSection("proxy.vhosts").getKeys()) {
      Configuration section = Cherry.CONFIG.getConfig().getSection("proxy.vhosts." + hostname);
      hostname = hostname.replaceAll(",", ".").replaceAll(";", ":");
      String host;
      String port;
      Pattern hostnamePattern = Pattern.compile("(.*):([0-9]{1,5})");
      Matcher hostnameMatcher = hostnamePattern.matcher(hostname);
      if (hostnameMatcher.find()) {
        host = hostnameMatcher.group(1);
        port = hostnameMatcher.group(2);
      } else {
        continue;
      }
      Console.log(PREFIX + "Add vhost: " + COLOR + hostname);
      List<String> server = section.getStringList("server");
      String protocolName = section.getString("ping.protocol.name");
      String protocolVersion = section.getString("ping.protocol.version");
      List<String> playersServer = section.getStringList("ping.players");
      String description = section.getString("ping.description");
      String icon = section.getString("ping.icon");
      ProxyPing serverPing = new ProxyPing(protocolName, protocolVersion, playersServer, description, icon);
      Proxy proxy = new Proxy(host, port, server, serverPing);
      proxy.srv = section.getString("srv");
      proxies.put(hostname, proxy);
    }

  }

}
