package co.gameserv.utc.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HealOtherCustomEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player healer;
    private Player healed;

    public HealOtherCustomEvent(Player player, Player interacted) {
        this.healer = player;
        this.healed = interacted;
    }

    public Player getHealer() {
        return this.healer;
    }

    public Player getHealed() {
        return this.healed;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
