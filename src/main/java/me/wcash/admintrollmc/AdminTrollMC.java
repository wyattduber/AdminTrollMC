package me.wcash.admintrollmc;

import me.wcash.admintrollmc.commands.atmc;
import me.wcash.admintrollmc.listeners.LoginListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public final class AdminTrollMC extends JavaPlugin {

    public FileConfiguration config;
    public File customConfigFile;
    public LoginListener ll;
    public static String[] versions = new String[2];
    public HashMap<String, Object> configValues;

    public List<String> commands;

    @Override
    public void onEnable() {

        /* Load and Initiate Configs */
        try {
            reloadCustomConfig();
            config = getCustomConfig();
            saveCustomConfig();
        } catch (Exception e) {
            error("Error setting up the config! Contact the developer if you cannot fix this issue. Stack Trace:");
            error(e.getMessage());
        }

        /* Config Parsing */
        if (parseConfig()) {
            error("Config Not Properly Configured! Plugin will not function!");
        }

        /* Initialize Listeners */
        initListeners();

        /* Commands */
        try {
            initCommands();
        } catch (NullPointerException e) {
            error("Error setting up commands! Contact the developer if you cannot fix this issue. Stack Trace:");
            error(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean reload() {
        boolean flag = true;

        reloadCustomConfig();
        config = getCustomConfig();
        saveCustomConfig();

        /* Un-Register Listeners */
        PlayerJoinEvent.getHandlerList().unregister(ll);

        /* Config Parsing */
        if (parseConfig()) {
            flag = false;
            error("Config Not Properly Configured! Plugin will not function!");
        }

        /* Initialize Listeners */
        initListeners();
        return flag;
    }

    public boolean parseConfig() {
        try {
            configValues = (HashMap<String, Object>) config.getValues(false);
        } catch (Exception ex) {
            warn("Error loading config! Plugin will not work properly!");
            return true;
        }

        log("Config Loaded!");
        return false;
    }

    public void initListeners() {
        try {
            new UpdateChecker(this, 88409).getVersion(version -> {
                // Initializes Login Listener when no Updates
                if (compareVersions(this.getPluginMeta().getVersion(), version) < 0) {
                    versions[0] = version;
                    versions[1] = this.getPluginMeta().getVersion();
                    getServer().getPluginManager().registerEvents(new LoginListener(true, versions), this);
                } else {
                    getServer().getPluginManager().registerEvents(new LoginListener(false, versions), this);
                }
            });
        } catch (Exception e) {
            error("Error initializing Update Checker! Contact the developer if you cannot fix this issue. Stack Trace:");
            error(e.getMessage());
        }
        log("Minecraft Listeners Loaded!");
    }

    public void initCommands() throws NullPointerException {
        // Init the command
        Objects.requireNonNull(this.getCommand("atmc")).setExecutor(new atmc());

        // List out all sub-commands
        commands = new ArrayList<>();
        commands.add("reload");
        commands.add("fakecrash");
        commands.add("fakeop");
    }

    public Object getConfigValue(String key) {
        return configValues.get(key);
    }

    /* Standard Methods */

    public static AdminTrollMC getPlugin() {
        return getPlugin(AdminTrollMC.class);
    }

    public void reloadCustomConfig() {
        saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(Objects.requireNonNull(this.getResource("config.yml")), StandardCharsets.UTF_8);
        } catch (Exception e) {
            error("Error loading default config! Contact the developer if you cannot fix this issue. Stack Trace:");
            error(e.getMessage());
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
    }

    public FileConfiguration getCustomConfig() {
        if (config == null) {
            reloadCustomConfig();
        }
        return config;
    }

    public void saveCustomConfig() {
        if (config == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(getDataFolder(), "config.yml");
        }
        if (!customConfigFile.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    public void log(String message) {
        this.getLogger().log(Level.INFO, message);
    }

    public void warn(String message) {
        this.getLogger().log(Level.WARNING, message);
    }

    public void error(String message) {
        this.getLogger().log(Level.SEVERE, message);
    }

    public void debug(String message) {
        this.getLogger().log(Level.FINE, message);
    }

    public void sendMessage(CommandSender sender, TextComponent component) {
        if (sender instanceof Player player) {
            player.sendMessage(Component.text("§f[§9AdminTrollMC§f] ").append(component));
        } else {
            log(component.content());
        }
    }

    // Method to compare two versions numerically
    private int compareVersions(String installedVersion, String newestVersion) {
        String[] installedParts = installedVersion.split("\\.");
        String[] newestParts = newestVersion.split("\\.");

        int minLength = Math.min(installedParts.length, newestParts.length);
        for (int i = 0; i < minLength; i++) {
            int installedPart = Integer.parseInt(installedParts[i]);
            int newestPart = Integer.parseInt(newestParts[i]);
            if (installedPart < newestPart) {
                return -1; // installed version is older
            } else if (installedPart > newestPart) {
                return 1; // installed version is newer
            }
        }

        // If we reach here, versions are equal up to minLength
        // So, if one version has more parts, it is considered newer
        // installed version is newer
        // versions are exactly the same
        return Integer.compare(installedParts.length, newestParts.length); // installed version is older
    }

}
