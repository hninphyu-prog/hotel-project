/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import model.Department;
import model.User;
import database.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class Add_DepController implements Initializable {

    @FXML
    private Label lblid;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtname;
     @FXML
    private Label lbl;

    /**
     * Initializes the controller class.
     */
    Connection con;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    ObservableList<Department> DepartmentList;
    private Department dep;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
      
        try {
            con = db.getConnection();
            
            
            txtname.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.trim().isEmpty()) {
                    txtname.setStyle(" "); // Reset to green when not empty
                    lbl.setText(" ");
                } else {
                    txtname.setStyle("-fx-border-color: red;"); // Still red if cleared
                    lbl.setText("*Deparment name required*");
                                  
                }});
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Add_DepController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

   @FXML
private void handleUpdateAction(ActionEvent event) {
    String depName = txtname.getText().trim();

    if (depName.isEmpty()) {
        txtname.setStyle("-fx-border-color: red;");
        lbl.setText("*Deparment name required*");
       // JOptionPane.showMessageDialog(new JFrame(), "Please Fill Department Name!", "Error",
                 //   JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        String sql = "INSERT INTO department(department_id, department_name) VALUES(?, ?)";
        pst = con.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(lblid.getText()));
        pst.setString(2, depName);

        int rows = pst.executeUpdate();
        if (rows > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Department added successfully!");
            alert.showAndWait();
        }

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();

    } catch (Exception e) {
        Logger.getLogger(Add_DepController.class.getName()).log(Level.SEVERE, "Failed to insert department", e);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to add department");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}



    @FXML
    private void handleCancelAction(ActionEvent event) {
         txtname.clear();
         Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
         stage.close(); // Close the modal dialog
    }
    
    
    public void setConnection(Connection con) {
    this.con = con;
    }
  public void initializeDepartments() {
    try {
        String sql = "SELECT MAX(department_id) AS max_id FROM department";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();
        int nextId = 1; // Default if no departments exist
        if (rs.next()) {
            nextId = rs.getInt("max_id") + 1;
        }
        lblid.setText(String.valueOf(nextId));
    } catch (Exception e) {
        lblid.setText("Error");
        Logger.getLogger(Add_DepController.class.getName()).log(Level.SEVERE, "Failed to load department ID", e);
    }
}

}
