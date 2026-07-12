/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Hall;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
/**
 * FXML Controller class
 *
 * @author May Na Dar
 */
public class HallCustomerDataController implements Initializable {

    @FXML
    private ComboBox<String> txtState;
    @FXML
    private ComboBox<String> txtTownship;
    @FXML
    private ComboBox<String> txtNation;
    @FXML
    private TextField txtNrcNo;
    @FXML
    private TextField txtname;
    @FXML
    private TextField txtphno;
    @FXML
    private TextField txtemailaddress;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnComfirm;
     private ObservableList<Hall> cartList;
     ObservableList<String> stateList;
    ObservableList<String> citycodeList;
    ObservableList<String> nationList;
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    /**
     * Initializes the controller class.
     */
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();

        try {
            con = db.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HallCustomerDataController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Initialize NRC related ComboBoxes
        try {
            initcomboStateno();
            txtState.setItems(stateList);
       //  
       
      // txtTownship.setItems(citycodeList);
       
        } catch (SQLException ex) {
            Logger.getLogger(HallBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
     

        // Removed: Logic to load payment statuses into map and populate comboPay
        // as comboPay is no longer used for payment status selection for booking_detail.
    }

    /**
     * Setter method to receive the cartList from the previous controller (e.g., RoomBookingController).
     * @param cartList The list of rooms selected for booking.
     */
    public void setCartList(ObservableList<Hall> cartList) {
        this.cartList = cartList;
        System.out.println("Cart list received in CustomerInfoController. Size: " + cartList.size()); // Debugging
    }

    @FXML
    private void stateAction(ActionEvent event) throws SQLException {
        citycodeList = FXCollections.observableArrayList();
        String stateno = txtState.getValue();
        String sql = "select city_code from city where region_state_id=?";
        pst = con.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(stateno));
        rs = pst.executeQuery();
        while (rs.next()) {
            
            citycodeList.add(rs.getString("city_code"));
        }
        txtTownship.setItems(citycodeList);
    }

    @FXML
    private void townshipAction(ActionEvent event) {
        nationList = FXCollections.observableArrayList();
        String[] nation = {"N", "E", "P"};
        for (String n : nation) {
            nationList.add(n);
        }
       txtNation.setItems(nationList);     
    }

    public void initcomboStateno() throws SQLException {
        stateList = FXCollections.observableArrayList();
        String sql = "select region_state_id from region_state";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
           // System.out.println(rs.getInt("region_state_id"));
            stateList.add(String.valueOf(rs.getInt("region_state_id")));
        }
        
    }
    @FXML
    private void handlerBackAction(ActionEvent event) {
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

 
    @FXML
private void handlerComfirmAction(ActionEvent event) throws IOException {
    String guestName = txtname.getText();
    String stateNo = txtState.getValue();
    String townshipCode = txtTownship.getValue();
    String nationType = txtNation.getValue();
    String nrcNumber = txtNrcNo.getText();
    String guestPhNo = txtphno.getText();
    String email = txtemailaddress.getText();

    boolean allFieldsEmpty = (guestName == null || guestName.trim().isEmpty()) &&
                             (guestPhNo == null || guestPhNo.trim().isEmpty()) &&
                             (email == null || email.trim().isEmpty()) &&
                             (nrcNumber == null || nrcNumber.trim().isEmpty()) &&
                             (stateNo == null || stateNo.isEmpty()) &&
                             (townshipCode == null || townshipCode.isEmpty()) &&
                             (nationType == null || nationType.isEmpty());

    if (allFieldsEmpty) {
        showAlert(AlertType.ERROR, "Missing Information", "Please input all required fields.");
        return;
    }

    // Name validation
    if (guestName == null || guestName.trim().isEmpty()) {
        showAlert(AlertType.ERROR, "Invalid Input", "Name field cannot be empty.");
        return;
    }
    if (!Pattern.matches("^[a-zA-Z\\s]+$", guestName.trim())) {
        showAlert(AlertType.ERROR, "Invalid Input", "Name must contain only letters and spaces.");
        return;
    }

    // NRC validation
    if (stateNo == null || stateNo.isEmpty()) {
        showAlert(AlertType.ERROR, "Invalid Input", "Please select an NRC State/Region.");
        return;
    }
    if (townshipCode == null || townshipCode.isEmpty()) {
        showAlert(AlertType.ERROR, "Invalid Input", "Please select an NRC Township Code.");
        return;
    }
    if (nationType == null || nationType.isEmpty()) {
        showAlert(AlertType.ERROR, "Invalid Input", "Please select an NRC Nationality Type.");
        return;
    }
    if (nrcNumber == null || nrcNumber.trim().isEmpty()) {
        showAlert(AlertType.ERROR, "Invalid Input", "NRC Number field cannot be empty.");
        return;
    }
    if (!Pattern.matches("^\\d+$", nrcNumber.trim())) {
        showAlert(AlertType.ERROR, "Invalid Input", "NRC Number (the last part) must contain only digits.");
        return;
    }

    // Phone number validation
    if (guestPhNo == null || guestPhNo.trim().isEmpty()) {
        showAlert(AlertType.ERROR, "Invalid Input", "Phone Number field cannot be empty.");
        return;
    }
    if (!Pattern.matches("^(09|\\+959)\\d{7,9}$", guestPhNo.trim())) {
        showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid phone number (e.g., 09xxxxxxxxx or +959xxxxxxxxx).");
        return;
    }

    // Email validation
    if (email == null || email.trim().isEmpty()) {
        showAlert(AlertType.ERROR, "Invalid Input", "Email field cannot be empty.");
        return;
    }
    if (!Pattern.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", email.trim())) {
        showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid email address.");
        return;
    }

    // Combine NRC parts
    String nrc = stateNo + "/" + townshipCode + "(" + nationType + ")" + nrcNumber;

    int guestId = -1;
    try {
        // Check for existing guest
        String selectSql = "SELECT guest_id FROM guest WHERE nrc = ?";
        try (PreparedStatement selectPst = con.prepareStatement(selectSql)) {
            selectPst.setString(1, nrc);
            try (ResultSet selectRs = selectPst.executeQuery()) {
                if (selectRs.next()) {
                    guestId = selectRs.getInt("guest_id");
                    showAlert(AlertType.INFORMATION, "Info", "Customer with NRC " + nrc + " already exists.");
                }
            }
        }

        // Insert new guest if not found
        if (guestId == -1) {
            String insertGuestSql = "INSERT INTO guest (name, ph_no, email, nrc) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertGuestPst = con.prepareStatement(insertGuestSql, Statement.RETURN_GENERATED_KEYS)) {
                insertGuestPst.setString(1, guestName);
                insertGuestPst.setString(2, guestPhNo);
                insertGuestPst.setString(3, email);
                insertGuestPst.setString(4, nrc);

                int affectedRows = insertGuestPst.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = insertGuestPst.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            guestId = generatedKeys.getInt(1);
                        //    showAlert(AlertType.INFORMATION, "Success", "New Customer saved with Guest ID: " + guestId);
                            System.out.println("New Customer saved in guest Table with Guest ID: " + guestId);
                        }
                    }
                } else {
                    showAlert(AlertType.ERROR, "Database Error", "Failed to insert new guest.");
                    return;
                }
            }
        }

        // Insert into hall_book
        if (guestId > 0) {
            int bookingId = -1;
            LocalDate bookingDate = LocalDate.now();
            int emp_id = StaffPageController.empID;

            String insertBookingSql = "INSERT INTO hall_book (guest_id, emp_id, book_Date) VALUES (?, ?, ?)";
            try (PreparedStatement insertBookingPst = con.prepareStatement(insertBookingSql, Statement.RETURN_GENERATED_KEYS)) {
                insertBookingPst.setInt(1, guestId);
                insertBookingPst.setInt(2, emp_id);
                insertBookingPst.setDate(3, java.sql.Date.valueOf(bookingDate));

                int bookingRows = insertBookingPst.executeUpdate();
                if (bookingRows > 0) {
                    try (ResultSet generatedKeys = insertBookingPst.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            bookingId = generatedKeys.getInt(1);
                  //          showAlert(AlertType.INFORMATION, "Booking Created", "Booking ID: " + bookingId);
                            System.out.println("The data is successfully into the hall book table");
                        }
                    }
                } else {
                    showAlert(AlertType.ERROR, "Booking Error", "Failed to create booking.");
                    return;
                }
            }

            // Insert into hall_booked_detail
            if (bookingId > 0 && cartList != null && !cartList.isEmpty()) {
                int paymentStatusId = getPaymentStatusId("Deposit");
                if (paymentStatusId == -1) {
                    showAlert(AlertType.ERROR, "Error", "Payment status 'Deposit' not found.");
                    return;
                }

                String insertBookingDetailSql = "INSERT INTO hall_booked_detail (hall_booked_id, hall_id, payment_status_id, want_date, start_time, end_time, booking_status,price) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
                for (Hall hall : cartList) {
                    try (PreparedStatement insertDetailPs = con.prepareStatement(insertBookingDetailSql)) {
                        insertDetailPs.setInt(1, bookingId);
                        insertDetailPs.setInt(2, Integer.parseInt(hall.getHallNo()));
                        insertDetailPs.setInt(3, paymentStatusId);
                        insertDetailPs.setDate(4, java.sql.Date.valueOf(LocalDate.parse(hall.getBookingDate())));
                        insertDetailPs.setTime(5, java.sql.Time.valueOf(LocalTime.parse(hall.getStartTime())));
                        insertDetailPs.setTime(6, java.sql.Time.valueOf(LocalTime.parse(hall.getEndTime())));
                        insertDetailPs.setString(7, "booking");
                        insertDetailPs.setInt(8, hall.getPrice());

                        insertDetailPs.executeUpdate();
                        System.out.println("Inserted booking detail for Hall " + hall.getHallNo());
                    } catch (SQLException e) {
                        showAlert(AlertType.ERROR, "Database Error", "Failed to insert detail for hall " + hall.getHallNo() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                showAlert(AlertType.INFORMATION, "Success", "Booking successfully completed.");
               Stage modalStage=(Stage)btnComfirm.getScene().getWindow();
               modalStage.close();
            } else {
                showAlert(AlertType.WARNING, "No Halls", "Booking created, but no halls were added.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        showAlert(AlertType.ERROR, "Unexpected Error", "An unexpected error occurred: " + e.getMessage());
    }
    
    // 1. Show next modal (e.g., HallBookingSuccess.fxml)
/*try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HallBill.fxml"));
    Parent root = loader.load();

    Stage successStage = new Stage();
    successStage.initModality(Modality.APPLICATION_MODAL);
    successStage.initOwner(((Node) event.getSource()).getScene().getWindow());
//    successStage.initStyle(StageStyle.UNDECORATED); // Same style
    successStage.setTitle("Booking Success");
    successStage.setScene(new Scene(root));
    successStage.showAndWait(); // Wait until success window is closed
    
    loader =new FXMLLoader(getClass().getResource("/view/HallBill.fxml"));
    root=loader.load();
    HallBillController hallbill=loader.getController();
    Stage hallbillstage=new Stage();
   hallbillstage.setScene(new Scene(root));
   hallbillstage.showAndWait();
    
    
} catch (IOException ex) {
    ex.printStackTrace();
    showAlert(AlertType.ERROR, "Load Error", "Could not load the success window.");
}*/

// 2. Close the current (hallCustomerData) modal after success modal is closed
Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
currentStage.close();

}

     private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
     private int getPaymentStatusId(String statusName) throws SQLException {
        String sql = "SELECT payment_status_id FROM payment_status WHERE payment_status = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, statusName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("payment_status_id");
                }
            }
        }
        return -1; // Status not found
    }
}