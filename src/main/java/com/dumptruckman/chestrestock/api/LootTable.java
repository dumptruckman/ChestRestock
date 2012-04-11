package com.dumptruckman.chestrestock.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public interface LootTable {

    void addToInventory(Inventory inv);

    static interface LootSection {

        int getRolls();

        ItemStack getItem();

        Map<Float, Set<LootSection>> getChildSections();
    }
}
