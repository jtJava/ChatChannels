package me.iowa.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public abstract class ReflectedSerializable implements ConfigurationSerializable {

    // Deserialization
    public ReflectedSerializable(Map<String, Object> data) {
        for (Field field : this.getClass().getDeclaredFields()) {
            Object value = data.get(field.getName());

            if (value != null) {
                if (Modifier.isTransient(field.getModifiers())) continue;
                try {
                    field.setAccessible(true);
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {
            if (Modifier.isTransient(field.getModifiers())) continue;
            try {
                field.setAccessible(true);
                data.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
