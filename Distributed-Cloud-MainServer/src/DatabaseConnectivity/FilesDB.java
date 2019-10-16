package DatabaseConnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bhushan
 */
public class FilesDB {

    private final DBConnection dbconnection;
    private final Connection connection;

    public FilesDB() {
        dbconnection = new DBConnection();
        connection = dbconnection.getConnection();
    }

    public List<String> get_file_list() {
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Files");
            List<String> fileList = new ArrayList<>();

            while (rs.next()) {
                fileList.add(rs.getString("filename"));
            }
            return fileList;
        } catch (SQLException ex) {
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean checkIfExist(String filename) {
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Files");

            while (rs.next()) {
                if (filename.equals(rs.getString("filename"))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FilesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void addFile(String filename, String owner) {
        if (!checkIfExist(filename)) {
            try (PreparedStatement ps = connection.prepareStatement("insert into Files values(?, ?)")) {
                ps.setString(1, filename);
                ps.setString(2, owner);
                ps.executeUpdate();
                System.out.println("File added");
            } catch (SQLException ex) {
                Logger.getLogger(FilesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
