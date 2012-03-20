package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class DefaultChestManager implements ChestManager {

    private ChestRestockPlugin plugin;
    private File chestsFile;
    
    private Map<BlockLocation, CRChest> chestsMap = new HashMap<BlockLocation, CRChest>();
    
    DefaultChestManager(ChestRestockPlugin plugin) {
        this.plugin = plugin;
        chestsFile = new File(plugin.getDataFolder(), "chests");
        if (!chestsFile.exists()) {
            chestsFile.mkdirs();
        }
    }
    
    private File getChestFile(BlockLocation location) {
        return new File(chestsFile, location.toString() + ".yml");
    }

    @Override
    public CRChest getChest(Block block, InventoryHolder holder) {
        BlockLocation location = BlockLocation.get(block);
        CRChest rChest = chestsMap.get(location);
        if (rChest != null) {
            Logging.finer("Got cached chest at " + location);
            return rChest;
        }
        File chestFile = getChestFile(location);
        if (!chestFile.exists()) {
            if (!(holder.getInventory() instanceof DoubleChestInventory)) {
                Logging.finest("No file for single chest found.");
                return null;
            }
            Logging.finer("Searching for other side of double chest...");
            Chest otherSide = getOtherSide(block);
            if (otherSide == null) {
                Logging.fine("Chest claims to be double but other side not found!");
                return null;
            }
            location = BlockLocation.get(otherSide.getBlock());
            rChest = chestsMap.get(location);
            if (rChest != null) {
                Logging.finer("Got cached chest (other-side) at " + location);
                return rChest;
            }
            chestFile = getChestFile(location);
            if (!chestFile.exists()) {
                Logging.finest("No file found for other side of double chest.");
                return null;
            }
        }
        return loadChest(chestFile);
    }
    
    //public void removeChest()

    @Override
    public CRChest newChest(Block block, InventoryHolder holder) {
        return loadChest(getChestFile(BlockLocation.get(block)));
    }

    @Override
    public boolean removeChest(BlockLocation location) {
        boolean cached = chestsMap.remove(location) != null;
        boolean filed = getChestFile(location).delete();
        if (cached || filed) {
            Logging.fine("Removed chest from cache: " + cached + ".  Able to delete file: " + filed);
            return true;
        }
        Block chestBlock = location.getBlock();
        if (chestBlock != null) {
            Chest chest = getOtherSide(chestBlock);
            if (chest != null) {
                location = BlockLocation.get(chest.getBlock());
                cached = chestsMap.remove(location) != null;
                filed = getChestFile(location).delete();
                if (cached || filed) {
                    Logging.fine("Removed chest from cache: " + cached + ".  Able to delete file: " + filed);
                    return true;
                }
            }
        }
        Logging.fine("Found no chest to remove in cache or files");
        return false;
    }

    private CRChest loadChest(File chestFile) {
        try {
            BlockLocation location = BlockLocation.get(
                    chestFile.getName().substring(0, chestFile.getName().indexOf(".yml")));
            return new DefaultCRChest(plugin, location, chestFile, CRChest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            Logging.warning("Block location could not be parsed from file name: " + chestFile.getName());
            return null;
        } catch (IllegalStateException e) {
            Logging.warning(e.getMessage());
            return null;
        }
    }

    @Override
    public Chest getOtherSide(Block chestBlock) {
        Chest otherSide = null;
        if (chestBlock.getRelative(1, 0, 0).getState() instanceof Chest) {
            otherSide = (Chest) chestBlock.getRelative(1, 0, 0).getState();
        } else if (chestBlock.getRelative(-1, 0, 0).getState() instanceof Chest) {
            otherSide = (Chest) chestBlock.getRelative(-1, 0, 0).getState();
        } else if (chestBlock.getRelative(0, 0, 1).getState() instanceof Chest) {
            otherSide = (Chest) chestBlock.getRelative(0, 0, 1).getState();
        } else if (chestBlock.getRelative(0, 0, -1).getState() instanceof Chest) {
            otherSide = (Chest) chestBlock.getRelative(0, 0, -1).getState();
        }
        return otherSide;
    }

    @Override
    public Block getTargetedInventoryHolder(Player player) throws IllegalStateException {
        Block block = player.getTargetBlock(null, 100);
        if (block == null || !(block.getState() instanceof InventoryHolder)) {
            throw new IllegalStateException(plugin.getMessager().getMessage(Language.TARGETING));
        }
        return block;
    }
}
