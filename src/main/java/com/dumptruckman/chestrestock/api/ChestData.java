package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;

public interface ChestData {
    
    public RestockableChest getChest(BlockLocation blockLocation);
    
    public void updateChest(RestockableChest chest);
    
    public void removeChest(RestockableChest chest);
}
