package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.minecraft.pluginbase.config.AdvancedConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.Config;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.SimpleConfigEntry;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface CRChest extends Config {
    
    final int MAX_SIZE = 54;

    ConfigEntry<Boolean> PRESERVE_SLOTS = new AdvancedConfigEntry<Boolean>(Boolean.class,
            "preserve_slots", true) {
        @Override
        public Object serialize(Boolean b) {
            return b.toString();
        }

        @Override
        public Boolean deserialize(Object o) {
            return Boolean.valueOf(o.toString());
        }
    };

    ConfigEntry<Boolean> INDESTRUCTIBLE = new AdvancedConfigEntry<Boolean>(Boolean.class,
            "indestructible", true) {
        @Override
        public Object serialize(Boolean b) {
            return b.toString();
        }

        @Override
        public Boolean deserialize(Object o) {
            return Boolean.valueOf(o.toString());
        }
    };

    ConfigEntry<Integer> PLAYER_LIMIT = new AdvancedConfigEntry<Integer>(Integer.class,
            "player_limit", -1) {
        @Override
        public Object serialize(Integer i) {
            return i.toString();
        }

        @Override
        public Integer deserialize(Object o) {
            return Integer.valueOf(o.toString());
        }
    };

    ConfigEntry<Boolean> UNIQUE = new AdvancedConfigEntry<Boolean>(Boolean.class,
            "unique", true) {
        @Override
        public Object serialize(Boolean b) {
            return b.toString();
        }

        @Override
        public Boolean deserialize(Object o) {
            return Boolean.valueOf(o.toString());
        }
    };

    ConfigEntry<Integer> PERIOD = new AdvancedConfigEntry<Integer>(Integer.class,
            "period", 900) {
        @Override
        public Object serialize(Integer i) {
            return i.toString();
        }

        @Override
        public Integer deserialize(Object o) {
            return Integer.valueOf(o.toString());
        }
    };

    ConfigEntry<String> NAME = new SimpleConfigEntry<String>(String.class, "name", "");

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
            "items", new ItemStack[MAX_SIZE]) {
        @Override
        public Object serialize(ItemStack[] itemStacks) {
            return DataStrings.valueOf(itemStacks);
        }

        @Override
        public ItemStack[] deserialize(Object o) {
            return DataStrings.parseInventory(o.toString(), MAX_SIZE);
        }
    };

    BlockLocation getLocation();

    InventoryHolder getInventoryHolder();

    void update();
}
