package dev.sergivos.motdcountdown.listener;

import dev.sergivos.motdcountdown.MOTDCountdown;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {

  private final static long SECOND = 1000;
  private final static long MINUTE = SECOND * 60;
  private final static long HOUR = MINUTE * 60;
  private final static long DAY = HOUR * 24;

  private final MOTDCountdown plugin = MOTDCountdown.getInstance();
  private long endDate;

  public PingListener() {
    reloadData();
  }

  @EventHandler
  public void onServerPing(ProxyPingEvent event) {
    ServerPing response = event.getResponse();

    response.setDescriptionComponent(new TextComponent(
        format(this.plugin.getConfig().getString("motd.first") + "\n" +
            format(this.plugin.getConfig().getString("motd.second")))
    ));
    response.getPlayers().setMax(this.plugin.getConfig().getInt("maxplayers"));

    event.setResponse(response);
  }

  public boolean reloadData() {
    if (!this.plugin.reloadConfig()) {
      return false;
    }

    try {
      SimpleDateFormat loadFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
      loadFormat.setTimeZone(TimeZone.getTimeZone(this.plugin.getConfig().getString("timezone")));
      this.endDate = loadFormat.parse(this.plugin.getConfig().getString("date")).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private String format(String s) {
    return ChatColor
        .translateAlternateColorCodes('&', s.replace("%countdown%", getRemainingTime()));
  }

  private String getRemainingTime() {
    long diff = endDate - new Date().getTime();

    if (diff <= 0) {
      return this.plugin.getConfig().getString("motd.ended");
    }

    long days = diff / DAY;
    diff %= DAY;

    long hours = diff / HOUR;
    diff %= HOUR;

    long min = diff / MINUTE;
    diff %= MINUTE;

    long sec = diff / SECOND;

    StringBuilder sb = new StringBuilder();

    if (days > 0) {
      sb.append(days).append("dias");
      if (hours > 0) {
        sb.append(", ");
      }
    }

    if (hours > 0) {
      sb.append(hours).append("horas");
      if (min > 0) {
        sb.append(", ");
      }
    }

    if (min > 0) {
      sb.append(min).append("minutos");
      if (sec > 0) {
        sb.append(", ").append(sec).append("segundos");
      }
    }

    return this.plugin.getConfig().getString("motd.countdown").replace("%time%", sb.toString());
  }

}
