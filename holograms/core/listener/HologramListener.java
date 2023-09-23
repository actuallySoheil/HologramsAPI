package me.wixi.holograms.core.listener;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.val;
import me.wixi.holograms.api.hologram.WixiHologram;
import me.wixi.holograms.core.hologram.Hologram;
import me.wixi.holograms.core.managers.HologramManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import me.wixi.holograms.core.handler.PlayerDuplexHandler;

public class HologramListener implements Listener {

    private final HologramManager hologramManager;
    private final ListMultimap<ChunkCoordinate, Hologram> hologramRespawnMap;

    public HologramListener(HologramManager hologramManager) {
        this.hologramManager = hologramManager;
        this.hologramRespawnMap = ArrayListMultimap.create();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPluginDisable(PluginDisableEvent event) {
        this.hologramManager.removeHolograms();
    }

    @EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();
        val channelPipeline = ((CraftPlayer) player).getHandle()
                .playerConnection.networkManager.channel.pipeline();
        channelPipeline.addBefore(
                "packet_handler",
                player.getName() + "_packet_injector",
                new PlayerDuplexHandler(this.hologramManager, player)
        );
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onPlayerQuit(PlayerQuitEvent event) {
        val player = event.getPlayer();
        val channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            if (!channel.pipeline().names().contains(player.getName() + "_packet_injector")) return;
            channel.pipeline().remove(player.getName() + "_packet_injector");
        });
    }

    @EventHandler(ignoreCancelled = true)
    private void onWorldLoad(WorldLoadEvent event) {
        val world = event.getWorld();

        this.hologramRespawnMap.keys()
                .stream()
                .filter(chunkCoordinate -> chunkCoordinate.worldName.equals(world.getName()))
                .filter(chunkCoordinate -> world.isChunkLoaded(chunkCoordinate.x, chunkCoordinate.z))
                .forEach(chunkCoordinate -> respawnHologramsFromCoordinate(world, chunkCoordinate));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onWorldUnload(WorldUnloadEvent event) {
        val world = event.getWorld();

        this.hologramManager.getHolograms()
                .stream()
                .filter(WixiHologram::isSpawned)
                .filter(hologram -> hologram.getLocation().getWorld().equals(world))
                .forEach(hologram -> {
                            world.getPlayers().forEach(hologram::hideFromPlayer);
                            this.hologramRespawnMap.put(toChunkCoordinate(hologram.getLocation()), (Hologram) hologram);
                        }
                );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        respawnHologramsFromCoordinate(event.getWorld(), toChunkCoordinate(event.getChunk()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onChunkUnload(ChunkUnloadEvent event) {
        val chunkCoordinate = toChunkCoordinate(event.getChunk());
        val world = event.getWorld();

        for (val hologram : this.hologramManager.getHolograms()) {
            if (!hologram.isSpawned()) continue;
            if (!toChunkCoordinate(hologram.getLocation()).equals(chunkCoordinate)) continue;

            world.getPlayers().forEach(hologram::showToPlayer);

            if (hologram.isSpawned()) {
                event.setCancelled(true);
                respawnHologramsFromCoordinate(world, chunkCoordinate);
                return;
            }

            this.hologramRespawnMap.put(chunkCoordinate, (Hologram) hologram);
        }

    }

    private void respawnHologramsFromCoordinate(World world, ChunkCoordinate coordinate) {
        this.hologramRespawnMap.asMap().keySet()
                .stream()
                .filter(chunkCoordinate -> chunkCoordinate.equals(coordinate))
                .forEach(chunkCoordinate -> {
                            this.hologramRespawnMap.get(coordinate)
                                    .forEach(hologram -> world.getPlayers().forEach(hologram::showToPlayer));
                            this.hologramRespawnMap.asMap().remove(coordinate);
                        }
                );
    }

    private ChunkCoordinate toChunkCoordinate(Chunk chunk) {
        return new ChunkCoordinate(chunk);
    }

    private ChunkCoordinate toChunkCoordinate(Location location) {
        return new ChunkCoordinate(
                location.getWorld().getName(),
                location.getBlockX() >> 4,
                location.getBlockZ() >> 4
        );
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private static class ChunkCoordinate {

        private final String worldName;
        private final int x, z;

        private ChunkCoordinate(Chunk chunk) {
            this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
        }

    }

}