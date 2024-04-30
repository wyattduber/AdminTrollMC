package me.wcash.admintrollmc.listeners;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.commands.ATMCCommand;
import me.wcash.admintrollmc.commands.trollcommands.*;
import me.wcash.admintrollmc.database.Database;
import me.wcash.admintrollmc.player.TrollPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

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
            // Update username if applicable
            if (!db.getPlayerName(player.getUniqueId()).equals(player.getName())) {
                db.updatePlayerUsername(player.getName(), player.getUniqueId());
            }

            startTrollingOnLogin(player);
        }

        /* Add player to onlinePlayers list */
        atmc.onlinePlayers.put(player.getName(), player);
    }

    private void startTrollingOnLogin(TrollPlayer player) {
        UUID uuid = player.getUniqueId();

        // Frozen Check
        if (db.getStatus(uuid, "isFrozen")) {
            int secondsLeft = db.getTimeLeft(uuid, "frozenTimeLeft");
            atmc.cmd.freezeUnfreeze.freeze(player.getName(), secondsLeft);
        }

        // Burning Check
        if (db.getStatus(uuid, "isBurning")) {
            int secondsLeft = db.getTimeLeft(uuid, "burningTimeLeft");
            BurnExtinguish.burn(player.getName(), secondsLeft);
        }

        // Jumping Check
        if (db.getStatus(uuid, "isDontStopJumping")) {
            int secondsLeft = db.getTimeLeft(uuid, "dontStopJumpingTimeLeft");
            DontStopJumping.startJumping(player.getName(), secondsLeft);
        }

        // Confused Check
        if (db.getStatus(uuid, "isConfused")) {
            int secondsLeft = db.getTimeLeft(uuid, "confusedTimeLeft");
            Confuse.confuse(player.getName(), secondsLeft);
        }

        // Deafened Check
        if (db.getStatus(uuid, "isDeafened")) {
            int secondsLeft = db.getTimeLeft(uuid, "deafenedTimeLeft");
            Deafen.deafen(player.getName(), secondsLeft);
        }

        // Blind Check
        if (db.getStatus(uuid, "isBlind")) {
            int secondsLeft = db.getTimeLeft(uuid, "blindTimeLeft");
            Blind.blind(player.getName(), secondsLeft);
        }
    }

}
