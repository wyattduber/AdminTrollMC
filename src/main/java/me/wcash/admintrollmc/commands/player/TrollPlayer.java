package me.wcash.admintrollmc.commands.player;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public abstract class TrollPlayer implements Player {

    private boolean isFrozen = false;
    private boolean isBurning = false;
    private BukkitTask freezeTask = null;

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public boolean isBurning() {
        return isBurning;
    }

    public void setBurning(boolean isBurning) {
        this.isBurning = isBurning;
    }

    public void setFreezeTask(BukkitTask freezeTask) {
        this.freezeTask = freezeTask;
    }

    public BukkitTask getFreezeTask() {
        return freezeTask;
    }

}
