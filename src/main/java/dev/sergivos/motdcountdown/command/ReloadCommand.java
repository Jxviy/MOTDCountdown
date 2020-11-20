package dev.sergivos.motdcountdown.command;

import dev.sergivos.motdcountdown.MOTDCountdown;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

  private final MOTDCountdown plugin = MOTDCountdown.getInstance();

  public ReloadCommand() {
    super("motdcountdown");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("motdcountdown.reload")) {
      sender.sendMessage(
          ChatColor.RED + "You do not have permission to do that. Plugin by @sergivb01");
      return;
    }

    if (!this.plugin.getPingListener().reloadData()) {
      sender.sendMessage(ChatColor.RED
          + "There was an issue while attempting to reload the configuration. Please check the proxy logs");
      return;
    }

    sender.sendMessage(ChatColor.GREEN + "Successfully reloaded config. Check the MOTD :)");
  }

}
