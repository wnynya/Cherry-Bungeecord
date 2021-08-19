package io.wany.cherry.bungeecord.command;

import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TestCommand extends Command {

  public TestCommand() {
    super("testcommand", "cherry.bungeecord.testcommand");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
      if (args.length == 0) {
        commandSender.sendMessage(new TextComponent("메시지가 없습니다."));
        return;
      }
      String msg = "";
      int n = 0;
      for (String s : args) {
        if (n == args.length - 1) {
          msg += args[n];
        }
        else {
          msg += args[n] + " ";
        }
        n++;
      }
      p.sendMessage(ChatMessageType.CHAT, new TextComponent(Message.effect(msg)));
  }
  }

}
