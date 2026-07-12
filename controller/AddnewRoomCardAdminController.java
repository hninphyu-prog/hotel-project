/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.RoomType;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AddnewRoomCardAdminController implements Initializable {

    @FXML
    private Label lblRoomId;
    @FXML
    private Label lblRoomTypeName;
    @FXML
    private TextField txtFloorNo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;
    private RoomType roomType;
    Connection con;
    ResultSet rs;
    Statement st;
    PreparedStatement   pst;
    @FXML
    private Label lblFloorNoAlert;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db =new Database();
            con = db.getConnection();
            String sql = "select count(*) as total from room";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                lblRoomId.setText(String.valueOf(rs.getInt("total")+1));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddnewRoomCardAdminController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddnewRoomCardAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleAddAction(ActionEvent event) throws SQLException {
        lblFloorNoAlert.setVisible(false);
        boolean complete = true;
        String pattern="[0-9]{1,}";
        if(txtFloorNo.getText().isEmpty()){
            lblFloorNoAlert.setText("enter floor no");
            lblFloorNoAlert.setVisible(true);
            complete=false;
        }else if(!txtFloorNo.getText().matches(pattern)){
            lblFloorNoAlert.setText("floor no can only be number");
            lblFloorNoAlert.setVisible(true);
            complete=false; 
        }else if(Integer.parseInt(txtFloorNo.getText())==0){
            lblFloorNoAlert.setText("floor no can not be zero");
            lblFloorNoAlert.setVisible(true);
            complete=false; 
        }
        if(complete){
            JOptionPane.showMessageDialog(null, "all right");
            String sql = "insert into room values(?,?,?,?)";
            pst= con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(lblRoomId.getText()));
            pst.setInt(2, roomType.getRoom_type_id());
            pst.setInt(3, Integer.parseInt(txtFloorNo.getText()));
            pst.setInt(4, 1);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "new room added successfully");
            Stage modalStage = (Stage)btnAdd.getScene().getWindow();
            modalStage.close();
        }
        
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage modalStage = (Stage)btnCancel.getScene().getWindow();
            modalStage.close();
    }
    
    public void setData(RoomType type){
        roomType = type;
        lblRoomTypeName.setText(roomType.getRoom_type());
    }
    
}
