package co.gameserv.utc.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BleedingCustomEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player bleeder;

    public BleedingCustomEvent(Player player) {
        this.bleeder = player;
    }

    public Player getBleeder() {
        return this.bleeder;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

