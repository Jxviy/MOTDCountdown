package dev.sergivos.motdcountdown;

import dev.sergivos.motdcountdown.command.ReloadCommand;
import dev.sergivos.motdcountdown.listener.PingListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@Getter
public final class MOTDCountdown extends Plugin {

  @Getter
  private static MOTDCountdown instance;

  private Configuration config;
  private PingListener pingListener;

  public void onEnable() {
    instance = this;

    if (!loadConfig()) {
      getLogger().severe("FAILED TO LOAD MOTDCOUNTDOWN PLUGIN");
      return;
    }

    this.pingListener = new PingListener();

    ProxyServer.getInstance().getPluginManager().registerListener(this, this.pingListener);
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
  }

  public void onDisable() {
    instance = null;
  }

  private boolean loadConfig() {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }

    File file = new File(getDataFolder(), "config.yml");

    if (!file.exists()) {
      try (InputStream in = getResourceAsStream("config.yml")) {
        Files.copy(in, file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }

    return reloadConfig();
  }

  public boolean reloadConfig() {
    try {
      this.config = ConfigurationProvider.getProvider(YamlConfiguration.class)
          .load(new File(getDataFolder(), "config.yml"));
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

}
