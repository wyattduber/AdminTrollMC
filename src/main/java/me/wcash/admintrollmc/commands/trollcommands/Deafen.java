package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.player.TrollPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;

public class Deafen {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent deafen(String target, int seconds) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        // Check if they are already confused
        if (player.isDeafened())
            return Component.text("Player is already deafened!", NamedTextColor.RED);

        player.setDeafened(true);
        player.setIsDeafenedTask(Bukkit.getScheduler().runTaskAsynchronously(atmc, () -> {
            try {

                for (long i = 0; i < seconds * 1000L; i += 1000L) {
                    player.getPlayer().stopAllSounds();
                    Thread.sleep(1000L);
                }

                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException ignored) {}

            player.setDeafened(false);
            player.clearIsJumpingTask();
        }));

        return Component.text(String.format("Deafened " + target + " for " + AdminTrollMC.formatSeconds(seconds) + "!"));
    }

    public static TextComponent unDeafen(String target) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        // Check if they are already confused
        if (!player.isDeafened())
            return Component.text("Player is not deaf!", NamedTextColor.RED);

        player.setConfused(false);
        player.getPlayer().removePotionEffect(PotionEffectType.NAUSEA);
        player.getIsDeafenedTask().cancel();
        player.clearIsDeafenedTask();

        return Component.text(target + " is no longer deaf!");
    }

}
