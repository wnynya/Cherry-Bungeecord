package io.wany.cherry.bungeecord.amethyst;

import io.wany.cherry.bungeecord.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Color {

  private int red = 0;
  private int green = 0;
  private int blue = 0;
  private int alpha = 255;

  public Color(Type type, String s) {

    switch (type) {
      case HEX: {
        Pattern pattern_hex_8 = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
        Pattern pattern_hex_6 = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
        Pattern pattern_hex_3 = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
        Pattern pattern_hex_2 = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])");
        Pattern pattern_hex_1 = Pattern.compile("([0-9a-fA-F])");

        Matcher m8 = pattern_hex_8.matcher(s);
        Matcher m6 = pattern_hex_6.matcher(s);
        Matcher m3 = pattern_hex_3.matcher(s);
        Matcher m2 = pattern_hex_2.matcher(s);
        Matcher m1 = pattern_hex_1.matcher(s);

        if (m8.find()) {
          red = Integer.parseInt(m8.group(1) + m8.group(2) + "", 16);
          green = Integer.parseInt(m8.group(3) + m8.group(4) + "", 16);
          blue = Integer.parseInt(m8.group(5) + m8.group(6) + "", 16);
          alpha = Integer.parseInt(m8.group(7) + m8.group(8) + "", 16);
        }
        else if (m6.find()) {
          red = Integer.parseInt(m6.group(1) + m6.group(2) + "", 16);
          green = Integer.parseInt(m6.group(3) + m6.group(4) + "", 16);
          blue = Integer.parseInt(m6.group(5) + m6.group(6) + "", 16);
        }
        else if (m3.find()) {
          red = Integer.parseInt(m3.group(1) + m3.group(1) + "", 16);
          green = Integer.parseInt(m3.group(2) + m3.group(2) + "", 16);
          blue = Integer.parseInt(m3.group(3) + m3.group(3) + "", 16);
        }
        else if (m2.find()) {
          red = Integer.parseInt(m2.group(1) + m2.group(2) + "", 16);
          green = Integer.parseInt(m2.group(1) + m2.group(2) + "", 16);
          blue = Integer.parseInt(m2.group(1) + m2.group(2) + "", 16);
        }
        else if (m1.find()) {
          red = Integer.parseInt(m1.group(1) + m1.group(1) + "", 16);
          green = Integer.parseInt(m1.group(1) + m1.group(1) + "", 16);
          blue = Integer.parseInt(m1.group(1) + m1.group(1) + "", 16);
        }

        break;
      }

      case RGB: {

        Pattern pattern_rgb = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
        Pattern pattern_rgba = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
        Pattern pattern_rgba_s = Pattern.compile("(([rgbaRGBA]){1,4})[(.,:;|! ]+?(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?");

        Matcher m1 = pattern_rgb.matcher(s);
        Matcher m2 = pattern_rgba.matcher(s);
        Matcher m3 = pattern_rgba_s.matcher(s);

        if (m3.find()) {
          String head = m3.group(1).toLowerCase();
          for (int n = 0; n < head.length(); n++) {
            switch (head.charAt(n)) {
              case 'r': {
                red = Integer.parseInt(m3.group((n + 1) * 3) + "");
                break;
              }
              case 'g': {
                green = Integer.parseInt(m3.group((n + 1) * 3) + "");
                break;
              }
              case 'b': {
                blue = Integer.parseInt(m3.group((n + 1) * 3) + "");
                break;
              }
              case 'a': {
                alpha = Integer.parseInt(m3.group((n + 1) * 3) + "");
                break;
              }
            }
          }
        }
        else if (m2.find()) {
          red = Integer.parseInt(m2.group(1) + "");
          green = Integer.parseInt(m2.group(4) + "");
          blue = Integer.parseInt(m2.group(7) + "");
          alpha = Integer.parseInt(m2.group(10) + "");
          break;
        }
        else if (m1.find()) {
          red = Integer.parseInt(m1.group(1) + "");
          green = Integer.parseInt(m1.group(4) + "");
          blue = Integer.parseInt(m1.group(7) + "");
          break;
        }

        break;
      }

      case CMYK: {
        Pattern pattern_cmyk = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
        Pattern pattern_cmyk_s = Pattern.compile("(([cmykCMYK]){1,4})[(.,:;|! ]+?(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?");

        Matcher m1 = pattern_cmyk.matcher(s);
        Matcher m2 = pattern_cmyk_s.matcher(s);

        double cyan = 0;
        double magenta = 0;
        double yellow = 0;
        double black = 0;

        if (m2.find()) {
          String head = m2.group(1).toLowerCase();
          for (int n = 0; n < head.length(); n++) {
            switch (head.charAt(n)) {
              case 'c': {
                cyan = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
              case 'm': {
                magenta = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
              case 'y': {
                yellow = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
              case 'k': {
                black = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
            }
          }
        }
        else if (m1.find()) {
          cyan = Integer.parseInt(m1.group(1) + "") / 100.0;
          magenta = Integer.parseInt(m1.group(4) + "") / 100.0;
          yellow = Integer.parseInt(m1.group(7) + "") / 100.0;
          black = Integer.parseInt(m1.group(10) + "") / 100.0;
        }

        red = (int) (255.0 * (1.0 - cyan) * (1.0 - black));
        green = (int) (255.0 * (1.0 - magenta) * (1.0 - black));
        blue = (int) (255.0 * (1.0 - yellow) * (1.0 - black));

        break;
      }

      case MFC: {
        Pattern pattern_mfc = Pattern.compile("([§&]x)?([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])");

        Matcher m = pattern_mfc.matcher(s);
        if (m.find()) {
          red = Integer.parseInt(m.group(3) + m.group(5) + "", 16);
          green = Integer.parseInt(m.group(7) + m.group(9) + "", 16);
          blue = Integer.parseInt(m.group(11) + m.group(13) + "", 16);
          break;
        }

        Pattern pattern_mfc_single = Pattern.compile("([§&])?([0-9a-fA-F])");
        Matcher m2 = pattern_mfc_single.matcher(s);
        if (m2.find()) {
          String val = m2.group(2);
          switch (val) {
            case "0": { red = 0; green = 0; blue = 0; break; }
            case "1": { red = 0; green = 0; blue = 170; break; }
            case "2": { red = 0; green = 170; blue = 0; break; }
            case "3": { red = 0; green = 170; blue = 170; break; }
            case "4": { red = 170; green = 0; blue = 0; break; }
            case "5": { red = 170; green = 0; blue = 170; break; }
            case "6": { red = 255; green = 170; blue = 0; break; }
            case "7": { red = 170; green = 170; blue = 170; break; }
            case "8": { red = 85; green = 85; blue = 85; break; }
            case "9": { red = 85; green = 85; blue = 255; break; }
            case "a": { red = 85; green = 255; blue = 85; break; }
            case "b": { red = 85; green = 255; blue = 255; break; }
            case "c": { red = 255; green = 85; blue = 85; break; }
            case "d": { red = 255; green = 85; blue = 255; break; }
            case "e": { red = 255; green = 255; blue = 85; break; }
            case "f": { red = 255; green = 255; blue = 255; break; }
          }
        }

        break;
      }

      case ANSI: {

        Pattern pattern_ansi_rgb = Pattern.compile("(([0-9]){1,3})([.,:;|! ](([0-9]){1,3}))([.,:;|! ](([0-9]){1,3}))");
        Matcher m = pattern_ansi_rgb.matcher(s);
        if (m.find()) {
          red = Integer.parseInt(m.group(1) + "");
          green = Integer.parseInt(m.group(4) + "");
          blue = Integer.parseInt(m.group(7) + "");
        }

        Pattern pattern_ansi_single_dark = Pattern.compile("([34])([0-7])");
        Matcher m2 = pattern_ansi_single_dark.matcher(s);
        if (m2.find()) {
          String val = m2.group(2);
          switch (val) {
            case "0": { red = 0; green = 0; blue = 0; break; }
            case "1": { red = 0; green = 0; blue = 170; break; }
            case "2": { red = 0; green = 170; blue = 0; break; }
            case "3": { red = 0; green = 170; blue = 170; break; }
            case "4": { red = 170; green = 0; blue = 0; break; }
            case "5": { red = 170; green = 0; blue = 170; break; }
            case "6": { red = 255; green = 170; blue = 0; break; }
            case "7": { red = 170; green = 170; blue = 170; break; }
          }
        }

        Pattern pattern_ansi_single_light = Pattern.compile("(9|10)([0-7])");
        Matcher m3 = pattern_ansi_single_light.matcher(s);
        if (m3.find()) {
          String val = m3.group(2);
          switch (val) {
            case "0": { red = 85; green = 85; blue = 85; break; }
            case "1": { red = 85; green = 85; blue = 255; break; }
            case "2": { red = 85; green = 255; blue = 85; break; }
            case "3": { red = 85; green = 255; blue = 255; break; }
            case "4": { red = 255; green = 85; blue = 85; break; }
            case "5": { red = 255; green = 85; blue = 255; break; }
            case "6": { red = 255; green = 255; blue = 85; break; }
            case "7": { red = 255; green = 255; blue = 255; break; }
          }
        }

        break;
      }

      case HSV: {
        Pattern pattern_hsv = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
        Pattern pattern_hsv_s = Pattern.compile("(([hsvHSV]){1,3})[(.,:;|! ]+?(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?");

        Matcher m1 = pattern_hsv.matcher(s);
        Matcher m2 = pattern_hsv_s.matcher(s);

        int hue = 0;
        double saturation = 0;
        double value = 0;

        if (m2.find()) {
          String head = m2.group(1).toLowerCase();
          for (int n = 0; n < head.length(); n++) {
            switch (head.charAt(n)) {
              case 'h': {
                hue = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
              case 's': {
                saturation = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
              case 'v': {
                value = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
            }
          }
        }
        else if (m1.find()) {
          hue = Integer.parseInt(m1.group(1) + "");
          saturation = Integer.parseInt(m1.group(4) + "") / 100.0;
          value = Integer.parseInt(m1.group(7) + "") / 100.0;
        }

        double c = value * saturation;
        double x = c * ( 1 - Math.abs( ( (hue / 60) % 2 ) - 1 ) );
        double m = value - c;

        double r = 0, g = 0, b = 0;

        if (0 <= hue && hue < 60) { r = c; g = x; b = 0; }
        else if (60 <= hue && hue < 120) { r = x; g = c; b = 0; }
        else if (120 <= hue && hue < 180) { r = 0; g = c; b = x; }
        else if (180 <= hue && hue < 240) { r = 0; g = x; b = c; }
        else if (240 <= hue && hue < 300) { r = x; g = 0; b = c; }
        else if (300 <= hue && hue < 360) { r = c; g = 0; b = x; }

        red = (int) (r + m) * 255;
        green = (int) (g + m) * 255;
        blue = (int) (b + m) * 255;

        Console.log(c + " " + x + " " + m + " " + r + " " + hue + " " + saturation + " " + value + " " + red + " " + green + " " + blue);

        break;
      }

      case HSL: {
        Pattern pattern_hsv = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
        Pattern pattern_hsv_s = Pattern.compile("(([hslHSL]){1,3})[(.,:;|! ]+?(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?");

        Matcher m1 = pattern_hsv.matcher(s);
        Matcher m2 = pattern_hsv_s.matcher(s);

        double hue = 0;
        double saturation = 0;
        double lightness = 0;

        if (m2.find()) {
          String head = m2.group(1).toLowerCase();
          for (int n = 0; n < head.length(); n++) {
            switch (head.charAt(n)) {
              case 'h': {
                hue = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
              case 's': {
                saturation = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
              case 'l': {
                lightness = Integer.parseInt(m2.group((n + 1) * 3) + "");
                break;
              }
            }
          }
        }
        else if (m1.find()) {
          hue = Integer.parseInt(m1.group(1) + "");
          saturation = Integer.parseInt(m1.group(4) + "") / 100.0;
          lightness = Integer.parseInt(m1.group(7) + "") / 100.0;
        }

        double c = (1 - ( Math.abs( (lightness * 2) - 1 ) * saturation ) );
        double x = c * ( 1 - Math.abs( ( (hue / 60) % 2 ) - 1 ) );
        double m = lightness - (c / 2);

        double r = 0, g = 0, b = 0;

        if (0 <= hue && hue < 60) { r = c; g = x; }
        else if (60 <= hue && hue < 120) { r = x; g = c; }
        else if (120 <= hue && hue < 180) { g = c; b = x; }
        else if (180 <= hue && hue < 240) { g = x; b = c; }
        else if (240 <= hue && hue < 300) { r = x; b = c; }
        else if (300 <= hue && hue < 360) { r = c; b = x; }

        red = (int) (r + m) * 255;
        green = (int) (g + m) * 255;
        blue = (int) (b + m) * 255;

        break;
      }
    }

    if ( red > 255 ) { red = 255; } if ( red < 0 ) { red = 0; }
    if ( green > 255 ) { green = 255; } if ( green < 0 ) { green = 0; }
    if ( blue > 255 ) { blue = 255; } if ( blue < 0 ) { blue = 0; }
    if ( alpha > 255 ) { alpha = 255; } if ( alpha < 0 ) { alpha = 0; }

  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }

  public int getAlpha() {
    return alpha;
  }

  public int getCyan() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double k = 1 - Math.max(r, Math.max(g, b));
    return (int) ((1 - r - k) / (1 - k));
  }

  public int getMagenta() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double k = 1 - Math.max(r, Math.max(g, b));
    return (int) ((1 - g - k) / (1 - k));
  }

  public int getYellow() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double k = 1 - Math.max(r, Math.max(g, b));
    return (int) ((1 - b - k) / (1 - k));
  }

  public int getBlack() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    return (int) (1 - Math.max(r, Math.max(g, b)));
  }



  public String getRedAsHex() {
    return Integer.toHexString(0x100 | red).substring(1);
  }

  public String getGreenAsHex() {
    return Integer.toHexString(0x100 | green).substring(1);
  }

  public String getBlueAsHex() {
    return Integer.toHexString(0x100 | blue).substring(1);
  }

  public String getAlphaAsHex() {
    return Integer.toHexString(0x100 | alpha).substring(1);
  }



  public int[] getRGB() {
    return new int[] {getRed(), getGreen(), getBlue()};
  }

  public int[] getRGBA() {
    return new int[] {getRed(), getGreen(), getBlue(), getAlpha()};
  }

  public String[] getHEX() {
    return new String[] {getRedAsHex(), getGreenAsHex(), getBlueAsHex()};
  }

  public String[] getHEXWithAlpha() {
    return new String[] {getRedAsHex(), getGreenAsHex(), getBlueAsHex(), getAlphaAsHex()};
  }

  public int[] getCMYK() {
    return new int[] {getCyan(), getMagenta(), getYellow(), getBlack()};
  }



  public String getRGBString() {
    return "rgb(" + getRed() + "," + getGreen() + "," + getBlue() + ")";
  }

  public String getRGBAString() {
    return "rgba(" + getRed() + "," + getGreen() + "," + getBlue() + "," + getAlpha() + ")";
  }

  public String getHexString() {
    return "#" + getRedAsHex() + getGreenAsHex() + getBlueAsHex();
  }

  public String getHexStringWithAlpha() {
    return "#" + getRedAsHex() + getGreenAsHex() + getBlueAsHex() + getAlphaAsHex();
  }

  public String getCMYKString() {
    return "cmyk(" + getCyan() + "," + getMagenta() + "," + getYellow() + "," + getBlack() + ")";
  }

  public String getMFC() {
    switch (getRGBString()) {
      case "0,0,0": {
        return "§0";
      }
      case "0,0,170": {
        return "§1";
      }
      case "0,170,0": {
        return "§2";
      }
      case "0,170,170": {
        return "§3";
      }
      case "170,0,0": {
        return "§4";
      }
      case "170,0,170": {
        return "§5";
      }
      case "255,170,0": {
        return "§6";
      }
      case "170,170,170": {
        return "§7";
      }
      case "85,85,85": {
        return "§8";
      }
      case "85,85,255": {
        return "§9";
      }
      case "85,255,85": {
        return "§a";
      }
      case "85,255,255": {
        return "§b";
      }
      case "255,85,85": {
        return "§c";
      }
      case "255,85,255": {
        return "§d";
      }
      case "255,255,85": {
        return "§e";
      }
      case "255,255,255": {
        return "§f";
      }
      default: {
        String[] hex = getHEX();
        String h = hex[0] + hex[1] + hex[2];
        return h.replaceAll("([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])", "§x§$1§$2§$3§$4§$5§$6");
      }
    }
  }

  public String getANSIEscape() {
    return "\u001b[38;2;" + getRed() + ";" + getGreen() + ";" + getBlue() + "m";
  }



  public enum Type {
    HEX, RGB, CMYK, HSL, HSV, MFC, ANSI
  }



  public static String getGradientString(String s) {
    Pattern p = Pattern.compile("gradient\\[([#&§0-9a-fA-FrgbRGB,;]+)\\](.*);");
    Matcher m = p.matcher(s);
    ArrayList<Matcher> matches = new ArrayList<>();
    while (m.find()) {
      String[] colorStrings = m.group(1).split(";");
      String content = m.group(2);
      List<Color> colors = new ArrayList<>();
      for (String colorString : colorStrings) {
        if (colorString.startsWith("#")) {
          colors.add(new Color(Type.HEX, colorString));
        }
        else if (colorString.startsWith("&") || colorString.startsWith("§")) {
          colors.add(new Color(Type.MFC, colorString));
        }
        else if (colorString.contains(",")) {
          colors.add(new Color(Type.RGB, colorString));
        }
      }
      s = s.replace(m.group(0), getGradientString(colors, content));
    }
    return s;
  }

  public static String getGradientString(List<Color> colors, String string) {
    String master = "";
    String cleanString = string.replaceAll("§[0-9a-fA-FrRkKxXmMnNLloO]" , "");
    int length = cleanString.length();
    int blockSize = 0;
    int left = 0;
    if (colors.size() == 0) {
      return string;
    }
    else if (colors.size() == 1) {
      colors.add(colors.get(0));
    }
    if (colors.size() > 0 && colors.size() <= cleanString.length()) {
      blockSize = length / (colors.size() - 1);
      left = length % (colors.size() - 1);
    }
    else {
      return string;
    }
    int pos = 0;
    boolean bold = false;
    boolean italic = false;
    boolean strike = false;
    boolean underline = false;
    boolean random = false;
    for (int n = 0; n < colors.size() - 1; n++) {
      int gradientLength = 0;
      if (n == colors.size() - 2) {
        gradientLength = blockSize + left;
        blockSize = gradientLength;
      }
      else {
        gradientLength = blockSize;
      }
      List<Color> gradient = getGradient(colors.get(n), colors.get(n + 1), gradientLength + 1);
      for (int m = 0; m < blockSize; m++) {
        char c = cleanString.charAt(pos);
        char s = string.charAt(pos);
        while (s != c) {
          if (s == '§') {
            switch (string.charAt(pos + 1)) {
              case 'r': {
                bold = false;
                italic = false;
                strike = false;
                underline = false;
                random = false;
                break;
              }
              case 'l': {
                bold = true;
                break;
              }
              case 'm': {
                strike = true;
                break;
              }
              case 'n': {
                underline = true;
                break;
              }
              case 'o': {
                italic = true;
                break;
              }
              case 'k': {
                random = true;
                break;
              }
            }
            string = string.replaceFirst("§" + string.charAt(pos + 1), "");
            s = string.charAt(pos);
          }
          else {
            s = c;
          }
        }
        Color color = gradient.get(m);
        master += color.getMFC();
        if (bold) { master += "§l"; }
        if (strike) { master += "§m"; }
        if (underline) { master += "§n"; }
        if (italic) { master += "§o"; }
        if (random) { master += "§k"; }
        master += cleanString.charAt(pos);
        pos++;
      }
    }
    return master;
  }

  public static List<Color> getGradient(Color startColor, Color endColor, int length) {
    int rd = endColor.getRed() - startColor.getRed();
    int gd = endColor.getGreen() - startColor.getGreen();
    int bd = endColor.getBlue() - startColor.getBlue();
    List<Color> colors = new ArrayList<>();
    for (int n = 0; n < length; n++) {
      int r = (int) (startColor.getRed() + Math.round(rd * (n / ((length - 1) * 1.0))));
      int g = (int) (startColor.getGreen() + Math.round(gd * (n / ((length - 1) * 1.0))));
      int b = (int) (startColor.getBlue() + Math.round(bd * (n / ((length - 1) * 1.0))));
      Color color = new Color(Type.RGB, r + "," + g + "," + b);
      colors.add(color);
    }
    return colors;
  }



  public static String and2mfc(String s) {
    return s.replaceAll("&([0-9a-fA-Frlmnok])", "§$1");
  }

  public static String rgb2mfc(String s) {
    Pattern p = Pattern.compile("(([rgbRGB]){1,3})\\(?(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?\\)?;");
    Matcher m = p.matcher(s);
    ArrayList<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    for (String find : matches) {
      Color color = new Color(Type.RGB, find);
      s = s.replace(find, color.getMFC());
    }
    return s;
  }

  public static String hex2mfc(String s) {
    Pattern p = Pattern.compile("#(([a-fA-F0-9])|(([a-fA-F0-9]){2})|(([a-fA-F0-9]){3})|(([a-fA-F0-9]){6}));");
    Matcher m = p.matcher(s);
    ArrayList<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    for (String find : matches) {
      Color color = new Color(Type.HEX, find);
      s = s.replace(find, color.getMFC());
    }
    return s;
  }

  public static String cmyk2mfc(String s) {
    Pattern p = Pattern.compile("(([cmykCMYK]){1,4})\\(?(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?\\)?;");
    Matcher m = p.matcher(s);
    ArrayList<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    for (String find : matches) {
      Color color = new Color(Type.CMYK, find);
      s = s.replace(find, color.getMFC());
    }
    return s;
  }

  public static String hsv2mfc(String s) {
    Pattern p = Pattern.compile("(([hsvHSV]){1,3})\\(?(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?\\)?;");
    Matcher m = p.matcher(s);
    ArrayList<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    for (String find : matches) {
      Color color = new Color(Type.HSV, find);
      s = s.replace(find, color.getMFC());
    }
    return s;
  }

  public static String hsl2mfc(String s) {
    Pattern p = Pattern.compile("(([hslHSL]){1,3})\\(?(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?\\)?;");
    Matcher m = p.matcher(s);
    ArrayList<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    for (String find : matches) {
      Color color = new Color(Type.HSV, find);
      s = s.replace(find, color.getMFC());
    }
    return s;
  }



  public static String mfc2and(String s) {
    return s.replaceAll("§", "&");
  }

  public static String mfc2ansi(String s) {
    Pattern p = Pattern.compile("§x(§[0-9a-fA-F]){6}");
    Matcher m = p.matcher(s);
    ArrayList<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    for (String find : matches) {
      Color color = new Color(Type.MFC, find);
      s = s.replace(find, color.getANSIEscape());
    }

    s = s.replaceAll("§0", "\u001b[30m");
    s = s.replaceAll("§4", "\u001b[31m");
    s = s.replaceAll("§2", "\u001b[32m");
    s = s.replaceAll("§6", "\u001b[33m");
    s = s.replaceAll("§1", "\u001b[34m");
    s = s.replaceAll("§5", "\u001b[35m");
    s = s.replaceAll("§3", "\u001b[36m");
    s = s.replaceAll("§7", "\u001b[37m");
    s = s.replaceAll("§8", "\u001b[90m");
    s = s.replaceAll("§c", "\u001b[91m");
    s = s.replaceAll("§a", "\u001b[92m");
    s = s.replaceAll("§e", "\u001b[93m");
    s = s.replaceAll("§9", "\u001b[94m");
    s = s.replaceAll("§d", "\u001b[95m");
    s = s.replaceAll("§b", "\u001b[96m");
    s = s.replaceAll("§f", "\u001b[97m");

    s = s.replaceAll("§l", "\u001b[1m");
    s = s.replaceAll("§m", "\u001b[2m");
    s = s.replaceAll("§n", "\u001b[4m");
    s = s.replaceAll("§o", "\u001b[3m");
    s = s.replaceAll("§r", "\u001b[0m");
    s = s.replaceAll("§k", "\u001b[5m");

    return s;
  }

  public static String chatEffect(String string) {
    string = Color.and2mfc(string);
    //string = Color.getGradientString(string);
    string = Color.hex2mfc(string);
    string = Color.rgb2mfc(string);
    //string = Color.cmyk2mfc(string);
    //string = Color.hsl2mfc(string);
    //string = Color.hsv2mfc(string);
    return string;
  }

}
