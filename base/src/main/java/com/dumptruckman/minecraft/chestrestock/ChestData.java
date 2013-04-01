package com.dumptruckman.minecraft.chestrestock;

import com.dumptruckman.minecraft.chestrestock.minecraft.Item;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.properties.*;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestData implements Properties, ChestOptions {

    @NotNull
    private final Properties properties;

    ChestData(@NotNull final Properties properties) {
        this.properties = properties;
    }

    /**
     * The items this chest restocks with.
     */
    SimpleProperty<Item[]> ITEMS = PropertyFactory.newProperty(Item[].class, "items",
            new Item[ChestConstants.getMaxInventorySize()]).build();

    /**
     * Data pertaining to players interacting with this chest.  See {@link ChestUser}
     */
    MappedProperty<ChestUser> PLAYERS = PropertyFactory.newMappedProperty(ChestUser.class, "players")
            .serializer(new PropertySerializer<ChestUser>() {

                @Override
                public ChestUser deserialize(Object o) {
                    int lootCount = 0;
                    long lastRestockTime = 0;
                    try {
                        if (o instanceof ConfigurationSection) {
                            o = ((ConfigurationSection) o).getValues(false);
                        }
                        Map<String, Object> map = (Map<String, Object>) o;
                        if (map == null) {
                            map = new HashMap<String, Object>();
                        }
                        Object obj = map.get("restockCount");
                        if (obj == null) {
                            obj = 0;
                        }
                        lootCount = Integer.valueOf(obj.toString());
                        obj = map.get("lastRestockTime");
                        if (obj == null) {
                            obj = 0L;
                        }
                        lastRestockTime = Long.valueOf(obj.toString());
                    } catch (ClassCastException e) {
                        Logging.warning("Error in player data!");
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        Logging.warning("Error in player data!");
                        e.printStackTrace();
                    }
                    return Players.newCRPlayer(lootCount, lastRestockTime);
                }

                @Override
                public Object serialize(ChestUser crPlayer) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("restockCount", crPlayer.getLootCount());
                    map.put("lastRestockTime", crPlayer.getLastRestockTime());
                    return map;
                }
            }).build();

    /**
     * The last time this chest was restocked (when not {@link ChestOptions#UNIQUE}).
     */
    SimpleProperty<Long> LAST_RESTOCK = PropertyFactory.newProperty(Long.class, "lastRestockTime", 0L).build();

    @Override
    public void flush() throws PluginBaseException {
        properties.flush();
    }

    @Override
    public void reload() throws PluginBaseException {
        properties.reload();
    }

    @Override
    @Nullable
    public <T> T get(@NotNull final SimpleProperty<T> tSimpleProperty) throws IllegalArgumentException {
        return properties.get(tSimpleProperty);
    }

    @Override
    @Nullable
    public <T> List<T> get(@NotNull final ListProperty<T> tListProperty) throws IllegalArgumentException {
        return properties.get(tListProperty);
    }

    @Override
    @Nullable
    public <T> Map<String, T> get(@NotNull final MappedProperty<T> tMappedProperty) throws IllegalArgumentException {
        return properties.get(tMappedProperty);
    }

    @Override
    @Nullable
    public <T> T get(@NotNull final MappedProperty<T> tMappedProperty, @NotNull final String s) throws IllegalArgumentException {
        return properties.get(tMappedProperty, s);
    }

    @Override
    @NotNull
    public NestedProperties get(@NotNull final NestedProperty nestedProperty) throws IllegalArgumentException {
        return properties.get(nestedProperty);
    }

    @Override
    public <T> boolean set(@NotNull final SimpleProperty<T> tSimpleProperty, final T t) throws IllegalArgumentException {
        return properties.set(tSimpleProperty, t);
    }

    @Override
    public <T> boolean set(@NotNull final ListProperty<T> tListProperty, final List<T> ts) throws IllegalArgumentException {
        return properties.set(tListProperty, ts);
    }

    @Override
    public <T> boolean set(@NotNull final MappedProperty<T> tMappedProperty, final Map<String, T> stringTMap) throws IllegalArgumentException {
        return properties.set(tMappedProperty, stringTMap);
    }

    @Override
    public <T> boolean set(@NotNull final MappedProperty<T> tMappedProperty, @NotNull final String s, final T t) throws IllegalArgumentException {
        return properties.set(tMappedProperty, s, t);
    }

    @Override
    public <T> void setPropertyValidator(@NotNull final ValueProperty<T> tValueProperty, @NotNull final PropertyValidator<T> tPropertyValidator) {
        properties.setPropertyValidator(tValueProperty, tPropertyValidator);
    }

    @Override
    public <T> boolean isValid(@NotNull final ValueProperty<T> tValueProperty, @Nullable final T t) {
        return properties.isValid(tValueProperty, t);
    }

    @Override
    public boolean addObserver(@NotNull final Observer observer) {
        return properties.addObserver(observer);
    }

    @Override
    public boolean deleteObserver(@NotNull final Observer observer) {
        return properties.deleteObserver(observer);
    }
}
