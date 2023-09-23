package me.wixi.holograms.core.managers;

import com.google.common.collect.ImmutableList;
import lombok.val;
import me.wixi.holograms.api.hologram.WixiHologram;
import me.wixi.holograms.core.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import me.wixi.holograms.core.listener.HologramListener;

import java.util.ArrayList;
import java.util.Collection;

public class HologramManager {

    private final ArrayList<WixiHologram> holograms;

    public HologramManager(JavaPlugin plugin) {
        this.holograms = new ArrayList<>();

        val server = plugin.getServer();
        val version = server.getClass().getPackage().getName().substring(23);
        val pluginManager = server.getPluginManager();
        if (!version.equals("v1_8_R3")) {
            plugin.getLogger().severe("[ERROR] WixiHologramsAPI only supports v1_8_R3, disabling plugin...");
            pluginManager.disablePlugin(plugin);
            return;
        }

        pluginManager.registerEvents(new HologramListener(this), plugin);
    }

    public WixiHologram newHologram(String hologramName, Location location, String... lines) {
        val hologram = new Hologram(hologramName);
        hologram.spawn(location, lines);
        this.holograms.add(hologram);
        return hologram;
    }

    public void removeHologram(WixiHologram hologram) {
        hologram.remove();
        this.holograms.remove(hologram);
    }

    public void removeHolograms() {
        this.holograms.forEach(WixiHologram::remove);
        this.holograms.clear();
    }

    public Collection<WixiHologram> getHolograms() {
        return ImmutableList.copyOf(this.holograms);
    }

}