package io.wany.cherry.bungeecord;

import io.wany.cherry.bungeecord.amethyst.Color;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;

public class Console {

  public static void message(BaseComponent message) {
    ProxyServer.getInstance().getConsole().sendMessage(message);
  }

  public static void log(String message) {
    System.out.println(Color.mfc2ansi(Message.effect(Prefix.CHERRY + message) + "\u001b[0m"));
  }

  public static void warn(String message) {
    System.out.println(Color.mfc2ansi(Message.effect(Prefix.CHERRY + message) + "\u001b[0m"));
  }

  public static void error(String message) {
    System.out.println(Color.mfc2ansi(Message.effect(Prefix.CHERRY+ message) + "\u001b[0m"));
  }

  public static void debug(String message) {
    if (!Cherry.DEBUG) {
      return;
    }
    System.out.println(Color.mfc2ansi(Message.effect(Prefix.CHERRY + Cherry.COLOR + Prefix.DEBUG + message) + "\u001b[0m"));
  }

  public static class Prefix {
    public static String CHERRY = "[Cherry]&r ";
    public static String DEBUG = "[Debug]:&r ";
  }

}
