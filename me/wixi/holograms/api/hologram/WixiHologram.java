package me.wixi.holograms.api.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface WixiHologram {

    /**
     * Create and spawn a hologram.
     *
     * @param location location of the hologram.
     * @param lines    lines of the hologram.
     */
    void spawn(Location location, String... lines);

    /**
     * Remove the hologram from all the players.
     */
    void remove();

    /**
     * Add page to the hologram.
     *
     * @param lines lines to add to the new page.
     */
    void addPage(String... lines);

    /**
     * Remove this page number from hologram.
     *
     * @param index page number.
     */
    void removePage(int index);

    /**
     * Show hologram page to all players.
     *
     * @param page page number to show.
     */
    void showPage(int page);

    /**
     * Show this hologram page to this player.
     *
     * @param player player to show to.
     * @param page   page number.
     */
    void showPageToPlayer(Player player, int page);

    /**
     * Show hologram to this player.
     *
     * @param player player to show.
     */
    void showToPlayer(Player player);

    /**
     * Hide hologram from the player.
     *
     * @param player player to hide from.
     */
    void hideFromPlayer(Player player);

    /**
     * Check if the hologram is spawned.
     *
     * @return true if the hologram is already spawned.
     */
    boolean isSpawned();

    /**
     * Get {@link WixiHologramPage hologram page} by number
     *
     * @param number page number
     * @return page
     */
    WixiHologramPage getPageByNumber(int number);

    /**
     * Get list of hologram pages
     *
     * @return hologram pages
     */
    List<WixiHologramPage> getPages();

    /**
     * Get the current page that hologram is in.
     *
     * @return current page.
     */
    WixiHologramPage getCurrentPage();

    /**
     * Get the amount of the pages
     *
     * @return amount of the pages
     */
    int getPagesCount();

    /**
     * Get hologram name.
     * @return hologram name.
     */
    String getName();

    /**
     * Get main hologram location that was spawned.<p>
     *
     * @return main location of the hologram.
     */
    Location getLocation();

}