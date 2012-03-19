package com.dumptruckman.chestrestock.util;

import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockLocation {

    private final String world;
    private final int x;
    private final int y;
    private final int z;

    private final String stringForm;

    private BlockLocation(Block block) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    private BlockLocation(World world, int x, int y, int z) {
        this(world.getName(), x, y, z);
    }

    private BlockLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.stringForm = this.world + "-" + this.x + "-" + this.y + "-" + this.z;
    }

    public final String getWorldName() {
        return world;
    }

    public final World getWorld() {
        return Bukkit.getWorld(this.world);
    }
    
    public final Block getBlock() {
        World world = getWorld();
        if (world == null) {
            return null;
        }
        return world.getBlockAt(getX(), getY(), getZ());
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getZ() {
        return z;
    }

    @Override
    public final String toString() {
        return stringForm;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BlockLocation) {
            BlockLocation otherLoc = (BlockLocation) o;
            if (this.getWorld().equals(otherLoc.getWorld())
                    && this.getX() == otherLoc.getX()
                    && this.getY() == otherLoc.getY()
                    && this.getZ() == otherLoc.getZ())  {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public static BlockLocation get(String stringFormat) {
        String[] sections = stringFormat.split("~");
        if (sections.length != 4) {
            return null;
        }
        try {
            return new BlockLocation(sections[0],
                    Integer.valueOf(sections[1]),
                    Integer.valueOf(sections[2]),
                    Integer.valueOf(sections[3]));
        } catch (Exception e) {
            Logging.finer("Unable to parse location: " + stringFormat);
            return null;
        }
    }

    public static BlockLocation get(Block block) {
        return new BlockLocation(block);
    }
}
