package me.wcash.admintrollmc.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteHelper {

    public static List<String> TabComplete(String currentArgument, List<String> commands, Player player) {
        List<String> validTabCompletes = new ArrayList<>();
        int currentArgumentLength = currentArgument.length();

        // Base Cases
        if (currentArgument.isEmpty()) {
            return validTabCompletes;
        } else if (commands.contains(currentArgument.toLowerCase())) {
            if (player.hasPermission("atmc." + currentArgument.toLowerCase()))
                validTabCompletes.add(currentArgument.toLowerCase());
            return validTabCompletes;
        }

        for (String command : commands) {
            if (command.substring(0, currentArgumentLength).equalsIgnoreCase(currentArgument)) {
                if (player.hasPermission("atmc." + command))
                    validTabCompletes.add(command);
            }
        }

        return validTabCompletes;
    }

}
