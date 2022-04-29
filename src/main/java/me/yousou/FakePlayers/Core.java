package me.yousou.FakePlayers;

import org.bukkit.plugin.java.JavaPlugin;

import me.yousou.FakePlayers.cmd.CommandFakeplayers;

public class Core extends JavaPlugin {
  private CommandFakeplayers cfp;
  
  @Override
  public void onEnable() {
    cfp = new CommandFakeplayers();
  }
  
  @Override
  public void onDisable() {
    cfp.savedelete();
  }
}

