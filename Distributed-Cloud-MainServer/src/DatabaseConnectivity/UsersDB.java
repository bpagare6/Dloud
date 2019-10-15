/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class UsersDB {

    private final DBConnection dbconnection;
    private final Connection connection;

    public UsersDB() {
        dbconnection = new DBConnection();
        connection = dbconnection.getConnection();
    }

    public boolean validate_user(String username, String password) {
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select password from Users where username='" + username + "'");

            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
