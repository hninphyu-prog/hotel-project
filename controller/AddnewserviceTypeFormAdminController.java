/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AddnewserviceTypeFormAdminController implements Initializable {

    @FXML
    private Label lblServiceTypeId;
    @FXML
    private TextField txtServiceType;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAdd;
    @FXML
    private Label lblServiceTypeAlert;
    
     //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db = new Database();
            con =db.getConnection();
            String sql= "select count(*) as total from laundary_service_type";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                lblServiceTypeId.setText(String.valueOf(rs.getInt("total")+1));
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddnewserviceTypeFormAdminController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddnewserviceTypeFormAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage modalStage = (Stage)btnCancel.getScene().getWindow();
            modalStage.close();
    }

    @FXML
    private void handleAddAction(ActionEvent event) throws SQLException {
        boolean complete= true;
        if(txtServiceType.getText().isEmpty()){
            lblServiceTypeAlert.setText("enter new service type");
            lblServiceTypeAlert.setVisible(true);
            complete=false;
        }
        if(complete){
            String sql = "insert into laundary_service_type values(?,?)";
            pst =con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(lblServiceTypeId.getText()));
            pst.setString(2, txtServiceType.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"new service type added successfully");
            Stage modalStage = (Stage)btnAdd.getScene().getWindow();
            modalStage.close();
        }
    }
    
}
