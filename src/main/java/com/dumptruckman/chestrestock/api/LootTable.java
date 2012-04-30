package com.dumptruckman.chestrestock.api;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public interface LootTable {

    void addToInventory(Inventory inv);

    interface LootSection {

        int getRolls();

        Map<Float, Set<LootSection>> getChildSections();

        boolean isSplit();

        float getTotalWeight();

        float getChance();
    }

    interface ItemSection extends LootSection {

        ItemStack getItem();

        EnchantSection getEnchantSection();
    }

    static interface EnchantSection extends LootSection {

        Enchantment getEnchantment();

        int getLevel();

        boolean isSafe();
    }
}
