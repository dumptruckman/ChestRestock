package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.InventoryTools;
import com.dumptruckman.chestrestock.util.ItemMetaUtil;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

/**
 * This class handles the formatting of strings for data i/o.
 */
public final class DataStrings {

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
     * ItemStack identifier.  For serializing directly from bukkit.
     */
    public static final String ITEM_ITEMSTACK = "is";
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
     * Parses an inventory in string form into an ItemStack array.
     *
     * @param inventoryString An inventory in string for to be parsed into an ItemStack array.
     * @param inventorySize The number of item slots in the inventory.
     * @return an ItemStack array containing the inventory contents parsed from inventoryString.
     */
    public static ItemStack[] parseInventory(String inventoryString, int inventorySize) {
        if (inventoryString.startsWith("{")) {
            return jsonParseInventory(inventoryString, inventorySize);
        } else {
            return legacyParseInventory(inventoryString, inventorySize);
        }
    }

    private static ItemStack[] legacyParseInventory(String inventoryString, int inventorySize) {
        String[] inventoryArray = inventoryString.split(DataStrings.ITEM_DELIMITER);
        ItemStack[] invContents = InventoryTools.fillWithAir(new ItemStack[inventorySize]);
        for (String itemString : inventoryArray) {
            String[] itemValues = DataStrings.splitEntry(itemString);
            try {
                com.dumptruckman.chestrestock.util.ItemWrapper itemWrapper = com.dumptruckman.chestrestock.util.ItemWrapper.wrap(itemValues[1]);
                invContents[Integer.valueOf(itemValues[0])] = itemWrapper.getItem();
            } catch (Exception e) {
                if (!itemString.isEmpty()) {
                    Logging.fine("Could not parse item string: " + itemString);
                    Logging.fine(e.getMessage());
                }
            }
        }
        return invContents;
    }

    private static ItemStack[] jsonParseInventory(String inventoryString, int inventorySize) {
        ItemStack[] invContents = InventoryTools.fillWithAir(new ItemStack[inventorySize]);
        if (inventoryString.isEmpty()) {
            return invContents;
        }
        JSONObject jsonItems;
        try {
            jsonItems = (JSONObject) JSON_PARSER.parse(inventoryString);
        } catch (ParseException e) {
            Logging.warning("Could not parse inventory! " + e.getMessage());
            return invContents;
        } catch (ClassCastException e) {
            Logging.warning("Could not parse inventory! " + e.getMessage());
            return invContents;
        }
        for (Object key : jsonItems.keySet()) {
            int index = -1;
            try {
                index = Integer.valueOf(key.toString());
            } catch (NumberFormatException e) {
                Logging.warning("Invalid key: " + key + " while parsing inventory");
                continue;
            }
            if (index == -1) {
                Logging.warning("Invalid key: " + key + " while parsing inventory");
                continue;
            }
            if (index > inventorySize) {
                Logging.warning("Invalid key: " + key + " while parsing inventory");
                continue;
            }
            Object value = jsonItems.get(key);
            if (value instanceof JSONObject) {
                invContents[index] = ItemWrapper.wrap((JSONObject) value).getItem();
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
        JSONObject jsonItems = new JSONObject();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getTypeId() != 0) {
                jsonItems.put(Integer.valueOf(i).toString(), new JSONItemWrapper(items[i]).asJSONObject());
            }
        }
        return jsonItems.toJSONString();
    }

    /**
     * This is meant to wrap an ItemStack so that it can easily be serialized/deserialized in String format.
     */
    public static abstract class ItemWrapper {

        protected ItemStack item;

        /**
         * Wraps the given {@link ItemStack} in an ItemWrapper so that it can be easily turned into a string.
         *
         * @param item The {@link ItemStack} to wrap.
         * @return The wrapped {@link ItemStack}.
         */
        public static ItemWrapper wrap(final ItemStack item) {
            return new JSONItemWrapper(item);
        }

        /**
         * Parses the given String as an ItemWrapper so that it can be easily turned into an {@link ItemStack}.
         *
         * @param itemString the String to parse.
         * @return The wrapped {@link ItemStack}.
         */
        public static ItemWrapper wrap(final String itemString) {
            try {
                if (itemString.startsWith("{")) {
                    return new JSONItemWrapper(itemString);
                } else {
                    Logging.warning("Data is not formatted correctly: " + itemString);
                }
            } catch (Exception e) {
                Logging.warning("Encountered exception while converting item from string: " + e.getMessage());
            }
            return new JSONItemWrapper(new ItemStack(Material.AIR));
        }

        /**
         * Parses the given String as an ItemWrapper so that it can be easily turned into an {@link ItemStack}.
         *
         * @param jsonItems the items in JSONObject form to parse.
         * @return The wrapped {@link ItemStack}.
         */
        public static ItemWrapper wrap(final JSONObject jsonItems) {
            return new JSONItemWrapper(jsonItems);
        }

        /**
         * Retrieves the ItemStack that this class is wrapping.
         *
         * @return The ItemStack this class is wrapping.
         */
        public final ItemStack getItem() {
            return this.item;
        }

        @Override
        public String toString() {
            try {
                return asString();
            } catch (Exception e) {
                Logging.warning("Encountered exception while converting item to string: " + e.getMessage());
            }
            return "";
        }

        public abstract String asString();
    }

    private static final JSONParser JSON_PARSER = new JSONParser();

    /**
     * This is meant to wrap an ItemStack so that it can easily be serialized/deserialized in String format.
     */
    private static final class JSONItemWrapper extends ItemWrapper {

        private JSONItemWrapper(final ItemStack item) {
            this.item = item;
        }

        private JSONItemWrapper(final String itemString) {
            JSONObject itemData = null;
            try {
                Object obj = JSON_PARSER.parse(itemString);
                if (obj instanceof JSONObject) {
                    itemData = (JSONObject) obj;
                }
            } catch (ParseException e) {
                Logging.warning("Could not parse item: " + itemString + "!  Item may be lost!");
            }
            if (itemData == null) {
                Logging.warning("Could not parse item: " + itemString + "!  Item may be lost!");
            }
            createItem(itemData);
        }

        private JSONItemWrapper(JSONObject itemData) {
            if (itemData == null) {
                Logging.warning("Could not parse item!  Item may be lost!");
            }
            createItem(itemData);
        }

        private void createItem(JSONObject itemData) {
            if (itemData != null && itemData.containsKey(ITEM_ITEMSTACK)) {
                Object obj = itemData.get(ITEM_ITEMSTACK);
                if (obj instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) obj;
                    // JSONObject apparently likes to store numbers as Longs
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if (entry.getValue() instanceof Long) {
                            entry.setValue(((Long) entry.getValue()).intValue());
                        }
                    }
                    this.item = ItemStack.deserialize(map);
                    if (map.containsKey("meta")) {
                        Object metaObj = map.get("meta");
                        if (metaObj instanceof Map) {
                            Map<String, Object> metaMap = (Map<String, Object>) metaObj;
                            ItemMetaUtil.addSerializedItemMetaToItem(this.item, metaMap);
                        }
                    }
                    return;
                } else {
                    Logging.warning("Could not parse item: " + obj);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String asString() {
            return asJSONObject().toJSONString();
        }

        public JSONObject asJSONObject() {
            final JSONObject jsonItem = new JSONObject();
            final Map<String, Object> map = getItem().serialize();
            if (map.containsKey("meta")) {
                map.put("meta", ItemMetaUtil.getSerializedItemMeta(getItem()));
            }
            jsonItem.put(ITEM_ITEMSTACK, map);
            return jsonItem;
        }
    }
}

