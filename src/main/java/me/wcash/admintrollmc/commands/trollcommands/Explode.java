package me.wcash.admintrollmc.commands.trollcommands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.commands.player.TrollPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class Explode {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public static TextComponent execute(String target, String power) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        // Do some number checks
        float parsedPower;
        try {
            parsedPower = Float.parseFloat(power);
        } catch (NumberFormatException e) {
            return Component.text("Invalid power! Submit a number between 1-10.", NamedTextColor.RED);
        }

        if (parsedPower < 1 || parsedPower > 10) {
            return Component.text("Invalid power! Submit a number between 1-10.", NamedTextColor.RED);
        }

        player.getLocation().createExplosion(parsedPower, false, false);

        return Component.text("Exploded " + target + "!");
    }

}
