package me.iowa.channel;

import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import me.iowa.storage.ReflectedSerializable;

@Getter
@Setter
public class Channel extends ReflectedSerializable {
    private String identifier;
    private String worldName;
    private int blockRadius;

    public Channel(String identifier, String worldName, int blockRadius) {
        super(Collections.emptyMap());
        this.identifier = identifier;
        this.worldName = worldName;
        this.blockRadius = blockRadius;
    }

    public Channel(Map<String, Object> data) {
        super(data);
    }
}
