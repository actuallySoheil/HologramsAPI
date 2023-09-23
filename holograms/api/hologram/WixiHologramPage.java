package me.wixi.holograms.api.hologram;

import me.wixi.holograms.core.hologram.HologramLine;

import java.util.List;

public interface WixiHologramPage {

    /**
     * Get current page.
     * @return current page.
     */
    int getPage();

    /**
     * Get lines of this page.
     * @return lines of this page.
     */
    String[] getLines();

    /**
     * Get the {@link HologramLine hologramLine} by line number
     * @param lineNumber line number to get
     * @return Hologram Line
     */
    WixiHologramLine getHologramLineByNumber(int lineNumber);

    /**
     * Get all the lines.
     * @return list of all lines in this page.
     */
    List<WixiHologramLine> getHologramLines();

}