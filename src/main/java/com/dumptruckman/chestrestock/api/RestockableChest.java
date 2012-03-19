package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.minecraft.pluginbase.config.AdvancedConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.Config;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.SimpleConfigEntry;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public interface RestockableChest extends Config {
    
    final int CHEST_SIZE = 54;

    ConfigEntry<Boolean> PRESERVE_SLOTS = new SimpleConfigEntry<Boolean>(Boolean.class,
            "preserve_slots", true);

    ConfigEntry<Boolean> INDESTRUCTIBLE = new SimpleConfigEntry<Boolean>(Boolean.class,
            "indestructible", true);

    ConfigEntry<Integer> PLAYER_LIMIT = new SimpleConfigEntry<Integer>(Integer.class,
            "player_loot_limit", -1);

    ConfigEntry<Boolean> UNIQUE = new SimpleConfigEntry<Boolean>(Boolean.class,
            "unique", true);

    ConfigEntry<Integer> PERIOD = new SimpleConfigEntry<Integer>(Integer.class,
            "period", 900);

    ConfigEntry<String> PERIOD_MODE = new SimpleConfigEntry<String>(String.class,
            "period_mode", "player") {
        @Override
        public boolean isValid(Object obj) {
            String value = obj.toString();
            return value.equalsIgnoreCase("player") || value.equalsIgnoreCase("fixed");
        }
    };

    ConfigEntry<String> RESTOCK_MODE = new SimpleConfigEntry<String>(String.class,
            "restock_mode", "replace") {
        @Override
        public boolean isValid(Object obj) {
            String value = obj.toString();
            return value.equalsIgnoreCase("add") || value.equalsIgnoreCase("replace");
        }
    };
    
    ConfigEntry<ItemStack[]> ITEMS = new AdvancedConfigEntry<ItemStack[]>(ItemStack[].class,
            "items", new ItemStack[CHEST_SIZE]) {
        @Override
        public Object serialize(ItemStack[] itemStacks) {
            return DataStrings.valueOf(itemStacks);
        }

        @Override
        public ItemStack[] deserialize(Object o) {
            return DataStrings.parseInventory(o.toString(), CHEST_SIZE);
        }
    };

    BlockLocation getLocation();

    boolean isDouble();

    Chest getChest();

    void update();
}
