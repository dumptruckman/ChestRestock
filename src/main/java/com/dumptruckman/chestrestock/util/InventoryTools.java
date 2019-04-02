package com.dumptruckman.chestrestock.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryTools {

    /**
     * Fills an ItemStack array with air.
     *
     * @param items The ItemStack array to fill.
     * @return The air filled ItemStack array.
     */
    public static ItemStack[] fillWithAir(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            items[i] = new ItemStack(Material.AIR);
        }
        return items;
    }

    public static boolean isEmpty(ItemStack[] items) {
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }
}
