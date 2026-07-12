/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import model.User;
import database.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * @author MITUSER-2
 */
public class AController implements Initializable {

    @FXML
    private TextField txtcurrentstatus;
    @FXML
    private TextField txtnewstatus;
    @FXML
    private Button btncancel;
    @FXML
    private Button btnupdate;
    @FXML
    private Label lblid;

    /**
     * Initializes the controller class.
     */
    private User u;
    int newStatus ;
    int index;
    Connection con;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    //ObservableList<User> UserList;
    // In ViewUserDataController.java
public static ObservableList<User> UserList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db=new Database();
        try {
            con= db.getConnection();
        } catch (ClassNotFoundException ex) {
        }
    }  
    // Add this to AController
private ObservableList<User> userList;

public void setUserList(ObservableList<User> list) {
    this.userList = list;
}


    @FXML
    private void handlecancelaction(ActionEvent event) {
        Stage modalStage = (Stage)btncancel.getScene().getWindow();
        modalStage.close();
    
    }

  
    public void setData(int i, User u) {
    this.u = u;
    this.index = i;
    lblid.setText(String.valueOf(u.getEmployee_id()));
    String status = u.getStatus();
    txtcurrentstatus.setText(status);

    if (status.equalsIgnoreCase("Active")) {
        txtnewstatus.setText("InActive");
    } else {
        txtnewstatus.setText("Active");
    }
}

@FXML
private void handleupdateaction(ActionEvent event) throws SQLException {
    String newStatusText = txtnewstatus.getText();
    u.setStatus(newStatusText);
    userList.set(index, u);


    String dbStatus = newStatusText.equalsIgnoreCase("Active") ?"Active" :"InActive";

    String sql = "UPDATE user SET status = ? WHERE employee_id = ?";
    pst = con.prepareStatement(sql);
    pst.setString(1, dbStatus);
    pst.setInt(2, u.getEmployee_id());
    pst.executeUpdate();

    JOptionPane.showMessageDialog(null, "Status updated successfully");
    Stage modalStage = (Stage) btnupdate.getScene().getWindow();
    modalStage.close();
}

    
}
