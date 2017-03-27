package being;

import being.view_trash.enums.ActionType;

import java.util.Arrays;

public class KeyboardEvent {
    private final int[] keys;
    private final Runnable runnable;
    private final int triggerActionType;

    private int currentActionType;
    private double lastActionTime;

    public KeyboardEvent(Runnable runnable, int triggerActionType, int... keys) {
        this.keys = keys;
        this.triggerActionType = triggerActionType;
        this.runnable = runnable;
        this.currentActionType = ActionType.NO_ACTION;
        this.lastActionTime = System.currentTimeMillis();
    }

    public int[] getKeys() {
        return keys;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public int getTriggerActionType() {
        return triggerActionType;
    }

    public int getCurrentActionType() {
        return currentActionType;
    }

    public void setCurrentActionType(int currentActionType) {
        if (this.currentActionType != currentActionType) {
            lastActionTime = System.currentTimeMillis();
        }
        this.currentActionType = currentActionType;
    }

    public void run() {
        runnable.run();
    }

    public double getLastActionTime() {
        return lastActionTime;
    }

    @Override
    public String toString() {
        return "KeyboardEvent{" +
                "keys=" + Arrays.toString(keys) +
                ", triggerActionType=" + triggerActionType +
                ", currentActionType=" + currentActionType +
                '}';
    }
}
