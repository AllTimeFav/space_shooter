package Records;

import javax.swing.*;
import java.sql.*;

public class Record {

    final String DB_URL = "jdbc:mysql://localhost:3306/humans";
    final String USER = "root";
    final String PASS = "JavaDB123";

    private int PK = 0;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public boolean checkName(String name) throws SQLException {
        String findQuery = "SELECT Player_Name FROM Player WHERE Player_Name = ?";
        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(findQuery)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void RegisterPlayer(String name) {
        String query = "INSERT INTO Player(Player_ID, Player_Name) VALUES(" + PK + ", '" + name + "')";

        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            PK++;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}