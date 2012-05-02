package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.locale.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.plugin.command.QueuedPluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateAllCommand extends QueuedPluginCommand<ChestRestockPlugin> {

    private static final Map<String, Class<? extends InventoryHolder>> inventoryTypes = new HashMap<String, Class<? extends InventoryHolder>>();
    private static String allTypes;

    static {
        inventoryTypes.put("chest", Chest.class);
        inventoryTypes.put("dispenser", Dispenser.class);
        inventoryTypes.put("brewing_stand", BrewingStand.class);
        inventoryTypes.put("furnace", Furnace.class);
        StringBuilder buffer = new StringBuilder();
        for (String type : inventoryTypes.keySet()) {
            if (!buffer.toString().isEmpty()) {
                buffer.append(", ");
            }
            buffer.append(type);
        }
        allTypes = buffer.toString();
    }

    public CreateAllCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_CREATEALL_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " createall [name] [-w:<worldname>|-r:<radius(chunks)> [-l]] [-i:<inventorytype>]");
        this.setArgRange(0, 2);
        this.addPrefixedKey(" createall");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall phatlootz -i:chest");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall infinispenser -w:world -i:dispenser");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall -w:hungergames -i:chest");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall cornucopia -r:20");
        this.setPermission(Perms.CMD_CREATEALL.getPermission());
        setConfirmMessage(new BundledMessage(Language.CMD_CREATEALL_CONFIRM));
    }

    @Override
    public void preConfirm(CommandSender sender, List<String> args) {
        World world = null;
        String name = null;
        int radius = 0;
        boolean loadChunks = false;
        Location location = null;
        Class<? extends InventoryHolder> type = null;
        for (String arg : args) {
            if (arg.startsWith("-w:") && arg.length() > 3) {
                String worldName = arg.substring(3);
                world = Bukkit.getWorld(worldName);
                if (world == null) {
                    messager.bad(Language.CMD_INVALID_WORLD, sender, worldName);
                    return;
                }
                break;
            } else if (arg.startsWith("-r:") && arg.length() > 3) {
                if (!(sender instanceof Player)) {
                    messager.bad(Language.IN_GAME_ONLY, sender);
                    return;
                }
                try {
                    radius = Integer.valueOf(arg.substring(3));
                    if (radius <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    messager.bad(Language.CMD_POSITIVE_NUMBER, sender);
                    return;
                }
                location = ((Player) sender).getLocation();
            } else if (arg.startsWith("-i:") && arg.length() > 3) {
                type = inventoryTypes.get(arg.substring(3).toLowerCase());
                if (type == null) {
                    messager.bad(Language.CMD_CREATEALL_INVALID_TYPE, sender, allTypes);
                    return;
                }
            } else if (arg.equalsIgnoreCase("-l")) {
                loadChunks = true;
            } else {
                name = arg;
            }
        }

        List<Block> affectedBlocks = new LinkedList<Block>();
        if (world != null) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState block : chunk.getTileEntities()) {
                    if (block instanceof InventoryHolder) {
                        if (type != null) {
                            if (type.isAssignableFrom(block.getClass())) {
                                affectedBlocks.add(block.getBlock());
                            }
                        } else {
                            affectedBlocks.add(block.getBlock());
                        }
                    }
                }
            }
        } else if (radius > 0 && location != null) {
            radius--;
            world = location.getWorld();
            Chunk chunk = location.getChunk();
            int oX = chunk.getX(), oZ = chunk.getZ();
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    chunk = world.getChunkAt(oX + x, oZ + z);
                    if (chunk.isLoaded()) {
asdfa
                    } else if (loadChunks) {
                        chunk.load();
                    }
                }
            }
        }
        setData(sender, new CreateAllData(world, name, radius, location));
        messager.normal(Language.CMD_PATIENCE, sender);
    }

    @Override
    public void onConfirm(CommandSender sender, List<String> args) {
        CreateAllData data = (CreateAllData) getData(sender);

    }

    public void finishCommand(CommandSender sender) {
        messager.good(Language.CMD_CREATEALL_SUCCESS, sender);
        clearData(sender);
    }

    @Override
    public void onExpire(CommandSender sender, List<String> args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private class CreateAllData {

        private World world;
        private String name;
        private int radius;
        private Location location;

        private CreateAllData(World world, String name, int radius, Location location) {
            this.world = world;
            this.name = name;
            this.radius = radius;
            this.location = location;
        }

        public World getWorld() {
            return world;
        }

        public String getName() {
            return name;
        }

        public int getRadius() {
            return radius;
        }

        public Location getLocation() {
            return location;
        }
    }
}
