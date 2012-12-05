package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandInfo;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.block.Block;

@CommandInfo(
        primaryAlias = "create",
        desc = "Initiates targeted chest for restocking.",
        max = 0
)
public class CreateCommand extends TargetedChestRestockCommand {

    @Override
    public Perm getPerm() {
        return Perms.CMD_CREATE;
    }

    @Override
    public Message getHelp() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean runCommand(ChestRestock p, BasePlayer sender, Block holder, CommandContext commandContext) {
        CRChest rChest = p.getChestManager().getChest(holder);
        if (rChest != null) {
            p.getMessager().message(sender, Language.CMD_CREATE_ALREADY_MADE);
            return true;
        }
        rChest = p.getChestManager().createChest(holder);
        if (rChest == null) {
            p.getMessager().message(sender, Language.CMD_CREATE_ERROR);
        } else {
            p.getMessager().message(sender, Language.CMD_CREATE_SUCCESS, rChest.get(CRChest.PERIOD));
        }
        return true;
    }

}
