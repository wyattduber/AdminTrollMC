package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.commands.player.TrollPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class BurnExtinguish {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent burn(String target, int seconds) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        if (player.isBurning()) {
            return Component.text(target + " is already burning!", NamedTextColor.RED);
        }

        player.setFireTicks(seconds * 20);
        player.setBurning(true);

        return Component.text("Burning " + target + " for " + AdminTrollMC.formatSeconds(seconds) + "!");
    }

    public static TextComponent extinguish(String target) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        if (!player.isBurning()) {
            return Component.text(target + " is not burning!", NamedTextColor.RED);
        }

        player.setFireTicks(0);
        player.setBurning(false);

        return Component.text("Extinguished " + target + "!");
    }

}
