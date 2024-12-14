package jp.cyberhub.cyberecon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;

    // 引数付きコンストラクタ
    public Database(String host, int port, String database, String user, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true";
        this.user = user;
        this.password = password;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        initializeTables();
    }

    private void initializeTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS economy (" +
                "player_uuid VARCHAR(36) PRIMARY KEY, " +
                "balance DOUBLE NOT NULL DEFAULT 0)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

