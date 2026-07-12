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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import utils.PasswordUtil;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ResetPasswordController implements Initializable {

    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;
    @FXML
    private PasswordField Tpassword;
    @FXML
    private TextField txtpassword;
    @FXML
    private CheckBox checkpassword;
    @FXML
    private Label lblid;
    @FXML
    private Label lblname;

    /**
     * Initializes the controller class.
     */
    private User u;
    String newpassword="11111111";
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
         Database db = new Database();
       
        try {
            con = db.getConnection();
             // Bind text fields
        txtpassword.textProperty().bindBidirectional(Tpassword.textProperty());

        // Initially show PasswordField only
        txtpassword.setVisible(false);
        txtpassword.setManaged(false);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ResetPasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
          
       

    }
  // Add this to AController
private ObservableList<User> userList;

public void setUserList(ObservableList<User> list) {
    this.userList = list;
}
public void setData(int i, User u) {
    this.u = u;
    this.index = i;
    lblid.setText(String.valueOf(u.getEmployee_id()));
    lblname.setText(u.getUser_name());
    Tpassword.setText(newpassword);
    
}

@FXML
private void handleUpdateAction(ActionEvent event) throws SQLException {
    String renewpassword = Tpassword.getText();

    // Hash the password before saving
   // String hashedPassword = hashPassword(renewpassword);
    String hashedPassword = PasswordUtil.hashPassword(txtpassword.getText()); // 🔐 hash the password

    // Optional: update the user object in memory if needed
    // u.setPassword(hashedPassword); // if such method exists

    userList.set(index, u);

    String sql = "UPDATE user SET password = ? WHERE employee_id = ?";
    pst = con.prepareStatement(sql);
    pst.setString(1, hashedPassword);
    pst.setInt(2, u.getEmployee_id());
    pst.executeUpdate();

    JOptionPane.showMessageDialog(null, "Password updated successfully!");
    Stage modalStage = (Stage) btnUpdate.getScene().getWindow();
    modalStage.close();
}

    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close(); // Close the modal dialog
    }

    @FXML
    private void handlecheckpasswordAction(ActionEvent event) {
    
    boolean show = checkpassword.isSelected();

    // Toggle visibility
    txtpassword.setVisible(show);
    txtpassword.setManaged(show);

    Tpassword.setVisible(!show);
    Tpassword.setManaged(!show);
}
    }
    

