/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
            ResultSet rs = statement.executeQuery("select password from AllUsers where username='" + username + "'");

            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean add_user(String name, String email, String username, String password) {
        try {
            PreparedStatement prepared = connection.prepareStatement("insert into AllUsers values (?,?,?,?)");
            prepared.setString(1, name);
            prepared.setString(2, email);
            prepared.setString(3, username);
            prepared.setString(4, password);
            prepared.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;   
    }

}
