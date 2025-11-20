package org.example.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtils {

    public static final String CONNECTION_URL = "jdbc:sqlite:db/sqlite.db";

    public static final String USER_CREATION_QUERY = "INSERT INTO users (username, password) VALUES (?, ?)";
    public static final String USER_CREATION_ADMIN_ROLE_QUERY = "INSERT INTO user_roles (username, role_name) VALUES (?, \"admin\")";

    public static final String PERMISSION_GIVE_QUERY = "INSERT INTO user_roles (username, role_name) VALUES (?, ?)";
    public static final String PERMISSION_EXISTENCE_CHECK_QUERY = "SELECT COUNT(*) FROM roles_permissions WHERE permission = ?";
    public static final String PERMISSION_CREATION_QUERY = "INSERT INTO roles_permissions (role_name, permission) VALUES (?, ?)";

    public static final String PERMISSION_REVOKE_QUERY = "DELETE FROM user_roles WHERE username = ? AND role_name = ?";

    public static final String USER_MODEL_LIST_QUERY = """
            SELECT 
                users.username,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM user_roles
                        WHERE user_roles.username = users.username
                          AND user_roles.role_name = \"admin\"
                    )
                    THEN true
                    ELSE false
                END AS is_admin
            FROM users
            """;

    public static final String ROLE_LIST_QUERY = "SELECT role_name FROM user_roles WHERE username = ?";

    public static final String USERS_TABLE_CREATION_QUERY = """
            CREATE TABLE IF NOT EXISTS users (
              id integer PRIMARY KEY AUTOINCREMENT,
              username text NOT NULL,
              password text NOT NULL,
              UNIQUE (username)
            )
            """;
    public static final String USER_ROLES_TABLE_CREATION_QUERY = """
            CREATE TABLE IF NOT EXISTS user_roles (
              id integer PRIMARY KEY AUTOINCREMENT,
              username text NOT NULL,
              role_name text NOT NULL
            )
            """;
    public static final String ROLES_PERMISSIONS_TABLE_CREATION_QUERY = """
            CREATE TABLE IF NOT EXISTS roles_permissions (
              id integer PRIMARY KEY AUTOINCREMENT,
              role_name text NOT NULL,
              permission text NOT NULL
            )
            """;
    public static final String ADMIN_ROLE_EXISTENCE_CHECK_QUERY = "SELECT COUNT(*) FROM roles_permissions WHERE role_name = \"admin\"";
    public static final String ADMIN_ROLE_CREATION_QUERY = "INSERT INTO roles_permissions (role_name, permission) VALUES (\"admin\", \"*\")";

    public static void executeStatement(Connection connection, String query, String param1) throws SQLException {
        PreparedStatement adminStatement = connection.prepareStatement(query);
        adminStatement.setString(1, param1);
        adminStatement.executeUpdate();
        adminStatement.close();
    }

    public static void executeStatement(Connection connection, String query, String param1, String param2) throws SQLException {
        PreparedStatement adminStatement = connection.prepareStatement(query);
        adminStatement.setString(1, param1);
        adminStatement.setString(2, param2);
        adminStatement.executeUpdate();
        adminStatement.close();
    }

}
