package io.wany.cherry.bungeecord.amethyst;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;

public class JsonConfig {

  private File file;
  private JsonObject object;

  public JsonConfig(File file) {
    this.file = file;
    this.file.mkdirs();

  }

  public void set(String path, Object value) {
    String[] paths = path.split("\\.");
    JsonObject o = object;
    for (int n = 0; n < paths.length; n++) {
      if (o.has(paths[n])) { o.remove(paths[n]); }
      if (n + 1 == paths.length) {
        if (value instanceof String) {
          o.addProperty(paths[n], (String) value);
        }
        else if (value instanceof Number) {
          o.addProperty(paths[n], (Number) value);
        }
        else if (value instanceof Boolean) {
          o.addProperty(paths[n], (Boolean) value);
        }
      }
      else {
        JsonElement el = new JsonObject();
        o.add(paths[n], el);
      }
    }
  }

}
