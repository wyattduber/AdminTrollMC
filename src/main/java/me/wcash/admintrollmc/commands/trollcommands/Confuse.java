package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.player.TrollPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Confuse {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent confuse(String target, int seconds) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        // Check if they are already confused
        if (player.isConfused())
            return Component.text("Player is already confused!", NamedTextColor.RED);

        player.setConfused(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, seconds, 255));

        return Component.text(String.format("Confused " + target + " for " + AdminTrollMC.formatSeconds(seconds) + "!"));
    }

    public static TextComponent clearMind(String target) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        // Check if they are already confused
        if (!player.isConfused())
            return Component.text("Player is not confused!", NamedTextColor.RED);

        player.setConfused(false);
        player.removePotionEffect(PotionEffectType.CONFUSION);

        return Component.text(target + " is no longer confused!");
    }
}
