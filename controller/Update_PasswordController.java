/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import model.Employee;
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
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import utils.PasswordUtil; // Assuming this class has hashPassword method

/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class Update_PasswordController implements Initializable {

    @FXML
    private Button btnCancle;
    @FXML
    private Button btnConfirm;
    @FXML
    private PasswordField txtcurrentpasswotd;
    @FXML
    private PasswordField txtconfirmnewpassword;
    @FXML
    private PasswordField txtnewpassword;
    Connection con=null;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    private Employee emp;
    String user_name="Hotel"; // This should ideally be dynamically set based on the logged-in user
   // A private instance variable to hold the employee_id
    private int employee_id; 
    
    // A setter method to receive the employee ID from the calling controller
    public void setEmployeeId(int id) {
        this.employee_id = id;
        System.out.println("Update_PasswordController received employee_id: " + this.employee_id);
    }

    /**
     * Initializes the controller class.
     * Establishes a database connection when the controller is initialized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        try {
            con = db.getConnection();
        } catch (ClassNotFoundException ex) {
            // Log the exception if the database driver is not found
            Logger.getLogger(Update_PasswordController.class.getName()).log(Level.SEVERE, "Database driver not found.", ex);
            JOptionPane.showMessageDialog(null, "Error: Database driver not found. Please check your setup.");
        } catch (Exception ex) {
            // Catch any other exceptions during connection
            Logger.getLogger(Update_PasswordController.class.getName()).log(Level.SEVERE, "Failed to connect to database.", ex);
            JOptionPane.showMessageDialog(null, "Error: Failed to connect to database. " + ex.getMessage());
        }
    }    

    /**
     * Handles the action when the "Cancel" button is clicked.
     * Clears all password fields.
     * @param event The ActionEvent that triggered this method.
     */
    @FXML
    private void handlebtnCanAction(ActionEvent event) {
        // Clear all password fields
        txtcurrentpasswotd.clear();
        txtnewpassword.clear();
        txtconfirmnewpassword.clear();
         Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        //Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        //stage.close(); // Close the modal dialog
    
    }

    /**
     * Handles the action when the "Confirm" button is clicked.
     * Validates input, verifies the current password, hashes the new password,
     * and updates it in the database.
     * @param event The ActionEvent that triggered this method.
     */
    @FXML
    private void handlebtnConfirmAction(ActionEvent event) {
        String currentPassword = txtcurrentpasswotd.getText();
        String newPassword = txtnewpassword.getText();
        String confirmPassword = txtconfirmnewpassword.getText();

        // Input validation: Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "New passwords do not match.");
            return;
        }

        // Input validation: Ensure password fields are not empty
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All password fields must be filled.");
            return;
        }

        // Database operations
        try {
            // Step 1: Check current password
            // Retrieve the hashed password from the database for the given employee
            String checkQuery = "SELECT password FROM user WHERE employee_id = ?";
            pst = con.prepareStatement(checkQuery);
            pst.setInt(1, employee_id);
            //pst.setString(2, user_name);
            rs = pst.executeQuery();

            if (rs.next()) {
                String dbHashedPassword = rs.getString("password"); // This is the hash stored in the DB

                // Hash the current password entered by the user
                String hashedEnteredCurrentPassword = PasswordUtil.hashPassword(currentPassword);

                // Compare the hashed entered current password with the hashed password from the database
                // This is the secure way to verify the current password
                if (!dbHashedPassword.equals(hashedEnteredCurrentPassword)) {
                    JOptionPane.showMessageDialog(null, "Current password is incorrect.");
                    return; // Exit if current password is wrong
                }
            } else {
                // This case indicates that the employee (user_name and employee_id) was not found
                // or there's an issue with the query/data.
                JOptionPane.showMessageDialog(null, "Employee not found or unauthorized.");
                return;
            }

            // Step 2: Hash the new password before updating it in the database
            String hashedNewPassword = PasswordUtil.hashPassword(newPassword);

            // Step 3: Update to new hashed password
            String updateQuery = "UPDATE user SET password = ? WHERE employee_id = ?";
            pst = con.prepareStatement(updateQuery);
            pst.setString(1, hashedNewPassword); // Store the new HASHED password
            pst.setInt(2,employee_id);
            //pst.setString(3, user_name);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Password updated successfully.");
                // Clear fields upon successful update
                txtcurrentpasswotd.clear();
                txtnewpassword.clear();
                txtconfirmnewpassword.clear();
                
            } else {
                JOptionPane.showMessageDialog(null, "Password update failed. No records were updated.");
            }

        } catch (SQLException e) {
            // Log SQL exceptions for debugging
            Logger.getLogger(Update_PasswordController.class.getName()).log(Level.SEVERE, "SQL Error updating password", e);
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            Logger.getLogger(Update_PasswordController.class.getName()).log(Level.SEVERE, "An unexpected error occurred during password update", e);
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
        } finally {
            // Ensure database resources are closed in a finally block to prevent resource leaks
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                // Only close the connection if it was opened in this method or if it's no longer needed globally.
                // If 'con' is a shared connection, it might be better to leave it open until the app exits.
                // For simplicity in this example, we'll close it.
                if (con != null && !con.isClosed()) con.close(); 
            } catch (SQLException ex) {
                Logger.getLogger(Update_PasswordController.class.getName()).log(Level.SEVERE, "Error closing database resources", ex);
            }
        }
    }
}
