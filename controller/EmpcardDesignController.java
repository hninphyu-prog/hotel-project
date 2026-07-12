package controller;

import model.Employee;
import database.Database;
import java.io.File;
import java.io.IOException;
import java.io.InputStream; // Added for loading from classpath
import java.net.MalformedURLException; // Added for URL handling
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class EmpcardDesignController implements Initializable {

    @FXML
    private AnchorPane cardForm;
    @FXML
    private ImageView empImage;
    @FXML
    private Label lblID;
    @FXML
    private Label lblName;
    @FXML
    private Label LblPosition;
    @FXML
    private Button btnEdit;

    @FXML
    private Button btnView;
    @FXML
    private Label LblDepartment;
    @FXML
    private Label lblStatus;

    private Employee emp;
    private Connection con = null;

    // Define a constant for the directory where uploaded images will be stored
    // This should be consistent with where EmployeeDetailModalController saves images.
    private static final String UPLOADED_IMAGE_DIR = "C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Database db = new Database();
        try {
            con = db.getConnection();
            if (con == null) {
                System.err.println("❌ Database connection is null in EmpcardDesignController. Initialize failed.");
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("DB connection error: " + ex.getMessage());
            Logger.getLogger(EmpcardDesignController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public void setData(Employee emp) {



        this.emp = emp;

        System.out.println("setData called for employee: " + emp.getEmpname());
        System.out.println("Position ID: " + emp.getPos_id() + ", Department ID: " + emp.getDep_id() + ", Image Name: " + emp.getImage_name());

        lblID.setText(String.valueOf(emp.getEid()));
        lblName.setText(emp.getEmpname());

        if (con != null) {
            LblPosition.setText(getPositionNameById(emp.getPos_id()));
            LblDepartment.setText(getDepartmentNameById(emp.getDep_id()));
        } else {
            LblPosition.setText("Unknown Position");
            LblDepartment.setText("Unknown Department");
        }

        lblStatus.setText(emp.getStatus() != null ? emp.getStatus() : "Unknown");

        File file = new File("C:\\Users\\USER\\Documents\\NetBeansProjects\\JavaFXApplication4\\src\\image\\" + emp.getImage_name());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            empImage.setImage(image);
        }
        empImage.setPreserveRatio(true);
    }

   
    private void setDefaultImage() {
        // Ensure you have a 'default.png' or 'default.jpg' in your src/main/resources/image folder
        URL defaultUrl = getClass().getResource("/image/default.jpg"); // Use .jpg as per your other controller
        if (defaultUrl != null) {
            empImage.setImage(new Image(defaultUrl.toExternalForm()));
            System.out.println("✅ Loaded default image from classpath.");
        } else {
            System.err.println("❌ Default image (default.jpg) not found in /image/ folder!");
            // Optionally, clear the image or set a blank one if no default can be loaded
            empImage.setImage(null);
        }
    }

    public void setConnection(Connection con) {
        this.con = con;
        if (this.con == null) {
            System.out.println("❌ Warning: Connection is null when set in EmpcardDesignController!");
        }
    }

    private String getPositionNameById(int posId) {
        if (con == null) {
            return "DB Error"; // Indicate that DB connection is problematic
        }

        String sql = "SELECT position_name FROM position WHERE position_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, posId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("position_name");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching position name for ID " + posId + ": " + e.getMessage());
        }
        return "Unknown";
    }

    private String getDepartmentNameById(int depId) {
        if (con == null) {
            return "DB Error"; // Indicate that DB connection is problematic
        }

        String sql = "SELECT department_name FROM department WHERE department_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, depId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("department_name");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching department name for ID " + depId + ": " + e.getMessage());
        }
        return "Unknown";
    }

    @FXML
    private void handlebtnViewAction(ActionEvent event) {
        if (emp == null) {
            System.out.println("No employee set for this card to view.");
            return;
        }
        try {
            String sql = "SELECT * FROM employee WHERE employee_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, emp.getEid());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Construct Employee object with all details needed by EmpViewModalController
                // Ensure all fields from your database schema are correctly mapped
                Employee fullEmp = new Employee(
                    rs.getInt("employee_id"), // Assuming Eid is int
                    rs.getString("employee_name"),
                    rs.getString("nrc"),
                    rs.getString("gender"),
                    rs.getString("DOB"),
                    rs.getString("phone_no"),
                    rs.getString("email"),
                    rs.getInt("position_id"),
                    rs.getInt("department_id"),
                    rs.getInt("salary"), // Changed to getDouble as salary is likely double
                    rs.getString("hire_date"),
                    rs.getString("leave_date"),
                    rs.getString("Image"), // Make sure "Image" is the correct column name
                    rs.getString("Status"),
                    rs.getString("Address")
                );
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmpViewModal.fxml"));
                Parent root = loader.load();
                EmpViewModalController controller = loader.getController();
                controller.setEmployeeData(fullEmp);  // IMPORTANT


                /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmpViewModal.fxml"));
                Parent root = loader.load();

                EmpViewModalController controller = loader.getController();
                controller.setEmployeeData(fullEmp);*/

                Stage stage = new Stage();
                stage.setTitle("Employee Details - " + fullEmp.getEmpname());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                System.out.println("No employee found in DB with ID: " + emp.getEid());
            }

        } catch (SQLException e) {
            System.err.println("Database error during view action: " + e.getMessage());
            Logger.getLogger(EmpcardDesignController.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            System.err.println("Error loading EmpViewModal.fxml: " + e.getMessage());
            Logger.getLogger(EmpcardDesignController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        if (emp == null) {
            System.out.println("No employee set for this card to edit.");
            return;
        }
        try {
            String sql = "SELECT * FROM employee WHERE employee_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, emp.getEid());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Construct Employee object with all details needed by EmployeeDetailModalController
                // Ensure all fields from your database schema are correctly mapped
                Employee fullEmp = new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("employee_name"),
                    rs.getString("nrc"),
                    rs.getString("gender"),
                    rs.getString("DOB"),
                    rs.getString("phone_no"),
                    rs.getString("email"),
                    rs.getInt("position_id"),
                    rs.getInt("department_id"),
                    rs.getInt("salary"), // Changed to getDouble
                    rs.getString("hire_date"),
                    rs.getString("leave_date"),
                    rs.getString("Image"), // Make sure "Image" is the correct column name
                    rs.getString("Status"),
                    rs.getString("Address")
                );

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeDetailModal.fxml"));
                Parent root = loader.load();

                EmployeeDetailModalController controller = loader.getController();
                controller.setEmployeeData(fullEmp);

                Stage stage = new Stage();
                stage.setTitle("Edit Employee - " + fullEmp.getEmpname());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                System.out.println("No employee found in DB with ID: " + emp.getEid());
            }

        } catch (SQLException e) {
            System.err.println("Database error during edit action: " + e.getMessage());
            Logger.getLogger(EmpcardDesignController.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            System.err.println("Error loading EmployeeDetailModal.fxml: " + e.getMessage());
            Logger.getLogger(EmpcardDesignController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
