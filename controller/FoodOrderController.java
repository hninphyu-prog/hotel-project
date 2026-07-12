/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.Database;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.Food;
import javafx.scene.layout.Region; 
import model.FoodCartItem;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
/**
 * FXML Controller class
 *
 * @author Dell
 */
public class FoodOrderController implements Initializable {

    @FXML
    private HBox btn_container; // HBox for food type buttons
    @FXML
    private Label lblCuisine; 
    //for pagination
    @FXML
    private Pagination paginationFood;
    private int cards_per_page = 6; // Number of food cards to show per page
    private int total_food_items;
    private int pageCount;
    @FXML
    private ComboBox<String> comboMeal; 

    private ObservableList<Food> allFoodItems = FXCollections.observableArrayList();

    private ObservableList<Food> filteredFoodList = FXCollections.observableArrayList();
 @FXML
    private TableView<FoodCartItem> tbCart; // Specify the model type for TableView
    @FXML
    private TableColumn<FoodCartItem, Integer> colNo; // Use FoodCartItem as the row type
    @FXML
    private TableColumn<FoodCartItem, String> colItem;
    @FXML
    private TableColumn<FoodCartItem, Double> colPrice;
    @FXML
    private TableColumn<FoodCartItem, Integer> colQty;
    @FXML
    private TableColumn<FoodCartItem, Double> colAmount;
    @FXML
    private TableColumn<FoodCartItem, Void> colAction; // For the remove button

    // ObservableList to hold items in the cart
    public static ObservableList<FoodCartItem> cartItems = FXCollections.observableArrayList();

    private Database db;
    private Connection con = null;

    private PreparedStatement pst;
    private ResultSet rs;

    // To keep track of the currently active food type button for styling purposes
    private Button currentActiveFoodTypeButton = null;
   @FXML
    private Button btnConfirm;

    
  
    @FXML
    private TextField txtTotal;

    @FXML
    private Button btnEnter;
    private StaffPageController staffPageController;
    
