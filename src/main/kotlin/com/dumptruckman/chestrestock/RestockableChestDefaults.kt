package com.dumptruckman.chestrestock

import com.dumptruckman.chestrestock.config.Konfig
import org.bukkit.configuration.Configuration

class RestockableChestDefaults(config: Configuration) : RestockableChestOptions(config, null) {

    val other: Other = Other()

    inner class Other : Konfig(this@RestockableChestDefaults, "other") {

        var auto_create: Boolean by KonfigValue(false).withComments(
                "This will automatically initialize any chest not already managed by ChestRestock with these defaults.",
                "Essentially the same thing as using \"/cr create\" on every chest encountered")

        var auto_create_new_chests: Boolean by KonfigValue(false).withComments(
                "This will automatically initialize any chest not already managed by ChestRestock with these defaults.",
                "Essentially the same thing as using \"/cr create\" on every chest encountered",
                "Requires auto_create to be enabled!",
                "This basically determines if NEWLY PLACED chests will be enabled or disabled. "
                        + "When set to false, all NEWLY PLACED chests will act like NORMAL chests.")
    }
}