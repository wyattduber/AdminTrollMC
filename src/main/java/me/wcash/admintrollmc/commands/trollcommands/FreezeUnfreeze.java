package me.wcash.admintrollmc.commands.trollcommands;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import me.wcash.admintrollmc.AdminTrollMC;
import me.wcash.admintrollmc.commands.player.TrollPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeUnfreeze implements Listener {

    private static final AdminTrollMC atmc = AdminTrollMC.getPlugin();

    public TextComponent freeze(String target, int seconds) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        if (player.isFrozen()) {
            return Component.text(target + " is already frozen!", NamedTextColor.RED);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, seconds * 20, 255));
        player.setFrozen(true);
        player.setFreezeTicks(seconds * 20);

        player.setFreezeTask(Bukkit.getScheduler().runTaskAsynchronously(atmc, () -> {
            try {
                // Start Listening
                atmc.getServer().getPluginManager().registerEvents(this, atmc);

                // Wait for the duration
                Thread.sleep(seconds * 1000L);

                // Stop Listening and remove all frozen effects
                player.setFrozen(false);
                player.removePotionEffect(PotionEffectType.SLOW);
                player.clearFreezeTask();
                PlayerJumpEvent.getHandlerList().unregister(this);
                EntityDamageEvent.getHandlerList().unregister(this);
            } catch (InterruptedException e) {
                atmc.error("Failed to freeze player " + target + " for " + seconds + " seconds! Stack Trace:");
                atmc.error(e.getMessage());
            }

            player.setFrozen(false);
        }));

        return Component.text(target + "frozen for " + AdminTrollMC.formatSeconds(seconds) + "!");
    }

    public TextComponent unfreeze(String target) {
        TrollPlayer player = atmc.onlinePlayers.get(target);
        if (player == null) {
            return Component.text("Player is not online!", NamedTextColor.RED);
        }

        if (!player.isFrozen()) {
            return Component.text(target + " is not frozen!", NamedTextColor.RED);
        }

        if (Bukkit.getScheduler().getPendingTasks().contains(player.getFreezeTask()))
            player.getFreezeTask().cancel();

        player.setFrozen(false);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.getFreezeTask().cancel();
        player.clearFreezeTask();
        PlayerJumpEvent.getHandlerList().unregister(this);
        EntityDamageEvent.getHandlerList().unregister(this);

        return Component.text("Unfroze " + target + "!");
    }

    @EventHandler
    private void onPlayerJump(PlayerJumpEvent event) {
        TrollPlayer trollPlayer = (TrollPlayer) event.getPlayer();

        if (trollPlayer.isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerFreezeDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            TrollPlayer trollPlayer = (TrollPlayer) player;

            if (trollPlayer.isFrozen() && event.getCause() == EntityDamageEvent.DamageCause.FREEZE) {
                event.setCancelled(true);
            }
        }
    }

}
