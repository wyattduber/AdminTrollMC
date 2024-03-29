package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

public class fakeop {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent execute(String target) {
        Player player = Bukkit.getPlayer(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        player.sendMessage(Component.text("[Server: Opped " + player.getName() + "]", NamedTextColor.DARK_GRAY));

        return Component.text("Successfully fake-opped " + target + "!");
    }

}
