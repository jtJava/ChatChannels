package me.iowa.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import me.iowa.ChatChannels;

@Getter
public class ChannelManager {
    private final Map<String, Channel> channels = new HashMap<>();

    public ChannelManager() {
        this.addChannel(new Channel("Global", "*", 0));

        for (Channel channel : (List<Channel>) ChatChannels.getInstance().getConfig().getList("channels")) {
            this.addChannel(channel);
        }
    }

    public void addChannel(Channel channel) {
        channels.put(channel.getIdentifier(), channel);
    }

    public Channel removeChannel(String identifier) {
        return channels.remove(identifier);
    }

    public Optional<Channel> getChannel(String identifier) {
        return Optional.ofNullable(channels.get(identifier));
    }

    public void save() {
        ChatChannels plugin = ChatChannels.getInstance();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getConfig().set("channels", new ArrayList<>(this.channels.values()));
        });
    }
}
