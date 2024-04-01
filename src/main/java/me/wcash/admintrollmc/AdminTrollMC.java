package me.wcash.admintrollmc;

import me.wcash.admintrollmc.commands.player.TrollPlayer;
import me.wcash.admintrollmc.listeners.LoginListener;
import me.wcash.admintrollmc.listeners.LogoutListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public final class AdminTrollMC extends JavaPlugin {

    public FileConfiguration config;
    public File customConfigFile;
    public LoginListener loginListener;
    public LogoutListener logoutListener;
    public static String[] versions = new String[2];
    public HashMap<String, Object> configValues;
    public HashMap<String, TrollPlayer> onlinePlayers;

    @Override
    public void onEnable() {

        /* Load and Initialize Configs */
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

        /* Initialize Online Players Map */
        onlinePlayers = new HashMap<>(); // Can be empty on startup since nobody is online

        /* Initialize Listeners */
        initListeners();

        /* Initialize the Base Command */
        try {
            Objects.requireNonNull(this.getCommand("atmc")).setExecutor(new me.wcash.admintrollmc.commands.ATMCCommand());
        } catch (NullPointerException e) {
            error("Error setting up commands! Contact the developer if you cannot fix this issue. Stack Trace:");
            error(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        /* Cancel all pending/running tasks */
        Bukkit.getScheduler().cancelTasks(this);
    }

    public boolean reload() {
        boolean flag = true;

        // De-Register listeners
        PlayerJoinEvent.getHandlerList().unregister(loginListener);
        PlayerQuitEvent.getHandlerList().unregister(logoutListener);

        reloadCustomConfig();
        config = getCustomConfig();
        saveCustomConfig();

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
                    loginListener = new LoginListener(true, versions);
                    getServer().getPluginManager().registerEvents(loginListener, this);
                } else {
                    loginListener = new LoginListener(false, versions);
                    getServer().getPluginManager().registerEvents(loginListener, this);
                }
            });

            logoutListener = new LogoutListener();
        } catch (Exception e) {
            error("Error initializing Update Checker! Contact the developer if you cannot fix this issue. Stack Trace:");
            error(e.getMessage());
        }
        log("Minecraft Listeners Loaded!");
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

    /* Public Helper Methods */

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
        if (component == null) return; // Some command responses return a null component because they don't need a response

        if (sender instanceof Player player) {
            player.sendMessage(Component.text("§f[§9AdminTrollMC§f] ").append(component));
        } else {
            log(component.content());
        }
    }

    // Send message with color codes
    public void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage("§f[§9AdminTrollMC§f] " + replaceColors(message));
        } else {
            log(message);
        }
    }

    /**
     * The escape sequence for minecraft special chat codes
     */
    private static final char ESCAPE = '§';

    /**
     * Replace all the color codes (prepended with &) with the corresponding color code.
     * This uses raw char arrays, so it can be considered to be extremely fast.
     *
     * @param text the text to replace the color codes in
     * @return string with color codes replaced
     */
    public static String replaceColors(String text) {
        char[] chrarray = text.toCharArray();

        for (int index = 0; index < chrarray.length; index ++) {
            char chr = chrarray[index];

            // Ignore anything that we don't want
            if (chr != '&') {
                continue;
            }

            if ((index + 1) == chrarray.length) {
                // we are at the end of the array
                break;
            }

            // get the forward char
            char forward = chrarray[index + 1];

            // is it in range?
            if ((forward >= '0' && forward <= '9') || (forward >= 'a' && forward <= 'f') || (forward >= 'k' && forward <= 'r')) {
                // It is! Replace the char we are at now with the escape sequence
                chrarray[index] = ESCAPE;
            }
        }

        // Rebuild the string and return it
        return new String(chrarray);
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

    public static String formatSeconds(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        StringBuilder formattedTime = new StringBuilder();
        if (hours > 0) {
            formattedTime.append(hours).append(" hour");
            if (hours > 1) {
                formattedTime.append("s");
            }
            formattedTime.append(" ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append(" minute");
            if (minutes > 1) {
                formattedTime.append("s");
            }
            formattedTime.append(" ");
        }
        if (remainingSeconds > 0 || (hours == 0 && minutes == 0)) {
            formattedTime.append(remainingSeconds).append(" second");
            if (remainingSeconds != 1) {
                formattedTime.append("s");
            }
        }

        return formattedTime.toString();
    }

}
