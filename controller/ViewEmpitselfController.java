/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import model.Employee;
import database.Database;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class ViewEmpitselfController implements Initializable {

    @FXML
    private ImageView EmployeeImg;
    @FXML
    private Label lblid;
    @FXML
    private Label lblname;
    @FXML
    private Label lbldepartment;
    @FXML
    private Label lblposition;
    @FXML
    private Button btnchangepassword;

    /**
     * Initializes the controller class.
     */
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    private Employee emp;
    String user_name="Hotel"; // This should ideally be dynamically set based on the logged-in user
   private int employee_id; 
    private static final String UPLOADED_IMAGE_DIR = "C:\\Users\\USER\\Documents\\NetBeansProjects\\JavaFXApplication4\\src\\image\\";

 

    
public void setEmployeeId(int id) throws IOException {
    this.employee_id = id;
    loadEmployeeData();
}


    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Database db = new Database();
        
        try {
            con = db.getConnection();
            
            // TODO
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewEmpitselfController.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
    }
    
    private void loadEmployeeData() throws IOException {
    String query = "SELECT e.employee_id, e.employee_name, e.Image, d.department_name, p.position_name " +
                    "FROM employee e " +
                    "JOIN department d ON e.department_id = d.department_id  " +
                    "JOIN position p ON e.position_id = p.position_id  " +
                    " WHERE e.employee_id = ?;";

    try (PreparedStatement pst = con.prepareStatement(query)) {
        pst.setInt(1, employee_id);
        rs = pst.executeQuery();

        if (rs.next()) {
            lblid.setText(String.valueOf(rs.getInt("employee_id")));
            lblname.setText(rs.getString("employee_name"));
            lbldepartment.setText(rs.getString("department_name"));
            lblposition.setText(rs.getString("position_name"));
            String imageFileName = rs.getString("Image"); // e.g., "hotel.jpg"
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File file = new File(UPLOADED_IMAGE_DIR + imageFileName);
                if (file.exists()) {
                    EmployeeImg.setImage(new Image(file.toURI().toURL().toString()));
                    System.out.println("✔ Image loaded: " + file.getAbsolutePath());
                } else {
                    System.out.println("✘ Image file not found: " + file.getAbsolutePath());
                }
            } else {
                System.out.println("✘ No image filename in database");
            }
         
          
        } else {
                System.out.println("DEBUG: No employee found with ID: " + employee_id);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error loading employee data:");
            e.printStackTrace();
        }
    }

        
       
       
   @FXML
private void handleChangePasswordAction(ActionEvent event) {
     Stage modalStage =(Stage)btnchangepassword.getScene().getWindow();
                    modalStage.close();

}
}