package pw.nergon.licenseserver;

import pw.nergon.licenseserver.config.ConfigHandler;
import pw.nergon.licenseserver.config.ConfigInfo;
import pw.nergon.licenseserver.database.MySQLHandler;
import pw.nergon.licenseserver.log.Logger;
import pw.nergon.licenseserver.network.ConnectionHandler;

import java.util.concurrent.CountDownLatch;

public class LicenseServer {

    public static LicenseServer instance;
    private Logger logger;
    private ConfigHandler configHandler;
    private MySQLHandler handler;
    private ConnectionHandler connectionHandler;


    private LicenseServer() {
        instance = this;
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        logger = new Logger();
        configHandler = new ConfigHandler();
        handler = new MySQLHandler();
        connectionHandler = new ConnectionHandler();
    }

    //Shutdown Hook to close Files and Connections correctly
    private void shutdown() {
        getLogger().log("Closing connections and exiting program");
        handler.close();
    }

    public Logger getLogger() {
        return logger;
    }

    public MySQLHandler getHandler() {
        return handler;
    }

    public ConfigInfo getConfigInfo() {
        return configHandler.getLoadedConfig();
    }

    public static void main(String[] args) {
        new LicenseServer();
    }

}
