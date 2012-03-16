package com.dumptruckman.chestrestock.api;

import com.dumptruckman.actionmenu2.api.MenuView;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface ChestManager {
    
    RestockableChest getChest(Chest chest);
    
    RestockableChest newChest(Chest chest, Player owner);
    
    MenuView newSignView(Sign sign);
}
