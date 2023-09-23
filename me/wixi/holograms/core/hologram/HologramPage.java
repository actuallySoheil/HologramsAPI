package me.wixi.holograms.core.hologram;

import lombok.val;
import me.wixi.holograms.api.hologram.WixiHologramPage;
import me.wixi.holograms.core.util.StringUtil;
import me.wixi.holograms.api.hologram.WixiHologramLine;

import java.util.*;

public class HologramPage implements WixiHologramPage {

    private final int page;
    private final String[] lines;

    private final HashMap<Integer, HologramLine> hologramLines;

    protected HologramPage(Hologram hologram, int page, String... lines) {
        this.page = page;
        this.lines = lines;
        this.hologramLines = new HashMap<>();

        val linesList = Arrays.asList(lines);
        Collections.reverse(linesList);

        int lineNumber = 0;

        for (val line : linesList) {
            if (!line.isEmpty() && !line.equals(StringUtil.color("&r"))) {
                this.hologramLines.put(
                        lineNumber,
                        new HologramLine(
                                hologram.getLocation().clone().add(0, 0.33 * lineNumber, 0),
                                StringUtil.color(line)
                        )
                );
            }
            lineNumber++;
        }
    }

    @Override
    public int getPage() {
        return this.page;
    }

    @Override
    public String[] getLines() {
        return this.lines;
    }

    @Override
    public WixiHologramLine getHologramLineByNumber(int index) {
        return this.hologramLines.get(index);
    }

    @Override
    public List<WixiHologramLine> getHologramLines() {
        return new ArrayList<>(this.hologramLines.values());
    }

}