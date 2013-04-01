package com.dumptruckman.minecraft.chestrestock;

/**
 * Constants related to ChestRestock chests.
 */
public class ChestConstants {

    /**
     * The minimum for max inventory size.
     */
    public static final int MIN_MAX_INVENTORY_SIZE = 54;

    private static int MAX_INVENTORY_SIZE = MIN_MAX_INVENTORY_SIZE;

    /**
     * Sets the maximum size of any inventory for use in ChestRestock.
     *
     * @param size a value no less than {@link #MIN_MAX_INVENTORY_SIZE}.
     */
    public static void setMaxInventorySize(int size) {
        if (size < MIN_MAX_INVENTORY_SIZE) {
            throw new IllegalArgumentException("Size may not be less than " + MIN_MAX_INVENTORY_SIZE);
        }
        MAX_INVENTORY_SIZE = size;
    }

    /**
     * @return the max size for any inventory for use in ChestRestock.
     */
    public static int getMaxInventorySize() {
        return MAX_INVENTORY_SIZE;
    }
}
