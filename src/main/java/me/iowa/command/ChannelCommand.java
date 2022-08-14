package me.iowa.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import java.util.Optional;
import me.iowa.ChatChannels;
import me.iowa.channel.Channel;
import me.iowa.storage.database.DataManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChannelCommand extends BaseCommand {
    private static final ChatChannels CHAT_CHANNELS = ChatChannels.getInstance();

    @Subcommand("radius")
    @Syntax("[channel] [amount]")
    @CommandCompletion("@channels @range:0-100")
    @CommandPermission("channels.modify")
    public void radius(CommandSender sender, String channel, int radius) {
        Optional<Channel> optional = CHAT_CHANNELS.getChannelManager().getChannel(channel);
        if (optional.isPresent()) {
            Channel actualChannel = optional.get();
            actualChannel.setBlockRadius(radius);
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelRadiusChanged(channel, String.valueOf(radius)));
            this.saveChanges(sender);
        } else {
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelNotFound());
        }
    }

    @Subcommand("worldName")
    @Syntax("[channel] [amount]")
    @CommandCompletion("@channels @worlds")
    @CommandPermission("channels.modify")
    public void worldName(CommandSender sender, String channel, String worldName) {
        Optional<Channel> optional = CHAT_CHANNELS.getChannelManager().getChannel(channel);
        if (optional.isPresent()) {
            Channel actualChannel = optional.get();
            actualChannel.setWorldName(worldName);
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelWorldChanged(channel, worldName));
            this.saveChanges(sender);
        } else {
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelNotFound());
        }
    }

    @Subcommand("delete")
    @Syntax("[channel]")
    @CommandCompletion("@channels")
    @CommandPermission("channels.modify")
    public void delete(CommandSender sender, String channel) {
        Optional<Channel> optional = CHAT_CHANNELS.getChannelManager().getChannel(channel);
        if (optional.isPresent()) {
            CHAT_CHANNELS.getChannelManager().removeChannel(channel);
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelDeleted(channel));
            this.saveChanges(sender);
        } else {
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelNotFound());
        }
    }

    @Subcommand("create")
    @Syntax("[channel]")
    @CommandCompletion("@channels")
    @CommandPermission("channels.modify")
    public void create(CommandSender sender, String channel, String worldName, int radius) {
        Optional<Channel> optional = CHAT_CHANNELS.getChannelManager().getChannel(channel);
        if (optional.isPresent()) {
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelExists());
        } else {
            CHAT_CHANNELS.getChannelManager().addChannel(new Channel(channel, worldName, radius));
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelCreated(channel));
            this.saveChanges(sender);
        }
    }

    @Subcommand("switch")
    @Syntax("[channel]")
    @CommandCompletion("@channels")
    public void switchTo(CommandSender sender, String channel) {
        Optional<Channel> optional = CHAT_CHANNELS.getChannelManager().getChannel(channel);
        if (optional.isPresent()) {
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelSwitched(channel));
        } else {
            sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getChannelNotFound());
        }
    }

    @Subcommand("togglefilter")
    public void toggleFilter(Player sender) {
        DataManager dataManager = DataManager.getInstance();
        boolean newValue = !dataManager.getPlayerData(sender.getUniqueId()).isFilterProfanity();
        dataManager.getPlayerData(sender.getUniqueId()).setFilterProfanity(newValue);
        sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getFilterToggled(newValue));
    }

    @Subcommand("filter")
    @Syntax("[phrase]")
    public void filter(CommandSender sender, String phrase) {
        CHAT_CHANNELS.getGeneralConfig().getFilteredPhrases().add(phrase);
        CHAT_CHANNELS.getGeneralConfig().save();
        sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getFilterAdded(phrase));
    }

    @Subcommand("unfilter")
    @Syntax("[phrase]")
    public void unfilter(CommandSender sender, String phrase) {
        CHAT_CHANNELS.getGeneralConfig().getFilteredPhrases().remove(phrase);
        CHAT_CHANNELS.getGeneralConfig().save();
        sender.sendMessage(CHAT_CHANNELS.getMessagesConfig().getFilterRemoved(phrase));

    }

    @Subcommand("reload")
    @CommandPermission("channels.modify")
    public void reload(CommandSender executor) {
        try {
            CHAT_CHANNELS.onReload();
            executor.sendMessage(CHAT_CHANNELS.getMessagesConfig().getConfigReloaded());
        } catch (Exception exception) {
            exception.printStackTrace();
            executor.sendMessage(CHAT_CHANNELS.getMessagesConfig().getConfigReloadError());
        }
    }

    private void saveChanges(CommandSender executor) {
        try {
            CHAT_CHANNELS.getChannelManager().save();
            executor.sendMessage(CHAT_CHANNELS.getMessagesConfig().getConfigReloaded());
        } catch (Exception exception) {
            exception.printStackTrace();
            executor.sendMessage(CHAT_CHANNELS.getMessagesConfig().getConfigReloadError());
        }
    }
}
