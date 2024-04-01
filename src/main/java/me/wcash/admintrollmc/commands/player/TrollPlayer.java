package me.wcash.admintrollmc.commands.player;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public abstract class TrollPlayer implements Player {

    private boolean isFrozen = false;
    private boolean isBurning = false;
    private boolean isDontStopJumping = false;
    private BukkitTask freezeTask = null;
    private BukkitTask isJumpingTask = null;

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

    public boolean isDontStopJumping() {
        return isDontStopJumping;
    }

    public void setDontStopJumping(boolean isDontStopJumping) {
        this.isDontStopJumping = isDontStopJumping;
    }

    public void setFreezeTask(BukkitTask freezeTask) {
        this.freezeTask = freezeTask;
    }

    public BukkitTask getFreezeTask() {
        return freezeTask;
    }

    public void setIsJumpingTask(BukkitTask jumpingTask) {
        this.isJumpingTask = jumpingTask;
    }

    public BukkitTask getIsJumpingTask() {
        return isJumpingTask;
    }

}
