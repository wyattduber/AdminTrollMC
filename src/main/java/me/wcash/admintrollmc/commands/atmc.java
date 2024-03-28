package me.wcash.admintrollmc.commands;

import me.wcash.admintrollmc.AdminTrollMC;
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

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if ((sender instanceof Player player && player.hasPermission("atmc.reload")) || sender instanceof ConsoleCommandSender) {
                    if (atmc.reload()) {
                        atmc.sendMessage(sender, Component.text("Plugin Reloaded!"));
                    } else {
                        atmc.sendMessage(sender, Component.text("Plugin Reload Failed!", NamedTextColor.RED));
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
            }
        }

        return tabs;
    }
}
