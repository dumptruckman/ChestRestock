package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;

public interface ChestData {
    
    public RestockChest getChest(BlockLocation blockLocation);
    
    public void updateChest(RestockChest chest);
    
    public void removeChest(RestockChest chest);
}
