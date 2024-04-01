package me.wcash.admintrollmc.player;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public abstract class TrollPlayer implements Player {

    private boolean isFrozen = false;
    private boolean isBurning = false;
    private boolean isDontStopJumping = false;
    private boolean isConfused = false;
    private boolean isDeafened = false;
    private boolean isBlind= false;
    private BukkitTask freezeTask = null;
    private BukkitTask isJumpingTask = null;
    private BukkitTask isDeafenedTask = null;
    private List<BukkitTask> taskList = new ArrayList<>() {
        {
            add(freezeTask);
            add(isJumpingTask);
            add(isDeafenedTask);
        }
    };

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

    public void clearFreezeTask() {
        this.freezeTask = null;
    }

    public void setIsJumpingTask(BukkitTask jumpingTask) {
        this.isJumpingTask = jumpingTask;
    }

    public void clearIsJumpingTask() {
        this.isJumpingTask = null;
    }

    public BukkitTask getIsJumpingTask() {
        return isJumpingTask;
    }

    public boolean isConfused() {
        return isConfused;
    }

    public void setConfused(boolean isConfused) {
        this.isConfused = isConfused;
    }

    public boolean isDeafened() {
        return isDeafened;
    }

    public void setDeafened(boolean isDeaf) {
        this.isDeafened = isDeaf;
    }

    public BukkitTask getIsDeafenedTask() {
        return isDeafenedTask;
    }

    public void setIsDeafenedTask(BukkitTask isDeafenedTask) {
        this.isDeafenedTask = isDeafenedTask;
    }

    public void clearIsDeafenedTask() {
        this.isDeafenedTask = null;
    }

    public boolean isBlind() {
        return isBlind;
    }

    public void setBlind(boolean isBlind) {
        this.isBlind = isBlind;
    }

    public void setAllStatesFalse() {
        isBlind = false;
        isBurning = false;
        isConfused = false;
        isDeafened = false;
        isFrozen = false;
    }

    public List<BukkitTask> getTaskList(){
        return taskList;
    }

}
