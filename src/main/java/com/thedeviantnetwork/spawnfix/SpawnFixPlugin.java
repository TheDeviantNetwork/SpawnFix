package com.thedeviantnetwork.spawnfix;

import com.earth2me.essentials.User;
import com.earth2me.essentials.spawn.IEssentialsSpawn;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnFixPlugin extends JavaPlugin implements Listener {

    private IEssentials essentials;
    private IEssentialsSpawn spawns;

    public void onEnable()
    {
        PluginManager pluginManager = getServer().getPluginManager();
        this.essentials = ((IEssentials)pluginManager.getPlugin("Essentials"));
        this.spawns = ((IEssentialsSpawn)pluginManager.getPlugin("EssentialsSpawn"));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void PlayerLoginEvent(PlayerJoinEvent event)
    {
        User user = this.essentials.getOfflineUser(event.getPlayer().getName());

        if (user != null)
            if (user.getLogoutLocation() != null) {
                event.getPlayer().teleport(user.getLogoutLocation());
            } else {
                handleFirstSpawn(user);
            }
        else
            try {
                event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void handleFirstSpawn(User user)
    {
        Location spawn = spawns.getSpawn(essentials.getSettings().getNewbieSpawn());
        try
        {
            user.getTeleport().teleport(spawn, null, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
