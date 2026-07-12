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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AddServiceAdminFormController implements Initializable {

    @FXML
    private TextField txtServicePrice;
    @FXML
    private Label lblClothName;
    @FXML
    private ComboBox<String> combo_service_type;
    @FXML
    private Label lblServiceId;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAdd;
    @FXML
    private Label lbl_error_message;
    private int cloth_id;
    private String clothName;
    
    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    
    ObservableList<String> service_type = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db = new Database();
            con = db.getConnection();
            String sql = "select count(*) as total from laundary_service";
            st =con.createStatement();
            rs =st.executeQuery(sql);
            if(rs.next()){
                lblServiceId.setText(String.valueOf(rs.getInt("total")+1));
            }
            String sql2 = "select service_type from laundary_service_type";
            st = con.createStatement();
            rs = st.executeQuery(sql2);
            while(rs.next()){
                service_type.add(rs.getString("service_type"));
            }
            combo_service_type.setItems(service_type);
            combo_service_type.setValue(service_type.get(0));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddServiceAdminFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddServiceAdminFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage modalStage = (Stage)btnCancel.getScene().getWindow();
        modalStage.close();
    }

    @FXML
    private void handleAddaction(ActionEvent event) throws SQLException {
        if(txtServicePrice.getText().isEmpty()){
            lbl_error_message.setText("please enter service price");
            lbl_error_message.setVisible(true);
        }else{
            String pattern="[0-9]{1,}";
            if(txtServicePrice.getText().matches(pattern)&& Integer.parseInt(txtServicePrice.getText())!=0){
                int price = Integer.parseInt(txtServicePrice.getText());
                String ServiceType = combo_service_type.getValue();
                String sql = """
                             SELECT * FROM laundary_service,laundary_service_type WHERE laundary_service.service_type_id=laundary_service_type.service_type_id 
                             AND laundary_service_type.service_type=? and cloth_id=?;
                             """;
                pst = con.prepareStatement(sql);
                pst.setString(1, ServiceType);
                pst.setInt(2, cloth_id);
                rs= pst.executeQuery();
                if(rs.next()){
                    JOptionPane.showMessageDialog(null, ServiceType +" of service of this "+clothName+" type is already existed");
                }else{
                    int service_type_id =0;
                     sql = "select service_type_id from laundary_service_type where service_type=?";
                     pst = con.prepareStatement(sql);
                     pst.setString(1, ServiceType);
                     rs =pst.executeQuery();
                     if(rs.next()){
                        service_type_id= rs.getInt("service_type_id");
                     }
                     sql = "insert into laundary_service values(?,?,?,?,?)";
                     pst =con.prepareStatement(sql);
                     pst.setInt(1, Integer.parseInt(lblServiceId.getText()));
                     pst.setInt(2, service_type_id);
                     pst.setInt(3, cloth_id);
                     pst.setInt(4, price);
                     pst.setString(5, "1");
                     pst.executeUpdate();
                     JOptionPane.showMessageDialog(null,"service added successfully");
                     Stage modalStage = (Stage)btnAdd.getScene().getWindow();
                     modalStage.close();
                     
                    
                }
                
                
                
            }else{
                if(Integer.parseInt(txtServicePrice.getText())==0){
                    lbl_error_message.setText("price can not be zero");
                    lbl_error_message.setVisible(true);
                }else{
                    lbl_error_message.setText("enter only number for service price");
                    lbl_error_message.setVisible(true);
                }
            }
        }
        
    }
    
    public void setData(int id,String name){
        cloth_id=id;
        clothName=name;
        lblClothName.setText(clothName);
    }
    
}
