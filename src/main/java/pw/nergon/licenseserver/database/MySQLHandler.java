package pw.nergon.licenseserver.database;

import pw.nergon.licenseclient.LicenseClient;
import pw.nergon.licenseserver.LicenseServer;
import pw.nergon.licenseserver.log.Logger;

import java.sql.*;

public class MySQLHandler {

    private Connection connection;

    public MySQLHandler() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            LicenseServer.instance.getLogger().log("Trying to connect to Database");
            String host = LicenseServer.instance.getConfigInfo().getDbHost();
            String database = LicenseServer.instance.getConfigInfo().getDbName();
            String user = LicenseServer.instance.getConfigInfo().getDbUser();
            String passwd = LicenseServer.instance.getConfigInfo().getDbPass();
            String connectionCommand = "jdbc:mysql://"+host+"/"+database+"?useSSL=false";
            connection = DriverManager.getConnection(connectionCommand, user, passwd);
            LicenseServer.instance.getLogger().logSuccess("Connected to Database");
            LicenseServer.instance.getLogger().log("Trying to create Tables");
            createTables();
        } catch (Exception e) {
            LicenseServer.instance.getLogger().logError("Could no connect to Database");
            e.printStackTrace();
        }
    }

    public void createTables() {
        try {
            Statement statement = connection.createStatement();
            //Create Licenses Table
            statement.execute("CREATE TABLE IF NOT EXISTS licenses ( " +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "licensekey VARCHAR(15) NOT NULL, " +
                        "product_name VARCHAR(30) NOT NULL, " +
                        "uses INT, " +
                        "date_added DATE NOT NULL, " +
                        "banned TINYINT(1) DEFAULT 0, " +
                        "PRIMARY KEY(id)" +
                    ")");
            // Create Uses Table
            statement.execute("CREATE TABLE IF NOT EXISTS uses ( " +
                    "id INT NOT NULL AUTO_INCREMENT , " +
                    "license_id INT NOT NULL , " +
                    "client_ip VARCHAR(45) NOT NULL , " +
                    "client_mac VARCHAR(17) NOT NULL , " +
                    "banned TINYINT(1) NOT NULL , " +
                    "PRIMARY KEY (id)" +
                    ")");

        } catch (SQLException e) {
            LicenseServer.instance.getLogger().logError("Could not Create Tables");
            e.printStackTrace();
        }
    }

    public boolean checkLicense(String key, String ip, String mac) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT  * FROM  licenses LEFT JOIN uses ON licenses.id = uses.license_id WHERE licensekey = ?  ");
            statement.setString(1, key);
            ResultSet resultSet = statement.executeQuery();
            int rowCount = getRowCount(resultSet);
            if (rowCount == 0)
                return false;

            resultSet.first();
            if(resultSet.getInt("licenses.banned") != 0)
                return false;

            while (resultSet.next()) {
                if (resultSet.getString("client_ip").equals(ip) && resultSet.getString("client_mac").equals(mac)) {
                    return (resultSet.getInt("uses.banned") == 0);
                }
            }
            resultSet.first();
            if (rowCount == resultSet.getInt("uses")) {
                return false;
            }
            createNewUse(ip, mac, resultSet.getInt("licenses.id"));
            return true;
        } catch (SQLException e) {
            LicenseServer.instance.getLogger().logError("Could not check License");
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getRowCount(ResultSet set) {
        int rows = 0;
        try {
            set.last();
            rows = set.getRow();
            set.beforeFirst();
        } catch (SQLException e) {
            return 0;
        }
        return rows;
    }

    public void createNewUse(String ip, String mac, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO uses (license_id, client_ip, client_mac, banned) VALUES (?,?,?,0)");
            statement.setInt(1, id);
            statement.setString(2, ip);
            statement.setString(3, mac);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
