package controller;

import database.Database; // Import the Database utility class
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class FoodBillController implements Initializable {

    @FXML
    private AnchorPane voucherPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label lbOrderNo;

    @FXML
    private Label lbRoomNo;

    @FXML
    private Label lbDate;
    boolean print = false;
    @FXML
    public Button btnPrint;
    @FXML
    private Button btnBack;
    // Removed foodOrderController as it's not directly used for this specific passing mechanism
    
    private int foodOrderId; // Field to store the passed food order ID
    private int roomNo; // Field to store the passed room number

    // Setter for foodOrderId
    public void setFoodOrderId(int foodOrderId) {
        this.foodOrderId = foodOrderId;
        // Optionally, update the label immediately if it's set before initialize
        if (lbOrderNo != null) {
            lbOrderNo.setText(String.valueOf(this.foodOrderId));
        }
    }

    // Setter for roomNo
    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
        // Optionally, update the label immediately if it's set before initialize
        if (lbRoomNo != null) {
            lbRoomNo.setText(String.valueOf(this.roomNo));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set the current date for the bill
        lbDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.M.yyyy")));
        
        // The foodOrderId and roomNo will be set by the calling controller (FoodOrderListController)
        // after this initialize method runs, but before the stage is shown.
        // So, we call loadFoodBillData here, which will then use the set foodOrderId.
        // We ensure lbOrderNo and lbRoomNo are updated in setFoodOrderId and setRoomNo.
        loadFoodBillData();
        btnBack.setVisible(print);
    }

    public void loadFoodBillData() {
        // SQL query to retrieve food order details for a SPECIFIC food_order_id
        String sql = "SELECT " +
                     "    fo.food_order_id, " +
                     "    r.room_id, " +
                     "    f.food_name, " +
                     "    fod.quantity, " +
                     "    fod.price, " +
                     "    (fod.quantity * fod.price) AS Amount " +
                     "FROM " +
                     "    food_order fo " +
                     "JOIN " +
                     "    food_order_detail fod ON fo.food_order_id = fod.food_order_id " +
                     "JOIN " +
                     "    food f ON fod.food_id = f.food_id " +
                     "JOIN " +
                     "    booking_detail bd ON fo.booking_detail_id = bd.booking_detail_id " +
                     "JOIN " +
                     "    room r ON bd.room_id = r.room_id " +
                     "WHERE " +
                     "    fo.food_order_id = ?"; // Use a placeholder for the food_order_id

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Get connection using the Database utility class
            Database db = new Database();
            conn = db.getConnection(); // This method throws ClassNotFoundException
            
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, this.foodOrderId); // Set the food order ID obtained from the setter
                rs = pstmt.executeQuery();

                int row = 1; // We start adding data from row 1, as row 0 contains the header labels defined in FXML.
                int grandTotal = 0; // Initialize grand total

                boolean dataFound = false; // Flag to check if any data was retrieved

                while (rs.next()) {
                    dataFound = true;
                    // Extract data from the result set
                    // int orderNo = rs.getInt("food_order_id"); // Already have it from the setter
                    // int roomNo = rs.getInt("room_id"); // Already have it from the setter
                    String item = rs.getString("food_name");
                    int qty = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    int amount = rs.getInt("Amount");

                    // Set the Order No. and Room No. only once using the first row of results
                    // These labels are now set via the setters `setFoodOrderId` and `setRoomNo`
                    // to ensure they are available before loadFoodBillData is called.
                    if (row == 1) { // Set header labels if this is the first row
                        lbOrderNo.setText(String.valueOf(this.foodOrderId));
                        lbRoomNo.setText(String.valueOf(this.roomNo));
                    }

                    // Dynamically add data to the GridPane
                    // Column 0: Item
                    gridPane.add(new Label(item), 0, row);
                    // Column 1: Qty
                    gridPane.add(new Label(String.valueOf(qty)), 1, row);
                    // Column 2: Price
                    gridPane.add(new Label(String.valueOf(price)), 2, row);
                    // Column 3: Amount
                    gridPane.add(new Label(String.valueOf(amount)), 3, row);

                    grandTotal += amount; // Add current item's amount to grand total

                    row++;
                }

                // Add the total row at the bottom
                if (dataFound) { // Only add total if there were items for the specified order
                    Label totalLabel = new Label("Total:");
                    totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;"); // Apply bold and larger font size
                    gridPane.add(totalLabel, 2, row);

                    Label grandTotalLabel = new Label(String.valueOf(grandTotal));
                    grandTotalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;"); // Apply bold and larger font size
                    gridPane.add(grandTotalLabel, 3, row);
                } else {
                    // Handle case where no data is found for the given foodOrderId
                    System.out.println("No food order details found for Order ID: " + this.foodOrderId);
                    // Optionally show an alert
                    // showAlert(AlertType.INFORMATION, "No Data", "No details found for this order.");
                }

            } else {
                System.err.println("Database connection is null. Check Database.java connection setup.");
            }

        } catch (ClassNotFoundException e) {
            Logger.getLogger(FoodBillController.class.getName()).log(Level.SEVERE, "MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            Logger.getLogger(FoodBillController.class.getName()).log(Level.SEVERE, "Database access error.", e);
        } finally {
            // Close resources in a finally block to ensure they are always closed
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Logger.getLogger(FoodBillController.class.getName()).log(Level.SEVERE, "Error closing database resources.", e);
            }
        }
    }

    @FXML
    void handleBackAction(ActionEvent event) {
        if(!print){
            JOptionPane.showMessageDialog(null, "Please print voucher first");
        }else{
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void handlePrintAction(ActionEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            System.out.println("No printers installed");
            return;
        }
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if(job != null && job.showPrintDialog(voucherPane.getScene().getWindow())){
            boolean success = job.printPage(voucherPane);
            if(success){
                job.endJob();
                System.out.println("Voucher printed successfully.");
            }
            else{
                System.out.println("Printing failed.");
            }
        }
        print = true;
        btnBack.setVisible(print);
    }
}