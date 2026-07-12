/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import model.Employee;
import database.Database;
import java.io.File; // ADDED
import java.io.InputStream;
import java.net.MalformedURLException; // ADDED
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet; // Added for clarity with try-with-resources
import java.sql.SQLException;
import java.sql.Statement; // Added for clarity with try-with-resources
import java.time.LocalDate; // Although DOB is Label, keep if logic uses LocalDate internally
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class EmpViewModalController implements Initializable {

    // Constant for the directory where uploaded employee images are stored
    // This MUST match the constant in EmployeeDetailModalController and EmpcardDesignController
    //private static final String UPLOADED_IMAGE_DIR =" C:\\Users\\USER\\Documents\\NetBeansProjects\\JavaFXApplication4\\src\\image\\";
      private static final String UPLOADED_IMAGE_DIR = "C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\";

    @FXML
    private ImageView imgEMP;
    @FXML
    private Label lblName;
    @FXML
    private Label lblNRC;
    @FXML
    private Label lblGender;
    @FXML
    private Label lblDOB;
    @FXML
    private Label lblDepartment;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblHireDate;
    @FXML
    private Label lblPhone;

    @FXML
    private Label lblEmail;
    
    // Label for Position, assuming you want to display it
    @FXML
    private Label lblPosition; // ADDED: Assuming you want to display position too

    private Connection con;
    
    private ObservableList<String> departmentList = FXCollections.observableArrayList();
    private Map<String, Integer> departmentMap = new HashMap<>();
    
    // ADDED: Map for Position data, similar to Department for lookup
    private Map<String, Integer> positionMap = new HashMap<>(); // ADDED
    private ObservableList<String> positionList = FXCollections.observableArrayList(); // ADDED


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        try {
            con = db.getConnection();
            if (con == null) {
                System.err.println("Failed to establish database connection in EmpViewModalController.");
                // Potentially show an alert here or disable parts of UI
                return;
            }
            loadDepartmentData();
            
        }catch (ClassNotFoundException ex) {
            System.err.println("DB connection error: " + ex);
        }
        // Catch SQLException for DB initialization
        
        // Initial load of default image using the robust loadImage method
        System.out.println(">> Initializing EmpViewModalController, loading default image.");
        loadImage("default.jpg"); 
    }
    
    private void loadDepartmentData() {
        try (Statement stmt = con.createStatement(); // Use try-with-resources
             ResultSet rs = stmt.executeQuery("SELECT department_id, department_name FROM department")) { // Corrected SQL
            
            departmentList.clear();
            departmentMap.clear();

            while (rs.next()) {
                int id = rs.getInt("department_id");
                String name = rs.getString("department_name");
                departmentList.add(name);
                departmentMap.put(name, id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to load departments: " + e.getMessage());
        }
    }

    
    
    /**
     * Sets the employee data into the labels and loads the associated image.
     * @param emp The Employee object containing the data.
     */
    public void setEmployeeData(Employee emp) {
        if (emp == null) {
            System.out.println("Employee is null in setEmployeeData!");
            return;
        }
        
        lblName.setText(emp.getEmpname());
        lblNRC.setText(emp.getNrc());
        lblGender.setText(emp.getGender());
        
        // Handle DOB for display - ensure it's not null before setting
        lblDOB.setText(emp.getDob() != null ? emp.getDob() : "N/A");
        
        lblPhone.setText(emp.getPhno());
        lblEmail.setText(emp.getGmail());
        lblAddress.setText(emp.getAddress());
        
        // Set Department Label
        int deptId = emp.getDep_id();
        String deptName = departmentMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == deptId)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse("Unknown Department"); // Default if not found
        lblDepartment.setText(deptName);

        // Set Position Label (Assuming Employee has getPos_id() and you added lblPosition)
       
        lblHireDate.setText(emp.getHiredate());
        // If you have a leave date label, set it here too
        // lblLeaveDate.setText(emp.getLeavedate() != null ? emp.getLeavedate() : "N/A"); 
        
        System.out.println("Employee Image Name to load for view: " + emp.getImage_name());

        // This is crucial: Call the robust loadImage to display the employee image
        loadImage(emp.getImage_name()); 
    }
    private void loadImage(String imageName) {
    try {
        if (imageName == null || imageName.trim().isEmpty()) {
            System.out.println("⚠️ Image name is null or empty. Loading default image.");
            InputStream defaultImageStream = getClass().getResourceAsStream("/image/default.jpg");
            if (defaultImageStream != null) {
                imgEMP.setImage(new Image(defaultImageStream));
            } else {
                System.out.println("❌ Default image not found!");
            }
            return;
        }

        File file = new File(UPLOADED_IMAGE_DIR + imageName);
        System.out.println("Trying to load image from: " + file.getAbsolutePath());

        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            imgEMP.setImage(image);
        } else {
            System.out.println("⚠️ Image file not found. Loading default image.");
            InputStream defaultImageStream = getClass().getResourceAsStream("/image/default.jpg");
            if (defaultImageStream != null) {
                imgEMP.setImage(new Image(defaultImageStream));
            } else {
                System.out.println("❌ Default image not found!");
            }
        }
    } catch (Exception e) {
        System.out.println("⚠️ Error loading image: " + e.getMessage());
    }
}
}
    
    /**
     * Loads an image from either the dedicated UPLOADED_IMAGE_DIR (for user uploads),
     * or from the classpath (for bundled images like default.jpg).
     * @param imageName The file name of the image (e.g., "myphoto.jpg").
     * It assumes user-uploaded images are stored in UPLOADED_IMAGE_DIR with this name.
     */
   /* private void loadImage(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            System.out.println("⚠️ loadImage (View Modal): No image name provided, loading default.");
            imageName = "default.jpg"; // Fallback to default
        }

        System.out.println("🔍 View Modal: Attempting to load image with name: '" + imageName + "'");

        Image imageToLoad = null;

        // --- ATTEMPT 1: Load from the UPLOADED_IMAGE_DIR (for user-uploaded images) ---
        // This is the primary location for images saved via the app.
        File appImageFile = new File(UPLOADED_IMAGE_DIR + imageName);
        System.out.println("DEBUG View Modal: Checking file system path: " + appImageFile.getAbsolutePath());
        if (appImageFile.exists() && !appImageFile.isDirectory()) {
            try {
                // Use the file URI to load the image
                imageToLoad = new Image(appImageFile.toURI().toURL().toExternalForm());
                if (!imageToLoad.isError()) {
                    System.out.println("✅ View Modal: Loaded image from file system: " + appImageFile.getAbsolutePath());
                } else {
                    System.err.println("❌ View Modal: Error loading image from file system '" + appImageFile.getAbsolutePath() + "': " + imageToLoad.getException());
                    imageToLoad = null; // Reset to null to try next method
                }
            } catch (MalformedURLException e) {
                System.err.println("❌ View Modal: Malformed URL for file path '" + appImageFile.getAbsolutePath() + "': " + e.getMessage());
            }
        } else {
            System.out.println("❌ View Modal: Image not found in app data directory: " + appImageFile.getAbsolutePath());
        }


        // --- ATTEMPT 2: Load from Classpath (for default.jpg or bundled resources) ---
        // This is for images built into your application JAR.
        if (imageToLoad == null) {
            InputStream imageStream = getClass().getResourceAsStream("/image/" + imageName);
            if (imageStream != null) {
                imageToLoad = new Image(imageStream);
                if (!imageToLoad.isError()) {
                    System.out.println("✅ View Modal: Loaded image from classpath: /image/" + imageName);
                } else {
                    System.err.println("❌ View Modal: Error loading image from classpath '/image/" + imageName + "': " + imageToLoad.getException());
                    imageToLoad = null; // Reset to null to try next method
                }
            } else {
                System.out.println("❌ View Modal: Image not found in classpath: /image/" + imageName);
            }
        }

        // --- FINAL FALLBACK: Load default.jpg from classpath if all else fails ---
        if (imageToLoad == null) {
            System.out.println("⚠️ View Modal: Falling back to default.jpg.");
            InputStream defaultImageStream = getClass().getResourceAsStream("/image/default.jpg");
            if (defaultImageStream != null) {
                imageToLoad = new Image(defaultImageStream);
                if (imageToLoad.isError()) {
                    System.err.println("❌ View Modal: Error loading default.jpg: " + imageToLoad.getException());
                }
            } else {
                System.err.println("❌ View Modal: Critical: Could not load default.jpg either!");
            }
        }

        if (imageToLoad != null && !imageToLoad.isError()) {
            imgEMP.setImage(imageToLoad);
        } else {
            System.err.println("❌ View Modal: No image could be loaded for: " + imageName + " (after all attempts). ImageView might be empty.");
            imgEMP.setImage(null); // Ensure ImageView is clear if no image loads
        }
    }
    private void loadImage(String imageName) {
        if (imageName == null ) {
            System.out.println("⚠️ loadImage: No image name provided, loading default.");
            imageName = "default.jpg"; // Fallback to default  || imageName.trim().isEmpty()
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
            imgEMP.setImage(imageToLoad);
        } else {
            System.err.println("❌ No image could be loaded for: " + imageName + " (after all attempts). ImageView might be empty.");
            imgEMP.setImage(null); // Ensure ImageView is clear if no image loads
        }
    }
}*/
