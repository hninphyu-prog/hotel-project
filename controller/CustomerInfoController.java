package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import database.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Room; 
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
// import jdk.jshell.execution.LoaderDelegate; // This import seems unnecessary and might cause issues. Removed.

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class CustomerInfoController implements Initializable {
    @FXML
    private Button btnOK;
    @FXML
    private Button btnBack;

    @FXML
    private ComboBox<String> txtState;
    @FXML
    private ComboBox<String> txtTownship;
    @FXML
    private ComboBox<String> txtNation;
    @FXML
    private TextField txtNrcNo;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhNo;

    @FXML
    private ComboBox<String> comboPay; // This ComboBox is now NOT used for payment status selection for booking_detail.

    // private ToggleGroup rdPayment; // This was removed in your latest snippet.

    @FXML
    private TextField txtEmail;
    
    // The paymentStatusMap is no longer needed as we are directly querying in getPaymentStatusId.
    // private Map<String, Integer> paymentStatusMap; 
    
    private final int EMP_ID_FOR_BOOKING = 2; // Employee ID for booking, as defined in your snippet
    public static String selectedPaymentTypeName; // This variable's use case isn't clear for this context, but kept as in your code

    ObservableList<String> stateList;
    ObservableList<String> citycodeList;
    ObservableList<String> nationList;
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;

    // IMPORTANT: This field is crucial to receive the cartList from the previous scene
    private ObservableList<Room> cartList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();

        try {
            con = db.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Initialize NRC related ComboBoxes
        try {
            initcomboStateno();
            txtState.setItems(stateList);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Removed: Logic to load payment statuses into map and populate comboPay
        // as comboPay is no longer used for payment status selection for booking_detail.
    }

    /**
     * Setter method to receive the cartList from the previous controller (e.g., RoomBookingController).
     * @param cartList The list of rooms selected for booking.
     */
    public void setCartList(ObservableList<Room> cartList) {
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
            stateList.add(String.valueOf(rs.getInt("region_state_id")));
        }
    }

    @FXML
    void handleBackAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleOKAction(ActionEvent event) throws SQLException, IOException {
        String guestName = txtName.getText();
        // NRC
        String stateNo = txtState.getValue();
        String townshipCode = txtTownship.getValue();
        String nationType = txtNation.getValue();
        String nrcNumber = txtNrcNo.getText();
        String guestPhNo = txtPhNo.getText();
        String email = txtEmail.getText();

        // Check if ALL fields are empty or null
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

        // If not all fields are empty, proceed with specific validation checks
        // 1. Name Validation (Null not allowed, only letters and spaces)
        if (guestName == null || guestName.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Name field cannot be empty.");
            return;
        }
        if (!Pattern.matches("^[a-zA-Z\\s]+$", guestName.trim())) {
            showAlert(AlertType.ERROR, "Invalid Input", "Name must contain only letters and spaces.");
            return;
        }
        // 4. NRC, State, Township, Nation Validation (Null not allowed)
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
        // NRC Number (last part) must be digits only
        if (!Pattern.matches("^\\d+$", nrcNumber.trim())) {
            showAlert(AlertType.ERROR, "Invalid Input", "NRC Number (the last part) must contain only digits.");
            return;
        }

        // 2. Phone Number Validation (Null not allowed, Burmese format)
        if (guestPhNo == null || guestPhNo.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Phone Number field cannot be empty.");
            return;
        }
        // Burmese phone number regex: starts with 09 or +959, followed by 7 to 9 digits
        String burmesePhoneRegex = "^(09|\\+959)\\d{7,9}$";
        if (!Pattern.matches(burmesePhoneRegex, guestPhNo.trim())) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid phone number (e.g., 09xxxxxxxxx or +959xxxxxxxxx).");
            return;
        }

        // 3. Email Validation (Null not allowed, Email format)
        if (email == null || email.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Email field cannot be empty.");
            return;
        }
        // General email regex
        if (!Pattern.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", email.trim())) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid email address.");
            return;
        }

        // Combine NRC parts into the full NRC format
        String nrc = stateNo + "/" + townshipCode + "(" + nationType + ")" + nrcNumber;

        int guestId = -1; // Initialize guestId
        try {
            // 1. Check if guest already exists by NRC
            String selectSql = "SELECT guest_id FROM guest WHERE nrc = ?";
            try (PreparedStatement selectPst = con.prepareStatement(selectSql)) {
                selectPst.setString(1, nrc);
                try (ResultSet selectRs = selectPst.executeQuery()) {
                    if (selectRs.next()) {
                        guestId = selectRs.getInt("guest_id");
                        showAlert(AlertType.INFORMATION, "Info", "Customer with NRC " + nrc + " already exists." );
                    }
                }
            }

            if (guestId == -1) {
                // Guest does not exist, insert new guest
                // Ensure column names (name, ph_no) match your 'guest' table schema
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
                                showAlert(AlertType.INFORMATION, "Success", "New Customer Information saved with Guest ID: " + guestId + "!");
                            }
                        }
                    } else {
                        showAlert(AlertType.ERROR, "Database Error", "Failed to insert new guest information.");
                        return; // Stop if guest insertion failed
                    }
                }
            }

            // 2. Insert into booking table and get booking_id
            if (guestId > 0) {
                // Use EMP_ID_FOR_BOOKING from class field
                
                LocalDate bookingDate = LocalDate.now();

                String insertBookingSql = "INSERT INTO booking (guest_id, emp_id, booking_date) VALUES (?, ?, ?)";
                int bookingId = -1; // To store the generated booking_id
                int emp_id=StaffPageController.empID;
                try (PreparedStatement insertBookingPst = con.prepareStatement(insertBookingSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertBookingPst.setInt(1, guestId);
                    insertBookingPst.setInt(2, emp_id); // Using the class constant
                    insertBookingPst.setDate(3, java.sql.Date.valueOf(bookingDate));

                    int bookingRows = insertBookingPst.executeUpdate();

                    if (bookingRows > 0) {
                        try (ResultSet generatedKeys = insertBookingPst.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                bookingId = generatedKeys.getInt(1);
                                showAlert(AlertType.INFORMATION, "Booking Created", "Booking record created with Booking ID: " + bookingId + "!");
                            }
                        }
                    } else {
                        showAlert(AlertType.ERROR, "Booking Error", "Failed to create booking record.");
                        return; // Stop if booking insertion failed
                    }
                }

                // 3. Now insert into booking_detail table using the obtained bookingId
                //    for each room in cartList
                if (bookingId > 0 && cartList != null && !cartList.isEmpty()) {
                        int paymentStatusId = -1; 
                        String selectPaymentStatusSql = "SELECT payment_status_id FROM payment_status WHERE payment_status LIKE ?";
                        try (PreparedStatement selectPaymentStatusPs = con.prepareStatement(selectPaymentStatusSql)) {
                            selectPaymentStatusPs.setString(1, "%Deposit%"); 
                            try (ResultSet rs = selectPaymentStatusPs.executeQuery()) {
                                if (rs.next()) {
                                    paymentStatusId = rs.getInt("payment_status_id");
                                }
                            }
                        } catch (SQLException e) {
                            showAlert(AlertType.ERROR, "Database Error", "Error retrieving payment status ID: " + e.getMessage());
                            e.printStackTrace();
                            return;
                        }

                        if (paymentStatusId == -1) {
                            showAlert(AlertType.ERROR, "Configuration Error", "Payment status matching 'Deposit' not found in database. Please ensure a payment status like 'Deposit' exists in the 'payment_status' table.");
                            return;
                        }


                    String insertBookingDetailSql = "INSERT INTO booking_detail (booking_id, room_id, check_in_date, check_out_date, room_status, price, payment_status_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                
                    for (Room room : cartList) {
                        try (PreparedStatement insertDetailPs = con.prepareStatement(insertBookingDetailSql)) {

                            insertDetailPs.setInt(1, bookingId);
                            insertDetailPs.setInt(2, Integer.parseInt(room.getRoomno())); 
                            insertDetailPs.setString(3, room.getCheckInDate());
                            insertDetailPs.setString(4, room.getCheckOutDate());
                            insertDetailPs.setString(5, "Booking");
                            insertDetailPs.setDouble(6, room.getRoomprice()); 
                            insertDetailPs.setInt(7, paymentStatusId); 
                            

                            insertDetailPs.executeUpdate();

                            System.out.println("Booking detail inserted for room: " + room.getRoomno()); // Debugging
                        } catch (SQLException e) {
                            showAlert(AlertType.ERROR, "Database Error", "Failed to insert booking detail for room ID " + room.getRoomno() + ": " + e.getMessage());
                            Logger.getLogger(CustomerInfoController.class.getName()).log(Level.SEVERE, "Error inserting booking detail for room " + room.getRoomno(), e);
                            // It's advisable to implement transaction management here (rollback) if any booking detail insertion fails.
                        }
                    }
                    showAlert(AlertType.INFORMATION, "Booking Complete", "Booking successfully!");
                    Stage modalStage =(Stage)btnOK.getScene().getWindow();
                    modalStage.close();
                    
                    // Close the current window after successful booking
                    /*Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();*/

                } else if (cartList == null || cartList.isEmpty()) {
                    showAlert(AlertType.WARNING, "No Rooms Selected", "No rooms were added to the cart for booking details. Booking created but no details were added.");
                }
            } else {
                showAlert(AlertType.ERROR, "Process Error", "Could not obtain a valid Guest ID to proceed with booking.");
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "An error occurred during database operation: " + e.getMessage());
            Logger.getLogger(CustomerInfoController.class.getName()).log(Level.SEVERE, "Database error in handleOKAction", e);
        }

    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gets the payment_status_id from the payment_status table based on status_name.
     * This method directly queries the database for the status ID, as you provided.
     *
     * @param statusName The name of the payment status (e.g., "Deposit", "Paid", "Pending").
     * @return The payment_status_id if found, -1 if not found.
     * @throws SQLException if a database access error occurs.
     */
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

    // The loadPaymentStatuses method and paymentStatusMap field are no longer needed
    // because we are directly querying for the status ID when needed, not pre-loading
    // all statuses into a map for ComboBox population.
}