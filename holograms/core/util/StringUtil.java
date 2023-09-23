package me.wixi.holograms.core.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class StringUtil {

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}