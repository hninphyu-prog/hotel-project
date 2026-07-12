// src/view/FoodOrderListController.java
package controller;

import database.Database; // Import the Database utility class
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// Import the new model
import model.FoodOrderDisplay;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class FoodOrderListController implements Initializable {

    @FXML
    private TableView<FoodOrderDisplay> tbOrderList; // Set type to FoodOrderDisplay
    @FXML
    private TableColumn<FoodOrderDisplay, Integer> colOrderNo; // FoodOrderDisplay, Integer
    @FXML
    private TableColumn<FoodOrderDisplay, Integer> colRoomNo; // FoodOrderDisplay, Integer
    @FXML
    private TableColumn<FoodOrderDisplay, LocalDate> colOrderDate; // FoodOrderDisplay, LocalDate
    @FXML
    private TableColumn<FoodOrderDisplay, Void> colDetail; // FoodOrderDisplay, Void for button
    @FXML
    private TableColumn<FoodOrderDisplay, String> colStatus; // FoodOrderDisplay, String for status text (and button)
  

    private ObservableList<FoodOrderDisplay> orderData = FXCollections.observableArrayList();
    private Connection con;
  private StaffPageController staffPageController;
    
    public void setStaffPageController(StaffPageController staffPageController) {
        this.staffPageController = staffPageController;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set the current date
     //   lbDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.M.yyyy")));

        // Initialize database connection
        Database db = new Database();
        try {
            con = db.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FoodOrderListController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(AlertType.ERROR, "Database Error", "Failed to load database driver.");
            return; // Exit if connection fails
        } 

        // Set up table columns
        colOrderNo.setCellValueFactory(new PropertyValueFactory<>("foodOrderId"));
        colRoomNo.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        // For colStatus, we'll use a custom cell factory that can display text AND a button
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status")); // Still links to status string

        // Set up the 'Detail' column with a "View" button
        Callback<TableColumn<FoodOrderDisplay, Void>, TableCell<FoodOrderDisplay, Void>> detailCellFactory = new Callback<TableColumn<FoodOrderDisplay, Void>, TableCell<FoodOrderDisplay, Void>>() {
            @Override
            public TableCell<FoodOrderDisplay, Void> call(final TableColumn<FoodOrderDisplay, Void> param) {
                final TableCell<FoodOrderDisplay, Void> cell = new TableCell<FoodOrderDisplay, Void>() {

                    private final Button btnView = new Button("View");

                    {
                        btnView.setOnAction(event -> {
                            FoodOrderDisplay order = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FoodBill.fxml")); // Adjust path to your FXML
                                Parent root = loader.load();

                                FoodBillController controller = loader.getController();
                                controller.setFoodOrderId(order.getFoodOrderId()); // Pass the order ID
                                controller.setRoomNo(order.getRoomNo()); // Pass the room number
                                controller.loadFoodBillData();
                                controller.btnPrint.setVisible(false);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Food Bill - Order No: " + order.getFoodOrderId());
                                stage.show();
                            } catch (Exception e) {
                                Logger.getLogger(FoodOrderListController.class.getName()).log(Level.SEVERE, "Error opening Food Bill", e);
                                showAlert(AlertType.ERROR, "Error", "Could not open food bill details.");
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnView);
                        }
                    }
                };
                return cell;
            }
        };
        colDetail.setCellFactory(detailCellFactory);

        // Set up the 'Status' column with conditional "Deliver" button
        Callback<TableColumn<FoodOrderDisplay, String>, TableCell<FoodOrderDisplay, String>> statusCellFactory = new Callback<TableColumn<FoodOrderDisplay, String>, TableCell<FoodOrderDisplay, String>>() {
            @Override
            public TableCell<FoodOrderDisplay, String> call(final TableColumn<FoodOrderDisplay, String> param) {
                final TableCell<FoodOrderDisplay, String> cell = new TableCell<FoodOrderDisplay, String>() {
                    private final Button btnDeliver = new Button("Deliver");
                    private final Label lblStatus = new Label();

                    {
                        btnDeliver.setOnAction(event -> {
                            FoodOrderDisplay order = getTableView().getItems().get(getIndex());
                           // boolean success = updateOrderStatus(order.getFoodOrderId(), "Delivered");
                            Stage parentStage = (Stage) getTableView().getScene().getWindow(); // <--- Get parent stage

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FoodBill.fxml"));
                                Parent root = loader.load();

                                FoodBillController controller = loader.getController();
                                controller.setFoodOrderId(order.getFoodOrderId());
                                controller.setRoomNo(order.getRoomNo());
                                controller.loadFoodBillData(); // Ensure data is loaded

                                Stage stage = new Stage();
                                stage.initOwner(parentStage); // <--- Set the owner
                                stage.initModality(Modality.APPLICATION_MODAL); // <--- Set modality to block parent
                                stage.setScene(new Scene(root));
                                stage.setTitle("Food Bill - Order No: " + order.getFoodOrderId());
                                stage.showAndWait(); // <--- Corrected placement and made modal
                                
                                // After the modal window is closed, check the success of the delivery status update
                                boolean success = updateOrderStatus(order.getFoodOrderId(), "Delivered");
                                if (success) {
                                    // If successfully updated in DB, remove from the list
                                    orderData.remove(order);
                                }

                            } catch (Exception e) {
                                Logger.getLogger(FoodOrderListController.class.getName()).log(Level.SEVERE, "Error opening Food Bill", e);
                                showAlert(AlertType.ERROR, "Error", "Could not open food bill details.");
                            }
                          });
                        // Set styles for the button (optional)
                        btnDeliver.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        lblStatus.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
                    }

                    @Override
                    protected void updateItem(String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            if ("Preparing".equalsIgnoreCase(status)) {
                                setGraphic(btnDeliver); // Show button if status is "Preparing"
                                lblStatus.setText(""); // Hide label
                            } else {
                                lblStatus.setText(status); // Show status text otherwise
                                setGraphic(lblStatus); // Show label
                            }
                        }
                    }
                };
                return cell;
            }
        };
        colStatus.setCellFactory(statusCellFactory);

        // Load data into the TableView
        loadOrderData();
        tbOrderList.setItems(orderData); // Set the observable list to the table
    }

    private void loadOrderData() {
        orderData.clear(); // Clear existing data
        String sql = "SELECT fo.food_order_id, r.room_id, fo.food_order_date, fo.food_order_status, fo.booking_detail_id " +
                     "FROM food_order fo " +
                     "JOIN booking_detail bd ON fo.booking_detail_id = bd.booking_detail_id " +
                     "JOIN room r ON bd.room_id = r.room_id " +
                     "WHERE fo.food_order_status = 'Preparing' " +
                     "ORDER BY fo.food_order_id "; 

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int foodOrderId = rs.getInt("food_order_id");
                int roomNo = rs.getInt("room_id");
                LocalDate orderDate = rs.getDate("food_order_date").toLocalDate();
                String status = rs.getString("food_order_status");
                int bookingDetailId = rs.getInt("booking_detail_id");

                orderData.add(new FoodOrderDisplay(foodOrderId, roomNo, orderDate, status, bookingDetailId));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FoodOrderListController.class.getName()).log(Level.SEVERE, "Error loading food order data", ex);
            showAlert(AlertType.ERROR, "Database Error", "Failed to load food order data: " + ex.getMessage());
        }
    }

    private boolean updateOrderStatus(int foodOrderId, String newStatus) {
        String sql = "UPDATE food_order SET food_order_status = ? WHERE food_order_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, newStatus);
            pst.setInt(2, foodOrderId);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(AlertType.INFORMATION, "Status Updated", "Order " + foodOrderId + " status updated to " + newStatus + ".");
                return true; // Indicate success
            } else {
                showAlert(AlertType.WARNING, "Update Failed", "Could not update status for Order " + foodOrderId + ".");
                return false; // Indicate failure
            }
        } catch (SQLException ex) {
            Logger.getLogger(FoodOrderListController.class.getName()).log(Level.SEVERE, "Error updating order status", ex);
            showAlert(AlertType.ERROR, "Database Error", "Failed to update order status: " + ex.getMessage());
            return false; // Indicate failure due to exception
        }
    }
  @FXML
    void handleFoodOrder(ActionEvent event) {
           // This is the part where you should add the action.
        // Assuming you want to navigate to the FoodOrder.fxml to place a new order.
        if (staffPageController != null) {
            staffPageController.loadFXMLIntoDefaultAnchor("/view/FoodOrder.fxml");
            staffPageController.hideBlackMenu(); // Hide the side menu after navigation if it's open.
        } else {
            System.err.println("StaffPageController is not set. Cannot navigate to FoodOrder page.");
            showAlert(AlertType.ERROR, "Navigation Error", "Cannot navigate to Food Order page. Staff page controller is missing.");
        }
       
    }
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}