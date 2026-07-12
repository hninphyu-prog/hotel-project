/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import model.Department;
import model.Employee;
import model.Position;
import database.Database;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
//import static javafxapplication4.JavaFXApplication4.stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * FXML Controller class
 *
 * @author USER
 */
public class RegistrationController implements Initializable {

    @FXML
    private AnchorPane edi;
    @FXML
    private TextField txtName;
    @FXML
    private DatePicker TxtDob;
    @FXML
    private ComboBox<String> txtState;
    @FXML
    private ComboBox<String> txtTownShip;
    @FXML
    private ComboBox<String> txtNation;
    @FXML
    private TextField txtNrcNo;
    @FXML
    private TextField txtPhone;
    @FXML
    private RadioButton rdMale;
    @FXML
    private ToggleGroup tgGender;
    @FXML
    private RadioButton rdFemale;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblemail;
    @FXML
    private ComboBox<Position> comPosition;
    @FXML
    private DatePicker txtHire;
    @FXML
    private TextField txtSalary;
    @FXML
    private TextField txtAddress;
    @FXML
    private Button BtnClear;
    @FXML
    private Button btnSummit;
    @FXML
    private Label lblnrc;
    @FXML
    private Label lblsalary;
    @FXML
    private Label lblname;
    @FXML
    private ImageView imgUpload;
    @FXML
    private Button btnUpload;
    @FXML
    private Label lbladdress;
    @FXML
    private Label lblphone;
    @FXML
    private ComboBox<Department> comdepid;
     @FXML
    private Button btnback;

    
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    private Employee emp;
    ObservableList<Employee>CardListdata ;
    
    String Name,Town,Nation,Nrcno,Nrc,Phone,Email,Position,Address, sql,UserType;
    int State,salary;
    String savedImageFilename;
    int Posid,Depid;//to use submit action
      private static final String leavedate = "xxxx-xx-xx";
private static final String status = "Active";
int edii;
String savedImageFileName;
    File selectedFileForUpload;
    private final String IMAGE_STORAGE_DIR = "C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\";


    
    
    ObservableList<String> stateList;
    ObservableList<String> citycodeList;
    ObservableList<String> nationList;
    
    
    //ObservableList<String> positionList=FXCollections.observableArrayList();
    ObservableList<String> DepartmentList=FXCollections.observableArrayList();
    
    String emailpattern="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    //String emailpattern="^(.+)@(.+)$*";
    String nrcpattern="^[0-9]{6}";
    
    String phonepattern="^(09\\d{9}|01\\d{6,7})$";
    String salarypattern="[0-9]{1,8}";
    String namePattern = "^[a-zA-Z\\s'-]{1,50}$";
    String addressPattern = "^[a-zA-Z0-9\\s,./#-]{5,100}$";
    ObservableList<String> deptList = FXCollections.observableArrayList();
    ObservableList<String> posList = FXCollections.observableArrayList();
   // private int eid;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        try {
            con = db.getConnection();
            if (con == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database. Please check your connection.");
                return;
            }

            // Set initial prompt texts for ComboBoxes if not already in FXML
            txtState.setPromptText("State");
            txtTownShip.setPromptText("Township");
            txtNation.setPromptText("Nation");
            comPosition.setPromptText("Select Position");
            comdepid.setPromptText("Select Department");
            
            // Disable direct editing of ComboBoxes
            txtState.setEditable(false);
            txtTownShip.setEditable(false);
            txtNation.setEditable(false);
            comPosition.setEditable(false);
            comdepid.setEditable(false);

