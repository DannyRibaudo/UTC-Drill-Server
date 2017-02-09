package co.gameserv.utc.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class SelfHealCustomEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player selfhealer;
    private double before;
    private double after;

    public SelfHealCustomEvent(Player player, double a, double b) {
        this.selfhealer = player;
        this.before = a;
        this.after = b;
    }

    public Player getPlayer() {
        return this.selfhealer;
    }

    public double getBefore() {
        return this.before;
    }

    public double getAfter() {
        return this.after;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


