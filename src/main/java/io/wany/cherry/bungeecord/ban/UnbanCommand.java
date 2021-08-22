package io.wany.cherry.bungeecord.ban;

import io.wany.cherry.bungeecord.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UnbanCommand extends Command implements TabExecutor {

  public UnbanCommand() {
    super("unbban", "cherry.bungeecord.ban", "gunban");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {

    if (args.length == 0) {
      return;
    }

    String target = args[0];

    Ban.BannedPlayer bannedPlayer = null;
    try {
      UUID uuid = UUID.fromString(target);
      for (Ban.BannedPlayer bp : Ban.bannedPlayers) {
        if (bp.uuid.equals(uuid)) {
          bannedPlayer = bp;
          break;
        }
      }
    } catch (Exception e) {
      for (Ban.BannedPlayer bp : Ban.bannedPlayers) {
        if (bp.name.equals(target)) {
          bannedPlayer = bp;
          break;
        }
      }
    }

    if (bannedPlayer == null) {
      Message.info(commandSender, Ban.PREFIX + "플레이어를 찾을 수 없습니다.");
      return;
    }

    Ban.bannedPlayers.remove(bannedPlayer);

    Ban.save();

    Message.info(commandSender, Ban.PREFIX + bannedPlayer.name + "의 서버 접속 제한 조치가 해제되었습니다.");

  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

    if (args.length == 0) {
      return Collections.emptyList();
    }

    else if (args.length == 1) {
      List<String> bannedPlayers = new ArrayList<>();
      for (Ban.BannedPlayer p : Ban.bannedPlayers) {
        bannedPlayers.add(p.name);
      }
      return bannedPlayers;
    }

    else {
      return Collections.emptyList();
    }

  }


}
