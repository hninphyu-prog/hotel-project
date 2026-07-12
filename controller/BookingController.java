/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BookingList;
import database.Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * FXML Controller class
 *
 * @author Dell
 */
public class BookingController implements Initializable {

    @FXML
    private TableView<BookingList> tb_bookinglist;
    @FXML
    private TableColumn<BookingList, String> colName;
    @FXML
    private TableColumn<BookingList, String> colRoomNo;
    @FXML
    private TableColumn<BookingList, String> colPhNo;
    @FXML
    private TableColumn<BookingList, LocalDate>colBookDate;
    @FXML
    private TableColumn<BookingList, LocalDate> colCheckInDate;
    @FXML
    private TableColumn<BookingList, LocalDate> colCheckOutDate;
    @FXML
    private TableColumn<BookingList, String> colStatus;
    @FXML
    private TableColumn<BookingList, Void> colCancel;
    @FXML
    private TableColumn<BookingList, Void>colCheckIn;
    @FXML
    private TextField txtSearch;
    @FXML
    private DatePicker bookingDate; // This DatePicker will be used for date search
  
    @FXML
    private Button btnRefresh;
    private Connection con;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         Database db = new Database();
        try {
            con =db.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        colName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        colPhNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colRoomNo.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        colBookDate.setCellValueFactory(new PropertyValueFactory<>("bookDate"));
        colCheckInDate.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOutDate.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));

       // lbDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.M.yyyy")));

        loadBookingList("", null); // Load all bookings initially with empty search text and no date filter
        addCancelButtonToTable();
        addCheckInButtonToTable();
    }    
    
    @FXML
    private void handleRefreshAction(ActionEvent event) {
        txtSearch.clear(); // Clear search text field on refresh
        bookingDate.setValue(null); // Clear date picker on refresh
        loadBookingList("", null); // Refresh loads all bookings
    }

    // Unified method to load booking list with both text and date search filters
  private void loadBookingList(String searchText, LocalDate searchDate) {
    ObservableList<BookingList> list = FXCollections.observableArrayList();

    StringBuilder queryBuilder = new StringBuilder(
        "SELECT bd.booking_detail_id, g.name, g.ph_no, r.room_id, b.booking_date, bd.check_in_date, bd.check_out_date, bd.room_status " +
        "FROM booking_detail bd JOIN booking b ON bd.booking_id = b.booking_id " +
        "JOIN guest g ON b.guest_id = g.guest_id " +
        "JOIN room r ON bd.room_id = r.room_id " +
        "WHERE bd.room_status = 'Booking'"
    );

    int paramIndex = 1;

    // Add search condition for both guest name and room ID if searchText is not empty
    if (searchText != null && !searchText.trim().isEmpty()) {
        queryBuilder.append(" AND (LOWER(g.name) LIKE LOWER(?) OR r.room_id LIKE ?)");
    }

    // Add date search condition if searchDate is not null
    if (searchDate != null) {
        queryBuilder.append(" AND (b.booking_date = ? OR bd.check_in_date = ?)");
    }

    try (PreparedStatement ps = con.prepareStatement(queryBuilder.toString())) {
        // Set parameters based on what's present
        if (searchText != null && !searchText.trim().isEmpty()) {
            ps.setString(paramIndex++, "%" + searchText + "%");
            ps.setString(paramIndex++, "%" + searchText + "%");
        }
        if (searchDate != null) {
            ps.setDate(paramIndex++, java.sql.Date.valueOf(searchDate));
            ps.setDate(paramIndex++, java.sql.Date.valueOf(searchDate));
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new BookingList(
                    new SimpleIntegerProperty(rs.getInt("booking_detail_id")),
                    new SimpleStringProperty(rs.getString("name")),
                    new SimpleStringProperty(rs.getString("ph_no")),
                    new SimpleStringProperty(rs.getString("room_id")),
                    new SimpleObjectProperty<>(rs.getDate("booking_date").toLocalDate()),
                    new SimpleObjectProperty<>(rs.getDate("check_in_date").toLocalDate()),
                    new SimpleObjectProperty<>(rs.getDate("check_out_date").toLocalDate()),
                    new SimpleStringProperty(rs.getString("room_status"))
                ));
            }
        }
    } catch (SQLException e) {
        Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, "Error loading booking list", e);
        showAlert(AlertType.ERROR, "Database Error", "Failed to load booking list: " + e.getMessage());
    }

    list.sort(Comparator.comparing((BookingList booking) -> {
        LocalDate today = LocalDate.now();
        return booking.getCheckInDate().isEqual(today) ? LocalDate.MIN : booking.getCheckInDate();
    }));

    tb_bookinglist.setItems(list);
}
    
    // Original loadBookingList() now calls the overloaded version with empty string and null date
    private void loadBookingList() {
        loadBookingList("", null);
    }

    private void addCancelButtonToTable() {
        Callback<TableColumn<BookingList, Void>, TableCell<BookingList, Void>> cellFactory = new Callback<TableColumn<BookingList, Void>, TableCell<BookingList, Void>>() {
            @Override
            public TableCell<BookingList, Void> call(final TableColumn<BookingList, Void> param) {
                final TableCell<BookingList, Void> cell = new TableCell<BookingList, Void>() {

                    private final Button btn = new Button("Cancel");

                    {
                        btn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;"); // Red background
                        btn.setOnAction((ActionEvent event) -> {
                            BookingList data = getTableView().getItems().get(getIndex());
                            
                            // Show confirmation alert
                            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                            confirmAlert.setTitle("Confirm Cancellation");
                            confirmAlert.setHeaderText("Cancel Booking?");
                            confirmAlert.setContentText("Are you sure you want to cancel booking for " + data.getGuestName() + " (Room: " + data.getRoomNo() + ")?");

                            Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
                            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                                try {
                                    // Update room status to 'Cancel' in database
                                    updateRoomStatus(data.getBookingDetailId(), "Cancel");
                                } catch (SQLException ex) {
                                    Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                showAlert(AlertType.INFORMATION, "Booking Cancelled", "Booking for " + data.getGuestName() + " has been cancelled.");
                                loadBookingList(txtSearch.getText(), bookingDate.getValue()); // Refresh table, keeping existing filters
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colCancel.setCellFactory(cellFactory);
    }

    // Method to add "Check-in" button to the table column
    private void addCheckInButtonToTable() {
        Callback<TableColumn<BookingList, Void>, TableCell<BookingList, Void>> cellFactory = new Callback<TableColumn<BookingList, Void>, TableCell<BookingList, Void>>() {
            @Override
            public TableCell<BookingList, Void> call(final TableColumn<BookingList, Void> param) {
                final TableCell<BookingList, Void> cell = new TableCell<BookingList, Void>() {

                    private final Button btn = new Button("Check-in");

                    {
                        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;"); // Green background
                        btn.setOnAction((ActionEvent event) -> {
                            BookingList data = getTableView().getItems().get(getIndex());
                            try {
                                // Update room status to 'Check-in' in database
                                updateRoomStatus(data.getBookingDetailId(), "Check-in");
                            } catch (SQLException ex) {
                                Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            showAlert(AlertType.INFORMATION, "Check-in Successful", data.getGuestName() + " has been checked into Room " + data.getRoomNo() + ".");
                            loadBookingList(txtSearch.getText(), bookingDate.getValue()); // Refresh table, keeping existing filters
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colCheckIn.setCellFactory(cellFactory);
    }

    @FXML
    void handleSearchTextAction(ActionEvent event) {
        String searchText = txtSearch.getText();
        loadBookingList(searchText, bookingDate.getValue()); // Call with current search text and selected date
    }

    @FXML
    void handleSearchDate(ActionEvent event) {
        LocalDate selectedDate = bookingDate.getValue();
        loadBookingList(txtSearch.getText(), selectedDate); // Call with current search text and selected date
    }

    // method to use both btn Cancel and Check-in 
    private void updateRoomStatus(int bookingDetailId, String status) throws SQLException {
        String query = "UPDATE booking_detail SET room_status = ? WHERE booking_detail_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, bookingDetailId);
            ps.executeUpdate();
        }
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}