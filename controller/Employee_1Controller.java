package controller;

import controller.Add_DepController;
import controller.EmpcardDesignController;
import static controller.RoomBookingController.cartList;
import controller.Update_PasswordController;
import model.Department;
import model.Employee;
import database.Database;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Import Alert
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Employee_1Controller implements Initializable {

    @FXML private TextField txtsearch;
    @FXML private Button btnadd;
    @FXML private Button btnback;
    @FXML private Button btnaddDep;
    @FXML private Button btnRefresh;
    @FXML private ScrollPane menu_scropane;
    @FXML private GridPane menu_grid; // Keep this, but we'll create a new GridPane inside showcard()
    
    Connection con;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    ObservableList<Department> DepartmentList;
    private Department dep;
    
    ObservableList<String> departmentList = FXCollections.observableArrayList();
    ObservableList<String> statusList = FXCollections.observableArrayList();
    ObservableList<Employee> empList = FXCollections.observableArrayList();

    @FXML private ComboBox<String> combo_department;
    @FXML private ComboBox<String> combo_status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Database db = new Database();
            con = db.getConnection();
            if (con == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database. Please check your connection.");
                return; // Exit if connection fails
            }

            initAllDepartment(); // Populates departmentList including "All Departments"
            initStatusName();    // Populates statusList including "All Statuses"

            combo_department.setItems(departmentList);
            combo_department.setValue("All Departments"); // Set initial value to "All Departments"

            combo_status.setItems(statusList);
            combo_status.setValue("All Statuses");       // Set initial value to "All Statuses"
            
            // Initial display of all employees
            initEmp();
            showcard();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "Database driver not found", ex);
            showAlert(Alert.AlertType.ERROR, "Application Error", "Database driver not found. Please contact support.");
        } catch (SQLException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "SQL Error during initialization", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to initialize employee data: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "IO Error during initialization", ex);
            showAlert(Alert.AlertType.ERROR, "Application Error", "Failed to load employee cards: " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "Unexpected error during initialization", ex);
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred during initialization: " + ex.getMessage());
        }
    }    

    @FXML
    private void handleSearchAction(ActionEvent event) {
        try {
            // If search text is empty, reset to show all based on combo box selections
            if (txtsearch.getText().isEmpty()) {
                initEmp(); 
            } else {
                String empName = "%" + txtsearch.getText() + "%";
                initEmpWithName(empName);
            }
            showcard(); // Always refresh the display after data is loaded
        } catch (SQLException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "SQL Error during search", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to perform search: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "IO Error during search", ex);
            showAlert(Alert.AlertType.ERROR, "Application Error", "Failed to display search results: " + ex.getMessage());
        }
    }
    @FXML
        void handleAddDepAction(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Add_Dep.fxml"));
        Parent root = loader.load();

        Add_DepController addDepController = loader.getController();
        addDepController.setConnection(con);
        addDepController.initializeDepartments(); // this calls the new logic


        Stage modalStage = new Stage();
        modalStage.setTitle("Add Department");
        modalStage.setScene(new Scene(root));
        modalStage.initModality(Modality.APPLICATION_MODAL);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modalStage.initOwner(currentStage);
        modalStage.showAndWait();

    } catch (IOException e) {
        Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "Failed to load Add_Dep FXML.", e);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("UI Error");
        alert.setHeaderText("Could not open Add_Dep dialog.");
        alert.setContentText("An error occurred while trying to open the Add_Dep.");
        alert.showAndWait();
    }
}

     /* @FXML
    void handleAddDepAction(ActionEvent event) {
        try {
            // Load the FXML for the Update Password dialog
            // Ensure the path is correct relative to your project's resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Add_Dep.fxml"));
            Parent root = loader.load();

            // Get the controller for the Update_Password dialog
            Add_DepController Add_DepController = loader.getController();



            // Create a new stage for the modal dialog
            Stage modalStage = new Stage();
            modalStage.setTitle("Update Password");
            modalStage.setScene(new Scene(root));

            // Set the modality to APPLICATION_MODAL to block interaction with other windows
            modalStage.initModality(Modality.APPLICATION_MODAL);
            
            // Set the owner of the modal stage to the current stage
            // This ensures the modal dialog is always on top of its parent
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            modalStage.initOwner(currentStage);

            // Show the modal dialog and wait for it to close
            modalStage.showAndWait();

            // Optional: Actions to perform after the modal dialog is closed
            // For example, refresh data if needed, or show a confirmation.
            System.out.println("Add_Dep dialog closed.");

        } catch (IOException e) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "Failed to load Add_Dep FXML.", e);
            // Show a JavaFX Alert instead of JOptionPane
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("UI Error");
            alert.setHeaderText("Could not open Add_Dep dialog.");
            alert.setContentText("An error occurred while trying to open the Add_Dep.");
            alert.showAndWait();
        }
    }
*/

    

    @FXML
    private void handleAddAction(ActionEvent event) {
        try {
           /* FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Registration.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register New Employee"); // Set a title for the new window
            stage.show();*/
            
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Registration.fxml")); // Adjust path if needed
            Parent root = loader.load();
            
            RegistrationController registrationController = loader.getController();
           
            Stage reStage = new Stage();
            reStage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal window
            reStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets the owner window
            reStage.setTitle("Emp Registration");
            reStage.setScene(new Scene(root));
            reStage.showAndWait(); // Show the window and wait for it to be closed


        } catch (IOException e) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "Error loading Registration.fxml", e);
            showAlert(Alert.AlertType.ERROR, "Application Error", "Could not load employee registration screen.");
        }
    }

  

    @FXML
    private void handleRefreshAction(ActionEvent event) {
        try {
            combo_status.setValue("All Statuses");     // Reset to "All Statuses"
            combo_department.setValue("All Departments"); // Reset to "All Departments"
            txtsearch.setText("");                     // Clear search text
            initEmp();                                 // Re-fetch data based on "All" selections
            showcard();                                // Re-display cards
          //  showAlert(Alert.AlertType.INFORMATION, "Refresh", "Employee list refreshed successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "SQL Error during refresh", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to refresh employee list: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "IO Error during refresh", ex);
            showAlert(Alert.AlertType.ERROR, "Application Error", "Failed to display refreshed list: " + ex.getMessage());
        }
    }

    // Modified to include "All Departments"
    public void initAllDepartment() throws SQLException {
        departmentList.clear();
        departmentList.add("All Departments"); // Add "All" option first
        String sql = "SELECT department_name FROM department";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departmentList.add(rs.getString("department_name"));
            }
        }
    }

    @FXML
    private void handle_combo_dep_action(ActionEvent event) {
        try {
            // If search text is empty, filter by department and current status
            if (txtsearch.getText().isEmpty()) {
                initEmp();
            } else { // If search text is present, filter by department, status, AND search text
                String empName = "%" + txtsearch.getText() + "%";
                initEmpWithName(empName);
            }
            showcard();
        } catch (SQLException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "SQL Error during department filter", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to filter by department: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "IO Error during department filter", ex);
            showAlert(Alert.AlertType.ERROR, "Application Error", "Failed to display filtered list: " + ex.getMessage());
        }
    }
    
    // Modified to include "All Statuses"
    public void initStatusName() {
        statusList.clear();
        statusList.add("All Statuses"); // Add "All" option first
        statusList.add("Active");
        statusList.add("InActive");
    }

    @FXML
    private void handle_combo_status_action(ActionEvent event) {
        try {
            // If search text is empty, filter by status and current department
            if (txtsearch.getText().isEmpty()) {
                initEmp();
            } else { // If search text is present, filter by status, department, AND search text
                String empName = "%" + txtsearch.getText() + "%";
                initEmpWithName(empName);
            }
            showcard();
        } catch (SQLException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "SQL Error during status filter", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to filter by status: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Employee_1Controller.class.getName()).log(Level.SEVERE, "IO Error during status filter", ex);
            showAlert(Alert.AlertType.ERROR, "Application Error", "Failed to display filtered list: " + ex.getMessage());
        }
    }

    /**
     * Fetches employee data based on currently selected department and status filters.
     * Handles "All Departments" and "All Statuses" selections.
     */
    public void initEmp() throws SQLException {
        empList.clear(); // Clear previous list

        String department = combo_department.getValue();
        String status = combo_status.getValue();
        
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT e.employee_id, e.employee_name, e.position_id, e.department_id, e.Image, e.Status ");
        sqlBuilder.append("FROM employee e ");
        sqlBuilder.append("JOIN department d ON e.department_id = d.department_id ");
        sqlBuilder.append("JOIN position p ON e.position_id = p.position_id ");
        sqlBuilder.append("WHERE 1=1 "); // Always true, makes appending AND clauses easier

        // Add department filter if not "All Departments"
        if (!"All Departments".equals(department)) {
            sqlBuilder.append("AND d.department_name = ? ");
        }

        // Add status filter if not "All Statuses"
        if (!"All Statuses".equals(status)) {
            sqlBuilder.append("AND e.Status = ? ");
        }
        
        // Add ORDER BY clause for consistent display
        sqlBuilder.append("ORDER BY e.employee_name ASC");

        String sql = sqlBuilder.toString();
        
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            int paramIndex = 1;
            if (!"All Departments".equals(department)) {
                pst.setString(paramIndex++, department);
            }
            if (!"All Statuses".equals(status)) {
                pst.setString(paramIndex++, status);
            }
            
            rs = pst.executeQuery();
            while (rs.next()) {
                empList.add(new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("employee_name"),
                    rs.getInt("position_id"),
                    rs.getInt("department_id"),
                    rs.getString("Image"),
                    rs.getString("Status")
                ));
            }
        }
    }
    
    /**
     * Fetches employee data based on search name, and currently selected department and status filters.
     * Handles "All Departments" and "All Statuses" selections.
     * @param empName The search string for employee name (e.g., "%John%").
     */
    public void initEmpWithName(String empName) throws SQLException {
        empList.clear(); // Clear previous list

        String department = combo_department.getValue();
        String status = combo_status.getValue();
        
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT e.employee_id, e.employee_name, e.position_id, e.department_id, e.Image, e.Status ");
        sqlBuilder.append("FROM employee e ");
        sqlBuilder.append("JOIN department d ON e.department_id = d.department_id ");
        sqlBuilder.append("JOIN position p ON e.position_id = p.position_id ");
        sqlBuilder.append("WHERE e.employee_name LIKE ? "); // Always include name filter

        // Add department filter if not "All Departments"
        if (!"All Departments".equals(department)) {
            sqlBuilder.append("AND d.department_name = ? ");
        }

        // Add status filter if not "All Statuses"
        if (!"All Statuses".equals(status)) {
            sqlBuilder.append("AND e.Status = ? ");
        }
        
        // Add ORDER BY clause for consistent display
        sqlBuilder.append("ORDER BY e.employee_name ASC");

        String sql = sqlBuilder.toString();
        
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            int paramIndex = 1;
            pst.setString(paramIndex++, empName); // Set employee name first
            
            if (!"All Departments".equals(department)) {
                pst.setString(paramIndex++, department);
            }
            if (!"All Statuses".equals(status)) {
                pst.setString(paramIndex++, status);
            }
            
            rs = pst.executeQuery();
            while (rs.next()) {
                empList.add(new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("employee_name"),
                    rs.getInt("position_id"),
                    rs.getInt("department_id"),
                    rs.getString("Image"),
                    rs.getString("Status")
                ));
            }
        }
    }

    public void showcard() throws IOException {
        // Clear existing cards from the grid
        menu_grid.getChildren().clear();
        menu_grid.getRowConstraints().clear();
        menu_grid.getColumnConstraints().clear();

        int row = 0;
        int column = 0;
        
        if (!empList.isEmpty()) {
            for (Employee emp : empList) {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/view/EmpcardDesign.fxml"));
                AnchorPane pane = load.load();
                
                EmpcardDesignController cardC = load.getController();
                cardC.setConnection(con); // Pass the connection to the card controller
                cardC.setData(emp);
                
                if (column == 3) { // 3 cards per row
                    column = 0;
                    row++;
                }

                menu_grid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));
            }
        } else {
            // Optionally display a message if no employees are found
            // For example, add a Label to the grid
            Label noResultsLabel = new Label("No employees found matching the criteria.");
            menu_grid.add(noResultsLabel, 0, 0);
            GridPane.setMargin(noResultsLabel, new Insets(20));
        }
        
        // Set the content of the ScrollPane to the updated GridPane
        menu_scropane.setContent(menu_grid);
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
}