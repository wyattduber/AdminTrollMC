package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.player.TrollPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Blind {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent blind(String target, int seconds) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        // Check if they are already blinded
        if (player.isBlind())
            return Component.text("Player is already blinded!", NamedTextColor.RED);

        player.setBlind(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, seconds, 255));

        return Component.text("Blinded " + target + "for " + AdminTrollMC.formatSeconds(seconds) + "!");
    }

    public static TextComponent regainSight(String target) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        // Check if they are already blinded
        if (!player.isBlind())
            return Component.text("Player is not blinded!", NamedTextColor.RED);

        player.setBlind(false);
        player.removePotionEffect(PotionEffectType.BLINDNESS);

        return Component.text(target + " has regained their sight!");
    }

}
