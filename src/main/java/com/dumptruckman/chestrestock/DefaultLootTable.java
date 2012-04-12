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
        for (Map.Entry<Float, Set<LootSection>> entry : section.getChildSections().entrySet()) {
            for (LootSection childSection : entry.getValue()) {
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

    static class DefaultLootSection implements LootSection {

        private Map<Float, Set<LootSection>> sectionMap = new LinkedHashMap<Float, Set<LootSection>>();

        private int rolls = 1;
        private int id = 0;
        private short data = 0;
        private int amount = 1;

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
                }  else if (key.equalsIgnoreCase("amount")) {
                    amount = (short) section.getInt("amount", 1);
                } else {
                    try {
                        float chance = Float.valueOf(key);
                        chance /= 100;
                        ConfigurationSection newSection = section.getConfigurationSection(key);
                        if (newSection != null) {
                            Set<LootSection> sectionSet = sectionMap.get(chance);
                            if (sectionSet == null) {
                                sectionSet = new LinkedHashSet<LootSection>();
                                sectionMap.put(chance, sectionSet);
                            }
                            Logging.finer("Adding LootSection to table with chance: " + chance);
                            sectionSet.add(new DefaultLootSection(newSection));
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

        public Map<Float, Set<LootSection>> getChildSections() {
            return sectionMap;
        }
    }
}