    public void setStaffPageController(StaffPageController staffPageController) {
        this.staffPageController = staffPageController;
    }
    private String currentUserRole; // To store the role

public void setCurrentUserRole(String role) {
    this.currentUserRole = role;
    try {
        filterFoodItemsByMealCourseAndType(); 
    } catch (SQLException ex) {
        Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_container.setSpacing(10);
        db = new Database();
        try {
            con = db.getConnection(); 
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setUpRoomNoBtn();
        ObservableList<String> mealCourseNames = FXCollections.observableArrayList();
        String sql = "Select meal_course_id,meal_course_name from meal_course";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String name = rs.getString("meal_course_name");
                mealCourseNames.add(name);
            }
            comboMeal.setItems(mealCourseNames);

               comboMeal.valueProperty().addListener((obs, oldValue, newValue) -> {
                try {
                    if (currentActiveFoodTypeButton != null && "Popular".equals(currentActiveFoodTypeButton.getText())) {
                        loadPopularFoodItemsForSelectedMealCourse(); // Re-load popular if "Popular" is active
                    } else {
                        filterFoodItemsByMealCourseAndType(); // Otherwise, do standard filtering
                    }
                    // MODIFICATION END
                } catch (SQLException ex) {
                    Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            comboMeal.getSelectionModel().selectFirst();

        } catch (SQLException ex) {
            Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            loadAllFoodItems(); // Load ALL food items from the database into 'allFoodItems' list
            loadFoodTypeButtons(); // Dynamically load and setup food type buttons (including an "ALL" button)
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            filterFoodItemsByMealCourseAndType();
        } catch (SQLException ex) {
            Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
         setupCartTableView();
           if(Check_outController.room_id==0){
            btnEnter.setText("Enter Room No");
            
        }else{
            btnEnter.setText(String.valueOf(Check_outController.room_id));
        }
           calculateTotal();
    }
      private void loadPopularFoodItemsForSelectedMealCourse() throws SQLException {
        filteredFoodList.clear();
        String selectedMealCourseName = comboMeal.getSelectionModel().getSelectedItem();
        int mealCourseId = -1;

        if (selectedMealCourseName != null && !selectedMealCourseName.isEmpty()) {
            mealCourseId = getMealCourseIdByName(selectedMealCourseName);
        }

        if (mealCourseId != -1) {
            String sql = "SELECT f.food_id, f.food_name, f.food_price, f.food_image, f.food_type_id, f.meal_course_id, SUM(fod.quantity) AS total_quantity " +
                         "FROM food_order_detail fod " +
                         "JOIN food_order fo ON fod.food_order_id = fo.food_order_id " +
                         "JOIN food f ON fod.food_id = f.food_id " +
                         "WHERE f.meal_course_id = ? " +
                         "GROUP BY f.food_id, f.food_name, f.food_price, f.food_image, f.food_type_id, f.meal_course_id " +
                         "ORDER BY total_quantity DESC, f.food_name ASC limit 3";

            pst = con.prepareStatement(sql);
            pst.setInt(1, mealCourseId);
            rs = pst.executeQuery();

            while (rs.next()) {
                filteredFoodList.add(new Food(
                    rs.getInt("food_id"),
                    rs.getString("food_name"),
                    rs.getInt("food_price"),
                    rs.getString("food_image"),
                    rs.getInt("food_type_id"),
                    rs.getInt("meal_course_id")
                ));
            }
        }
        setupPagination(filteredFoodList);
    }
 private void setupCartTableView() {
        // Set up cell value factories for each column
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
         colQty.setCellFactory(column -> new QuantityCell(this));
        // Set up the "Action" column with a button to remove items
        colAction.setCellFactory(param -> new TableCell<FoodCartItem, Void>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(event -> {
                    FoodCartItem itemToRemove = getTableView().getItems().get(getIndex());
                    cartItems.remove(itemToRemove);
                    updateCartTableNumbers(); // Re-number the "No." column
                    calculateTotal(); // Recalculate total after removal
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });

        // Set the items for the TableView
        tbCart.setItems(cartItems);
        
        // Listen for changes in cartItems to update total
        cartItems.addListener((javafx.collections.ListChangeListener.Change<? extends FoodCartItem> change) -> {
            calculateTotal();
        });
      
    }
 
     // --- Method to handle adding food to the cart ---
   public void addFoodToCart(Food food) {
        boolean found = false;
        for (FoodCartItem item : cartItems) {
            if (item.getItemName().equals(food.getFoodName())) { 
                TextInputDialog dialog = new TextInputDialog("1");
                dialog.setTitle("Update Quantity");
                dialog.setHeaderText("Enter quantity to add to " + food.getFoodName());
                dialog.setContentText("Quantity:");
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    try {
                        int quantityToAdd = Integer.parseInt(result.get());
                        if (quantityToAdd > 0) {
                            item.setQuantity(item.getQuantity() + quantityToAdd);
                            tbCart.refresh(); 
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid quantity entered: " + result.get());
                    }
                }
                found = true; 
                break;
            }
        }

        if (!found) {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Add to Cart");
            dialog.setHeaderText("Enter quantity for " + food.getFoodName());
            dialog.setContentText("Quantity:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                try {
                    int initialQuantity = Integer.parseInt(result.get());
                    if (initialQuantity > 0) {
                        int newNo = cartItems.size() + 1; 
                        FoodCartItem newItem = new FoodCartItem(newNo, food.getFoodName(), food.getFoodPrice(), initialQuantity, food); // ပြင်ဆင်ထားသော လိုင်း
                        cartItems.add(newItem);
                        updateCartTableNumbers(); 
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid quantity entered: " + result.get());
                }
            }
        }
        calculateTotal();
    }
    
    private void updateCartTableNumbers() {
        for (int i = 0; i < cartItems.size(); i++) {
            cartItems.get(i).setNo(i + 1);
        }
        tbCart.refresh(); 
    }

    public void calculateTotal() {
        int total = 0;
        for (FoodCartItem item : cartItems) {
            total += item.getAmount();
        }
           txtTotal.setText(String.valueOf(total));
    }
    public void removeItemFromCart(FoodCartItem item) {
        cartItems.remove(item);
        updateCartTableNumbers(); 
        calculateTotal();
    }
    
    @FXML
    void handleEnterAction(ActionEvent event) throws IOException {
      if (staffPageController != null) {
            staffPageController.loadFXMLIntoDefaultAnchor("/view/Check_out.fxml");
        } else {
            System.err.println("StaffPageController reference is null. Cannot load FXML.");
           
        }
    }

    @FXML
    private void handleConfirmAction(ActionEvent event) {
         if (Check_outController.room_id == 0) {
            showAlert(AlertType.WARNING, "No Room Selected", "Please select a room  before confirming the order.");
            return; 
        }
         if (cartItems.isEmpty()) {
            showAlert(AlertType.WARNING, "Cart Empty", "Please add items to the cart before confirming the order.");
            return;
        }   
        int empId = 1; // this part will modify later,now use default data


        Connection orderCon = null; 
        PreparedStatement pstFoodOrder = null;
        PreparedStatement pstFoodOrderDetail = null;
        ResultSet generatedKeys = null;

        try {
            orderCon = db.getConnection(); 
            orderCon.setAutoCommit(false); 
            // 1. Insert into food_order table
            String insertFoodOrderSQL = "INSERT INTO food_order (booking_detail_id, food_order_date, emp_id,food_order_status) VALUES (?, ?, ?,?)";
            pstFoodOrder = orderCon.prepareStatement(insertFoodOrderSQL, Statement.RETURN_GENERATED_KEYS);

            pstFoodOrder.setInt(1, Check_outController.booking_detail_id);
            pstFoodOrder.setDate(2, java.sql.Date.valueOf(LocalDate.now())); 
            pstFoodOrder.setInt(3, StaffPageController.empID); 
            pstFoodOrder.setString(4, "Preparing");
            
            int affectedRows = pstFoodOrder.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating food order failed, no rows affected.");
            }

            // Get the generated food_order_id
            generatedKeys = pstFoodOrder.getGeneratedKeys();
            int foodOrderId = -1;
            if (generatedKeys.next()) {
                foodOrderId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating food order failed, no ID obtained.");
            }

            // 2. Insert into food_order_detail table for each item in cart
            String insertFoodOrderDetailSQL = "INSERT INTO food_order_detail (food_order_id, food_id, quantity, price) VALUES (?, ?, ?, ?)";
            pstFoodOrderDetail = orderCon.prepareStatement(insertFoodOrderDetailSQL);

            for (FoodCartItem item : cartItems) {
                pstFoodOrderDetail.setInt(1, foodOrderId);

                if (item.getOriginalFood() == null || item.getOriginalFood().getFoodId() == 0) {
                     // Log or throw an error if foodId is missing
                     System.err.println("Error: Food ID is missing for item: " + item.getItemName());
                     throw new SQLException("Food ID is missing for item: " + item.getItemName());
                }
                pstFoodOrderDetail.setInt(2, item.getOriginalFood().getFoodId()); 
                pstFoodOrderDetail.setInt(3, item.getQuantity()); 
                pstFoodOrderDetail.setDouble(4, item.getPrice()); 
                pstFoodOrderDetail.addBatch(); 
            }

            pstFoodOrderDetail.executeBatch(); 
            orderCon.commit(); 

  showAlert(AlertType.INFORMATION, "Order Confirmed",
                    "Room " + Check_outController.room_id + ", Order No. " + foodOrderId + ", successfully placed!");

            txtTotal.clear();
            cartItems.clear();
            tbCart.refresh();
            calculateTotal(); 

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, "Error confirming order", ex);
            if (orderCon != null) {
                try {
                    orderCon.rollback();
                } catch (SQLException e1) {
                    Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, "Error during rollback", e1);
                }
            }
            showAlert(AlertType.ERROR, "Order Failed", "Failed to place order: " + ex.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstFoodOrder != null) pstFoodOrder.close();
                if (pstFoodOrderDetail != null) pstFoodOrderDetail.close();
                if (orderCon != null) orderCon.close();
            } catch (SQLException ex) {
                Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, "Error closing resources", ex);
            }
        }
        /* try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FoodBill.fxml")); // Adjust path if needed
            Parent root = loader.load();
            
            FoodBillController foodBillController = loader.getController();
             foodBillController.setFoodOrderController(this);
            Stage foodBillStage = new Stage();
            foodBillStage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal window
            foodBillStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets the owner window
            //foodBillStage.setTitle("Food Bill");
            foodBillStage.setScene(new Scene(root));
            foodBillStage.initStyle(StageStyle.UNDECORATED); // Optional: remove window decorations
            foodBillStage.showAndWait(); // Show the window and wait for it to be closed

                        
        } catch (IOException e) {
            Logger.getLogger(RoomBookingController.class.getName()).log(Level.SEVERE, "Error loading FoodBill.fxml", e);
            JOptionPane.showMessageDialog(null, "Error opening food bill  form: " + e.getMessage());
        }*/
        Check_outController.booking_detail_id=0;
        Check_outController.room_id=0;
        setUpRoomNoBtn();
    }

private class QuantityCell extends TableCell<FoodCartItem, Integer> {
        private final Button btnInc = new Button("+");
        private final Button btnDec = new Button("-");
        private final Label lblQty = new Label();
        private final HBox hbox = new HBox(5, btnDec, lblQty, btnInc);

