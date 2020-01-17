package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.LootTable;
import com.dumptruckman.chestrestock.api.LootTable.ItemSection;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class DefaultLootTable implements LootTable, ItemSection {

    private ItemSection topSection;

    DefaultLootTable(String name, ConfigurationSection section) {
        topSection = new DefaultItemSection(name, section);
    }

    public void addToInventory(Inventory inv) {
        Logging.finest("Adding loot table to inventory " + topSection.getRolls() + " times");
        for (int i = 0; i < topSection.getRolls(); i++) {
            addSectionToInventory(inv, topSection, null);
        }
    }

    private void addSectionToInventory(Inventory inv, LootSection section, EnchantSection enchantToUse) {
        ItemStack item = null;
        EnchantSection enchantSection = null;
        if (section instanceof ItemSection) {
            ItemSection itemSection = (ItemSection) section;
            item = itemSection.getItem();
            enchantSection = itemSection.getEnchantSection();
        }
        if (enchantSection == null) {
            enchantSection = enchantToUse;
        }
        if (item != null) {
            if (enchantSection != null) {
                addEnchantToItem(item, enchantSection);
            }
            inv.addItem(item);
            enchantSection = null;
        }
        Random randGen = new Random(System.nanoTime());
        Logging.finest("Total weight of '" + section + "': " + section.getTotalWeight());
        float splitPicker = randGen.nextFloat() * section.getTotalWeight();
        float currentWeight = 0F;
        boolean splitPicked = false;
        for (Map.Entry<Float, Set<LootSection>> entry : section.getChildSections().entrySet()) {
            for (LootSection childSection : entry.getValue()) {
                if (childSection instanceof EnchantSection) {
                    continue;
                }
                if (section.isSplit()) {
                    currentWeight += entry.getKey();
                    Logging.finest("splitPicker: " + splitPicker + " <= " + currentWeight);
                    if (splitPicker <= currentWeight) {
                        Logging.finest("Picked split: Adding " + childSection + " to inventory "
                                + childSection.getRolls() + " times");
                        for (int i = 0; i < childSection.getRolls(); i++) {
                            addSectionToInventory(inv, childSection, enchantSection);
                        }
                        splitPicked = true;
                        break;
                    }
                } else {
                    for (int i = 0; i < childSection.getRolls(); i++) {
                        float chance = entry.getKey();
                        int successfulRolls = (int) chance;
                        chance -= successfulRolls;
                        float randFloat = randGen.nextFloat();
                        if (randFloat <= chance) {
                            successfulRolls++;
                        }
                        if (successfulRolls > 0) {
                            Logging.finest("Adding " + childSection + " to inventory " + successfulRolls + " times");
                            for (int j = 0; j < successfulRolls; j++) {
                                addSectionToInventory(inv, childSection, enchantSection);
                            }
                        }
                    }
                }
            }
            if (splitPicked) {
                break;
            }
        }
    }

    private void addEnchantToItem(ItemStack item, EnchantSection enchantSection) {
        Enchantment enchantment = enchantSection.getEnchantment();
        int enchantLevel = enchantSection.getLevel();
        Random randGen = new Random(System.nanoTime());
        if (enchantment != null && enchantLevel != 0) {
            if (enchantLevel < 0) {
                enchantLevel = -enchantLevel;
                if (enchantSection.isSafe() && enchantLevel > enchantment.getMaxLevel()) {
                    enchantLevel = enchantment.getMaxLevel();
                }
                enchantLevel = randGen.nextInt(enchantLevel) + 1;
                Logging.finest("Using random enchant level for " + enchantment + ": " + enchantLevel);
            }
            if (enchantSection.isSafe()) {
                item.addEnchantment(enchantment, enchantLevel);
            } else {
                item.addUnsafeEnchantment(enchantment, enchantLevel);
            }
        }
        Logging.finest("Total weight of '" + enchantSection + "': " + enchantSection.getTotalWeight());
        float splitPicker = randGen.nextFloat() * enchantSection.getTotalWeight();
        float currentWeight = 0F;
        boolean splitPicked = false;
        for (Map.Entry<Float, Set<LootSection>> entry : enchantSection.getChildSections().entrySet()) {
            for (LootSection childSection : entry.getValue()) {
                if (!(childSection instanceof EnchantSection)) {
                    continue;
                }
                EnchantSection childEnchantSection = (EnchantSection) childSection;
                if (enchantSection.isSplit()) {
                    currentWeight += entry.getKey();
                    Logging.finest("splitPicker: " + splitPicker + " <= " + currentWeight);
                    if (splitPicker <= currentWeight) {
                        Logging.finest("Picked split: Adding " + childEnchantSection + " to item "
                                + childEnchantSection.getRolls() + " times");
                        for (int i = 0; i < childEnchantSection.getRolls(); i++) {
                            addEnchantToItem(item, childEnchantSection);
                        }
                        splitPicked = true;
                        break;
                    }
                } else {
                    for (int i = 0; i < childEnchantSection.getRolls(); i++) {
                        float chance = entry.getKey();
                        int successfulRolls = (int) chance;
                        chance -= successfulRolls;
                        float randFloat = randGen.nextFloat();
                        if (randFloat <= chance) {
                            successfulRolls++;
                        }
                        if (successfulRolls > 0) {
                            Logging.finest("Adding " + childEnchantSection + " to item " + successfulRolls + " times");
                            for (int j = 0; j < successfulRolls; j++) {
                                addEnchantToItem(item, childEnchantSection);
                            }
                        }
                    }
                }
            }
            if (splitPicked) {
                break;
            }
        }
    }

    @Override
    public int getRolls() {
        return topSection.getRolls();
    }

    @Override
    public ItemStack getItem() {
        return topSection.getItem();
    }

    @Override
    public Map<Float, Set<LootSection>> getChildSections() {
        return topSection.getChildSections();
    }

    @Override
    public boolean isSplit() {
        return topSection.isSplit();
    }

    @Override
    public float getTotalWeight() {
        return topSection.getTotalWeight();
    }

    @Override
    public float getChance() {
        return topSection.getChance();
    }

    @Override
    public EnchantSection getEnchantSection() {
        return topSection.getEnchantSection();
    }

    static class DefaultLootSection implements LootSection {

        private Map<Float, Set<LootSection>> sectionMap = new LinkedHashMap<Float, Set<LootSection>>();

        protected EnchantSection enchantSection = null;

        private int rolls = 1;
        private float chance = 1F;
        private boolean split = false;
        private float totalWeight = 0F;
        protected String name;

        // Related to items
        protected Material itemType = Material.AIR;
        protected short itemData = 0;
        protected int itemAmount = 1;

        // Related to enchants
        protected String enchantName = "";
        protected int enchantLevel = 1;
        protected boolean enchantSafe = true;

        DefaultLootSection(String name, ConfigurationSection section) {
            this.name = name;
            Set<String> keys = section.getKeys(false);
            if (keys.isEmpty()) {
                Logging.warning("There is an empty loot section!");
            }
            for (String key : section.getKeys(false)) {
                if (key.equalsIgnoreCase("rolls")) {
                    rolls = section.getInt("rolls", 1);
                } else if (key.equalsIgnoreCase("type")) {
                    String type = section.getString("type");
                    itemType = parseMaterial(type);
                } else if (key.equalsIgnoreCase("data")) {
                    itemData = (short) section.getInt("data", 0);
                } else if (key.equalsIgnoreCase("amount")) {
                    itemAmount = (short) section.getInt("amount", 1);
                } else if (key.equalsIgnoreCase("chance")) {
                    chance = (float) section.getDouble("chance", 1);
                } else if (key.equalsIgnoreCase("split")) {
                    split = section.getBoolean("split", false);
                } else if (key.equalsIgnoreCase("name")) {
                    enchantName = section.getString("name", "");
                } else if (key.equalsIgnoreCase("level")) {
                    enchantLevel = section.getInt("level", 1);
                } else if (key.equalsIgnoreCase("safe")) {
                    enchantSafe = section.getBoolean("safe", true);
                } else {
                    try {
                        ConfigurationSection newSection = section.getConfigurationSection(key);
                        if (newSection != null) {
                            LootSection lootSection;
                            if (key.equalsIgnoreCase("enchant")) {
                                enchantSection = new DefaultEnchantSection(key, newSection);
                                lootSection = enchantSection;
                            } else if (this instanceof EnchantSection) {
                                lootSection = new DefaultEnchantSection(key, newSection);
                            } else {
                                lootSection = new DefaultItemSection(key, newSection);
                            }
                            Set<LootSection> sectionSet = sectionMap.get(lootSection.getChance());
                            if (sectionSet == null) {
                                sectionSet = new LinkedHashSet<LootSection>();
                                sectionMap.put(lootSection.getChance(), sectionSet);
                            }
                            totalWeight += lootSection.getChance();
                            Logging.finer("Adding section '" + lootSection + "' to section '" + this + "' with chance '"
                                    + lootSection.getChance() + "' increasing total weight of '" + this + "' to "
                                    + totalWeight);
                            sectionSet.add(lootSection);
                        } else {
                            Logging.warning("Could not parse section: " + key);
                        }
                    } catch (NumberFormatException ignore) {
                        Logging.warning("Loot table contains invalid data!");
                    }
                }
            }
        }

        private Material parseMaterial(String typeString) {
            Material itemType = Material.getMaterial(typeString);
            if (itemType == null) {
                itemType = Material.getMaterial(typeString, true);
            }
            return itemType;
        }

        @Override
        public int getRolls() {
            return rolls;
        }

        @Override
        public boolean isSplit() {
            return split;
        }

        @Override
        public Map<Float, Set<LootSection>> getChildSections() {
            return sectionMap;
        }

        @Override
        public float getTotalWeight() {
            return totalWeight;
        }

        @Override
        public float getChance() {
            return chance;
        }
    }

    static class DefaultItemSection extends DefaultLootSection implements ItemSection {

        DefaultItemSection(String name, ConfigurationSection section) {
            super(name, section);
        }

        @Override
        public ItemStack getItem() {
            if (itemType != Material.AIR && itemAmount > 0 && itemData >= 0) {
                return new ItemStack(itemType, itemAmount, itemData);
            }
            return null;
        }

        @Override
        public String toString() {
            return "[ItemSection] " + name;
        }

        @Override
        public EnchantSection getEnchantSection() {
            return enchantSection;
        }
    }

    static class DefaultEnchantSection extends DefaultLootSection implements EnchantSection {

        DefaultEnchantSection(String name, ConfigurationSection section) {
            super(name, section);
        }

        @Override
        public Enchantment getEnchantment() {
            return Enchantment.getByName(enchantName.toUpperCase());
        }

        @Override
        public int getLevel() {
            return enchantLevel;
        }

        @Override
        public boolean isSafe() {
            return enchantSafe;
        }

        @Override
        public String toString() {
            return "[EnchantSection] " + name;
        }
    }
}
