package me.wcash.admintrollmc.database;

import me.wcash.admintrollmc.AdminTrollMC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Database {

    private String dbPath;
    private final Connection dbcon;
    private final AdminTrollMC atmc;

    public Database(String dbName) throws SQLException {
        atmc = AdminTrollMC.getPlugin();
        dbPath = (atmc.getDataFolder() + "/" + dbName);
        dbPath = "jdbc:sqlite:" + dbPath;
        dbcon = DriverManager.getConnection(dbPath);

        // Check if the table exists and update its structure if necessary
        updateTableStructure("player",
                "CREATE TABLE player (name TEXT NOT NULL, uuid TEXT NOT NULL, " +
                        "isFrozen BIT NOT NULL, frozenTimeLeft INT NOT NULL, " +
                        "isBurning BIT NOT NULL, burningTimeLeft INT NOT NULL" +
                        "isDontStopJumping BIT NOT NULL, dontStopJumpingTimeLeft INT NOT NULL" +
                        "isConfused BIT NOT NULL, confusedTimeLeft INT NOT NULL" +
                        "isDeafened BIT NOT NULL, deafenedTimeLeft INT NOT NULL" +
                        "isBlind BIT NOT NULL, blindTimeLeft INT NOT NULL" +
                        ")");
    }

    public String getDbPath() { return dbPath; }

    public boolean testConnection() {
        try {
            PreparedStatement stmt = dbcon.prepareStatement("SELECT name FROM player");
            stmt.executeQuery();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /* Player Methods */

    public boolean doesPlayerExistInDatabase(UUID uuid) {
        try {
            PreparedStatement stmt = dbcon.prepareStatement("SELECT name FROM player WHERE uuid=?");
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            atmc.error("Error fetching name for user " + getName(uuid) + "!");
            atmc.error("Error Message: " + e.getMessage());
            return false;
        }
    }

    public void insertPlayer(String name, UUID uuid) {
        try {
            PreparedStatement stmt = dbcon.prepareStatement("INSERT INTO player(name, uuid, isFrozen, frozenTimeLeft, isBurning, burningTimeLeft, isDontStopJumping, dontStopJumpingTimeLeft, isConfused, confusedTimeLeft, isDeafened, deafenedTimeLeft, isBlind, blindTimeLeft) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            // Set Name and UUID
            stmt.setString(1, uuid.toString());
            stmt.setString(2, name);

            // Set other values to defaults
            stmt.setBoolean(3, false);
            stmt.setInt(4, 0);
            stmt.setBoolean(5, false);
            stmt.setInt(6, 0);
            stmt.setBoolean(7, false);
            stmt.setInt(8, 0);
            stmt.setBoolean(9, false);
            stmt.setInt(10, 0);
            stmt.setBoolean(11, false);
            stmt.setInt(12, 0);
            stmt.setBoolean(13, false);
            stmt.setInt(14, 0);

            // Execute the query
            stmt.execute();
        } catch (SQLException e) {
            atmc.error("Error inserting user " + getName(uuid) + " into database!");
            atmc.error("Error Message: " + e.getMessage());
        }
    }

    public void updatePlayerUsername(String name, UUID uuid) {
        try {
            PreparedStatement stmt = dbcon.prepareStatement("UPDATE player SET name = ? WHERE uuid=?");
            stmt.setString(1, name);
            stmt.setString(2, uuid.toString());

            stmt.execute();
        } catch (SQLException e) {
            atmc.error("Error updating username for " + getName(uuid) + "!");
            atmc.error("Error Message: " + e.getMessage());
        }
    }

    /* Player Attribute Methods */

    public <T> T getAttribute(UUID uuid, String attribute, Class<T> type) {
        try {
             PreparedStatement stmt = dbcon.prepareStatement("SELECT ? FROM player WHERE uuid=?");

             stmt.setString(1, attribute);
             stmt.setString(2, uuid.toString());

             ResultSet rs = stmt.executeQuery();

             if (rs.next()) {
                 var value = rs.getObject(attribute);
                 if (type.isInstance(value)) {
                     return type.cast(value);
                 } else {
                     // Handle cases where key doesn't exist or value cannot be cast to type
                     // For simplicity, you can throw an exception here or return a default value
                     throw new IllegalArgumentException("Invalid key or incompatible type");
                 }
             } else {
                 return null;
             }
        } catch (SQLException e) {
            atmc.error("Error getting attribute " + attribute + " for user " + getName(uuid) + "!");
            atmc.error("Error Message: " + e.getMessage());
            return null;
        }
    }

    public void setAttribute(UUID uuid, String attribute, Object value) {
        try {
            PreparedStatement stmt = dbcon.prepareStatement("UPDATE player SET ? = ? WHERE uuid=?");
            stmt.setString(1, attribute);
            stmt.setObject(2, value);
            stmt.setString(3, uuid.toString());

            stmt.execute();
        } catch (SQLException e) {
            atmc.error("Error setting attribute " + attribute + " for user " + getName(uuid) + "!");
            atmc.error("Error Message: " + e.getMessage());
        }
    }

    /* Private Methods */

    private void updateTableStructure(String tableName, String createTableQuery) throws SQLException {
        DatabaseMetaData meta = dbcon.getMetaData();
        ResultSet rs = meta.getTables(null, null, tableName, null);

        if (rs.next()) {
            // Table exists
            ResultSet columns = meta.getColumns(null, null, tableName, null);
            List<String> existingColumns = new ArrayList<>();

            while (columns.next()) {
                existingColumns.add(columns.getString("COLUMN_NAME"));
            }

            // Check for columns to add
            try (Statement stmt = dbcon.createStatement()) {
                String[] createTableParts = createTableQuery.split("\\(");
                String columnsPart = createTableParts[1];
                columnsPart = columnsPart.substring(0, columnsPart.length() - 1); // Remove trailing ')'
                String[] requiredColumns = columnsPart.split(",");

                for (String requiredColumn : requiredColumns) {
                    requiredColumn = requiredColumn.trim().split("\\s+")[0]; // Get only the column name
                    if (!existingColumns.contains(requiredColumn)) {
                        // Column doesn't exist, add it
                        stmt.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + requiredColumn);
                        if (atmc.getConfigValue("debug", Boolean.class)) atmc.debug("Added column '" + requiredColumn + "' to table '" + tableName + "'.");
                    }
                }

                // Check for columns to remove
                for (String existingColumn : existingColumns) {
                    if (!createTableQuery.contains(existingColumn)) {
                        // Column exists in the table but not in the required structure, remove it
                        stmt.executeUpdate("ALTER TABLE " + tableName + " DROP COLUMN " + existingColumn);
                        if (atmc.getConfigValue("debug", Boolean.class)) atmc.debug("Removed column '" + existingColumn + "' from table '" + tableName + "'.");
                    }
                }
            }
        } else {
            // Table does not exist, create it
            if (atmc.getConfigValue("debug", Boolean.class)) atmc.debug("Creating table '" + tableName + "'...");
            try (Statement stmt = dbcon.createStatement()) {
                stmt.executeUpdate(createTableQuery);
                if (atmc.getConfigValue("debug", Boolean.class)) atmc.debug("Table '" + tableName + "' created successfully.");
            }
        }
    }


    private String getName(UUID minecraftID) {
        return Objects.requireNonNull(atmc.getServer().getPlayer(minecraftID)).getName();
    }

}