package com.lielamar.minestore.shared.storage.mysql;

import com.lielamar.minestore.shared.storage.StorageHandler;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLStorage extends StorageHandler {

    private Connection connection;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;

    public MySQLStorage(String host, String database, String username, String password, int port) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;

        try { openConnection(); }
        catch(SQLException | ClassNotFoundException e) { e.printStackTrace(); }
    }


    /**
     * Opens a MySQL connection
     *
     * @throws SQLException             Throws an exception if couldn't connect to the sql database
     * @throws ClassNotFoundException   Throws an exception if the Driver class wasn't found
     */
    private void openConnection() throws SQLException, ClassNotFoundException {
        if(isValidConnection())
            throw new IllegalStateException("A MySQL instance already exists for the following database: " + this.database);

        synchronized(this) {
            if(isValidConnection()) throw new IllegalStateException("A MySQL instance already exists for the following database: " + this.database);

            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", this.host, this.port, this.database), this.username, this.password);
            this.createTables();
            System.out.println("[Minestore Debug] Connected to the MySQL Database successfully");
        }
    }

    /**
     * Creates the required tables on the database
     *
     * @throws SQLException   Throws an exception if couldn't connect to the sql database
     */
    private void createTables() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS packages (id VARCHAR(128) PRIMARY KEY, name VARCHAR(255), description VARCHAR(255), price DOUBLE(20,2), unit_limit INT)";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "CREATE TABLE IF NOT EXISTS purchases (id INT AUTO_INCREMENT PRIMARY KEY, purchase_id VARCHAR(255), package_id VARCHAR(128), price DOUBLE(20,2), player_ign VARCHAR(16), player_uuid VARCHAR(36), buyer_name VARCHAR(255), buyer_email VARCHAR(255), "
                + "buyer_address1 VARCHAR(255), buyer_address2 VARCHAR(255), buyer_city VARCHAR(255), buyer_zipcode INT(10), buyer_state VARCHAR(255), buyer_country VARCHAR(255), delivered INT(1))";
        stmt = this.connection.prepareStatement(query);
        stmt.executeUpdate();

        query = "CREATE TABLE IF NOT EXISTS commands (package_id VARCHAR(128), command VARCHAR(255), server VARCHAR(255))";
        stmt = this.connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    /**
     * Checks if the connection is valid (not null & open)
     *
     * @return                Whether or not the connection is valid
     * @throws SQLException   Throws an exception if the connection is not available
     */
    public boolean isValidConnection() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }


    @Override
    public int isPackageDelivered(String purchaseId) {
        try {
            if(!isValidConnection()) return 1;

            PreparedStatement statement = this.connection.prepareStatement("SELECT `delivered` FROM purchases WHERE `purchase_id` = ?;");
            statement.setString(1, purchaseId);
            ResultSet result = statement.executeQuery();

            if(result.next()) {
                return result.getInt("delivered");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public boolean setPackageDelivered(String purchaseId, boolean delivered) {
        int deliveredRaw = (delivered) ? 1 : 0;

        try {
            if(!isValidConnection())
                return false;

            PreparedStatement statement = this.connection.prepareStatement("UPDATE purchases SET `delivered` = ? WHERE `purchase_id` = ?;");
            statement.setInt(1, deliveredRaw);
            statement.setString(2, purchaseId);
            ResultSet result = statement.executeQuery();

            if(result.next())
                return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getPackageId(String purchaseId) {
        try {
            if(!isValidConnection()) return null;

            PreparedStatement statement = this.connection.prepareStatement("SELECT `package_id` FROM purchases WHERE `purchase_id` = ?;");
            statement.setString(1, purchaseId);
            ResultSet result = statement.executeQuery();

            if(result.next()) {
                return result.getString("package_id");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBuyerIGN(String purchaseId) {
        try {
            if(!isValidConnection()) return null;

            PreparedStatement statement = this.connection.prepareStatement("SELECT `player_ign` FROM purchases WHERE `purchase_id` = ?;");
            statement.setString(1, purchaseId);
            ResultSet result = statement.executeQuery();

            if(result.next()) {
                return result.getString("player_ign");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Map<String, String> getCommandsOfPackage(String packageId) {
        try {
            if(!isValidConnection()) return null;

            PreparedStatement statement = this.connection.prepareStatement("SELECT `command`, `server` FROM commands WHERE `package_id` = ?;");
            statement.setString(1, packageId);
            ResultSet result = statement.executeQuery();

            Map<String, String> commandServer = new HashMap<>();
            while(result.next()) {
                commandServer.put(result.getString("command"), result.getString("server"));
            }

            return commandServer;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String getPackageName(String packageId) {
        try {
            if(!isValidConnection()) return null;

            PreparedStatement statement = this.connection.prepareStatement("SELECT `name` FROM packages WHERE `id` = ?;");
            statement.setString(1, packageId);
            ResultSet result = statement.executeQuery();

            if(result.next())
                return result.getString("name");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}