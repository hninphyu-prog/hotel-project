/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;
import utils.PasswordUtil;

import model.Employee;
import database.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class CreatAccountController implements Initializable {

    @FXML
    private TextField txteid;
    @FXML
    private TextField txtname;
    @FXML
    private TextField txtrole;
    @FXML
    private TextField txtpassword;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancle;
    @FXML
    private PasswordField Tpassword;
    @FXML
    private CheckBox chkShowPassword;
     @FXML
    private Label lblpassword;
    
    
    Connection con=null;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    private Employee emp;
    private static final String PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            Database db = new Database();
        try {
           con = db.getConnection();
           
           txteid.setEditable(false);
           txtname.setEditable(false);
           txtrole.setEditable(false);
           

        // Bind text fields
        txtpassword.textProperty().bindBidirectional(Tpassword.textProperty());

        // Initially show PasswordField only
        txtpassword.setVisible(false);
        txtpassword.setManaged(false);
        setupTextFormatter(Tpassword,txtpassword, PASSWORD_PATTERN, lblpassword, "Please Use Strong Password.");


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreatAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    } 
    // Helper method for TextFormatter setup
    
    private void setupTextFormatter(PasswordField passwordField ,TextField textField,String pattern, Label errorLabel, String errorMessage) {
        errorLabel.setVisible(false); // Hide error label initially
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(pattern) || newText.isEmpty()) {
                errorLabel.setVisible(false);
                textField.setBorder(null); // Reset border
                passwordField.setBorder(null);
                return change;
            } else {
                errorLabel.setText(errorMessage);
                errorLabel.setVisible(true);
                textField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2))));
                passwordField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2))));
                return change; // Allow typing but show error
            }
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
        passwordField.setTextFormatter(new TextFormatter<>(filter));
        
        
    }


    public void setEmployee(Employee emp) {
        this.emp = emp;
        showData();
    }

    private void showData() {
        this.emp = emp;
        if (emp != null) {
            txteid.setText(String.valueOf(emp.getEid()));
            txtname.setText((String) emp.getName());
            txtrole.setText(getPositionNameById(emp.getPos_id()));
        }
    }
    private String getPositionNameById(int posId) {
    String positionName = "Unknown";
    String sql = "SELECT position_name FROM position WHERE position_id = ?";

    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, posId);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            positionName = rs.getString("position_name");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return positionName;
}

   
    
    @FXML
    private void handleShowPasswordAction(ActionEvent event) {
    boolean show = chkShowPassword.isSelected();

    // Toggle visibility
    txtpassword.setVisible(show);
    txtpassword.setManaged(show);

    Tpassword.setVisible(!show);
    Tpassword.setManaged(!show);
}
    private void clear() {
    txteid.clear();
    txtname.clear();
    txtrole.clear();
    Tpassword.clear();
    txtpassword.clear();
}
  @FXML
    void handleCancleAction(ActionEvent event) {
        clear();
        
    }

 
    @FXML
    private void handleOKAction(ActionEvent event) {
    if (txtpassword.getText().length() < 8 || txtpassword.getText().length() > 20) {
        JOptionPane.showMessageDialog(null, "Password must be 8–20 characters long.");
        return;
    }
    else  if (!txtpassword.getText().matches(".*[A-Z].*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one uppercase letter.");
        return;
    }

    else if (!txtpassword.getText().matches(".*[a-z].*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one lowercase letter.");
        return ;
    }

    else if (!txtpassword.getText().matches(".*\\d.*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one digit.");
        return ;
    }

    else if (!txtpassword.getText().matches(".*[@#$%^&+=!].*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one special character (@#$%^&+=!).");
        return ;
    }

    /*else if (!txtpassword.getText().matches(" ")) {
        JOptionPane.showMessageDialog(null, "Password must not contain spaces.");
        return; 
    } */
    else{
        
  
    int eid = Integer.parseInt(txteid.getText());
    String name = txtname.getText();
    String role = txtrole.getText();
    String password = PasswordUtil.hashPassword(txtpassword.getText()); // 🔐 hash the password
  //  String password = txtpassword.getText();
    String status="Active";

    // Check if fields are empty
    if ( name.isEmpty() || role.isEmpty() || password.isEmpty()) {
        System.out.println("Please fill all fields.");
        return;
    }

    // Save to database
    try {
        
        String hashedPassword = PasswordUtil.hashPassword(txtpassword.getText());

String sql = "INSERT INTO user (employee_id, user_name, password, user_role, status) VALUES (?, ?, ?, ?, ?)";
pst = con.prepareStatement(sql);
pst.setInt(1, eid);
pst.setString(2, name);
pst.setString(3, hashedPassword); // 🔐 save hashed
pst.setString(4, role);
pst.setString(5, status);

      

     
    

        int rowsInserted = pst.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("User account created successfully!");
        }
        

        pst.close();
        con.close();
        clear();
        Stage modalStage =(Stage)btnOk.getScene().getWindow();
                    modalStage.close();


    
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
  
     
    public void loadEmployeeData(int eid) {
      
    try {
        if (con == null || con.isClosed()) {
            Database db = new Database();
            con = db.getConnection();
        }


    

        String sql = "SELECT employee_id,employee_name, position_id FROM employee WHERE employee_id = ?";
      PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, eid);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("employee_id");
            String name = rs.getString("employee_name");
            int posId = rs.getInt("position_id");
            String posName = getPositionNameById(posId);
            // Show on UI
            txteid.setText(String.valueOf(id));
            txtname.setText(name);
            txtrole.setText(posName);
           
        } else {
            System.out.println("Employee not found.");
        }

        rs.close();
        pst.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


        
    

   /* @FXML
    private void handleCancleAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Employee.fxml"));
        Parent root = fxmlLoader.load();

        // Get current stage from event source
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }
    private boolean validatePassword(String password) {
    if (password.length() < 8 || password.length() > 20) {
        JOptionPane.showMessageDialog(null, "Password must be 8–20 characters long.");
        return false;
    }

    if (!password.matches(".*[A-Z].*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one uppercase letter.");
        return false;
    }

    if (!password.matches(".*[a-z].*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one lowercase letter.");
        return false;
    }

    if (!password.matches(".*\\d.*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one digit.");
        return false;
    }

    if (!password.matches(".*[@#$%^&+=!].*")) {
        JOptionPane.showMessageDialog(null, "Password must contain at least one special character (@#$%^&+=!).");
        return false;
    }

    if (password.contains(" ")) {
        JOptionPane.showMessageDialog(null, "Password must not contain spaces.");
        return false;
    }

    return true;
}*/

    
}
