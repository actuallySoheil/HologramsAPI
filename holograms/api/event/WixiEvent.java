package me.wixi.holograms.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WixiEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}