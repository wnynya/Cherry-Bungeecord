package io.wany.cherry.bungeecord;

import io.wany.cherry.bungeecord.amethyst.Color;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

  public static void info(ProxiedPlayer player, String message) {
    BaseComponent component = parse(effect(message));
    player.sendMessage(component);
  }

  public static void error(ProxiedPlayer player, String message) {
    BaseComponent component = Message.parse(effect(message));
    player.sendMessage(component);
  }

  public static void warn(ProxiedPlayer player, String message) {
    BaseComponent component = Message.parse(effect(message));
    player.sendMessage(component);
  }

  public static void info(CommandSender sender, String message) {
    if (sender instanceof ProxiedPlayer player) {
      info(player, message);
    }
    else {
      Console.log(message);
    }
  }

  public static void warn(CommandSender sender, String message) {
    if (sender instanceof ProxiedPlayer player) {
      info(player, message);
    }
    else {
      Console.log(message);
    }
  }

  public static void error(CommandSender sender, String message) {
    if (sender instanceof ProxiedPlayer player) {
      info(player, message);
    }
    else {
      Console.log(message);
    }
  }

  public static void info(CommandSender sender, String prefix, String message) {
    if (sender instanceof ProxiedPlayer player) {
      info(player, prefix + message);
    }
    else {
      if (prefix.equals(Cherry.PREFIX)) {
        prefix = "";
      }
      Console.log(prefix + message);
    }
  }

  public static void warn(CommandSender sender, String prefix, String message) {
    if (sender instanceof ProxiedPlayer player) {
      info(player, prefix + message);
    }
    else {
      if (prefix.equals(Cherry.PREFIX)) {
        prefix = "";
      }
      Console.log(prefix + message);
    }
  }

  public static void error(CommandSender sender, String prefix, String message) {
    if (sender instanceof ProxiedPlayer player) {
      info(player, prefix + message);
    }
    else {
      if (prefix.equals(Cherry.PREFIX)) {
        prefix = "";
      }
      Console.log(prefix + message);
    }
  }

  public static String effect(String string) {
    string = Color.chatEffect(string);
    return string;
  }

  public static BaseComponent parse(String message) {
    List<TextComponent> components = stringifier(message);
    TextComponent component = null;
    for (TextComponent textComponent : components) {
      if (component == null) {
        component = components.get(0);
      }
      else {
        component.addExtra(textComponent);
      }
    }
    return component;
  }

  private static List<TextComponent> parser(TextComponent component) {
    List<TextComponent> components = new ArrayList<>();
    TextComponent textComponent = new TextComponent();
    textComponent.setText(component.getText());
    textComponent.setColor(component.getColor());
    textComponent.setBold(component.isBold());
    textComponent.setStrikethrough(component.isStrikethrough());
    textComponent.setUnderlined(component.isUnderlined());
    textComponent.setItalic(component.isItalic());
    textComponent.setObfuscated(component.isObfuscated());
    textComponent.setClickEvent(component.getClickEvent());
    textComponent.setHoverEvent(component.getHoverEvent());
    textComponent.setFont(component.getFont());
    textComponent.setInsertion(component.getInsertion());
    components.add(textComponent);
    if (component.getExtra().size() > 0) {
      for (BaseComponent bc : component.getExtra()) {
        List<TextComponent> tcs = parser((TextComponent) bc);
        components.addAll(tcs);
      }
    }
    return components;
  }

  public static String stringify(BaseComponent component) {
    List<TextComponent> components = parser((TextComponent) component);
    StringBuilder stringBuilder = new StringBuilder();
    for (TextComponent tc : components) {
      if (tc.getColor() != null) {
        ChatColor chatColor = tc.getColor();
        Color color = new Color(Color.Type.RGB, chatColor.getColor().getRed() + ", " + chatColor.getColor().getGreen() + ", " + chatColor.getColor().getBlue());
        stringBuilder.append(color.getMFC());
      }
      else {
        stringBuilder.append("§r");
      }
      if (tc.isBold()) {
        stringBuilder.append("§l");
      }
      if (tc.isStrikethrough()) {
        stringBuilder.append("§m");
      }
      if (tc.isUnderlined()) {
        stringBuilder.append("§n");
      }
      if (tc.isItalic()) {
        stringBuilder.append("§o");
      }
      if (tc.isObfuscated()) {
        stringBuilder.append("§k");
      }
      stringBuilder.append(tc.getText());
    }
    return stringBuilder.toString();
  }

  private static List<TextComponent> stringifier(String string) {
    List<TextComponent> components = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    TextComponent component = new TextComponent();
    Matcher matcher = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$").matcher(string);

    int length = string.length();
    for (int i = 0; i < length; i++) {
      TextComponent old;
      String character = String.valueOf(string.charAt(i));
      if (character.equals("§")) {
        i++;
        if (i >= length) {
          break;
        }

        if (builder.length() > 0)
        {
          old = component;
          old.setText(builder.toString());
          builder = new StringBuilder();
          components.add(old);
        }

        String c = String.valueOf(string.charAt(i)).toLowerCase();

        if (c.equals("l")) {
          component.setBold(true);
        }
        else if (c.equals("m")) {
          component.setStrikethrough(true);
        }
        else if (c.equals("n")) {
          component.setUnderlined(true);
        }
        else if (c.equals("o")) {
          component.setItalic(true);
        }
        else if (c.equals("k")) {
          component.setObfuscated(true);
        }
        else if (c.equals("r")) {
          component = new TextComponent();
          component.setColor(ChatColor.RESET);
          component.setItalic(false);
        }
        else if (c.equals("x")) {
          if (i + 12 >= length) {
            break;
          }
          StringBuilder color = new StringBuilder("#");
          for (int j = 0; j < 6; ++j) {
            color.append(string.charAt(i + 2 + j * 2));
          }
          component = new TextComponent();
          component.setColor(ChatColor.of(color.toString()));
          component.setItalic(false);
          i += 12;
        }
        else if (c.matches("[0-9a-f]")) {
          component = new TextComponent();
          component.setColor(ChatColor.getByChar(c.charAt(0)));
          component.setItalic(false);
        }
      }
      else {
        int pos = string.indexOf(32, i);
        if (pos == -1)
        {
          pos = string.length();
        }

        if (matcher.region(i, pos).find())
        {
          if (builder.length() > 0)
          {
            old = component;
            old.setText(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          }

          old = component;
          String urlString = string.substring(i, pos);
          component.setText(urlString);
          component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlString));
          components.add(component);
          i += pos - i - 1;
          component = old;
        }
        else
        {
          builder.append(character);
        }
      }
    }

    component.setText(builder.toString());
    components.add(component);
    if (components.size() > 1)
    {
      List<TextComponent> returnValue = new ArrayList<>(Collections.singletonList(new TextComponent()));
      returnValue.addAll(components);
      return returnValue;
    }
    return components;
  }

  public static String formatPlayer(ProxiedPlayer player, String format) {
    format = format.replace("{name}", player.getName());
    format = format.replace("{displayname}", Message.effect(player.getDisplayName()));
    format = format.replace("{uuid}", player.getUniqueId().toString());
    return format;
  }

  public static class CommandFeedback {
    public static String UNKNOWN = "알 수 없는 명령어입니다";
    public static String NO_PERMISSION = "명령어의 사용 권한이 없습니다";
    public static String NO_ARGS = "명령어의 구성 요소가 부족합니다";
    public static String NO_PLAYER = "플레이어를 찾을 수 없습니다";
    public static String ONLY_PLAYER = "플레이어만 사용 가능한 명령어입니다";
    public static String WAND_NO_WORLD = "월드를 찾을 수 없습니다";
    public static String WAND_NO_POSITION = "선택 영역이 없습니다";
    public static String WAND_ERROR_LOCATION = "선택할 수 없는 좌표입니다";
    public static String WAND_NO_INTEGER = "정수만 사용할 수 있습니다";
    public static String WAND_OUT_INTEGER = "사용할 수 없는 영역의 정수입니다";
  }

}
