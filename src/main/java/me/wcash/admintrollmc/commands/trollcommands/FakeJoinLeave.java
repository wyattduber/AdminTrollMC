package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FakeJoinLeave {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent execute(String target, String type) {
        Player player = Bukkit.getPlayer(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        String message = atmc.getConfigValue("fake-" + type + "-message-format", String.class);

        // Handle Placeholders in join message, will add more in future
        message = message.replace("%USERNAME%", player.getName());

        // Handle color codes
        message = AdminTrollMC.replaceColors(message);

        Bukkit.broadcast(Component.text(message));

        return null;
    }

}
