/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject;

import hust.soict.hedspi.examonlineproject.database.DBConnect;
import hust.soict.hedspi.examonlineproject.frame.LoginFrame;
import java.sql.*;

/**
 *
 * @author DuyKhanh
 */
public class App {
    public static void main(String[] args) throws SQLException {
        LoginFrame.runFrame();
        
        DBConnect.getConnection().close();
    }
}
