package me.iowa.storage.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import lombok.Data;
import me.iowa.ChatChannels;
import me.iowa.channel.Channel;
import org.bukkit.Bukkit;

@Data
public class ChatProfile {
    private UUID uuid;
    private Channel chatChannel;
    private boolean filterProfanity;

    public ChatProfile(UUID uuid) {
        String channelIdentifier;
        boolean filterProfanity;
        try {
            PreparedStatement preparedStatement = ChatChannels.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM `chatchannels` WHERE `uuid` = ?");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            channelIdentifier = preparedStatement.getResultSet().getString("chatchannel");
            filterProfanity = preparedStatement.getResultSet().getBoolean("filterprofanity");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("There was an issue retrieving a profile from the database, setting to defaults.");
            this.uuid = uuid;
            channelIdentifier = "Global";
            filterProfanity = false;
        }
        this.chatChannel = ChatChannels.getInstance().getChannelManager().getChannel(channelIdentifier).orElseGet(() -> ChatChannels.getInstance().getChannelManager().getChannel("Global").get());
        this.filterProfanity = filterProfanity;
    }

    public PreparedStatement getSaveStatement() throws SQLException {
        PreparedStatement preparedStatement = ChatChannels.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO `chatchannels` (`uuid`, `chatchannel`) VALUES (?, ?, ?)");
        preparedStatement.setString(1, this.uuid.toString());
        preparedStatement.setString(2, this.chatChannel.getIdentifier());
        preparedStatement.setBoolean(3, this.filterProfanity);
        return preparedStatement;
    };
}
