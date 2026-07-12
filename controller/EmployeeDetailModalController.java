package controller;

import model.Employee;
import database.Database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files; // ADDED
import java.nio.file.Path;  // ADDED
import java.nio.file.Paths; // ADDED
import java.nio.file.StandardCopyOption; // ADDED
import java.sql.Connection;
import java.sql.PreparedStatement; // Explicitly import PreparedStatement
import java.sql.ResultSet; // Explicitly import ResultSet
import java.sql.SQLException;
import java.sql.Statement; // Explicitly import Statement
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
// import javafxapplication4.JavaFXApplication4; // Removed if not directly used/needed here

public class EmployeeDetailModalController implements Initializable {

    // Constant for the directory where uploaded employee images are stored
    private static final String UPLOADED_IMAGE_DIR = "C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\";
    @FXML private ImageView imgview;
    @FXML private TextField txtname, txtgender, txtNrc, txtEmail, txtEid;
    @FXML private TextField txtphno, txtAddress, txtSalary, txtHireDate, txtLeaveDate;
    @FXML private Button btnCancle, btnUpdate;
    @FXML private ComboBox<String> comstatus;
    @FXML private ComboBox<String> comPosition;
    @FXML private ComboBox<String> comDepartment;

    @FXML private DatePicker datedob;
    

    private Connection con;
    private ObservableList<String> comstatusList = FXCollections.observableArrayList("Active", "InActive");
    private ObservableList<String> positionList = FXCollections.observableArrayList();
    private ObservableList<String> departmentList = FXCollections.observableArrayList();
    private Map<String, Integer> positionMap = new HashMap<>();
    private Map<String, Integer> departmentMap = new HashMap<>();

    // Stores the absolute path of the image selected by the user via FileChooser
    private String selectedImagePath = null;

    // Stores the current employee's image filename as it exists in the DB (or after a new file is copied)
    private String currentEmployeeImageName = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        try {
            con = db.getConnection();
            if (con == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database. Please check your connection.");
                return;
            }
            comstatus.setItems(comstatusList);
            loadPositionData();
            loadDepartmentData();

        } catch (ClassNotFoundException ex) {
            System.err.println("DB connection error: " + ex); // Use err for errors
            showAlert(Alert.AlertType.ERROR, "Application Error", "Database driver not found. Please contact support.");
        }

        comstatus.setOnAction(e -> {
            String selectedStatus = comstatus.getValue();
            if ("InActive".equals(selectedStatus)) {
                txtLeaveDate.setText(LocalDate.now().toString());
                // Disable status ComboBox to prevent manual changes after selecting InActive
                // However, be cautious: if user selects Inactive, saves, then wants to re-activate,
                // this might cause issues. Consider enabling it back when loading "Active" employee.
                // For now, let's just keep it simple.
                // comstatus.setDisable(true); 
            } else {
                txtLeaveDate.clear();
                // comstatus.setDisable(false);
            }
        });