            // Disable future dates and dates for under 18 for DOB
            TxtDob.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate todayMinus18 = LocalDate.now().minusYears(18);
                    setDisable(empty || date.isAfter(todayMinus18));
                }
            });

            // Disable future dates for Hire Date
            txtHire.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.isAfter(today));
                }
            });

            // Set up TextFormatters with immediate validation feedback
            setupTextFormatter(txtEmail, emailpattern, lblemail, "Invalid Email");
            setupTextFormatter(txtNrcNo, nrcpattern, lblnrc, "NRC No. must be 6 digits");
            setupTextFormatter(txtPhone, phonepattern, lblphone, "Invalid Phone Number");
            setupTextFormatter(txtSalary, salarypattern, lblsalary, "Invalid Salary (digits only)");
            setupTextFormatter(txtName, namePattern, lblname, "Invalid Name");
            setupTextFormatter(txtAddress, addressPattern, lbladdress, "Invalid Address");

            // Load ComboBox items
            
            initcomboStateno();  // Load State
        txtState.setItems(stateList);
            
            loadPositions();
            loadDepartments();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, "JDBC Driver not found.", ex);
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Database driver not found. Please contact support.");
        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, "SQL error during initialization.", ex);
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to load data: " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, "Unexpected error during initialization.", ex);
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "An unexpected error occurred: " + ex.getMessage());
        }
    }

    // Helper method for TextFormatter setup
    private void setupTextFormatter(TextField textField, String pattern, Label errorLabel, String errorMessage) {
        errorLabel.setVisible(false); // Hide error label initially
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(pattern) || newText.isEmpty()) {
                errorLabel.setVisible(false);
                textField.setBorder(null); // Reset border
                return change;
            } else {
                errorLabel.setText(errorMessage);
                errorLabel.setVisible(true);
                textField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(2))));
                return change; // Allow typing but show error
            }
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void loadDepartments() throws SQLException {
        ObservableList<Department> departmentList = FXCollections.observableArrayList();
        String sql = "SELECT department_id, department_name FROM department ORDER BY department_name";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("department_id");
                String name = rs.getString("department_name");
                departmentList.add(new Department(id, name));
            }
        }
        comdepid.setItems(departmentList);
    }

    private void loadPositions() throws SQLException {
        ObservableList<Position> positionList = FXCollections.observableArrayList();
        String sql = "SELECT position_id, position_name FROM position ORDER BY position_name";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("position_id");
                String name = rs.getString("position_name");
                positionList.add(new Position(id, name));
            }
        }
        comPosition.setItems(positionList);
    }

    @FXML
    void handlebackAction(ActionEvent event) {
       
      Stage modalStage =(Stage)btnback.getScene().getWindow();
                    modalStage.close();
}

    

    @FXML
    private void handleUploadAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Get the current stage from the event source
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        selectedFileForUpload = fileChooser.showOpenDialog(currentStage);

        if (selectedFileForUpload != null) {
            try {
                // Get just the filename (e.g., "myimage.png")
                savedImageFileName = selectedFileForUpload.getName();

                // Display image preview
                Image img = new Image(selectedFileForUpload.toURI().toString());
                imgUpload.setImage(img);
                imgUpload.setPreserveRatio(true); // Maintain aspect ratio
                imgUpload.setFitWidth(150); // Set a reasonable display size
                imgUpload.setFitHeight(150);

                System.out.println("Image selected for upload: " + selectedFileForUpload.getAbsolutePath());
            } catch (Exception e) {
                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, "Error loading selected image.", e);
                showAlert(Alert.AlertType.ERROR, "Image Error", "Failed to load selected image: " + e.getMessage());
                savedImageFileName = null;
                selectedFileForUpload = null;
                imgUpload.setImage(null); // Clear image view on error
            }
        } else {
            System.out.println("No image file selected.");
            // If user cancels, keep previous image or set to default if none was there.
            // For now, if cancel, don't change savedImageFileName or imgUpload unless explicitly cleared.
        }
    }
   
    
    

    @FXML
    private void txtNameAction(ActionEvent event) {
    }

    @FXML
    private void DobAction(ActionEvent event) {
    }

    @FXML
    private void StateAction(ActionEvent event) {
        citycodeList = FXCollections.observableArrayList();
        String selectedState = txtState.getValue();

        if (selectedState == null || selectedState.isEmpty()) {
            txtTownShip.getItems().clear();
            txtTownShip.setPromptText("Select Township");
            return;
        }
     

        try {
            // Assuming state values are just the numbers, e.g., "1", "2"
            int stateId = Integer.parseInt(selectedState);
            String sql = "SELECT city_code FROM city WHERE region_state_id = ? ORDER BY city_code";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, stateId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        citycodeList.add(rs.getString("city_code"));
                    }
                }
            }
            txtTownShip.setItems(citycodeList);
            txtTownShip.getSelectionModel().clearSelection(); // Clear previous township selection
            txtTownShip.setPromptText("Select Township"); // Reset prompt
        } catch (SQLException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, "SQL error loading townships.", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load townships: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.WARNING, "Invalid state number format.", ex);
            showAlert(Alert.AlertType.WARNING, "Input Error", "Selected state number format is invalid.");
        }
    }

    @FXML
    private void townshipAction(ActionEvent event) {
        // Nation codes (N), (E), (P) are fixed, no DB interaction needed here.
        ObservableList<String> nationList = FXCollections.observableArrayList();
        nationList.addAll("(N)", "(E)", "(P)");
        txtNation.setItems(nationList);
        txtNation.getSelectionModel().clearSelection(); // Clear previous nation selection
        txtNation.setPromptText("Select Nation"); // Reset prompt
    }
    
    @FXML
    private void nationActiom(ActionEvent event) {
    }

    @FXML
    private void NrcNoAction(ActionEvent event) {
    }

    @FXML
    private void PhoneAction(ActionEvent event) {
    }

    @FXML
    private void rbmaleAction(ActionEvent event) {
    }

    @FXML
    private void rbFemaleAction(ActionEvent event) {
    }

    @FXML
    private void emailAction(ActionEvent event) {
    }
 
