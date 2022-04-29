package me.yousou.FakePlayers.handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mojang.authlib.GameProfile;

import me.yousou.FakePlayers.Core;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.player.EntityHuman;

public class FPHandler implements Listener {
  private List<EntityPlayer> fakes = new ArrayList<>();
  
  public FPHandler() {
    Bukkit.getPluginManager().registerEvents(this, Core.getPlugin(Core.class));
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    sendFakes(e.getPlayer());
  }
  
  public void createFake(String name) { 
    EntityPlayer ep;
    MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    WorldServer w = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
    
    Player t = Bukkit.getServer().getPlayer(name);
    if (t != null) {
      ep = new EntityPlayer(server, w, new GameProfile(t.getUniqueId(), t.getName()));
    } else {
      OfflinePlayer op = Bukkit.getOfflinePlayer(name);
      ep = new EntityPlayer(server, w, new GameProfile(op.getUniqueId(), name));
    } 
    Player random = null;
    for (Player on : Bukkit.getOnlinePlayers()) {
      random = on; 
    }
    Location loc = random.getLocation();
    ep.teleportTo(w, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()));
    for (Player on : Bukkit.getOnlinePlayers()) {
      PlayerConnection conn = (((CraftPlayer)on).getHandle()).b;
      conn.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, ep));
      conn.a(new PacketPlayOutNamedEntitySpawn((EntityHuman)ep));
    } 
    fakes.add(ep);
  }
  
  private void sendFakes(Player p) {
    for (EntityPlayer ep : fakes) {
      PlayerConnection conn = (((CraftPlayer)p).getHandle()).b;
      conn.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, ep));
      conn.a(new PacketPlayOutNamedEntitySpawn((EntityHuman)ep));
    } 
  }
  
  public void removeFake(EntityPlayer ep) {
    if (fakes.contains(ep)) {
      fakes.remove(ep);
      for (Player on : Bukkit.getOnlinePlayers()) {
        PlayerConnection conn = (((CraftPlayer)on).getHandle()).b;
        conn.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, ep));
        conn.a(new PacketPlayOutEntityDestroy(ep.getBukkitEntity().getEntityId()));
      } 
    } 
  }
  
  public int removeFakes(int amount) {
    for (int i = 0; i < amount; i++) {
      if (fakes.size() > 0)
        removeFake(fakes.get(0)); 
    } 
    return amount;
  }
  
  public void destroyAllFakes() {
    removeFakes(fakes.size());
  }
}
