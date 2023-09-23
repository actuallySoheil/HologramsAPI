package me.wixi.holograms.core.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.val;
import me.wixi.holograms.api.event.hologram.HologramClickEvent;
import me.wixi.holograms.core.hologram.HologramLine;
import me.wixi.holograms.core.managers.HologramManager;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDuplexHandler extends ChannelDuplexHandler {

    private final HologramManager hologramManager;
    private final Player player;

    private final HashMap<UUID, Instant> playerHologramInteractCooldownMap;

    public PlayerDuplexHandler(HologramManager hologramManager, Player player) {
        this.hologramManager = hologramManager;
        this.player = player;

        this.playerHologramInteractCooldownMap = new HashMap<>();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {

        if (packet instanceof PacketPlayInUseEntity packetPlayInUseEntity) {

            val uniqueId = this.player.getUniqueId();
            if (this.playerHologramInteractCooldownMap.containsKey(uniqueId) &&
                    Instant.now().isBefore(this.playerHologramInteractCooldownMap.get(uniqueId))) {
                super.channelRead(channelHandlerContext, packet);
                return;
            }

            this.playerHologramInteractCooldownMap.put(uniqueId, Instant.now().plus(Duration.ofMillis(250L)));
            val field = packet.getClass().getDeclaredField("a"); // a = entityId
            field.setAccessible(true);

            val entityId = field.getInt(packetPlayInUseEntity);
            for (val hologram : this.hologramManager.getHolograms()) {
                val currentPage = hologram.getCurrentPage();

                currentPage.getHologramLines().forEach(hologramLine -> {
                    if (entityId != ((HologramLine) hologramLine).getArmorStand().getId()) return;
                    val hologramInteractEvent = new HologramClickEvent(this.player, hologram, currentPage, hologramLine);
                    Bukkit.getServer().getPluginManager().callEvent(hologramInteractEvent);
                });

            }

            field.setAccessible(false);
        }

        super.channelRead(channelHandlerContext, packet);
    }

}