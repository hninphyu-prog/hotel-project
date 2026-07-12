/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
/**
 *
 * @author Dell
 */

public class Database {
        Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    
    public Connection getConnection() throws ClassNotFoundException{
     Connection con=null;
        String  username="root";
        String password="sabelsan-2005";
        String url="jdbc:mysql://localhost:3306/myat_hotel";
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Load Drivder....");
        try {
            con=DriverManager.getConnection(url,username,password);
            System.out.println("Database Connected");
        } catch (SQLException ex) {
            System.out.println("not connected");
        }
        return con;
    }
    
   
}
