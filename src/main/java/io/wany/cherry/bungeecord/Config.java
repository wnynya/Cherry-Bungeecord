package io.wany.cherry.bungeecord;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class Config {

  private static HashMap<String, Config> configs = new HashMap<>();

  private Configuration config;
  private String name;

  public Config (String name) {
    this.name = name;
    this.config = loadConfig();
  }

  public Configuration getConfig() {
    return config;
  }

  public Configuration loadConfig() {

    if (!Cherry.PLUGIN.getDataFolder().exists()) {
      Cherry.PLUGIN.getDataFolder().mkdir();
    }

    File file = new File(Cherry.PLUGIN.getDataFolder(), name + ".yml");

    if (!file.exists()) {
      try (InputStream in = Cherry.PLUGIN.getResourceAsStream(name + ".yml")) {
        Files.copy(in, file.toPath());
      }
      catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    try {
      return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Cherry.PLUGIN.getDataFolder(), name + ".yml"));
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  public void saveConfig() {
    try {
      ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(Cherry.PLUGIN.getDataFolder(), name + ".yml"));
    }
    catch (Exception e) {

    }
  }

  public void set(String path, Object value) {
    config.set(path, value);
    this.saveConfig();
  }

  public boolean getBoolean(String path) {
    return config.getBoolean(path);
  }

  public String getString(String path) {
    return config.getString(path);
  }

  public int getInt(String path) {
    return config.getInt(path);
  }

  public double getDouble(String path) {
    return config.getDouble(path);
  }

  public List<?> getList(String path) {
    return config.getList(path);
  }

  public static Config get(String name) {
    if (configs.containsKey(name)) {
      return configs.get(name);
    }
    else {
      return new Config(name);
    }
  }
  
}
