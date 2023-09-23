package me.wixi.holograms.core.hologram;

import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import me.wixi.holograms.api.hologram.WixiHologram;
import me.wixi.holograms.api.hologram.WixiHologramLine;
import me.wixi.holograms.api.hologram.WixiHologramPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hologram implements WixiHologram {

    private final HashMap<Integer, HologramPage> hologramPages;
    private final String name;

    private Location location;
    private boolean spawned;
    private int pageCount;
    private int currentPage;

    public Hologram(String name) {
        this.name = name;
        this.hologramPages = new HashMap<>();

        this.pageCount = 0;
        this.currentPage = 1;
    }

    @Override
    public void spawn(Location location, String... lines) {
        if (this.spawned) return;

        this.location = location;
        this.spawned = true;
        this.currentPage = 1;

        addPage(lines);
        showPage(1);
    }

    @Override
    public void addPage(String... lines) {
        this.pageCount++;
        this.hologramPages.put(this.pageCount, new HologramPage(this, this.pageCount, lines));
    }

    @Override
    public void removePage(int index) {
        if (index < 1 || index > this.pageCount) return;

        this.hologramPages.remove(index);
        this.pageCount--;

        val pageHashMap = new HashMap<Integer, HologramPage>();
        int tempPageCount = 1;
        for (val hologramPage : this.hologramPages.values()) {
            pageHashMap.put(tempPageCount, hologramPage);
            tempPageCount++;
        }

        this.hologramPages.clear();
        for (int i = 1; i <= this.pageCount; i++) pageHashMap.put(i, pageHashMap.get(i));

        pageHashMap.clear();
    }

    @Override
    public void remove() {
        this.spawned = false;

        this.hologramPages.values()
                .forEach(hologramPage -> hologramPage.getHologramLines()
                        .forEach(WixiHologramLine::remove));
    }

    @Override
    public void showPage(int page) {
        hidePage(this.currentPage);
        this.currentPage = page;

        this.hologramPages.get(page).getHologramLines()
                .forEach(wixiHologramLine -> {
                            wixiHologramLine.create(wixiHologramLine.getLocation(), wixiHologramLine.getLine());
                            wixiHologramLine.show();
                        }
                );
    }

    @Override
    public void showPageToPlayer(Player player, int page) {
        if (page < 1 || page > this.pageCount) return;
        hidePageFromPlayer(player, this.currentPage);
        this.currentPage = page;
        this.hologramPages.get(page).getHologramLines().forEach(hologramLine -> hologramLine.showToPlayer(player));
    }

    @Override
    public void showToPlayer(Player player) {
        showPageToPlayer(player, 1);
    }

    private void hidePage(int page) {
        this.hologramPages.get(page).getHologramLines().forEach(WixiHologramLine::remove);
    }

    private void hidePageFromPlayer(Player player, int page) {
        this.hologramPages.get(page).getHologramLines().forEach(hologramLine -> hologramLine.hideFromPlayer(player));
    }

    @Override
    public void hideFromPlayer(Player player) {
        hidePageFromPlayer(player, 1);
    }

    @Override
    public boolean isSpawned() {
        return this.spawned;
    }

    @Override
    public HologramPage getPageByNumber(int number) {

        int pageNumber = 1;
        for (WixiHologramPage page : getPages()) {
            if (pageNumber == number) return (HologramPage) page;
            pageNumber++;
        }

        return null;
    }

    @Override
    public int getPagesCount() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public List<WixiHologramPage> getPages() {
        return new ArrayList<>(this.hologramPages.values());
    }

    @Override
    public WixiHologramPage getCurrentPage() {
        return getPageByNumber(this.currentPage);
    }

}