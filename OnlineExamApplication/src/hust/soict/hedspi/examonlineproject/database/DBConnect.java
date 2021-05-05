/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject.database;

import java.sql.*;

/**
 *
 * @author DuyKhanh
 */
public class DBConnect {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project_jobfair", "postgres", "duykhanhng");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connect Failure");
            System.out.println(e.getMessage());
        }
        
        return con;
    }
}