@FXML
private void positionAction(ActionEvent event) throws SQLException {
  
}

@FXML
    void handlecomdepidAction(ActionEvent event) throws SQLException {
   
}



    @FXML
    private void hireAction(ActionEvent event) {
    }

    @FXML
    private void salaryAction(ActionEvent event) {
    }

    @FXML
    private void addressAction(ActionEvent event) {
    }
    @FXML
    private void clearAction() {
        txtName.clear();
        TxtDob.setValue(null);
        txtState.getSelectionModel().clearSelection();
        txtTownShip.getSelectionModel().clearSelection();
        txtNation.getSelectionModel().clearSelection();
        txtNrcNo.clear();
        txtPhone.clear();
        rdMale.setSelected(false);
        rdFemale.setSelected(false);
        if (tgGender.getSelectedToggle() != null) {
            tgGender.getSelectedToggle().setSelected(false); // Deselect any gender
        }
        txtEmail.clear();
        comPosition.getSelectionModel().clearSelection();
        comdepid.getSelectionModel().clearSelection();
        txtHire.setValue(null);
        txtSalary.clear();
        txtAddress.clear();
        imgUpload.setImage(null);
        savedImageFileName = null; // Clear saved file name
        selectedFileForUpload = null; // Clear the selected file object

        // Reset prompt texts for comboboxes if needed
        txtState.setPromptText("Select State");
        txtTownShip.setPromptText("Select Township");
        txtNation.setPromptText("Select Nation");
        comPosition.setPromptText("Select Position");
        comdepid.setPromptText("Select Department");
        
        // Hide all validation labels and reset borders
        lblemail.setVisible(false);
        lblnrc.setVisible(false);
        lblsalary.setVisible(false);
        lblname.setVisible(false);
        lbladdress.setVisible(false);
        lblphone.setVisible(false);

        txtName.setBorder(null);
        txtNrcNo.setBorder(null);
        txtPhone.setBorder(null);
        txtEmail.setBorder(null);
        txtSalary.setBorder(null);
        txtAddress.setBorder(null);
        resetPromptTexts();

        showAlert(Alert.AlertType.INFORMATION, "Cleared", "All fields have been cleared.");
    }

    
    
    @FXML
    private void summitAction(ActionEvent event) throws SQLException, IOException {
         
           

    // 1. Image validation
    if (selectedFileForUpload == null) { // Check if a file has actually been selected
        JOptionPane.showMessageDialog(new JFrame(), "Please upload an image.", "Error",
                JOptionPane.ERROR_MESSAGE);
        return; // Important: Stop execution if validation fails
    } else if (savedImageFileName == null || savedImageFileName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(new JFrame(), "Image file name is missing.", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (savedImageFileName.length() > 255) {
        JOptionPane.showMessageDialog(new JFrame(), "Image file name too long (max 255 characters).", "Error",
                JOptionPane.ERROR_MESSAGE);
        return; // Important: Stop execution if validation fails
    }

    // 2. Other field validations (keeping your existing logic, but ensuring returns)
    if ("".equals(txtName.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "Full Name is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (!txtName.getText().matches(namePattern)) {
        JOptionPane.showMessageDialog(new JFrame(), "Invalid Name. Use only letters, spaces, hyphens, and apostrophes.", "Error",
                JOptionPane.ERROR_MESSAGE);
        txtName.getStyleClass().add("invalid"); // Apply CSS for visual feedback
        return;
    } else if (txtName.getText().length() > 100) {
        JOptionPane.showMessageDialog(new JFrame(), "Name is too long. Maximum 100 characters.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (txtState.getSelectionModel().getSelectedItem() == null) {
        JOptionPane.showMessageDialog(new JFrame(), "State Number is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (txtTownShip.getSelectionModel().getSelectedItem() == null) {
        JOptionPane.showMessageDialog(new JFrame(), "Township name is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (txtNation.getSelectionModel().getSelectedItem() == null) {
        JOptionPane.showMessageDialog(new JFrame(), "Nation Code is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }
    if ("".equals(txtNrcNo.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "NRC Number is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (!txtNrcNo.getText().matches(nrcpattern)) {
        JOptionPane.showMessageDialog(new JFrame(), "NRC Number must be 6 digits.", "Error",
                JOptionPane.ERROR_MESSAGE);
        txtNrcNo.getStyleClass().add("invalid");
        return;
    }

    if (TxtDob.getValue() == null) {
        JOptionPane.showMessageDialog(new JFrame(), "Date of Birth is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (TxtDob.getValue().isAfter(LocalDate.now().minusYears(18))) {
        JOptionPane.showMessageDialog(new JFrame(), "Employee must be at least 18 years old.", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!rdFemale.isSelected() && !rdMale.isSelected()) {
        JOptionPane.showMessageDialog(new JFrame(), "Please choose Gender!", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }
    if ("".equals(txtPhone.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "Phone Number is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (!txtPhone.getText().matches(phonepattern)) {
        JOptionPane.showMessageDialog(new JFrame(), "Invalid Phone Number format.", "Error",
                JOptionPane.ERROR_MESSAGE);
        txtPhone.getStyleClass().add("invalid");
        return;
    }

    if ("".equals(txtEmail.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "Email is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (!txtEmail.getText().matches(emailpattern)) {
        JOptionPane.showMessageDialog(new JFrame(), "Invalid Email format.", "Error",
                JOptionPane.ERROR_MESSAGE);
        txtEmail.getStyleClass().add("invalid");
        return;
    }

    Position selectedPosition = comPosition.getValue();
    if (selectedPosition == null) {
        JOptionPane.showMessageDialog(new JFrame(), "Position is required", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    int positionId = selectedPosition.getPosition_id();

    Department selectedDep = comdepid.getValue();
    if (selectedDep == null) {
        JOptionPane.showMessageDialog(new JFrame(), "Department is required", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    int depId = selectedDep.getDepartment_id();

    if (txtHire.getValue() == null) {
        JOptionPane.showMessageDialog(new JFrame(), "Hire Date is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }
    if ("".equals(txtSalary.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "Salary is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (!txtSalary.getText().matches(salarypattern)) {
        JOptionPane.showMessageDialog(new JFrame(), "Invalid Salary. Must be a number.", "Error",
                JOptionPane.ERROR_MESSAGE);
        txtSalary.getStyleClass().add("invalid");
        return;
    }

    if ("".equals(txtAddress.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "Address is required", "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else if (!txtAddress.getText().matches(addressPattern)) {
        JOptionPane.showMessageDialog(new JFrame(), "Invalid Address format.", "Error",
                JOptionPane.ERROR_MESSAGE);
        txtAddress.getStyleClass().add("invalid");
        return;
    }

    // If all validations pass, proceed with image file copying and database insertion
    String gender = rdMale.isSelected() ? "Male" : "Female";

    // Copy the image file
    Path destDir = Paths.get(IMAGE_STORAGE_DIR);
    if (!Files.exists(destDir)) {
        Files.createDirectories(destDir);
    }
    Path destFile = destDir.resolve(savedImageFileName);
    Files.copy(selectedFileForUpload.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
    System.out.println("Image copied to: " + destFile.toAbsolutePath());

    String Name = txtName.getText();
    int State = Integer.parseInt(txtState.getValue());
    String Town = txtTownShip.getValue();
    String Nation = txtNation.getValue();
    String Nrcno = txtNrcNo.getText();
    String Nrc = State + "/" + Town + Nation + Nrcno;
    String dob = TxtDob.getValue().toString();
    String Phone = txtPhone.getText();
    String Email = txtEmail.getText();
    String hire = txtHire.getValue().toString();
    int salary = Integer.parseInt(txtSalary.getText());
    String Address = txtAddress.getText();

    String sql = "INSERT INTO employee(employee_name, nrc, gender, DOB, phone_no, email, position_id, department_id, salary, hire_date, leave_date, Image, Status, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    pst.setString(1, Name);
    pst.setString(2, Nrc);
    pst.setString(3, gender);
    pst.setString(4, dob);
    pst.setString(5, Phone);
    pst.setString(6, Email);
    pst.setInt(7, positionId);
    pst.setInt(8, depId);
    pst.setInt(9, salary);
    pst.setString(10, hire);
    pst.setString(11, leavedate);
    pst.setString(12, savedImageFileName);
    pst.setString(13, status);
    pst.setString(14, Address);

    int affectedRows = pst.executeUpdate();
    int eid = 0;
    if (affectedRows > 0) {
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            eid = rs.getInt(1);
            System.out.println("Generated EID: " + eid);
        }
        rs.close();
    } else {
        JOptionPane.showMessageDialog(null, "Employee registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Fetch and print employee and position names for debugging/confirmation
    String sql1 = "SELECT employee_id, employee_name, position_id FROM employee WHERE employee_id = ?";
    try (PreparedStatement st = con.prepareStatement(sql1)) {
        st.setInt(1, eid);
        ResultSet rs2 = st.executeQuery();
        if (rs2.next()) {
            String empName = rs2.getString("employee_name");
            int posId = rs2.getInt("position_id");
            String posName = getPositionNameById(posId);
            System.out.println("Employee Name: " + empName);
            System.out.println("Position Name: " + posName);
        }
        rs2.close();
    }

    String departmentName = selectedDep.getDepartment_name().trim();
    String positionName = selectedPosition.getPosition_name().trim();

    if ("Front Office".equalsIgnoreCase(departmentName) && "Staff".equalsIgnoreCase(positionName)) {
        // saveEmployeeToDatabase() is not needed here if you've already inserted above
        // saveEmployeeToDatabase(); // This method seems redundant if the insert is done above

        // Load next page
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CreatAccount.fxml"));

            Parent root = loader.load();
            CreatAccountController controller = loader.getController();
            controller.loadEmployeeData(eid); // Pass the generated EID

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Page Load Error");
            error.setHeaderText(null);
            error.setContentText("Failed to load the account creation page.");
            error.showAndWait();
        }
    }
        txtName.clear();
        TxtDob.setValue(null);
        txtState.getSelectionModel().clearSelection();
        txtTownShip.getSelectionModel().clearSelection();
        txtNation.getSelectionModel().clearSelection();
        txtNrcNo.clear();
        txtPhone.clear();
        rdMale.setSelected(false);
        rdFemale.setSelected(false);
        if (tgGender.getSelectedToggle() != null) {
            tgGender.getSelectedToggle().setSelected(false); // Deselect any gender
        }
        txtEmail.clear();
        comPosition.getSelectionModel().clearSelection();
        comdepid.getSelectionModel().clearSelection();
        txtHire.setValue(null);
        txtSalary.clear();
        txtAddress.clear();
        imgUpload.setImage(null);
        savedImageFileName = null; // Clear saved file name
        selectedFileForUpload = null; // Clear the selected file object

        // Reset prompt texts for comboboxes if needed
        txtState.setPromptText("Select State");
        txtTownShip.setPromptText("Select Township");
        txtNation.setPromptText("Select Nation");
        comPosition.setPromptText("Select Position");
        comdepid.setPromptText("Select Department");
        
        // Hide all validation labels and reset borders
        lblemail.setVisible(false);
        lblnrc.setVisible(false);
        lblsalary.setVisible(false);
        lblname.setVisible(false);
        lbladdress.setVisible(false);
        lblphone.setVisible(false);

        txtName.setBorder(null);
        txtNrcNo.setBorder(null);
        txtPhone.setBorder(null);
        txtEmail.setBorder(null);
        txtSalary.setBorder(null);
        txtAddress.setBorder(null);
        resetPromptTexts();
   
    JOptionPane.showMessageDialog(null, "Employee registered successfully!");
       Stage modalStage =(Stage)btnSummit.getScene().getWindow();
                    modalStage.close();

}
         
   private String getPositionNameById(int positionId) throws SQLException {
    String sql = "SELECT position_name FROM position WHERE position_id = ?";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, positionId);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getString("position_name");
            }
        }
    }
    return "Unknown Position";
}


    
    
   private void saveEmployeeToDatabase() {
    try {
        // database insert code here
        System.out.println("Employee data saved successfully!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
   

            
    private void loadNextPage() {
    try {
        URL fxmlUrl = getClass().getResource("/view/CreatAccount.fxml");
        if (fxmlUrl == null) {
            System.err.println("FXML not found: /view/CreatAccount.fxml");
            return;
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Next Page");
        stage.show();

        // close current window
        ((Stage) btnSummit.getScene().getWindow()).close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
          
    

    
     public void initcomboStateno() throws SQLException{
        stateList =FXCollections.observableArrayList();
        String sql = "select region_state_id from region_state";
        st=con.createStatement();
        rs =st.executeQuery(sql);
        while(rs.next()){
            stateList.add(String.valueOf(rs.getInt("region_state_id")));
        }
        
    }
        
     private void resetPromptTexts() {
 // Clear and refresh ComboBox for Nation
    txtName.setPromptText("Enter your full name");
    txtNrcNo.setPromptText("Enter NRC Number");
    txtPhone.setPromptText("Enter phone number");
    txtEmail.setPromptText("Enter email address");
    txtSalary.setPromptText("Enter salary");
    txtAddress.setPromptText("Enter address");

    TxtDob.setPromptText("Date of Birth");
    TxtDob.setValue(null);
    TxtDob.getEditor().clear();

    txtHire.setPromptText("Hire Date");
    txtHire.setValue(null);
    txtHire.getEditor().clear();

    // Clear and refresh ComboBox for State
txtState.getSelectionModel().clearSelection();
txtState.setValue(null);
txtState.setPromptText("State");
txtState.getEditor().clear();  // Force refresh

// Clear and refresh ComboBox for Township
txtTownShip.getSelectionModel().clearSelection();
txtTownShip.setValue(null);
txtTownShip.setPromptText("Town");
txtTownShip.getEditor().clear();  // Force refresh

// Clear and refresh ComboBox for Nation
txtNation.getSelectionModel().clearSelection();
txtNation.setValue(null);
txtNation.setPromptText("Nation");
txtNation.getEditor().clear();  // Force refresh

// Clear and refresh ComboBox for Position
comPosition.getSelectionModel().clearSelection();
comPosition.setValue(null);
comPosition.setPromptText("Select your position");
comPosition.getEditor().clear();  // Force refresh

comdepid.getSelectionModel().clearSelection();
comdepid.setValue(null);
comdepid.setPromptText("Select your position");
comdepid.getEditor().clear();  // Force refresh
}
// In your RegistrationController.java file
     private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
}
    
}
