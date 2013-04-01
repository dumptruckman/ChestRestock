package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.locale.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.plugin.command.QueuedPluginCommand;
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
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " createall <chunk radius> [name] [-i:<inventorytype>]");
        this.setArgRange(1, 3);
        this.addPrefixedKey(" createall");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall 40 phatlootz -i:chest");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall 0 infinispenser -i:dispenser");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall 5 -i:chest");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " createall 2 cornucopia");
        this.setPermission(Perms.CMD_CREATEALL.getPermission());
        setConfirmMessage(new BundledMessage(Language.CMD_CREATEALL_CONFIRM));
    }

    @Override
    public void preConfirm(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            messager.bad(Language.IN_GAME_ONLY, sender);
            return;
        }
        Player player = (Player) sender;
        int radius = 0;
        try {
            radius = Integer.valueOf(args.get(0));
            if (radius < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            messager.bad(Language.CMD_POSITIVE_NUMBER, sender);
            return;
        }
        Location location = player.getLocation();
        World world = player.getWorld();
        String name = null;
        Class<? extends InventoryHolder> type = null;
        for (int i = 1; i < args.size(); i++) {
            if (args.get(i).startsWith("-i:") && args.get(i).length() > 3) {
                type = inventoryTypes.get(args.get(i).substring(3).toLowerCase());
                if (type == null) {
                    messager.bad(Language.CMD_CREATEALL_INVALID_TYPE, sender, allTypes);
                    return;
                }
            } else {
                name = args.get(i);
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
                        // TODO
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