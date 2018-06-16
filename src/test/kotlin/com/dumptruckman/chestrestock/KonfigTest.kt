package com.dumptruckman.chestrestock

import com.dumptruckman.bukkit.configuration.hocon.HoconConfiguration
import com.dumptruckman.chestrestock.config.InvalidValueException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class KonfigTest {

    var config: HoconConfiguration by Delegates.notNull()
    var konfig: ChestRestockConfig by Delegates.notNull()

    @Before
    fun setUp() {
        config = HoconConfiguration()
        konfig = ChestRestockConfig(config)
    }

    @Test
    fun testConfigBasics() {
        assertEquals(60, konfig.settings.restock_task_interval)
        assertEquals(60, config.getInt("settings.restock_task_interval"))
        konfig.settings.restock_task_interval = 40
        assertEquals(40, konfig.settings.restock_task_interval)
        assertEquals(40, config.getInt("settings.restock_task_interval"))

        konfig.trySet("settings.restock_task_interval", "5")
        assertEquals(5, konfig.settings.restock_task_interval)
        assertEquals(5, config.getInt("settings.restock_task_interval"))
    }

    @Test(expected = InvalidValueException::class)
    fun testConfigValidator() {
        konfig.settings.max_inventory_size = 1
    }

    @Test
    fun testExtendedConfig() {
        val config = HoconConfiguration()
        val konfig = RestockableChestDefaults(config)

        assertEquals(-1, konfig.player_limit)
        assertEquals(-1, config.getInt("player_limit"))
        konfig.player_limit = 5
        assertEquals(5, konfig.player_limit)
        assertEquals(5, config.getInt("player_limit"))

        konfig.trySet("player_limit", "10")
        assertEquals(10, konfig.player_limit)
        assertEquals(10, config.getInt("player_limit"))
    }

    @Test
    fun testDefaultsConfig() {
        val config = HoconConfiguration()
        val konfig = RestockableChestDefaults(config)

        konfig.player_limit = 1
        assertEquals(1, konfig.player_limit)
        assertEquals(1, config.getInt("player_limit"))

        val dependentConfig = HoconConfiguration()
        val dependentKonfig = RestockableChestOptions(dependentConfig, konfig)
        assertEquals(1, dependentKonfig.player_limit)
        assertEquals(1, dependentConfig.getInt("player_limit"))
    }
}