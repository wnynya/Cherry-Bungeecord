package io.wany.cherry.bungeecord;

import io.wany.cherry.bungeecord.amethyst.ServerData;
import io.wany.cherry.bungeecord.amethyst.ServerDataEmitter;
import io.wany.cherry.bungeecord.ban.Ban;
import io.wany.cherry.bungeecord.command.CherryCommand;
import io.wany.cherry.bungeecord.command.CmdGotoHub;
import io.wany.cherry.bungeecord.command.CmdSwap;
import io.wany.cherry.bungeecord.event.*;
import io.wany.cherry.bungeecord.proxy.Proxy;
import io.wany.cherry.bungeecord.wanyfield.WanyfieldSystem;
import net.md_5.bungee.api.plugin.Plugin;

public class Cherry extends Plugin {

  public static Cherry PLUGIN;

  public static final String COLOR = "#D2B0DD;";
  public static final String PREFIX = COLOR + "&l[Cherry]:&r ";
  public static boolean DEBUG = false;

  public static Config CONFIG;
  public static String CHANNEL;

  @Override
  public void onEnable() {

    PLUGIN = this;
    CONFIG = Config.get("config");
    CHANNEL = CONFIG.getString("channel");
    DEBUG = CONFIG.getBoolean("debug");

    MessagingChannel.register(CHANNEL);

    Proxy.onEnable();
    Ban.onEnable();

    getProxy().getPluginManager().registerCommand(this, new CherryCommand());

    getProxy().getPluginManager().registerListener(this, new PlayerDisconnect());
    getProxy().getPluginManager().registerListener(this, new ProxyPing());
    getProxy().getPluginManager().registerListener(this, new ServerConnect());
    getProxy().getPluginManager().registerListener(this, new ServerDisconnect());
    getProxy().getPluginManager().registerListener(this, new ServerKick());

    ServerDataEmitter.enable();
    ServerData.onEnable();
    CmdGotoHub.onEnable();
    WanyfieldSystem.onEnable();
    CmdSwap.onEnable();

  }

  @Override
  public void onDisable() {

    ServerData.disable();
    WanyfieldSystem.onDisable();

  }

}