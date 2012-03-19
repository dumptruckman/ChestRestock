package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.InventoryTools;
import com.dumptruckman.chestrestock.util.ItemWrapper;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.inventory.ItemStack;

/**
 * This class handles the formatting of strings for data i/o.
 */
public class DataStrings {

    /**
     * General delimiter to separate data items.
     */
    public static final String GENERAL_DELIMITER = ";";
    /**
     * Secondary delimiter to separate data items where general delimiter is used in a broader purpose.
     */
    public static final String SECONDARY_DELIMITER = ",";
    /**
     * Special delimiter to separate items since they use both the general and secondary delimiters already.
     */
    public static final String ITEM_DELIMITER = "/";
    /**
     * Delimiter to separate a key and it's value.
     */
    public static final String VALUE_DELIMITER = ":";
    /**
     * Item type identifier.
     */
    public static final String ITEM_TYPE_ID = "t";
    /**
     * Item durability identifier.
     */
    public static final String ITEM_DURABILITY = "d";
    /**
     * Item amount identifier.
     */
    public static final String ITEM_AMOUNT = "#";
    /**
     * Item enchantments identifier.
     */
    public static final String ITEM_ENCHANTS = "e";
    /**
     * Location x identifier.
     */
    public static final String LOCATION_X = "x";
    /**
     * Location y identifier.
     */
    public static final String LOCATION_Y = "y";
    /**
     * Location z identifier.
     */
    public static final String LOCATION_Z = "z";
    /**
     * Location world identifier.
     */
    public static final String LOCATION_WORLD = "wo";
    /**
     * Location pitch identifier.
     */
    public static final String LOCATION_PITCH = "pi";
    /**
     * Location yaw identifier.
     */
    public static final String LOCATION_YAW = "ya";

    private DataStrings() {
        throw new AssertionError();
    }

    /**
     * Splits a key:value string into a String[2] where string[0] == key and string[1] == value.
     *
     * @param valueString A key:value string.
     * @return A string array split on the {@link #VALUE_DELIMITER}.
     */
    public static String[] splitEntry(String valueString) {
        return valueString.split(VALUE_DELIMITER, 2);
    }

    /**
     * Creates a key:value string from the string form of the key object and value object.
     *
     * @param key   Object that is to be the key.
     * @param value Object that is to be the value.
     * @return String of key and value joined with the {@link #VALUE_DELIMITER}.
     */
    public static String createEntry(Object key, Object value) {
        return key + VALUE_DELIMITER + value;
    }

    /**
     * @param inventoryString An inventory in string for to be parsed into an ItemStack array.
     * @param inventorySize The number of item slots in the inventory.
     * @return an ItemStack array containing the inventory contents parsed from inventoryString.
     */
    public static ItemStack[] parseInventory(String inventoryString, int inventorySize) {
        String[] inventoryArray = inventoryString.split(DataStrings.ITEM_DELIMITER);
        ItemStack[] invContents = InventoryTools.fillWithAir(new ItemStack[inventorySize]);
        for (String itemString : inventoryArray) {
            String[] itemValues = DataStrings.splitEntry(itemString);
            try {
                ItemWrapper itemWrapper = ItemWrapper.wrap(itemValues[1]);
                invContents[Integer.valueOf(itemValues[0])] = itemWrapper.getItem();
                //Logging.debug("ItemString '" + itemString + "' unwrapped as: " + itemWrapper.getItem().toString());
            } catch (Exception e) {
                if (!itemString.isEmpty()) {
                    Logging.fine("Could not parse item string: " + itemString);
                    Logging.fine(e.getMessage());
                }
            }
        }
        return invContents;
    }

    /**
     * Converts an ItemStack array into a String for easy persistence.
     *
     * @param items The items you wish to "string-i-tize".
     * @return A string representation of an inventory.
     */
    public static String valueOf(ItemStack[] items) {
        StringBuilder builder = new StringBuilder();
        for (Integer i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getTypeId() != 0) {
                if (!builder.toString().isEmpty()) {
                    builder.append(DataStrings.ITEM_DELIMITER);
                }
                builder.append(DataStrings.createEntry(i, ItemWrapper.wrap(items[i]).toString()));
            }
        }
        return builder.toString();
    }
}

