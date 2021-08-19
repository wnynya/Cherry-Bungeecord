package io.wany.cherry.bungeecord.wanyfield;

import io.wany.cherry.bungeecord.Cherry;
import io.wany.cherry.bungeecord.command.*;

public class WanyfieldSystem {

  public static void onEnable() {
    if (Cherry.CONFIG.getBoolean("wanyfield")) {
      Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdGotoWanyfield());
      Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdGotoPlayground());
      Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdGotoLounge());
      Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdGotoRcmr());
      Cherry.PLUGIN.getProxy().getPluginManager().registerCommand(Cherry.PLUGIN, new CmdGotoGusen());
    }
  }

  public static void onDisable() {
  }

}
