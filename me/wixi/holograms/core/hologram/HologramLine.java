package me.wixi.holograms.core.hologram;

import lombok.Getter;
import lombok.val;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import me.wixi.holograms.api.hologram.WixiHologramLine;
import me.wixi.holograms.core.util.StringUtil;

public class HologramLine implements WixiHologramLine {

    private final Location location;
    private final String line;

    @Getter
    private EntityArmorStand armorStand;

    protected HologramLine(Location location, String line) {
        this.location = location;
        this.line = line;
    }

    @Override
    public void create(Location location, String line) {
        val worldServer = ((CraftWorld) this.location.getWorld()).getHandle();
        val x = this.location.getX();
        val y = this.location.getY();
        val z = this.location.getZ();

        this.armorStand = new EntityArmorStand(worldServer, x, y, z);
        this.armorStand.setGravity(false);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setCustomName(StringUtil.color(line));
        this.armorStand.setInvisible(true);
    }

    @Override
    public void remove() {
        if (this.armorStand == null) return;
        Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, new PacketPlayOutEntityDestroy(this.armorStand.getId())));
    }

    @Override
    public void show() {
        if (this.armorStand == null) create(this.location, this.line);
        Bukkit.getOnlinePlayers().forEach(this::showToPlayer);
    }

    @Override
    public void showToPlayer(Player player) {
        if (this.armorStand == null) create(this.location, this.line);

        val craftArmorStand = ((CraftArmorStand) this.armorStand.getBukkitEntity()).getHandle();
        sendPacket(player, new PacketPlayOutSpawnEntityLiving(craftArmorStand));

        sendPacket(
                player,
                new PacketPlayOutEntityMetadata(
                        this.armorStand.getId(),
                        craftArmorStand.getDataWatcher(),
                        false
                )
        );
    }

    @Override
    public void hideFromPlayer(Player player) {
        if (this.armorStand == null) return;
        sendPacket(player, new PacketPlayOutEntityDestroy(this.armorStand.getId()));
    }

    @Override
    public void setLineForPlayer(Player player, String line) {
        val entityArmorStand = ((CraftArmorStand) this.armorStand.getBukkitEntity()).getHandle();
        val dataWatcher = entityArmorStand.getDataWatcher();
        dataWatcher.watch(2, StringUtil.color(line));

        sendPacket(
                player,
                new PacketPlayOutEntityMetadata(
                        this.armorStand.getId(), dataWatcher, false
                )
        );
    }

    @Override
    public void setLine(String line) {
        this.armorStand.setCustomName(StringUtil.color(line));
    }

    @Override
    public String getLine() {
        return this.line;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}