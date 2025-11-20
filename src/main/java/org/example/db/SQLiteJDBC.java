package org.example.db;

import org.example.models.UserModel;
import java.sql.*;
import java.util.*;

public class SQLiteJDBC {

    private final Connection connection;

    public SQLiteJDBC() {
        try {
            connection = DriverManager.getConnection(SQLUtils.CONNECTION_URL);

            setupDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createUser(String username, String hashedPassword, boolean isAdmin) {
        try {
            SQLUtils.executeStatement(connection, SQLUtils.USER_CREATION_QUERY, username, hashedPassword);

            if(isAdmin) {
                SQLUtils.executeStatement(connection, SQLUtils.USER_CREATION_ADMIN_ROLE_QUERY, username);
            } else {
                givePermission(username, "png");
                givePermission(username, "jpg");
            }

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public void givePermission(String username, String permission) {
        try {
            SQLUtils.executeStatement(connection, SQLUtils.PERMISSION_GIVE_QUERY, username, permission);

            PreparedStatement permissionExistenceCheckStatement = connection.prepareStatement(SQLUtils.PERMISSION_EXISTENCE_CHECK_QUERY);
            permissionExistenceCheckStatement.setString(1, permission);
            ResultSet result = permissionExistenceCheckStatement.executeQuery();
            result.next();
            if(Objects.equals(result.getString(1), "0")) {
                SQLUtils.executeStatement(connection, SQLUtils.PERMISSION_CREATION_QUERY, permission, permission);
            }
            permissionExistenceCheckStatement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void revokePermission(String username, String permission) {
        try {
            SQLUtils.executeStatement(connection, SQLUtils.PERMISSION_REVOKE_QUERY, username, permission);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<UserModel> listUsers() {
        List<UserModel> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLUtils.USER_MODEL_LIST_QUERY);
            while (resultSet.next()) {
                users.add(UserModel.fromCurrentResultSetEntry(resultSet));
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return users;
    }

    public List<String> listPermissionsForUser(String username) {
        List<String> permissions = new ArrayList<>();

        try {
            PreparedStatement roleListStatement = connection.prepareStatement(SQLUtils.ROLE_LIST_QUERY);
            roleListStatement.setString(1, username);
            ResultSet resultSet = roleListStatement.executeQuery();
            while (resultSet.next()) {
                permissions.add(resultSet.getString("role_name"));
            }
            roleListStatement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return permissions;
    }

    public void setupDatabase() {
        try {
            int changedRows = 0;

            Statement statement = connection.createStatement();
            changedRows += statement.executeUpdate(SQLUtils.USERS_TABLE_CREATION_QUERY);
            changedRows += statement.executeUpdate(SQLUtils.USER_ROLES_TABLE_CREATION_QUERY);
            changedRows += statement.executeUpdate(SQLUtils.ROLES_PERMISSIONS_TABLE_CREATION_QUERY);

            ResultSet result = statement.executeQuery(SQLUtils.ADMIN_ROLE_EXISTENCE_CHECK_QUERY);
            result.next();
            if(Objects.equals(result.getString(1), "0")) {
                changedRows += statement.executeUpdate(SQLUtils.ADMIN_ROLE_CREATION_QUERY);
            }

            statement.close();

            if(changedRows > 0) {
                System.out.println("Database setup successful! Rows changed: " + changedRows);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void main() throws Exception {
        var jdbc = new SQLiteJDBC();

        System.out.println(jdbc.listPermissionsForUser("admin2"));
    }

}
