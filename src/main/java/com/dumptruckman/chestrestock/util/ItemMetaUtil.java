package com.dumptruckman.chestrestock.util;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ItemMetaUtil {

    public static Map<String, Object> getSerializedItemMeta(final ItemStack item) {
        return item.getItemMeta().serialize();
    }

    public static void addSerializedItemMetaToItem(final ItemStack item, Map<String, Object> metaMap) {
        metaMap.put("==", "ItemMeta");
        ItemMeta meta = (ItemMeta) ConfigurationSerialization.deserializeObject(metaMap);
        item.setItemMeta(meta);
    }
}
