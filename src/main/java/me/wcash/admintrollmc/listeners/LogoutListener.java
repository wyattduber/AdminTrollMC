package me.wcash.admintrollmc.listeners;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.player.TrollPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogoutListener implements Listener {

    private final AdminTrollMC atmc;

    public LogoutListener() {
        atmc = AdminTrollMC.getPlugin();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TrollPlayer trollPlayer = (TrollPlayer) player;
        trollPlayer.setFrozen(false);

        atmc.onlinePlayers.remove(player.getName());
    }

}
