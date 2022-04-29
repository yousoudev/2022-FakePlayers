package me.yousou.FakePlayers.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.yousou.FakePlayers.Core;
import me.yousou.FakePlayers.handler.FPHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandFakeplayers implements CommandExecutor {
  private FPHandler fph;
  
  private Random r;
  
  private List<String> alp;
  
  public CommandFakeplayers() {
    this.r = new Random();
    this.alp = new ArrayList<>(Arrays.asList(new String[] { 
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
            "k", "l", "m" }));
    Core.getPlugin(Core.class).getCommand("fakeplayers").setExecutor(this);
    this.fph = new FPHandler();
  }
  
  public void savedelete() {
    this.fph.destroyAllFakes();
  }
  
  private String rdmName() {
    String f = "";
    for (int i = 0; i < 12; i++)
      f = String.valueOf(f) + ((this.r.nextInt(1) == 0) ? this.alp.get(this.r.nextInt(this.alp.size())) : ((String)this.alp.get(this.r.nextInt(this.alp.size()))).toUpperCase()); 
    return f;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length < 1) {
      sender.sendMessage("/fakeplayers <add> <amount>");
      sender.sendMessage("/fakeplayers <remove> <amount>");
      sender.sendMessage("/fakeplayers <destroyall>");
      return true;
    } 
    if (args[0].equalsIgnoreCase("add")) {
      if (args.length != 2) {
        sender.sendMessage("/fakeplayers <add> <amount>");
        return true;
      } 
      int s = getInt(args[1]);
      if (s < 0) {
        sender.sendMessage("Specify a real number");
        return true;
      } 
      for (int i = 0; i < s; i++)
        this.fph.createFake(rdmName()); 
      sender.sendMessage("Created " + s + " fake players");
    } 
    if (args[0].equalsIgnoreCase("remove")) {
      if (args.length != 2) {
        sender.sendMessage("/fakeplayers <remove> <amount>");
        return true;
      } 
      int s = getInt(args[1]);
      if (s < 0) {
        sender.sendMessage("Specify a real number");
        return true;
      } 
      sender.sendMessage("Destroyed fake players");
      this.fph.removeFakes(s);
    } 
    if (args[0].equalsIgnoreCase("destroyall")) {
      if (args.length != 1) {
        sender.sendMessage("/fakeplayers <destroyall>");
        return true;
      } 
      this.fph.destroyAllFakes();
      sender.sendMessage("Destroyed all fake players");
    } 
    return true;
  }
  
  private int getInt(String text) {
    try {
      Integer i = Integer.valueOf(Integer.parseInt(text));
      if (i.intValue() > 0)
        return i.intValue(); 
    } catch (NumberFormatException numberFormatException) {}
    return -1;
  }
}
