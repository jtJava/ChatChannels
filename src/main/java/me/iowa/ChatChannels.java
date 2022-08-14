package me.iowa;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.iowa.channel.Channel;
import me.iowa.channel.ChannelManager;
import me.iowa.command.ChannelCommand;
import me.iowa.listener.ChatListener;
import me.iowa.storage.configuration.GeneralConfig;
import me.iowa.storage.configuration.MessagesConfig;
import me.iowa.storage.database.MySQL;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ChatChannels extends JavaPlugin {
    @Getter
    private static ChatChannels instance;

    private PaperCommandManager commandManager;
    private ChannelManager channelManager;

    private MySQL mySQL;

    private GeneralConfig generalConfig;
    private MessagesConfig messagesConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        ConfigurationSerialization.registerClass(Channel.class);

        this.mySQL = new MySQL();
        this.channelManager = new ChannelManager();

        this.commandManager = new PaperCommandManager(this);
        this.commandManager.getCommandCompletions().registerCompletion("channels", string -> this.channelManager.getChannels().keySet());
        this.commandManager.registerCommand(new ChannelCommand());

        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        this.mySQL.disconnect();
    }

    public void onReload() {
        this.reloadConfig();

        this.messagesConfig = new MessagesConfig(this.getConfig());
        this.generalConfig = new GeneralConfig(this.getConfig());

        this.channelManager = new ChannelManager();
    }
}
