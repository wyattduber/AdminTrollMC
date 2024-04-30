package me.wcash.admintrollmc.listeners;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.database.Database;
import me.wcash.admintrollmc.player.TrollPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener implements Listener {

    private final boolean updateRequired;
    private final String[] versions;
    private final AdminTrollMC atmc;
    private final Database db;

    public LoginListener(boolean updateRequired, String[] versions) {
        this.updateRequired = updateRequired;
        this.versions = versions;
        atmc = AdminTrollMC.getPlugin();
        db = atmc.db;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        TrollPlayer player = (TrollPlayer) event.getPlayer();
        
        /* Check for Updates and send message to player with permission to see updates */
        if (updateRequired && (player.hasPermission("atmc.update") || player.isOp())) {
            atmc.sendMessage(player, AdminTrollMC.replaceColors("Version &c" + versions[0] + " &favailable! You have &c" + versions[1] + "&f."));
            atmc.sendMessage(player, AdminTrollMC.replaceColors("Download it at: &9https://www.spigotmc.org/resources/mcdbridge-beta.88409/"));

            atmc.log("Version " + versions[0] + " available! You have " + versions[1] + ".");
            atmc.log("Download it at: https://www.spigotmc.org/resources/mcdbridge-beta.88409/");
        }

        /* Check if player exists in database, if not, add them. If so, check username and update if necessary */
        if (!db.doesPlayerExistInDatabase(player.getUniqueId())) {
            db.insertPlayer(player.getName(), player.getUniqueId());
        } else {
            String name = db.getPlayerName(player.getUniqueId());

            if (!name.equals(player.getName())) {
                db.updatePlayerUsername(player.getName(), player.getUniqueId());
            }
        }

        /* Add player to onlinePlayers list */
        atmc.onlinePlayers.put(player.getName(), (TrollPlayer) player);
    }

}
