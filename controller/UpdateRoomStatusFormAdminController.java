/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.AdminRoomController;
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
import model.Rooms;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class UpdateRoomStatusFormAdminController implements Initializable {

    @FXML
    private Label lblRoomNo;
    @FXML
    private TextField txtCurrentStatus;
    @FXML
    private TextField txtNewStatus;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnUpdate;
    private Rooms room;
    int newStatus ;
    int index;
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
            con= db.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UpdateRoomStatusFormAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage modalStage = (Stage)btnCancel.getScene().getWindow();
        modalStage.close();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) throws SQLException {
        
        room.setStatus(txtNewStatus.getText());
        AdminRoomController.roomList.set(index, room);
        String sql = "update room set status=? where room_id=?";
        pst =con.prepareStatement(sql);
        pst.setInt(1, newStatus);
        pst.setInt(2, Integer.parseInt(room.getRoomno()));
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "room status update successfully");
        Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
        modalStage.close();
        
    }
    
    public void setData(int i,Rooms r){
        room =r;
        index =i;
        lblRoomNo.setText(String.valueOf(room.getRoomno()));
        if(room.getStatus().equals("Available")){
            txtCurrentStatus.setText("Available");
            txtNewStatus.setText("Not Available");
            newStatus=0;
        }else{
            txtCurrentStatus.setText("Not Available");
            txtNewStatus.setText("Available");
            newStatus=1;
        }
    }
    
}
