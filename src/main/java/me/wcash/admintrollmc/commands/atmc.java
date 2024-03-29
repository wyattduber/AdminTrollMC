package me.wcash.admintrollmc.commands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.commands.trollcommands.fakecrash;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class atmc implements TabExecutor {

    public final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (sender instanceof Player player && !player.hasPermission("atmc.main")) {
            atmc.sendMessage(player, Component.text("You don't have permission!", NamedTextColor.RED));
        }

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload" -> {
                    if ((sender instanceof Player player && player.hasPermission("atmc.reload")) || sender instanceof ConsoleCommandSender) {
                        if (atmc.reload()) {
                            atmc.sendMessage(sender, Component.text("Plugin Reloaded!"));
                        } else {
                            atmc.sendMessage(sender, Component.text("Plugin Reload Failed! See console for details.", NamedTextColor.RED));
                        }
                    }
                }
                case "fakecrash" -> {
                    if (args.length == 1) return false; // Didn't send in a player name
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakecrash")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakecrash.execute(args[1]));
                    }
                }
            }

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> tabs = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                if (sender instanceof Player player && player.hasPermission("atmc.reload"))
                    tabs.add("reload");
                if (sender instanceof Player player && player.hasPermission("atmc.fakecrash"))
                    tabs.add("fakecrash");
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("fakecrash")) {
                    if (sender instanceof Player player && player.hasPermission("atmc.fakecrash")) {
                        tabs.addAll(getOnlinePlayers());
                    }
                }
            }
        }

        return tabs;
    }

    public List<String> getOnlinePlayers() {
        List<String> players = new ArrayList<>();

        for (Player onlinePlayer : atmc.getServer().getOnlinePlayers()) {
            players.add(onlinePlayer.getName());
        }

        return players;
    }
}
