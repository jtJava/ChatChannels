package me.iowa.storage.configuration;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;

public class MessagesConfig {
    private final String channelCreated;
    private final String channelDeleted;
    private final String channelRadiusChanged;
    private final String channelWorldChanged;
    private final String channelSwitched;
    private final String channelNotFound;
    private final String channelExists;

    private final String filterAdded;
    private final String filterRemoved;
    private final String filterToggled;

    private final String configReloaded;
    private final String configReloadError;

    @Getter
    private final LegacyComponentSerializer componentSerializer = LegacyComponentSerializer.builder()
            .character(LegacyComponentSerializer.AMPERSAND_CHAR)
            .hexCharacter(LegacyComponentSerializer.HEX_CHAR).build();

    public MessagesConfig(FileConfiguration configuration) {
        this.channelCreated = configuration.getString("messages.channel-created", "&fChannel &e%channel% &fcreated.");
        this.channelDeleted = configuration.getString("messages.channel-deleted", "&fChannel &e%channel% &fdeleted.");
        this.channelRadiusChanged = configuration.getString("messages.channel-radius-changed", "&fChannel &e%channel% &fradius changed to &e%radius%&f.");
        this.channelWorldChanged = configuration.getString("messages.channel-world-changed", "&fChannel &e%channel% &fworld changed to &e%world%&f.");
        this.channelSwitched = configuration.getString("messages.channel-switched", "&fYou are now speaking in the %channel% channel.");
        this.channelNotFound = configuration.getString("messages.channel-not-found", "&cWe couldn't find a channel with the given name.");
        this.channelExists = configuration.getString("messages.channel-exists", "&cA channel with the given name already exists.");

        this.filterAdded = configuration.getString("messages.filter-added", "&fFilter phrase &e%phrase% &fadded.");
        this.filterRemoved = configuration.getString("messages.filter-removed", "&fFilter phrase &e%phrase% &fremoved.");
        this.filterToggled = configuration.getString("messages.filter-toggled", "&fFilter has been %enabled/disabled%.");

        this.configReloaded = configuration.getString("messages.config-reloaded", "&aConfig reloaded.");
        this.configReloadError = configuration.getString("messages.config-reload-error", "&cConfig save/reload failed.");
    }

    public Component getChannelCreated(String channel) {
        return componentSerializer.deserialize(channelCreated.replace("%channel%", channel));
    }

    public Component getChannelDeleted(String channel) {
        return componentSerializer.deserialize(channelDeleted.replace("%channel%", channel));
    }

    public Component getChannelRadiusChanged(String channel, String radius) {
        return componentSerializer.deserialize(channelRadiusChanged.replace("%channel%", channel).replace("%radius%", radius));
    }

    public Component getChannelWorldChanged(String channel, String world) {
        return componentSerializer.deserialize(channelWorldChanged.replace("%channel%", channel).replace("%world%", world));
    }

    public Component getChannelSwitched(String channel) {
        return componentSerializer.deserialize(channelSwitched.replaceAll("%channel%", channel));
    }

    public Component getChannelExists() {
        return componentSerializer.deserialize(channelExists);
    }

    public Component getChannelNotFound() {
        return componentSerializer.deserialize(channelNotFound);
    }

    public Component getFilterAdded(String phrase) {
        return componentSerializer.deserialize(filterAdded.replace("%phrase%", phrase));
    }

    public Component getFilterRemoved(String phrase) {
        return componentSerializer.deserialize(filterRemoved.replace("%phrase%", phrase));
    }

    public Component getFilterToggled(boolean enabled) {
        return componentSerializer.deserialize(filterToggled.replace("%enabled%", enabled ? "enabled" : "disabled"));
    }

    public Component getConfigReloaded() {
        return componentSerializer.deserialize(configReloaded);
    }

    public Component getConfigReloadError() {
        return componentSerializer.deserialize(configReloadError);
    }
}
