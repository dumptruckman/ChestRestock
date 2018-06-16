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
    fun testDefaultsConfig() {
        konfig.settings.restock_task_interval = 1
        assertEquals(1, konfig.settings.restock_task_interval)
        assertEquals(1, config.getInt("settings.restock_task_interval"))

        val dependentConfig = HoconConfiguration()
        val dependentKonfig = ChestRestockConfig(dependentConfig, konfig)
        assertEquals(1, dependentKonfig.settings.restock_task_interval)
        assertEquals(1, dependentConfig.getInt("settings.restock_task_interval"))
    }
}