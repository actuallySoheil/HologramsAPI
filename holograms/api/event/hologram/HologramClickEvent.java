package me.wixi.holograms.api.event.hologram;

import me.wixi.holograms.api.event.WixiEvent;
import me.wixi.holograms.api.hologram.WixiHologram;
import me.wixi.holograms.api.hologram.WixiHologramLine;
import me.wixi.holograms.api.hologram.WixiHologramPage;
import org.bukkit.entity.Player;

public class HologramClickEvent extends WixiEvent {

    private final Player clicker;
    private final WixiHologram hologram;
    private final WixiHologramPage hologramPage;
    private final WixiHologramLine hologramLine;

    public HologramClickEvent(Player clicker, WixiHologram hologram, WixiHologramPage hologramPage, WixiHologramLine hologramLine) {
        this.clicker = clicker;
        this.hologram = hologram;
        this.hologramPage = hologramPage;
        this.hologramLine = hologramLine;
    }

    /**
     * Get the clicker of hologram.
     *
     * @return player who clicked the hologram.
     */
    public Player getClicker() {
        return clicker;
    }

    /**
     * Get the clicked hologram.
     *
     * @return clicked hologram.
     */
    public WixiHologram getHologram() {
        return hologram;
    }

    /**
     * Get the clicked hologram page.
     *
     * @return clicked hologram page.
     */
    public WixiHologramPage getHologramPage() {
        return hologramPage;
    }

    /**
     * Get the clicked hologram line.
     *
     * @return clicked hologram line.
     */
    public WixiHologramLine getHologramLine() {
        return hologramLine;
    }

}