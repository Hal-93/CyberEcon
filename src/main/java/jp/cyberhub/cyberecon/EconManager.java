package jp.cyberhub.cyberecon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EconManager {
    private final Database database;

    public EconManager(Database database) {
        this.database = database;
    }

    public double getBalance(String playerUUID) {
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM economy WHERE player_uuid = ?")) {
            stmt.setString(1, playerUUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                return 0.0; // 初期値
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public void setBalance(String playerUUID, double amount) {
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO economy (player_uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = ?")) {
            stmt.setString(1, playerUUID);
            stmt.setDouble(2, amount);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBalance(String playerUUID, double amount) {
        double currentBalance = getBalance(playerUUID);
        setBalance(playerUUID, currentBalance + amount);
    }
}