package me.wixi.holograms.api.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WixiHologramLine {

    /**
     * Create a hologram line.
     *
     * @param location location of the hologram
     * @param line     line of the hologram
     */
    void create(Location location, String line);

    /**
     * Remove the hologram line.
     */
    void remove();

    /**
     * Show hologram to players.
     */
    void show();

    /**
     * Show the line of this hologram to this player. <p>
     * NOTE: this method will only show this line of the hologram to the player and not the whole hologram.
     *
     * @param player line to show the player with
     */
    void showToPlayer(Player player);

    /**
     * Hide the line of this hologram from this player. <p>
     * NOTE: this method will only hide this line of the hologram to the player and not the whole hologram.
     *
     * @param player line to hide from player
     */
    void hideFromPlayer(Player player);

    /**
     * Replace the current line of the hologram
     *
     * @param line line to replace.
     */
    void setLine(String line);

    /**
     * Replace the current line only for this player
     *
     * @param player player to replace the line with
     * @param line   line to replace.
     */
    void setLineForPlayer(Player player, String line);

    /**
     * Get line of the hologram
     *
     * @return line of the hologram
     */
    String getLine();

    /**
     * Get location of this line.
     *
     * @return location of this line.
     */
    Location getLocation();

}