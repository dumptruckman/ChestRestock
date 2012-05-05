package com.dumptruckman.chestrestock.api;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

/**
 * Interface that describes a random loot table.
 */
public interface LootTable {

    /**
     * Adds the loot table to an inventory.
     *
     * @param inv The inventory to add the loot table to.
     */
    void addToInventory(Inventory inv);

    /**
     * Interface to describe a LootSection, which is a single section of the yaml file.
     */
    interface LootSection {

        /**
         * @return The number of rolls for the section.
         */
        int getRolls();

        /**
         * @return A map of the children section with keys representing the chance of that section.  The value
         * is a Set since multiple sections may have the same chance.
         */
        Map<Float, Set<LootSection>> getChildSections();

        /**
         * @return True if only one child should be picked for this LootSection.
         */
        boolean isSplit();

        /**
         * @return The total of all the chances for all the children of this LootSection.
         */
        float getTotalWeight();

        /**
         * @return The chance for this LootSection to be chosen.
         */
        float getChance();
    }

    /**
     * Interface to describe a LootSection that represents an Item (the default kind of LootSection).
     */
    interface ItemSection extends LootSection {

        /**
         * @return The item this LootSection represents.
         */
        ItemStack getItem();

        /**
         * @return The enchant section for this LootSection or null if none exists.
         */
        EnchantSection getEnchantSection();
    }

    /**
     * Interface to describe a LootSection that represents an item enchantment.
     */
    static interface EnchantSection extends LootSection {

        /**
         * @return The enchantment represented by this LootSection.
         */
        Enchantment getEnchantment();

        /**
         * @return The level of the enchantment.
         */
        int getLevel();

        /**
         * @return true if the enchantment should be applied safely.
         */
        boolean isSafe();
    }
}
