/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database; 
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback; 
import model.Food;

import static hotel_management.Hotel_Management.stage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert; 
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class AdminFoodController implements Initializable {

    @FXML
    private Pagination paginationFood;
    private int cards_per_page = 6; 
    private int total_food_items;
    private int pageCount;

    @FXML
   private ComboBox<String> comboMeal; 
    @FXML
    private ComboBox<String> comboCuisine; 
    @FXML
    private TextField txtFoodName;
    @FXML
    private TextField txtFoodPrice;
    @FXML
    private ComboBox<String> comboStatus; 
    @FXML
    private ComboBox<String> comboSearchCuisine; 
    @FXML
    private TextField txtSearchFood;
    @FXML
    private ImageView foodImage; 
    @FXML
    private Label lblFoodName; 
    @FXML
    private Label lblFoodPrice; 

    private ObservableList<Food> allFoodItems = FXCollections.observableArrayList();
    private ObservableList<Food> filteredFoodList = FXCollections.observableArrayList();

    private Database db;
    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs;
    @FXML
    private ImageView food_img;
    @FXML
    private AnchorPane cuisineAnchor;
 @FXML
    private AnchorPane foodAnchor;
 @FXML
    private TextField txtNewCusine;
    @FXML
    private GridPane gridBtn;


    @FXML
    public Button btnCreat;
     @FXML
    public Button btnCancel;

    @FXML
    public Button btnUpdate;
  @FXML
    public Button btnAddNewFood;

// NEW: Variable to hold the ID of the food currently being edited
    private int currentEditingFoodId = -1; 
    String savedImageFileName;

    File selectedFileForUpload;
    private final String IMAGE_STORAGE_DIR = "C:\\Users\\Dell\\OneDrive\\Desktop\\Khant Zaw Win Hotel\\Myat Thu Hotel\\src\\image\\";

    /**
     *  initialize 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Database();
        cuisineAnchor.setVisible(false);
        btnUpdate.setVisible(false);
        btnCancel.setVisible(false);
                btnAddNewFood.setVisible(true);

        try {
            con = db.getConnection();
            populateComboBoxes();
            loadAllFoodItems(); 
            loadCuisineButtons();
            setupPagination(allFoodItems); 
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Check database connection setting ");
        }
       
        if (comboSearchCuisine != null) {
            comboSearchCuisine.valueProperty().addListener((obs, oldValue, newValue) -> filterFoods());
        }
        if (txtSearchFood != null) {
            txtSearchFood.textProperty().addListener((obs, oldValue, newValue) -> filterFoods());
        }
        
    }
    @FXML
    private void handleCreateNewCuisine(ActionEvent event) {
        String newCuisineName = txtNewCusine.getText().trim(); // Get text and trim whitespace

       if (newCuisineName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Missing", "Please enter a cuisine name.");
            return;
        }

        if (isCuisineNameDuplicate(newCuisineName)) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Entry", "Cuisine '" + newCuisineName + "' already exists.");
            return;
        }

          // --- Confirmation Alert Added Here ---
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm New Cuisine Creation");
        confirmationAlert.setHeaderText("Create New Cuisine?");
        confirmationAlert.setContentText("Are you sure you want to create '" + newCuisineName + "' as a new cuisine type?");

        // Show the alert and wait for user's response
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Check if the user clicked OK (or another confirmation button)
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed, proceed with database insertion
            String sql = "INSERT INTO food_type (food_type_name) VALUES (?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, newCuisineName);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Cuisine '" + newCuisineName + "' created successfully!");
                    txtNewCusine.clear(); // Clear the text field
                    populateComboBoxes(); // Refresh combo boxes with new data
                    loadCuisineButtons(); // Reload dynamic cuisine buttons
                    filterFoods(); // Reapply filters to update cards
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed", "Failed to create new cuisine.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Error creating new cuisine", ex);
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to create new cuisine: " + ex.getMessage());
            }
        } 
    }

    // Helper method to check for duplicate cuisine names
    private boolean isCuisineNameDuplicate(String cuisineName) {
        String sql = "SELECT COUNT(*) FROM food_type WHERE food_type_name = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cuisineName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Duplicate found
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Error checking for duplicate cuisine", ex);
            // Handle this error appropriately, perhaps log and assume no duplicate to proceed
        }
        return false; // No duplicate
    }

      @FXML
    void handleCancel(ActionEvent event) {
   // Reset the flag indicating no food item is being edited
        currentEditingFoodId = -1;

        // Clear all input fields
        clearInputFields();

        // Adjust button visibility for "Add New Food" mode
        btnAddNewFood.setVisible(true); // Show the "Add New Food" button
        btnUpdate.setVisible(false);    // Hide the "Update" button
        btnCancel.setVisible(false);    // Hide the "Cancel" button

        // Re-enable ComboBoxes as they were disabled during edit mode
        comboMeal.setDisable(false);
        comboCuisine.setDisable(false);

        // Optionally, ensure the food entry pane is visible if it was hidden
        foodAnchor.setVisible(true);
        cuisineAnchor.setVisible(false); // Make sure cuisine pane is hidden
    }
     @FXML
    void handleUpdate(ActionEvent event) {
         if (currentEditingFoodId == -1) {
            showAlert(Alert.AlertType.ERROR, "No Food Selected", "Please select a food item to update.");
            return;
        }

        String foodName = txtFoodName.getText().trim();
        String foodPriceStr = txtFoodPrice.getText().trim(); // Get price as string for validation
        String status = comboStatus.getSelectionModel().getSelectedItem();

        // --- Input Validations (similar to addNewFood) ---
        if (foodName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Missing", "Please enter the Food Name.");
            return;
        }

        if (foodPriceStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Missing", "Please enter the Food Price.");
            return;
        }

        int foodPrice;
        try {
            foodPrice = Integer.parseInt(foodPriceStr);
            if (foodPrice < 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Price", "Food price cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Price", "Please enter a valid number for food price.");
            return;
        }

        if (status == null || status.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please select a Status (available/unavailable).");
            return;
        }

        // Image handling for update:
        String imageFileNameToSave = savedImageFileName; // Use the existing saved file name by default

        // If a new image was selected for upload, copy it and update the image file name
        if (selectedFileForUpload != null) {
            try {
                Path destDir = Paths.get(IMAGE_STORAGE_DIR); // IMAGE_STORAGE_DIR should be defined as a constant
                if (!Files.exists(destDir)) {
                    Files.createDirectories(destDir);
                }
                Path destFile = destDir.resolve(selectedFileForUpload.getName());
                Files.copy(selectedFileForUpload.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
                imageFileNameToSave = selectedFileForUpload.getName(); // Update to the new image name
                System.out.println("New image copied successfully: " + destFile.toAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Error copying new image for update", ex);
                showAlert(Alert.AlertType.ERROR, "Image Error", "Failed to save new image: " + ex.getMessage());
                return; // Stop update if image cannot be saved
            }
        }
        // If selectedFileForUpload is null, it means no new image was chosen,
        // so imageFileNameToSave remains the original savedImageFileName.

        // SQL for updating food details
        // Note: We are updating food_name, food_price, food_image, and status.
        // food_type_id and meal_course_id are usually fixed for an existing food item
        // or would require separate logic if they can be changed during update.
        // As per your request, we only update food_name, food_image, and status.
        // If food_price should also be updated, include it in the query.
        String sql = "UPDATE food SET food_name = ?, food_price = ?, food_image = ?, status = ? WHERE food_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, foodName);
            ps.setInt(2, foodPrice); // Assuming food_price is an INT
            ps.setString(3, imageFileNameToSave);
            ps.setString(4, status); // Directly use the selected status text
            ps.setInt(5, currentEditingFoodId); // Use the stored ID for the WHERE clause

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Food item updated successfully!");
                clearInputFields();
                //loadAllFoodItems(); // Refresh the table/cards
                //filterFoods(); // Reapply filters to update cards display
                handleCancel(null); // Reset UI to "add new" mode (hide update/cancel, show add new)
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update food item. No changes were made or food ID not found.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Database update error", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update food item: " + ex.getMessage());
        }

    }
   
     // NEW: Method to populate food details for editing
    public void editFood(Food food) {
        currentEditingFoodId = food.getFoodId(); // Store the ID of the food being edited
        foodAnchor.setVisible(true); // Make sure the editing pane is visible
        cuisineAnchor.setVisible(false); // Hide cuisine creation pane if it's visible
         btnUpdate.setVisible(true);
        btnCancel.setVisible(true);
        btnAddNewFood.setVisible(false);
        // Populate text fields
        txtFoodName.setText(food.getFoodName());
        txtFoodPrice.setText(String.valueOf(food.getFoodPrice()));

        // Populate image
        String imagePath = IMAGE_STORAGE_DIR + food.getFoodImage();
        File file = new File(imagePath);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            food_img.setImage(image);
            savedImageFileName = food.getFoodImage(); // Store the current image file name
            selectedFileForUpload = file; // Store the file for potential re-upload/update
        } else {
            food_img.setImage(null); // Clear image if not found
            System.err.println("Image file not found: " + imagePath);
            savedImageFileName = null;
            selectedFileForUpload = null;
        }

        // Populate ComboBoxes
        try {
            String mealCourseName = getMealCourseNameById(food.getMealCourseId());
            if (mealCourseName != null) {
                comboMeal.getSelectionModel().select(mealCourseName);
                
            } else {
                comboMeal.getSelectionModel().clearSelection();
            }

            String foodTypeName = getFoodTypeNameById(food.getFoodTypeId());
            if (foodTypeName != null) {
                comboCuisine.getSelectionModel().select(foodTypeName);
            } else {
                comboCuisine.getSelectionModel().clearSelection();
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Error populating combo boxes for edit", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load meal/cuisine types for editing.");
        }

        // Populate Status ComboBox
        comboStatus.getSelectionModel().select(food.getStatus());
    comboMeal.setDisable(true);
        comboCuisine.setDisable(true);
     
        // You might want to change the "Add New Food" button's text to "Update Food" here
        // For example: btnAddNewFood.setText("Update Food");
        // And then in handleAddNewFood, check currentEditingFoodId to determine if it's an Add or Update
    }
     private void loadCuisineButtons() {
        
        gridBtn.getChildren().clear(); // Clear existing buttons
        int col = 0;
        int row = 0;

        String sql = "SELECT food_type_name FROM food_type";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String foodTypeName = rs.getString("food_type_name");
                Button button = new Button(foodTypeName);
                button.setPrefSize(94.0, 53.0); // Match your FXML button size

                 // Set a margin for each button 
                GridPane.setMargin(button, new Insets(5)); 

                // Set action for the button
                button.setOnAction(event -> {
                    if (currentEditingFoodId != -1) {
                        showAlert(Alert.AlertType.WARNING, "Cannot Change Cuisine", "Can't choose cuisine while editing a food item. Please cancel or update first.");
                        return; 
                    }
                    String clickedCuisine = ((Button) event.getSource()).getText();
                    comboCuisine.getSelectionModel().select(clickedCuisine);
                    cuisineAnchor.setVisible(false);
                    if (foodAnchor != null) {
                         foodAnchor.setVisible(true);
                    }
                });

                gridBtn.add(button, col, row);


                col++;
                if (col == 4) { // 2 columns as per your GridPane column constraints
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Error loading cuisine buttons", ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load cuisine types for buttons.");
        }
    }
    private void loadAllFoodItems() throws SQLException {
        String sql = "SELECT food_id, food_name, food_price, food_image, food_type_id, meal_course_id, status FROM food";
        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            allFoodItems.clear();
            while (rs.next()) {
                allFoodItems.add(new Food(
                        rs.getInt("food_id"),
                        rs.getString("food_name"),
                        rs.getInt("food_price"),
                        rs.getString("food_image"),
                        rs.getInt("food_type_id"),
                        rs.getInt("meal_course_id"),
                        rs.getString("status") 
                ));
            }
        }
        filteredFoodList.setAll(allFoodItems);
    }

    private void setupPagination(ObservableList<Food> listToPaginate) {
        total_food_items = listToPaginate.size();
        pageCount = (int) Math.ceil((double) total_food_items / cards_per_page);
        paginationFood.setPageCount(pageCount > 0 ? pageCount : 1);
       // paginationFood.setCurrentPageIndex(0);
        paginationFood.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return createFoodPage(pageIndex, listToPaginate);
                } catch (IOException ex) {
                    Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, null, ex);
                    return new GridPane();
                }
            }
        });
    }

    public Node createFoodPage(int pageIndex, ObservableList<Food> listToPaginate) throws IOException {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(20));

        int startIndex = pageIndex * cards_per_page;
        int endIndex = Math.min(startIndex + cards_per_page, listToPaginate.size());

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            if (i < listToPaginate.size()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/FoodCard.fxml"));

                try {
                    AnchorPane pane = loader.load();
                    FoodCardController foodCardController = loader.getController();

                    foodCardController.setRole("Admin"); 
                    foodCardController.setData(listToPaginate.get(i)); 
                foodCardController.setAdminFoodController(this); // <--- ADD THIS LINE
                    grid.add(pane, col, row);

                    col++;
                    if (col == 3) { 
                        col = 0;
                        row++;
                    }
                } catch (IOException ex) {
                    System.err.println("Failed to load FoodCard.fxml or create controller for food: "+ listToPaginate.get(i).getFoodName() + " - " + ex.getMessage());
                    ex.printStackTrace();
                    return new GridPane();
                }
            }
        }
        return grid;
    }

    private void populateComboBoxes() throws SQLException {
        ObservableList<String> mealCourseNames = FXCollections.observableArrayList();
        String sqlMeal = "SELECT meal_course_name FROM meal_course";
        try (PreparedStatement psMeal = con.prepareStatement(sqlMeal);
             ResultSet rsMeal = psMeal.executeQuery()) {
            while (rsMeal.next()) {
                mealCourseNames.add(rsMeal.getString("meal_course_name"));
            }
        }
        comboMeal.setItems(mealCourseNames);

        ObservableList<String> foodTypeNames = FXCollections.observableArrayList();
        String sqlFoodType = "SELECT food_type_name FROM food_type";
        try (PreparedStatement psFoodType = con.prepareStatement(sqlFoodType);
             ResultSet rsFoodType = psFoodType.executeQuery()) {
            while (rsFoodType.next()) {
                foodTypeNames.add(rsFoodType.getString("food_type_name"));
            }
        }
        comboCuisine.setItems(foodTypeNames);

        ObservableList<String> searchCuisineNames = FXCollections.observableArrayList();
        searchCuisineNames.add("All"); 
        searchCuisineNames.addAll(foodTypeNames); 
        comboSearchCuisine.setItems(searchCuisineNames);
        comboSearchCuisine.getSelectionModel().selectFirst(); 

        comboStatus.setItems(FXCollections.observableArrayList("available", "unavailable"));
        comboStatus.getSelectionModel().selectFirst(); 
    }

    private int getMealCourseIdByName(String mealCourseName) throws SQLException {
        if (mealCourseName == null || mealCourseName.isEmpty()) return -1;
        String sql = "SELECT meal_course_id FROM meal_course WHERE meal_course_name = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mealCourseName);
            try (ResultSet rs_id = ps.executeQuery()) {
                if (rs_id.next()) {
                    return rs_id.getInt("meal_course_id");
                }
            }
        }
        return -1;
    }

    private int getFoodTypeIdByName(String foodTypeName) throws SQLException {
        if (foodTypeName == null || foodTypeName.isEmpty()) return -1;
        String sql = "SELECT food_type_id FROM food_type WHERE food_type_name = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, foodTypeName);
            try (ResultSet rs_id = ps.executeQuery()) {
                if (rs_id.next()) {
                    return rs_id.getInt("food_type_id");
                }
            }
        }
        return -1;
    }

   private void filterFoods() {
    String searchFoodText = txtSearchFood.getText() == null ? "" : txtSearchFood.getText().toLowerCase();
    String selectedCuisine = comboSearchCuisine.getSelectionModel().getSelectedItem();

    filteredFoodList.clear();

    for (Food food : allFoodItems) {
        boolean matchesSearch = food.getFoodName().toLowerCase().contains(searchFoodText);
        boolean matchesCuisine = true;

        if (selectedCuisine != null && !selectedCuisine.equals("All")) {
            try {
                String foodTypeName = getFoodTypeNameById(food.getFoodTypeId());
                if (foodTypeName == null || !foodTypeName.equals(selectedCuisine)) {
                    matchesCuisine = false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, null, ex);
                matchesCuisine = false;
            }
        }

        if (matchesSearch && matchesCuisine) { 
            filteredFoodList.add(food);
        }
    }
    setupPagination(filteredFoodList);
}

    private String getMealCourseNameById(int mealCourseId) throws SQLException {
        if (mealCourseId == -1) return null;
        String sql = "SELECT meal_course_name FROM meal_course WHERE meal_course_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mealCourseId);
            try (ResultSet rs_name = ps.executeQuery()) {
                if (rs_name.next()) {
                    return rs_name.getString("meal_course_name");
                }
            }
        }
        return null;
    }

    private String getFoodTypeNameById(int foodTypeId) throws SQLException {
        if (foodTypeId == -1) return null;
        String sql = "SELECT food_type_name FROM food_type WHERE food_type_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, foodTypeId);
            try (ResultSet rs_name = ps.executeQuery()) {
                if (rs_name.next()) {
                    return rs_name.getString("food_type_name");
                }
            }
        }
        return null;
    }

    @FXML
    void handleSearchFood(ActionEvent event) {
        filterFoods();
    }

    @FXML
    void handleFood(ActionEvent event) {
   cuisineAnchor.setVisible(false);
        foodAnchor.setVisible(true); // Ensure food entry pane is visible
        // When switching back to food, ensure correct buttons are visible
        if (currentEditingFoodId == -1) { // If not currently editing
            btnAddNewFood.setVisible(true);
            btnUpdate.setVisible(false);
            btnCancel.setVisible(false);
            clearInputFields(); // Clear fields when moving to add new food mode
            comboMeal.setDisable(false);
            comboCuisine.setDisable(false);
        } else { // If still editing
            btnAddNewFood.setVisible(false);
            btnUpdate.setVisible(true);
            btnCancel.setVisible(true);
            // Don't clear fields, keep the editing data
        }        
    }

    @FXML
    void handleCuisine(ActionEvent event) {
              cuisineAnchor.setVisible(true);
              btnCancel.setVisible(false);
              btnUpdate.setVisible(false);
              

    }

   @FXML
private void handleAddNewFood(ActionEvent event) {
    if (selectedFileForUpload == null || savedImageFileName == null || savedImageFileName.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Image Missing", "Please select an image for the new food item.");
        return;
    }
    
    String foodName = txtFoodName.getText();
    String foodPriceStr = txtFoodPrice.getText();
     if (foodName.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Input Missing", "Please enter the Food Name.");
        return;
    }

    if (foodPriceStr.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Input Missing", "Please enter the Food Price.");
        return;
    }
      

    String mealCourseName = comboMeal.getSelectionModel().getSelectedItem();
    String foodTypeName = comboCuisine.getSelectionModel().getSelectedItem();
    if (mealCourseName == null) {
        showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please select a Meal Course.");
        return;
    }

    if (foodTypeName == null) {
        showAlert(Alert.AlertType.WARNING, "Selection Missing", "Please select a Cuisine Type.");
        return;
    }
    String status = comboStatus.getSelectionModel().getSelectedItem();

   

    int foodPrice;
    try {
        foodPrice = Integer.parseInt(foodPriceStr);
        if (foodPrice < 0) {
            showAlert(Alert.AlertType.WARNING, "Invalid Price", "Food price cannot be negative.");
            return;
        }
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.WARNING, "Invalid Price", "Please enter a valid number for food price.");
        return;
    }

    try {
        Path destDir = Paths.get(IMAGE_STORAGE_DIR);
        if (!Files.exists(destDir)) {
            Files.createDirectories(destDir); // Create directory if it doesn't exist
        }
        Path destFile = destDir.resolve(savedImageFileName);
        Files.copy(selectedFileForUpload.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Image copied successfully: " + destFile.toAbsolutePath());

        int foodTypeId = getFoodTypeIdByName(foodTypeName);
        int mealCourseId = getMealCourseIdByName(mealCourseName);

        if (foodTypeId == -1) {
            showAlert(Alert.AlertType.ERROR, "Invalid Food Type", "Selected food type not found.");
            return;
        }
        if (mealCourseId == -1) {
            showAlert(Alert.AlertType.ERROR, "Invalid Meal Course", "Selected meal course not found.");
            return;
        }

        // Insert into database
        String sql = "INSERT INTO food (food_name, food_price, food_image, food_type_id, meal_course_id, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, foodName);
            ps.setDouble(2, foodPrice); // Assuming foodPrice is inserted as double in DB
            ps.setString(3, savedImageFileName);
            ps.setInt(4, foodTypeId);
            ps.setInt(5, mealCourseId);
            ps.setString(6, status);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "New food item added successfully!");
                clearInputFields(); // Clear fields after successful insertion
                //loadAllFoodItems(); // Refresh the food list
                //filterFoods(); // Reapply filters to correctly refresh the view
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to add new food item.");
            }
        }
    } catch (IOException ex) {
        Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Image copy error", ex);
        showAlert(Alert.AlertType.ERROR, "Image Error", "Failed to save image: " + ex.getMessage());
    } catch (SQLException ex) {
        Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Database insert error", ex);
        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to insert food into database: " + ex.getMessage());
    }
}

private void clearInputFields() {
    txtFoodName.clear();
    txtFoodPrice.clear();
    comboMeal.getSelectionModel().clearSelection();
    comboCuisine.getSelectionModel().clearSelection();
    comboStatus.getSelectionModel().selectFirst(); // Reset status to default "available"
    food_img.setImage(null); // Clear image preview
    savedImageFileName = null;
    selectedFileForUpload = null;
}

@FXML
private void handleUploadImageAction(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Image");
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
    );

    selectedFileForUpload = fileChooser.showOpenDialog(stage); // Use your main stage reference

    if (selectedFileForUpload != null) {
        try {
            // Get only the file name (e.g., "myimage.png")
            savedImageFileName = selectedFileForUpload.getName();

            // Display image preview immediately after selection
            Image img = new Image(selectedFileForUpload.toURI().toString());
            food_img.setImage(img);
            food_img.setPreserveRatio(true); // Preserve aspect ratio
            System.out.println("Image selected for upload: " + selectedFileForUpload.getAbsolutePath()); // Changed to English

            // Actual file copying happens when 'Add' or 'Update' is clicked.
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Image Load Error", "Failed to load image: " + e.getMessage()); // Changed to English
            Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, "Error loading image for preview", e);
            savedImageFileName = null; 
            selectedFileForUpload = null; 
        }
    } else {
        System.out.println("No image file selected."); 
    }
}

    @FXML
    void handleRefresh(ActionEvent event) throws SQLException {
        try {
        final int currentPageIndex = paginationFood.getCurrentPageIndex();

        loadAllFoodItems();

        filterFoods();
        
        int newPageCount = paginationFood.getPageCount();
        int targetPageIndex = currentPageIndex;

        if (targetPageIndex >= newPageCount && newPageCount > 0) {
            targetPageIndex = newPageCount - 1;
        } else if (newPageCount == 0) {
            targetPageIndex = 0;
        }
        
        paginationFood.setCurrentPageIndex(targetPageIndex);
    
    } catch (SQLException ex) {
        Logger.getLogger(AdminFoodController.class.getName()).log(Level.SEVERE, null, ex);
        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to refresh data: " + ex.getMessage());
    }
       
    }
private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}