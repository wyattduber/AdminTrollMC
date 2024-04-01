package me.wcash.admintrollmc.commands.trollcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Smite {

    public static TextComponent execute(String target) {
        Player player = Bukkit.getPlayer(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        player.getWorld().strikeLightning(player.getLocation());

        return Component.text("Smited " + target + "!");
    }

}
