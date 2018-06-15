package com.dumptruckman.chestrestock.config

import com.dumptruckman.bukkit.configuration.hocon.HoconConfiguration
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import kotlin.reflect.KClassifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

open class Konfig(private val config: Configuration,
                  private val delegateMap: MutableMap<String, KonfigValueDelegate<*>> = mutableMapOf(),
                  private val node: String = "") {

    constructor (parent: Konfig, node: String) :
            this(parent.config,
                    parent.delegateMap,
                    if (parent.node.isEmpty())
                        node
                    else
                        "${parent.node}${parent.config.options().pathSeparator()}$node")

    fun trySet(fullPath: String, value: String) {
        delegateMap.get(fullPath)?.trySet(value) ?: throw UnknownPathException()
    }

    fun hasProp(fullPath: String): Boolean = delegateMap.containsKey(fullPath)

    private fun getPath(property: KProperty<*>): String =
        if (node.isEmpty()) {
            property.name
        } else {
            "$node${config.options().pathSeparator()}${property.name}"
        }

    class KonfigValue<T>(private val defaultValue: T? = null) {

        var comments: Array<out String>? = null
        var validator: ((Any?) -> Boolean)? = null

        operator fun provideDelegate(konfig: Konfig, property: KProperty<*>): KonfigValueDelegate<T> {
            if (property !is KMutableProperty) {
                throw IllegalArgumentException("KonfigValue cannot be used on constant properties (val)")
            }

            val config = konfig.config
            val path = konfig.getPath(property)

            if (defaultValue != null) {
                config.addDefault(path, defaultValue)
            }

            if (config is HoconConfiguration && comments != null && !comments!!.isEmpty()) {
                config.setComments(path, *comments!!)
                comments
            }

            val result = KonfigValueDelegate<T>(konfig, property, validator)
            konfig.delegateMap.put(path, result)
            return result
        }

        fun withComments(vararg comments: String): KonfigValue<T> {
            this.comments = comments
            return this
        }

        fun withValidator(validator: (Any?) -> Boolean): KonfigValue<T> {
            this.validator = validator
            return this
        }
    }

    class KonfigValueDelegate<T>(private val owner: Konfig,
                                 private val property: KMutableProperty<*>,
                                 private val validator: ((Any?) -> Boolean)?) {

        val type = property.returnType.classifier

        fun trySet(value: String) {
            if (validator != null && !validator.invoke(value)) {
                throw InvalidValueException()
            }
            try {
                when (type) {
                    String::class -> property.setter.call(owner, value)
                    Int::class -> property.setter.call(owner, value.toInt())
                    Double::class -> property.setter.call(owner, value.toDouble())
                    Long::class -> property.setter.call(owner, value.toLong())
                    Boolean::class -> property.setter.call(owner, value.toBoolean())
                    else -> {
                        throw ComplexTypeException()
                    }
                }
            } catch (e: NumberFormatException) {
                throw WrongTypeException(type)
            }
        }

        operator fun getValue(konfig: Konfig, property: KProperty<*>): T {
            val pathString = konfig.getPath(property)
            val type = property.returnType
            val config = konfig.config

            return when (type.classifier) {
                String::class -> config.getString(pathString)
                Int::class -> config.getInt(pathString)
                Double::class -> config.getDouble(pathString)
                Long::class -> config.getLong(pathString)
                List::class -> config.getList(pathString)
                Boolean::class -> config.getBoolean(pathString)
                ConfigurationSection::class -> config.getConfigurationSection(pathString)
                else -> config.get(pathString)
            } as T
        }

        operator fun setValue(konfig: Konfig, property: KProperty<*>, value: T) {
            if (validator != null && !validator.invoke(value)) {
                throw InvalidValueException()
            }
            konfig.config.set(konfig.getPath(property), value)
        }
    }
}

class UnknownPathException : Exception()
class WrongTypeException(val expectedType: KClassifier?) : Exception()
class ComplexTypeException : Exception()
class InvalidValueException : Exception()