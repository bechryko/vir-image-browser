package org.example.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public record UserModel (String username, boolean isAdmin) {

    public static UserModel fromCurrentResultSetEntry(ResultSet resultSet) throws SQLException {
        return new UserModel(resultSet.getString("username"), resultSet.getBoolean("is_admin"));
    }

}
