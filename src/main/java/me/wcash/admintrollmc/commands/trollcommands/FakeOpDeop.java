package me.wcash.admintrollmc.commands.trollcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FakeOpDeop {

    public static TextComponent execute(CommandSender source, String target, String type) {
        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        TextComponent message;
        if (type.equals("op")) {
            message = Component.text("[Server: Made " + targetPlayer.getName() + " a server operator]", NamedTextColor.GRAY);
        }
        else {
            message = Component.text("[Server: Made " + targetPlayer.getName() + " no longer a server operator]", NamedTextColor.GRAY);
        }

        targetPlayer.sendMessage(message);
        source.sendMessage(message);

        return null;
    }

}
