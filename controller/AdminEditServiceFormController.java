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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Service;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AdminEditServiceFormController implements Initializable {

    @FXML
    private Label lblServiceId;
   
    @FXML
    private Label lblCurrentPrice;
    @FXML
    private RadioButton rd_Available;
    @FXML
    private ToggleGroup gender;
    @FXML
    private RadioButton rd_NotAvailable;
    @FXML
    private TextField txtNewPrice;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnUpdate;
    @FXML
    private Label lblClothName;
    @FXML
    private Label lblServiceType;
    
    private Service service;
    @FXML
    private Label lbl_error_message;
    
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
            Database db =new Database();
            con = db.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminEditServiceFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void handleCancelAction(ActionEvent event) {
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) throws SQLException {
        if(txtNewPrice.getText().isEmpty()){
            lbl_error_message.setText("enter new price");
            lbl_error_message.setVisible(true);
        }else{
            String pattern="[0-9]{1,}";
            if(txtNewPrice.getText().matches(pattern)&& Integer.parseInt(txtNewPrice.getText())!=0){
                int newPrice = Integer.parseInt(txtNewPrice.getText());
                service.setService_price(newPrice);
                int status= 0;
                String newStatus="Not Available";
                if(rd_Available.isSelected()){
                    status =1;
                    newStatus= "Available";
                }
                service.setStatus(newStatus);
                
                String sql = """
                         UPDATE laundary_service SET base_price=? , status=? WHERE service_id=?
                         """;
                pst =con.prepareStatement(sql);
                pst.setInt(1, newPrice);
                pst.setInt(2, status);
                pst.setInt(3, Integer.parseInt(lblServiceId.getText()));
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Updated successfully");
                Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
                modalStage.close();
            }else{
                if(Integer.parseInt(txtNewPrice.getText())==0){
                    lbl_error_message.setText("price can not be zero");
                    lbl_error_message.setVisible(true);
                }else{
                    lbl_error_message.setText("enter only number for service price");
                    lbl_error_message.setVisible(true);
                }
                
            }
            
            
        }
        
    }
    
    public void setData(Service s){
        service =s;
        lblServiceType.setText(service.getService_name());
        lblServiceId.setText(String.valueOf(service.getService_id()));
        lblClothName.setText(service.getCloth_name());
        
        if(service.getStatus().equals("Available")){
            rd_Available.setSelected(true);
        }else{
            rd_NotAvailable.setSelected(true);
        }
        lblCurrentPrice.setText(String.valueOf(service.getService_price()));
        txtNewPrice.setText(String.valueOf(service.getService_price()));
        
    }
    public  Service getData(){
        return service;
    }
    
}
