/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.hedspi.examonlineproject.take_infor_std;

import hust.soict.hedspi.examonlineproject.database.DBConnect;
import hust.soict.hedspi.examonlineproject.entity.Student;
import java.sql.*;
/**
 *
 * @author DuyKhanh
 */
public class TakeInfor {
    public static Student takeInfor(String student_id) {
        Student std = new Student();
        PreparedStatement ps;
        ResultSet rs;
        
        String query = "select * from student where student_id = '"+student_id+"'";
        try {
            ps = DBConnect.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                std.setStudent_id(rs.getString("student_id"));
                std.setName(rs.getString("name"));
                std.setClazz(rs.getString("class"));
            } else {
                std = null;
            }
            ps.close();
        } catch (SQLException e) {
            java.util.logging.Logger.getLogger(TakeInfor.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        
        return std;
    }
}
