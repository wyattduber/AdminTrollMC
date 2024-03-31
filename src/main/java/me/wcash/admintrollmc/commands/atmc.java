package me.wcash.admintrollmc.commands;

import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.commands.trollcommands.*;
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
    public final List<String> subcommands = new ArrayList<>()
    {
        {
            add("chatsudo");
            add("fakecrash");
            add("fakedeop");
            add("fakejoin");
            add("fakeleave");
            add("fakeop");
            add("reload");
        }
    };

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
                case "chatsudo" -> {
                    if (args.length == 1) {
                        atmc.sendMessage(sender, Component.text("Chat as a player: /atmc chatsudo <player> <message...>", NamedTextColor.RED)); // Didn't send in a player name
                        return true;
                    }
                    if (args.length == 2) {
                        atmc.sendMessage(sender, Component.text("Chat as a player: /atmc chatsudo <player> <message...>", NamedTextColor.RED)); // Didn't send in a message
                        return true;
                    }
                    if ((sender instanceof Player player && player.hasPermission("atmc.chatsudo")) || sender instanceof ConsoleCommandSender) {

                        // Build message from rest of "arguments"
                        StringBuilder message = new StringBuilder();
                        for (int i = 0; i< args.length; i++) {
                            if (i == 0 || i == 1) continue;
                            message.append(args[i]);
                        }

                        // Send message
                        atmc.sendMessage(sender, chatsudo.execute(args[1], message.toString()));
                        return true;
                    }
                }
                case "fakecrash" -> {
                    if (args.length == 1) {
                        atmc.sendMessage(sender, Component.text("Fake a disconnect crash: /atmc fakecrash <player>", NamedTextColor.RED)); // Didn't send in a player name
                        return true;
                    }
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakecrash")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakecrash.execute(args[1]));
                        return true;
                    }
                    return true;
                }
                case "fakedeop" -> {
                    if (args.length == 1) {
                        atmc.sendMessage(sender, Component.text("Fake deop a player: /atmc fakedeop <player>", NamedTextColor.RED)); // Didn't send in a player name
                        return true;
                    }
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakedeop")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakeopdeop.execute(sender, args[1], "deop"));
                        return true;
                    }
                }
                case "fakejoin" -> {
                    if (args.length == 1) {
                        atmc.sendMessage(sender, Component.text("Fake a player's join message: /atmc fakejoin <player>", NamedTextColor.RED)); // Didn't send in a player name
                        return true;
                    }
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakejoin")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakejoinleave.execute(args[1], "join"));
                        return true;
                    }
                }
                case "fakeleave" -> {
                    if (args.length == 1) {
                        atmc.sendMessage(sender, Component.text("Fake player's leave message: /atmc fakeleave <player>", NamedTextColor.RED)); // Didn't send in a player name
                        return true;
                    }
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakeleave")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakejoinleave.execute(args[1], "leave"));
                        return true;
                    }
                }
                case "fakeop" -> {
                    if (args.length == 1) {
                        atmc.sendMessage(sender, Component.text("Fake op a player: /atmc fakeop <player>", NamedTextColor.RED)); // Didn't send in a player name
                        return true;
                    }
                    if ((sender instanceof Player player && player.hasPermission("atmc.fakeop")) || sender instanceof ConsoleCommandSender) {
                        atmc.sendMessage(sender, fakeopdeop.execute(sender, args[1], "op"));
                        return true;
                    }
                }
                default -> {
                    returnAllowedSubCommandsList(sender);
                    return true;
                }
            }

        }
        else {
            returnAllowedSubCommandsList(sender);
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) return null;
        Player player = (Player) sender;

        List<String> tabs = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                return TabCompleteHelper.TabComplete(args[0], subcommands, player);
            }
            case 2 -> {
                return TabCompleteHelper.TabComplete(args[1], getOnlinePlayers(), player);
            }
            case 3 -> {
                switch (args[0].toLowerCase()) {
                    case "chatsudo" -> {
                        if (player.hasPermission("atmc.chatsudo")) {
                            tabs.add("message...");
                        }
                    }
                }
            }
        }

        return tabs;
    }

    private List<String> getOnlinePlayers() {
        List<String> players = new ArrayList<>();

        for (Player onlinePlayer : atmc.getServer().getOnlinePlayers()) {
            players.add(onlinePlayer.getName());
        }

        return players;
    }

    private void returnAllowedSubCommandsList(CommandSender sender) {
        TextComponent component = Component.text("Valid subcommands are: ", NamedTextColor.WHITE); // Make initial message

        for (int i = 0; i < atmc.commands.size(); i++) {
            if ((sender instanceof Player player && player.hasPermission("atmc." + atmc.commands.get(i))) || sender instanceof ConsoleCommandSender) {
                if (i != atmc.commands.size() - 1) {
                    component = component.append(Component.text(atmc.commands.get(i), NamedTextColor.RED)).append(Component.text(", ", NamedTextColor.WHITE));
                }
                else {
                    component = component.append(Component.text("and ", NamedTextColor.WHITE)).append(Component.text(atmc.commands.get(i), NamedTextColor.RED)).append(Component.text(".", NamedTextColor.WHITE));
                }
            }
        }

        atmc.sendMessage(sender, component);
    }
}
