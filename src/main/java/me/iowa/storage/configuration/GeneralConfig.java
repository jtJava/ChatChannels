package me.iowa.storage.configuration;

import java.util.List;
import lombok.Getter;
import me.iowa.ChatChannels;
import org.bukkit.configuration.file.FileConfiguration;

public class GeneralConfig {
    private final FileConfiguration configuration;

    @Getter
    private final List<String> filteredPhrases;

    public GeneralConfig(FileConfiguration configuration) {
        this.configuration = configuration;
        this.filteredPhrases = configuration.getStringList("filtered-phrases");
    }

    public void save() {
        ChatChannels plugin = ChatChannels.getInstance();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            this.configuration.set("filtered-phrases", filteredPhrases);
            ChatChannels.getInstance().saveConfig();
        });
    }
}
