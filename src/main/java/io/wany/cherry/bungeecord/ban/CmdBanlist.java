package io.wany.cherry.bungeecord.ban;

import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CmdBanlist extends Command {

  public CmdBanlist() {
    super("bbanlist", "cherry.bungeecord.ban", "gbanlist");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {

    if (Ban.bannedPlayers.size() == 0) {
      Message.info(commandSender, Ban.PREFIX + "서버 접속 제한 조치된 플레이어가 없습니다.");
      return;
    }

    for (Ban.BannedPlayer b : Ban.bannedPlayers) {
      Message.info(commandSender, Ban.PREFIX + b.name + " => " + b.reason + "(" + b.executor + ", " + b.executeTime + ")");
    }

  }


}
