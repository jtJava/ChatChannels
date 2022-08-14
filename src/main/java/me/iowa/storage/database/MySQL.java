package me.iowa.storage.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.bukkit.Bukkit;

public class MySQL {

    private Connection connection;

    public MySQL() {
        this.connect();
        this.createTable();
    }

    private void createTable() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `chatchanneldb`.`chatchannels` (`uuid` VARCHAR(36) NOT NULL, `chatchannel` INT NOT NULL DEFAULT 'Global', 'filterprofanity' BOOLEAN DEFAULT FALSE, PRIMARY KEY (`uuid`));");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        if (this.connection != null) {
            return;
        }

        try {
            String host = "localhost";
            String port = "3306";
            String database = "chatchanneldb";
            String username = "root";
            String password = "";

            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            Bukkit.getLogger().warning("There was an issue connecting to the database.");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
