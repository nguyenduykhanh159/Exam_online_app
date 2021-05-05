/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject.take_infor_std;

import hust.soict.hedspi.examonlineproject.database.DBConnect;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author DuyKhanh
 */
public class CheckLogin {
    public int checkLogin(String student_id, String password) {
        int check = 0;
        PreparedStatement ps;
        ResultSet rs;
        
        String query = "select * from account where student_id = '"+student_id+"' and password = '"+password+"'";
        
        try {
            ps = DBConnect.getConnection().prepareStatement(query);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                check = 1;           
            } else {
                check = 0;
            }
            ps.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CheckLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return check;
    }
}