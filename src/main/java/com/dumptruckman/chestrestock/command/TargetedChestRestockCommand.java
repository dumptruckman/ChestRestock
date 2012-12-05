package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class TargetedChestRestockCommand extends CRCommand {

    @Override
    public boolean runCommand(final ChestRestock p, final BasePlayer sender, final CommandContext commandContext) {
        if (!sender.isPlayer()) {
            p.getMessager().message(sender, Language.IN_GAME_ONLY);
            return true;
        }
        Player player = (Player) sender;
        Block holder = null;
        try {
            holder = p.getTargetedInventoryHolder(player);
        } catch (IllegalStateException e) {
            p.getMessager().message(sender, e.getMessage());
            return true;
        }
        return runCommand(p, sender, holder, commandContext);
    }

    public abstract boolean runCommand(final ChestRestock p, final BasePlayer sender, final Block holder, final CommandContext commandContext);
}