        private FoodOrderController controller;

        public QuantityCell(FoodOrderController controller) {
            this.controller = controller;
            hbox.setAlignment(Pos.CENTER);
            btnDec.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
            btnInc.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");

            btnInc.setOnAction(e -> {
                FoodCartItem item = getTableView().getItems().get(getIndex());
                item.setQuantity(item.getQuantity() + 1);
                lblQty.setText(String.valueOf(item.getQuantity())); 
                getTableView().refresh(); 
                if (controller != null) {
                    controller.calculateTotal(); 
                }
            });

            btnDec.setOnAction(e -> {
                FoodCartItem item = getTableView().getItems().get(getIndex());
                if (item.getQuantity() > 1) { 
                    item.setQuantity(item.getQuantity() - 1); 
                    lblQty.setText(String.valueOf(item.getQuantity())); 
                    getTableView().refresh(); 
                    if (controller != null) {
                        controller.calculateTotal(); 
                    }
                } else { 
                    if (controller != null) {
                        controller.removeItemFromCart(item);
                    }
                }
            });
        }

        @Override
        protected void updateItem(Integer qty, boolean empty) {
            super.updateItem(qty, empty);
            if (empty || qty == null) {
                setGraphic(null);
                setText(null);
            } else {
                FoodCartItem item = getTableView().getItems().get(getIndex());
                lblQty.setText(String.valueOf(item.getQuantity()));
                setGraphic(hbox);
            }
        }
    }


  