        // Load default image initially using the robust loadImage method
        System.out.println(">> Initializing EmployeeDetailModalController, loading default image.");
        loadImage("default.jpg"); // This will now correctly load from classpath
    }

    private void loadPositionData() {
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT position_id, position_name FROM position")) { // Corrected SQL
            
            positionList.clear();
            positionMap.clear();

            while (rs.next()) {
                int id = rs.getInt("position_id");
                String name = rs.getString("position_name");
                positionList.add(name);
                positionMap.put(name, id);
            }
            comPosition.setItems(positionList);

        } catch (SQLException e) {
            System.err.println("❌ Failed to load positions: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load positions: " + e.getMessage());
        }
    }

    private void loadDepartmentData() {
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT department_id, department_name FROM department")) { // Corrected SQL
            
            departmentList.clear();
            departmentMap.clear();

            while (rs.next()) {
                int id = rs.getInt("department_id");
                String name = rs.getString("department_name");
                departmentList.add(name);
                departmentMap.put(name, id);
            }
            comDepartment.setItems(departmentList);

        } catch (SQLException e) {
            System.err.println("❌ Failed to load departments: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load departments: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancleAction(ActionEvent event) {
        Stage modalStage = (Stage) btnCancle.getScene().getWindow();
        modalStage.close();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        // Basic validation
        if (txtEid.getText().isEmpty() || txtname.getText().isEmpty() || comPosition.getValue() == null || comDepartment.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all required fields (ID, Name, Position, Department).");
            return;
        }

        String imageFileNameForDb = currentEmployeeImageName; // Start with the currently loaded image name

        // If a new image was selected via FileChooser
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            File sourceFile = new File(selectedImagePath);
            if (sourceFile.exists()) {
                // Ensure the target directory exists
                File uploadDir = new File(UPLOADED_IMAGE_DIR);
                if (!uploadDir.exists()) {
                    if (!uploadDir.mkdirs()) { // Create directory if it doesn't exist
                        System.err.println("❌ Failed to create image upload directory: " + uploadDir.getAbsolutePath());
                        showAlert(Alert.AlertType.ERROR, "File System Error", "Could not create image storage directory.");
                        return; // Abort update if directory can't be created
                    }
                }

                Path destinationPath = Paths.get(UPLOADED_IMAGE_DIR, sourceFile.getName());
                
                try {
                    // Copy the selected file to your designated upload directory
                    Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    imageFileNameForDb = sourceFile.getName(); // This is the filename to save in the DB
                    System.out.println("✅ Image copied to: " + destinationPath.toAbsolutePath());
                } catch (IOException e) {
                    System.err.println("❌ Failed to copy image file '" + sourceFile.getName() + "': " + e.getMessage());
                    showAlert(Alert.AlertType.ERROR, "Image Save Error", "Could not save the selected image. Please try again.");
                    // Keep the old image name in DB if copying failed, or set to null if that's desired behavior on failure
                    // For now, we will proceed with the value of `imageFileNameForDb` from before this block.
                }
            } else {
                System.out.println("⚠️ Selected image file does not exist at preview path: " + selectedImagePath);
                // If the file selected for preview somehow doesn't exist anymore, keep existing DB image name.
            }
        }
        // If selectedImagePath is null, it means no new image was picked.
        // `imageFileNameForDb` will correctly retain `currentEmployeeImageName`.
        // If `imgview.getImage()` is null and `currentEmployeeImageName` was also null/empty,
        // then `imageFileNameForDb` will correctly remain null, resulting in DB update to NULL.


        try {
            String sql = "UPDATE employee SET "
            + "employee_name = ?, nrc = ?, gender = ?, DOB = ?, phone_no = ?, email = ?, "
            + "address = ?, position_id = ?, department_id = ?, salary = ?, hire_date = ?, leave_date = ?, Status = ?, Image = ? "
            + "WHERE employee_id = ?";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                Integer selectedPositionId = positionMap.get(comPosition.getValue());
                Integer selectedDepartmentId = departmentMap.get(comDepartment.getValue());

                stmt.setString(1, txtname.getText());
                stmt.setString(2, txtNrc.getText());
                stmt.setString(3, txtgender.getText());
                LocalDate dob = datedob.getValue();
                stmt.setString(4, dob != null ? dob.toString() : null);

                stmt.setString(5, txtphno.getText());
                stmt.setString(6, txtEmail.getText());
                stmt.setString(7, txtAddress.getText());
                stmt.setInt(8, selectedPositionId != null ? selectedPositionId : 0); // Handle null, adjust default if 0 is not valid
                stmt.setInt(9, selectedDepartmentId != null ? selectedDepartmentId : 0); // Handle null
                stmt.setDouble(10, Double.parseDouble(txtSalary.getText()));
                stmt.setString(11, txtHireDate.getText());
                stmt.setString(12, txtLeaveDate.getText());
                stmt.setString(13, comstatus.getValue());
                
                stmt.setString(14, imageFileNameForDb); // Use the determined filename (copied or existing)

                stmt.setInt(15, Integer.parseInt(txtEid.getText()));

                // These fields should be managed by the application, not directly editable by user after initial set
                txtEid.setEditable(false);
                txtHireDate.setEditable(false);
                txtLeaveDate.setEditable(false);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    // Update user table status if employee status becomes "InActive"
                    // Always sync status to user table (whether Active or InActive)
                    String newStatus = comstatus.getValue(); // Active or InActive
                    String userUpdateSQL = "UPDATE user SET status = ? WHERE employee_id = ?";
                    try (PreparedStatement userStmt = con.prepareStatement(userUpdateSQL)) {
                        userStmt.setString(1, newStatus);
                        userStmt.setInt(2, Integer.parseInt(txtEid.getText()));
                        userStmt.executeUpdate();
                        System.out.println("✅ User table status updated to: " + newStatus);
                    } catch (SQLException e) {
                        System.err.println("❌ Failed to update user status: " + e.getMessage());
                    }

                   /* if ("InActive".equals(comstatus.getValue())) {
                        String userUpdateSQL = "UPDATE user SET status = ? WHERE employee_id = ?";
                        try (PreparedStatement userStmt = con.prepareStatement(userUpdateSQL)) {
                            userStmt.setString(1, "InActive");
                            userStmt.setInt(2, Integer.parseInt(txtEid.getText()));
                            userStmt.executeUpdate();
                            System.out.println("✅ User table status updated to InActive.");
                        } catch (SQLException e) {
                             System.err.println("❌ Failed to update user status: " + e.getMessage());
                        }
                    }*/

                    System.out.println("✅ Employee data updated successfully.");
                    showAlert(Alert.AlertType.INFORMATION, "Update Success", "Employee updated successfully!");

                    // Reload the Employee.fxml scene
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.close(); // Close the modal dialog

                } else {
                    System.out.println("⚠️ No employee found with ID: " + txtEid.getText());
                    showAlert(Alert.AlertType.WARNING, "Update Failed", "No employee found with the given ID.");
                }
            } // PreparedStatement auto-closes here

        } catch (SQLException e) {
            System.err.println("❌ Database error while updating employee: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update employee: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Salary or ID is not a valid number: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Input Error", "Salary or Employee ID must be a valid number.");
        }
    }

    /**
     * Sets the employee data into the form fields and attempts to load the associated image.
     * @param emp The Employee object containing the data.
     */
    public void setEmployeeData(Employee emp) {
        if (emp == null) {
            System.out.println("Employee is null in setEmployeeData!");
            showAlert(Alert.AlertType.ERROR, "Data Error", "No employee data provided to display.");
            return;
        }
        
        // Store the current employee's image name from the DB
        currentEmployeeImageName = emp.getImage_name(); 
        // Reset selectedImagePath, as we're loading existing data, not selecting a new file yet
        selectedImagePath = null; 

        try {
            if (emp.getDob() != null && !emp.getDob().isEmpty()) {
                datedob.setValue(LocalDate.parse(emp.getDob()));
            } else {
                datedob.setValue(null);
            }
        }
        catch (DateTimeParseException e) {
            System.err.println("Invalid DOB format for employee ID " + emp.getEid() + ": " + emp.getDob() + " - " + e.getMessage());
            // It's often better to just clear the field or leave it as null on parse error.
            datedob.setValue(null); 
        }

        // Set position ComboBox
        String positionName = positionMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == emp.getPos_id())
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
        comPosition.setValue(positionName);

        // Set department ComboBox
        String departmentName = departmentMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == emp.getDep_id())
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
        comDepartment.setValue(departmentName);

        txtEid.setText(String.valueOf(emp.getEid()));
        txtname.setText(emp.getEmpname());
        txtNrc.setText(emp.getNrc());
        txtgender.setText(emp.getGender());
        txtphno.setText(emp.getPhno());
        txtEmail.setText(emp.getGmail());
        txtAddress.setText(emp.getAddress());
        txtSalary.setText(String.valueOf(emp.getSalary()));
        txtHireDate.setText(emp.getHiredate());
        txtLeaveDate.setText(emp.getLeavedate());
        comstatus.setValue(emp.getStatus());

        // Load the employee's image using its filename
        System.out.println("Employee Image Name from DB to load into modal: " + emp.getImage_name());
        loadImage(emp.getImage_name()); 
    }

    /**
     * Helper method to show an Alert dialog.
     * @param alertType The type of alert (e.g., Alert.AlertType.INFORMATION, Alert.AlertType.ERROR).
     * @param title The title of the alert window.
     * @param message The content message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Loads an image from either the dedicated UPLOADED_IMAGE_DIR (for user uploads),
     * or from the classpath (for bundled images like default.jpg).
     * @param imageName The file name of the image (e.g., "myphoto.jpg").
     * It assumes user-uploaded images are stored in UPLOADED_IMAGE_DIR with this name.
     */
    private void loadImage(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            System.out.println("⚠️ loadImage: No image name provided, loading default.");
            imageName = "default.jpg"; // Fallback to default
        }

        System.out.println("🔍 Attempting to load image with name: '" + imageName + "'");

        Image imageToLoad = null;

        // --- ATTEMPT 1: Load from the UPLOADED_IMAGE_DIR (for user-uploaded images) ---
        // This is the primary location for images saved via the app.
        File appImageFile = new File(UPLOADED_IMAGE_DIR + imageName);
        System.out.println("DEBUG: Checking file system path: " + appImageFile.getAbsolutePath());
        if (appImageFile.exists() && !appImageFile.isDirectory()) {
            try {
                // Use the file URI to load the image
                imageToLoad = new Image(appImageFile.toURI().toURL().toExternalForm());
                if (!imageToLoad.isError()) {
                    System.out.println("✅ Loaded image from file system: " + appImageFile.getAbsolutePath());
                } else {
                    System.err.println("❌ Error loading image from file system '" + appImageFile.getAbsolutePath() + "': " + imageToLoad.getException());
                    imageToLoad = null; // Reset to null to try next method
                }
            } catch (MalformedURLException e) {
                System.err.println("❌ Malformed URL for file path '" + appImageFile.getAbsolutePath() + "': " + e.getMessage());
            }
        } else {
            System.out.println("❌ Image not found in app data directory: " + appImageFile.getAbsolutePath());
        }


        // --- ATTEMPT 2: Load from Classpath (for default.jpg or bundled resources) ---
        // This is for images built into your application JAR.
        if (imageToLoad == null) {
            InputStream imageStream = getClass().getResourceAsStream("/image/" + imageName);
            if (imageStream != null) {
                imageToLoad = new Image(imageStream);
                if (!imageToLoad.isError()) {
                    System.out.println("✅ Loaded image from classpath: /image/" + imageName);
                } else {
                    System.err.println("❌ Error loading image from classpath '/image/" + imageName + "': " + imageToLoad.getException());
                    imageToLoad = null; // Reset to null to try next method
                }
            } else {
                System.out.println("❌ Image not found in classpath: /image/" + imageName);
            }
        }

        // --- FINAL FALLBACK: Load default.jpg from classpath if all else fails ---
        if (imageToLoad == null) {
            System.out.println("⚠️ Falling back to default.jpg.");
            InputStream defaultImageStream = getClass().getResourceAsStream("/image/default.jpg");
            if (defaultImageStream != null) {
                imageToLoad = new Image(defaultImageStream);
                if (imageToLoad.isError()) {
                    System.err.println("❌ Error loading default.jpg: " + imageToLoad.getException());
                }
            } else {
                System.err.println("❌ Critical: Could not load default.jpg either!");
            }
        }

        if (imageToLoad != null && !imageToLoad.isError()) {
            imgview.setImage(imageToLoad);
        } else {
            System.err.println("❌ No image could be loaded for: " + imageName + " (after all attempts). ImageView might be empty.");
            imgview.setImage(null); // Ensure ImageView is clear if no image loads
        }
    }
}