package me.wcash.admintrollmc.commands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.commands.trollcommands.fakecrash;
import me.wcash.admintrollmc.commands.trollcommands.fakeop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
                        return true;
                    }
                }
                case "fakecrash" -> {
                    if (args.length == 1) atmc.sendMessage(sender, Component.text("Fake a disconnect crash: /atmc fakecrash <player>", NamedTextColor.RED)); // Didn't send in a player name
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakecrash")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakecrash.execute(args[1]));
                    }
                    return true;
                }
                case "fakeop" -> {
                    if (args.length == 1) atmc.sendMessage(sender, Component.text("Fake op a player: /atmc fakeop <player>", NamedTextColor.RED)); // Didn't send in a player name
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakeop")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakeop.execute(args[1]));
                    }
                }
                default -> {
                    TextComponent component = Component.text("Valid subcommands are: ", NamedTextColor.RED); // Make initial message

                    for (int i = 0; i < atmc.commands.size(); i++) {
                        if ((sender instanceof Player player && player.hasPermission("atmc." + atmc.commands.get(i))) || sender instanceof ConsoleCommandSender) {
                            if (i != atmc.commands.size() - 1) {
                                component = component.append(Component.text(atmc.commands.get(i) + " ", NamedTextColor.RED));
                            }
                            else {
                                component = component.append(Component.text(atmc.commands.get(i) + ".", NamedTextColor.RED));
                            }
                        }
                    }

                    atmc.sendMessage(sender, component);
                    return true;
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
