package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.LootTable;
import com.dumptruckman.chestrestock.api.LootTable.LootSection;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class DefaultLootTable implements LootTable, LootSection {

    private LootSection topSection;

    DefaultLootTable(ConfigurationSection section) {
        topSection = new DefaultLootSection(section);
    }

    public void addToInventory(Inventory inv) {
        Logging.finest("Adding loot table to inventory " + topSection.getRolls() + " times");
        for (int i = 0; i < topSection.getRolls(); i++) {
            addSectionToInventory(inv, topSection);
        }
    }

    public void addSectionToInventory(Inventory inv, LootSection section) {
        ItemStack item = section.getItem();
        if (item != null) {
            inv.addItem(item);
        }
        Random randGen = new Random(System.nanoTime());
        Logging.finest("Total weight of section: " + getTotalWeight());
        float splitPicker = randGen.nextFloat() * getTotalWeight();
        float currentWeight = 0F;
        boolean splitPicked = false;
        for (Map.Entry<Float, Set<LootSection>> entry : section.getChildSections().entrySet()) {
            for (LootSection childSection : entry.getValue()) {
                if (isSplit()) {
                    currentWeight += entry.getKey();
                    Logging.finest("splitPicker: " + splitPicker + " <= " + currentWeight);
                    if (splitPicker <= currentWeight) {
                        Logging.finest("Adding " + childSection + " to inventory " + childSection.getRolls() + " times");
                        for (int i = 0; i < childSection.getRolls(); i++) {
                            addSectionToInventory(inv, childSection);
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
                                addSectionToInventory(inv, childSection);
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

    static class DefaultLootSection implements LootSection {

        private Map<Float, Set<LootSection>> sectionMap = new LinkedHashMap<Float, Set<LootSection>>();

        private int rolls = 1;
        private float chance = 1F;
        private int id = 0;
        private short data = 0;
        private int amount = 1;
        private boolean split = false;
        float totalWeight = 0F;

        DefaultLootSection(ConfigurationSection section) {
            Set<String> keys = section.getKeys(false);
            if (keys.isEmpty()) {
                Logging.warning("There is an empty loot section!");
            }
            for (String key : section.getKeys(false)) {
                if (key.equalsIgnoreCase("rolls")) {
                    rolls = section.getInt("rolls", 1);
                } else if (key.equalsIgnoreCase("id")) {
                    id = section.getInt("id", 0);
                } else if (key.equalsIgnoreCase("data")) {
                    data = (short) section.getInt("data", 0);
                } else if (key.equalsIgnoreCase("amount")) {
                    amount = (short) section.getInt("amount", 1);
                } else if (key.equalsIgnoreCase("chance")) {
                    chance = (float) section.getDouble("chance", 1);
                } else if (key.equalsIgnoreCase("split")) {
                    split = section.getBoolean("split", false);
                } else {
                    try {
                        ConfigurationSection newSection = section.getConfigurationSection(key);
                        if (newSection != null) {
                            LootSection lootSection = new DefaultLootSection(newSection);
                            Set<LootSection> sectionSet = sectionMap.get(lootSection.getChance());
                            if (sectionSet == null) {
                                sectionSet = new LinkedHashSet<LootSection>();
                                sectionMap.put(lootSection.getChance(), sectionSet);
                            }
                            totalWeight += lootSection.getChance();
                            Logging.finer("Adding LootSection to table with chance: " + lootSection.getChance() + " increasing total weight of section to " + totalWeight);
                            sectionSet.add(new DefaultLootSection(newSection));
                        } else {
                            Logging.warning("Could not parse section: " + key);
                        }
                    } catch (NumberFormatException ignore) {
                        Logging.warning("Loot table contains invalid data!");
                    }
                }
            }
        }

        public int getRolls() {
            return rolls;
        }

        public ItemStack getItem() {
            if (id > 0 && amount > 0 && data >= 0) {
                return new ItemStack(id, amount, data);
            }
            return null;
        }

        public boolean isSplit() {
            return split;
        }

        public Map<Float, Set<LootSection>> getChildSections() {
            return sectionMap;
        }

        public float getTotalWeight() {
            return totalWeight;
        }

        public float getChance() {
            return chance;
        }
    }
}
