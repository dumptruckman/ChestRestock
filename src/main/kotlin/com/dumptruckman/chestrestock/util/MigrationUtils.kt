package com.dumptruckman.chestrestock.util

import com.dumptruckman.bukkit.configuration.hocon.HoconConfiguration
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun migrateConfig_v2(oldConfigFile: File,
                     newConfigFile: File,
                     migrationDirectory: Path,
                     newConfig: FileConfiguration = HoconConfiguration()) {
    val oldConfig = YamlConfiguration.loadConfiguration(oldConfigFile)
    copyConfig(oldConfig, newConfig)
    newConfig.save(newConfigFile)
    cleanupFiles(oldConfigFile, migrationDirectory)
}

fun copyConfig(source: Configuration, destination: Configuration) {
    source.getValues(true).forEach {
        key, value -> if (value !is ConfigurationSection) destination.set(key, value)
    }
}

private fun cleanupFiles(oldConfigFile: File, migrationDirectory: Path) {
    val oldConfigPath = oldConfigFile.toPath()
    val migratedConfigFile = migrationDirectory.resolve(oldConfigPath)
    Files.copy(oldConfigPath, migratedConfigFile)
    Files.delete(oldConfigPath)
}