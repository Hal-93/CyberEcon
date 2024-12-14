package jp.cyberhub.cyberecon;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberEcon extends JavaPlugin {
    private Database database;
    private EconManager econManager;

    @Override
    public void onEnable() {
        // デフォルトの config.yml を作成
        saveDefaultConfig();

        // config.yml からデータベース情報を取得
        FileConfiguration config = getConfig();
        String host = config.getString("database.host");
        int port = config.getInt("database.port");
        String dbName = config.getString("database.name");
        String user = config.getString("database.user");
        String password = config.getString("database.password");

        // データベースインスタンス作成時に情報を渡す
        database = new Database(host, port, dbName, user, password);
        try {
            database.connect();
            getLogger().info("データベースに接続しました！");
        } catch (Exception e) {
            getLogger().severe("データベースに接続できませんでした！");
            e.printStackTrace();
            return;
        }

        econManager = new EconManager(database);

        // コマンド登録
        getCommand("balance").setExecutor(new EconCommand(econManager));
        getCommand("addmoney").setExecutor(new EconCommand(econManager));
    }



    @Override
    public void onDisable() {
        try {
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

