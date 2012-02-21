package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.ChestItem;
import com.dumptruckman.chestrestock.util.ItemWrapper;
import com.dumptruckman.chestrestock.util.SimpleItemWrapper;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author dumptruckman
 */
public class DefaultChestItem implements ChestItem {

    private int slot;
    private ItemWrapper itemWrapper;

    public DefaultChestItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemWrapper = new SimpleItemWrapper(itemStack);
    }

    @Override
    public ItemStack getItem() {
        return this.itemWrapper.getItem();
    }

    public int getSlot() {
        return this.slot;
    }
}
