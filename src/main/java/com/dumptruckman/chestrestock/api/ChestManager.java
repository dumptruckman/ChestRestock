package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

public interface ChestManager {
    
    RestockableChest getChest(Chest chest);
    
    Chest getTargetedChest(Player player) throws IllegalStateException;
    
    RestockableChest newChest(Chest chest);

    Chest getOtherSide(Block chestBlock);
    
    boolean removeChest(BlockLocation location);
}