    private void loadAllFoodItems() throws SQLException {
        String sql = "SELECT food_id, food_name, food_price, food_image, food_type_id, meal_course_id FROM food where status='Available' ";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();
        allFoodItems.clear(); 
        while (rs.next()) {
            allFoodItems.add(new Food(
                    rs.getInt("food_id"),
                    rs.getString("food_name"),
                    rs.getInt("food_price"),
                    rs.getString("food_image"),
                    rs.getInt("food_type_id"),
                    rs.getInt("meal_course_id")
            ));
        }
    }


    private void filterFoodItemsByMealCourseAndType() throws SQLException {
        filteredFoodList.clear(); // Clear previous filtered results

        String selectedMealCourseName = comboMeal.getSelectionModel().getSelectedItem();
        int selectedMealCourseId = -1; 
        if (selectedMealCourseName != null) {
            selectedMealCourseId = getMealCourseIdByName(selectedMealCourseName);
        }

        String selectedFoodTypeName = "ALL"; 
        if (currentActiveFoodTypeButton != null) {
            selectedFoodTypeName = currentActiveFoodTypeButton.getText();
        }
        int selectedFoodTypeId = -1; // Default to -1 if "ALL" food type or not found
        if (!"ALL".equals(selectedFoodTypeName)) {
             selectedFoodTypeId = getFoodTypeIdByName(selectedFoodTypeName);
        }

        for (Food food : allFoodItems) {
            boolean matchesMealCourse = (selectedMealCourseId == -1 || food.getMealCourseId() == selectedMealCourseId);
            boolean matchesFoodType = ("ALL".equals(selectedFoodTypeName) || food.getFoodTypeId() == selectedFoodTypeId);

            if (matchesMealCourse && matchesFoodType) {
                filteredFoodList.add(food);
            }
        }
        setupPagination(filteredFoodList); // Update pagination with the newly filtered list
    }

   
    private int getMealCourseIdByName(String mealCourseName) throws SQLException {
        if (mealCourseName == null || mealCourseName.isEmpty()) return -1;
        String sql = "SELECT meal_course_id FROM meal_course WHERE meal_course_name = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, mealCourseName);
        ResultSet rs_id = ps.executeQuery();
        if (rs_id.next()) {
            return rs_id.getInt("meal_course_id");
        }
        return -1; 
    }

   
    private int getFoodTypeIdByName(String foodTypeName) throws SQLException {
        if (foodTypeName == null || foodTypeName.isEmpty()) return -1;
        String sql = "SELECT food_type_id FROM food_type WHERE food_type_name = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, foodTypeName);
        ResultSet rs_id = ps.executeQuery();
        if (rs_id.next()) {
            return rs_id.getInt("food_type_id");
        }
        return -1; // Food type not found
    }

    
    private void loadFoodTypeButtons() throws ClassNotFoundException, SQLException {
        // Create the "ALL" food type button
        Button btnAll = new Button("ALL");
        btn_container.getChildren().add(btnAll);
        btnAll.getStyleClass().add("button-food-type"); 
        btnAll.setOnAction(event -> {
            resetFoodTypeButtonStyles();
            btnAll.getStyleClass().add("active-food-type-button"); 
            currentActiveFoodTypeButton = btnAll; 
            try {
                filterFoodItemsByMealCourseAndType(); 
            } catch (SQLException ex) {
                Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Button btnPopular =new Button("Popular");
        btn_container.getChildren().add(btnPopular);
        btnPopular.getStyleClass().add("button-food-type");
        btnPopular.setOnAction(event ->{

            resetFoodTypeButtonStyles();
            btnPopular.getStyleClass().add("active-food-type-button");
            currentActiveFoodTypeButton = btnPopular;
            try {
                loadPopularFoodItemsForSelectedMealCourse();
            } catch (SQLException ex) {
                Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
           
        
        

        currentActiveFoodTypeButton = btnAll;
        currentActiveFoodTypeButton.getStyleClass().add("active-food-type-button");

        String sql = "SELECT food_type_name FROM food_type";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        while (rs.next()) {
            String foodTypeName = rs.getString("food_type_name");
            Button typeButton = new Button(foodTypeName);
            btn_container.getChildren().add(typeButton);
            typeButton.getStyleClass().add("button-food-type"); 
            typeButton.setOnAction(event -> {
                resetFoodTypeButtonStyles(); 
                typeButton.getStyleClass().add("active-food-type-button"); 
                currentActiveFoodTypeButton = typeButton;
                try {
                    filterFoodItemsByMealCourseAndType(); 
                } catch (SQLException ex) {
                    Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    
    private void resetFoodTypeButtonStyles() {
        for (Node node : btn_container.getChildren()) {
            if (node instanceof Button) {
                node.getStyleClass().remove("active-food-type-button");
            }
        }
    }

   
    private void setupPagination(ObservableList<Food> listToPaginate) {
        total_food_items = listToPaginate.size();
        pageCount = (int) Math.ceil((double) total_food_items / cards_per_page);
        paginationFood.setPageCount(pageCount > 0 ? pageCount : 1); 
        paginationFood.setCurrentPageIndex(0); 

        paginationFood.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return createFoodPage(pageIndex, listToPaginate); 
                } catch (IOException ex) {
                    Logger.getLogger(FoodOrderController.class.getName()).log(Level.SEVERE, null, ex);
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
            loader.setLocation(getClass().getResource("/view/FoodCard.fxml")); // Path to your FoodCard FXML
            
            System.out.println("Attempting to load FoodCard.fxml for food: " + listToPaginate.get(i).getFoodName()); // New Line 1
            
            try {
                AnchorPane pane = loader.load();
                FoodCardController foodCardController = loader.getController();
                
                System.out.println("FoodCard.fxml loaded successfully for " + listToPaginate.get(i).getFoodName()); // New Line 2
                //handle two button on food card
                if (this.currentUserRole != null) {
                    foodCardController.setRole(this.currentUserRole);
                }
                
                foodCardController.setData(listToPaginate.get(i)); // Pass food data to the card controller
                System.out.println("setData called for " + listToPaginate.get(i).getFoodName()); // New Line 3

                foodCardController.setFoodOrderController(this); 
                System.out.println("setFoodOrderController called for " + listToPaginate.get(i).getFoodName()); // New Line 4

                grid.add(pane, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
                } catch (IOException ex) {
                    System.err.println("Failed to load FoodCard.fxml or create controller for food: " + listToPaginate.get(i).getFoodName() + " - " + ex.getMessage()); // New Line for Error
                    ex.printStackTrace(); // Print stack trace for detailed error
                    return new GridPane();
                }
            }
        }
        return grid;
    }
   
 @FXML
    void handleOrderList(ActionEvent event) {
        if (staffPageController != null) {
            staffPageController.loadFXMLIntoDefaultAnchor("/view/FoodOrderList.fxml");
        } else {
            System.err.println("StaffPageController reference is null. Cannot load FXML.");
           
        } 
    }
   
    public void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    //myat 
    public void setUpRoomNoBtn(){
        if(Check_outController.room_id==0){
            btnEnter.setText("Enter Room No");           
        }else{
            btnEnter.setText(String.valueOf(Check_outController.room_id));
        }
    }
        @FXML
    void handleRefreshAction(ActionEvent event) throws SQLException {
        loadAllFoodItems();
        filterFoodItemsByMealCourseAndType();

    }

}