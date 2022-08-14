package me.iowa.listener;

import me.iowa.ChatChannels;
import me.iowa.storage.database.ChatProfile;
import me.iowa.storage.database.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        ChatProfile senderProfile = DataManager.getInstance().getPlayerData(event.getPlayer().getUniqueId());
        event.getRecipients().removeIf(player -> {
            ChatProfile recipientProfile = DataManager.getInstance().getPlayerData(player.getUniqueId());
            // Not in the same channel
            if (!recipientProfile.getChatChannel().equals(senderProfile.getChatChannel())) {
                return true;
            }

            // World Check
            boolean global = recipientProfile.getChatChannel().getWorldName().equalsIgnoreCase("*");
            if (!global && !recipientProfile.getChatChannel().getWorldName().equalsIgnoreCase(senderProfile.getChatChannel().getWorldName())) {
                return true;
            }

            // Distance check
            if (senderProfile.getChatChannel().getBlockRadius() > 0) {
                double distance = event.getPlayer().getLocation().distance(player.getLocation());
                return distance > recipientProfile.getChatChannel().getBlockRadius();
            }

            // Profanity filter
            if (recipientProfile.isFilterProfanity()) {
                for (String filteredPhrase : ChatChannels.getInstance().getGeneralConfig().getFilteredPhrases()) {
                    if (event.getMessage().contains(filteredPhrase)) {
                        return true;
                    }
                }
            }

            return false;
        });
    }
}
