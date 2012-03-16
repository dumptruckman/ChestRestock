package com.dumptruckman.chestrestock;

import com.dumptruckman.actionmenu2.api.MenuView;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

class DefaultChestManager implements ChestManager {

    private ChestRestockPlugin plugin;
    private File chestsFile;
    
    private Map<BlockLocation, RestockableChest> chestsMap = new HashMap<BlockLocation, RestockableChest>();
    
    DefaultChestManager(ChestRestockPlugin plugin) {
        this.plugin = plugin;
        chestsFile = new File(plugin.getDataFolder(), "chests");
        if (!chestsFile.exists()) {
            chestsFile.mkdirs();
        }
    }
    
    private File getChestFile(BlockLocation location) {
        return new File(chestsFile, location.toString());
    }

    @Override
    public RestockableChest getChest(Chest chest) {
        RestockableChest rChest = chestsMap.get();
        BlockLocation location = BlockLocation.get(chest.getBlock());
        File chestFile = getChestFile(location);
        if (!chestFile.exists()) {
            if (!(chest.getInventory() instanceof DoubleChestInventory)) {
                return null;
            }
            Chest otherSide = null;
            Block chestBlock = chest.getBlock();
            if (chestBlock.getRelative(1, 0, 0).getState() instanceof Chest) {
                otherSide = (Chest) chestBlock.getRelative(1, 0, 0).getState();
            } else if (chestBlock.getRelative(-1, 0, 0).getState() instanceof Chest) {
                otherSide = (Chest) chestBlock.getRelative(-1, 0, 0).getState();
            } else if (chestBlock.getRelative(0, 0, 1).getState() instanceof Chest) {
                otherSide = (Chest) chestBlock.getRelative(0, 0, 1).getState();
            } else if (chestBlock.getRelative(0, 0, -1).getState() instanceof Chest) {
                otherSide = (Chest) chestBlock.getRelative(0, 0, -1).getState();
            }
            if (otherSide == null) {
                Logging.fine("Chest claims to be double but other side not found!");
                return null;
            }
            location = BlockLocation.get(otherSide.getBlock());
            chestFile = getChestFile(location);
            if (!chestFile.exists()) {
                return null;
            }
        }
    }
    
    public void removeChest()

    @Override
    public RestockableChest newChest(Chest chest, Player owner) {
        return null;
    }

    @Override
    public MenuView newSignView(Sign sign) {
        return new ChestSignView(plugin, sign);
    }
}
