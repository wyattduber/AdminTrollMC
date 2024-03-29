package me.wcash.admintrollmc.listeners;

import me.wcash.admintrollmc.AdminTrollMC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener implements Listener {

    private final boolean updateRequired;
    private final String[] versions;
    private final AdminTrollMC atmc;

    public LoginListener(boolean updateRequired, String[] versions) {
        this.updateRequired = updateRequired;
        this.versions = versions;
        atmc = AdminTrollMC.getPlugin();
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        /* Check for Updates and send message to player with permission to see updates */
        if (updateRequired && (event.getPlayer().hasPermission("atmc.update") || event.getPlayer().isOp())) {
            atmc.sendMessage(event.getPlayer(), AdminTrollMC.replaceColors("Version &c" + versions[0] + " &favailable! You have &c" + versions[1] + "&f."));
            atmc.sendMessage(event.getPlayer(), AdminTrollMC.replaceColors("Download it at: &9https://www.spigotmc.org/resources/mcdbridge-beta.88409/"));

            atmc.log("Version " + versions[0] + " available! You have " + versions[1] + ".");
            atmc.log("Download it at: https://www.spigotmc.org/resources/mcdbridge-beta.88409/");
        }
    }

}
