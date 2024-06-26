package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.player.TrollPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

public class DontStopJumping {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent startJumping(String target, int seconds) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        if (player.isDontStopJumping()) {
            return Component.text(target + " is already endlessly jumping!", NamedTextColor.RED);
        }

        player.setDontStopJumping(true);
        player.getPlayer().setJumping(true);

        player.setIsJumpingTask(Bukkit.getScheduler().runTaskAsynchronously(atmc, () -> {

            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException ignored) {}

            player.getPlayer().setJumping(false);
            player.setDontStopJumping(false);
            player.clearIsJumpingTask();
        }));

        return Component.text(target + "now jumping for " + AdminTrollMC.formatSeconds(seconds) + "!");
    }

    public static TextComponent stopJumping(String target) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        if (!player.isDontStopJumping()) {
            return Component.text(target + " is not endlessly jumping!", NamedTextColor.RED);
        }

        player.getIsJumpingTask().cancel();
        player.clearIsJumpingTask();
        player.setDontStopJumping(false);
        player.getPlayer().setJumping(false);

        return Component.text(target + " is no longer endlessly jumping!");
    }

}
