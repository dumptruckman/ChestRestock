package com.dumptruckman.chestrestock

import com.dumptruckman.chestrestock.i18n.MutableMessageSource
import com.dumptruckman.chestrestock.util.migrateConfig_v2
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Path
import java.util.Locale
import java.util.ResourceBundle

class ChestRestock : JavaPlugin() {

    val migrationFolder: Path = dataFolder.toPath().resolve("./migrated")
    val defaultsFolder: File = File(dataFolder, "world_defaults")

    override fun onLoad() {
        checkAndMigrateConfigs()
    }

    private fun checkAndMigrateConfigs() {
        val oldConfigFile = File(dataFolder, "config.yml")
        val newConfigFile = File(dataFolder, "main.conf")
        if (oldConfigFile.exists() && !newConfigFile.exists()) {
            migrateConfig_v2(oldConfigFile, newConfigFile, migrationFolder)
        }

        val oldDefaultsFile = File(dataFolder, "global_defaults.yml")
        val newDefaultsFile = File(dataFolder, "global_defaults.conf")
        if (oldDefaultsFile.exists() && !newDefaultsFile.exists()) {
            migrateConfig_v2(oldDefaultsFile, newDefaultsFile, migrationFolder)
        }
    }

    companion object {
        internal val messageSource = object : MutableMessageSource(
                ResourceBundle.getBundle("Messages", Locale.ENGLISH)) {
            override fun getString(key: CharSequence): String = resourceBundle.getString(key.toString())
        }
    }
}