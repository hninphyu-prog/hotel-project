/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.Database;
import hotel_management.Hotel_Management;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.Scene;
import utils.PasswordUtil;
/**
 * FXML Controller class
 *
 * @author Dell
 */
public class LoginFormController implements Initializable {
    Stage stage;
    Parent root;
   @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lbUsernameHidden;
    @FXML
    private Label lbPasswordHidden;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbUsernameHidden.setVisible(false);
        lbPasswordHidden.setVisible(false);
        // Add a listener to the username text field
        txtUsername.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Show the label if text is present, hide it if the text field is empty
                lbUsernameHidden.setVisible(!newValue.isEmpty());
            }
        });
        
        // Add a listener to the password text field
        txtPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Show the label if text is present, hide it if the text field is empty
                lbPasswordHidden.setVisible(!newValue.isEmpty());
            }
        });
    }    

  // LoginFormController.java
@FXML
private void handleBtnLoginAction(ActionEvent event) throws ClassNotFoundException, SQLException {
    String inputUsername = txtUsername.getText();
    String inputPassword = txtPassword.getText();

    if (inputUsername.isEmpty()) {
        showMessage("Validation Error", "Please enter your username.");
        return;
    }

    if (inputPassword.isEmpty()) {
        showMessage("Validation Error", "Please enter your password.");
        return;
    }

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        String hashedPassword = PasswordUtil.hashPassword(inputPassword);
        Database db = new Database();
        conn = db.getConnection();

        // Corrected SQL query to retrieve employee_id and user_role
        String sql = "SELECT employee_id, user_role FROM user WHERE BINARY user_name = ? AND BINARY password = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, inputUsername);
        ps.setString(2, hashedPassword);

        rs = ps.executeQuery();

        if (rs.next()) {
            // Login successful
            int employeeId = rs.getInt("employee_id");
            String userRole = rs.getString("user_role");

            // Store the user's information in the UserSession class
            utils.UserSession.setLoggedInUser(employeeId, inputUsername, userRole);

            // Redirect based on the user's role
            String fxmlFile;
            if ("Admin".equalsIgnoreCase(userRole)) {
                fxmlFile = "/view/AdminPage.fxml"; // Path to the Admin dashboard FXML file
            } else if ("Staff".equalsIgnoreCase(userRole)) {
                fxmlFile = "/view/StaffPage.fxml"; // Path to the Staff dashboard FXML file
            } else {
                showMessage("Login Failed", "Unknown user role.");
                return;
            }

            // Load the new dashboard and switch the scene
            try {
                root = FXMLLoader.load(getClass().getResource(fxmlFile));
                stage = (Stage) txtUsername.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Navigation Error", "Could not load the dashboard page.");
            }
        } else {
            // Login failed
            showMessage("Login Failed", "Invalid username or password.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        showMessage("Database Error", "An error occurred while connecting to the database.");
    } finally {
        // Close resources to prevent memory leaks
        if (rs != null) try { rs.close(); } catch (SQLException e) { /* Ignored */ }
        if (ps != null) try { ps.close(); } catch (SQLException e) { /* Ignored */ }
        if (conn != null) try { conn.close(); } catch (SQLException e) { /* Ignored */ }
    }
}

     private void showMessage(String title, String content) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
}
