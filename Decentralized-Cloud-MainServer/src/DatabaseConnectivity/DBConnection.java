/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectivity;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author bhushan
 */
public class DBConnection {

    private Connection connection;
    private String driver;
    private String url;
    private String username;
    private String password;

    public DBConnection() {
        driver = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/dloud";
        username = "root";
        password = "Bhushan@24";
    }

    public Connection getConnection() {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

}
