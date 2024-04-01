package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

public class FakeCrash {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent execute(String target) {
        Player player = Bukkit.getPlayer(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        player.kick(Component.text((String)atmc.getConfigValue("fake-crash-message")), PlayerKickEvent.Cause.UNKNOWN);

        return Component.text("Crashed " + target + "!");
    }

}
