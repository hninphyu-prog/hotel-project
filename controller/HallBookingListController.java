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
import model.HallBookingList;
import database.Database;
import java.sql.Connection;
import java.sql.Statement;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Hall;
/**
 * FXML Controller class
 *
 * @author Dell
 */
public class HallBookingListController implements Initializable {

    @FXML
    private TableView<HallBookingList> tb_bookinglist;
    @FXML
    private TableColumn<HallBookingList, String> colName;
    @FXML
    private TableColumn<HallBookingList, String>  colHallNo;
    @FXML
    private TableColumn<HallBookingList, String> colPhNo;
    @FXML
    private TableColumn<HallBookingList, LocalDate>colBookDate;
    @FXML
    private TableColumn<HallBookingList, LocalDate> colWantedDate;
   @FXML
    private TableColumn<HallBookingList, LocalTime> colStartTime;

    @FXML
    private TableColumn<HallBookingList, LocalTime> colEndTime;
    @FXML
    private TableColumn<HallBookingList, String> colStatus;
    @FXML
    private TableColumn<HallBookingList, Void> colCancel;
    @FXML
    private TableColumn<HallBookingList, Void>colCheckIn;
    @FXML
    private TextField txtSearch;
    @FXML
    private DatePicker bookingDate;
   
    @FXML
    private Button btnRefresh;
    private Connection con;
    
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
     public static String date = "";
     ObservableList<HallBookingList> list = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
          Database db = new Database();
        try {
            con =db.getConnection();
           // lbDate.setText(LocalDate.now().toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
            
        }
       
    colName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
    colPhNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
    colHallNo.setCellValueFactory(new PropertyValueFactory<>("hallId"));
 //   colBookDate.setCellValueFactory(new PropertyValueFactory<>("bookDate"));
    colWantedDate.setCellValueFactory(new PropertyValueFactory<>("wantedDate"));
   colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
   colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
   
    colStatus.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));

   
    loadBookingList();
    addCancelButtonToTable();
    addCheckInButtonToTable();
   txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
        try {
            searchByDateAndName();
        } catch (SQLException ex) {
            Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });

    // 👇 Dynamic search by selecting date
    bookingDate.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            date = newValue.toString();
            try {
                searchByDateAndName();
            } catch (SQLException ex) {
                Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            date = "";
            try {
                searchByDateAndName();
            } catch (SQLException ex) {
                Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });

    }    

    @FXML
    private void handleRefreshAction(ActionEvent event) {
        txtSearch.setText("");
        bookingDate.setValue(null);
        loadBookingList();
    }
    private void loadBookingList() {
    list.clear();

    String query = "SELECT hbd.Hall_Booked_Detail_id, g.name, g.ph_no, h.hall_id, hb.book_Date "
            + "AS booking_date, hbd.want_date, hbd.start_time, hbd.end_time, hbd.booking_status,hbd.price"
            + " FROM hall_booked_detail hbd JOIN hall_book hb ON hbd.Hall_Booked_id = hb.Hall_Booked_id "
            + "JOIN guest g ON hb.guest_id = g.guest_id JOIN hall h ON hbd.hall_id = h.hall_id WHERE"
            + " hbd.booking_status = 'booking';";
    try ( Statement st = con.createStatement(); 
            ResultSet rs = st.executeQuery(query)) {
        while (rs.next()) {
        list.add(new HallBookingList(
    rs.getInt("Hall_Booked_Detail_id"),
    rs.getString("name"),
    rs.getString("ph_no"),
    rs.getString("hall_id"),                             // or String.valueOf(rs.getInt("hall_id"))
    rs.getDate("booking_date").toLocalDate(),            // bookDate
    rs.getDate("want_date").toLocalDate(),               // wantedDate
    rs.getTime("start_time").toLocalTime(),
    rs.getTime("end_time").toLocalTime(),
    rs.getString("booking_status"),                      // ✅ fixed: comma here
    rs.getInt("price")                                   // ✅ fixed: removed semicolon
));


        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    list.sort(Comparator.comparing((HallBookingList booking) -> {
            LocalDate today = LocalDate.now();
            return booking.getBookDate().isEqual(today) ? LocalDate.MIN : booking.getBookDate();
        }));

    tb_bookinglist.setItems(list);
}
     private void addCancelButtonToTable() {
    Callback<TableColumn<HallBookingList, Void>, TableCell<HallBookingList, Void>> cellFactory =
        new Callback<TableColumn<HallBookingList, Void>, TableCell<HallBookingList, Void>>() {
            @Override
            public TableCell<HallBookingList, Void> call(final TableColumn<HallBookingList, Void> param) {
                final TableCell<HallBookingList, Void> cell = new TableCell<HallBookingList, Void>() {

                    private final Button btn = new Button("Cancel");

                    {
                        btn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

                        btn.setOnAction((ActionEvent event) -> {
                            HallBookingList data = getTableView().getItems().get(getIndex());
                           
                                 

                            // Show confirmation alert first
                            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmAlert.setTitle("Confirm Cancellation");
                            confirmAlert.setHeaderText("Cancel Booking?");
                            confirmAlert.setContentText("Are you sure you want to cancel booking for " 
                                                      + data.getGuestName() + " (Hall: " + data.getHallId() + ")?");

                            Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
                            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                                try {
                                    // Update booking status in database
                                    updateRoomStatus(data.getBookingDetailId(), "Cancel");

                                    // Reload the list after update
                                    loadBookingList();

                                    showAlert(Alert.AlertType.INFORMATION, "Booking Cancelled", 
                                              "Booking for " + data.getGuestName() + " has been cancelled.");
                                } catch (SQLException ex) {
                                    Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
                                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to cancel booking.");
                                }
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

private void addCheckInButtonToTable() {
    Callback<TableColumn<HallBookingList, Void>, TableCell<HallBookingList, Void>> cellFactory =
        new Callback<TableColumn<HallBookingList, Void>, TableCell<HallBookingList, Void>>() {
            @Override
            public TableCell<HallBookingList, Void> call(final TableColumn<HallBookingList, Void> param) {
                final TableCell<HallBookingList, Void> cell = new TableCell<HallBookingList, Void>() {

                    private final Button btn = new Button("Check-in");

                    {
                        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

                        btn.setOnAction((ActionEvent event) -> {
                            HallBookingList data = getTableView().getItems().get(getIndex());
                               if (!data.getWantedDate().isEqual(LocalDate.now())) {
    showAlert(Alert.AlertType.INFORMATION,
        "Check-in Date Mismatch",
        data.getGuestName() + " has been checked into Hall " + data.getHallId() +
        ", but the check-in date (" + data.getWantedDate() + ") does not match today's date (" + LocalDate.now() + ").");
    return;
}


                            try {
                                // Update booking status in database
                                updateRoomStatus(data.getBookingDetailId(), "Check in");

                                // Reload the list after update
                                loadBookingList();

                                showAlert(Alert.AlertType.INFORMATION, "Check-in Successful", 
                                          data.getGuestName() + " has been checked into Hall " + data.getHallId() + ".");
                            } catch (SQLException ex) {
                                Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
                                showAlert(Alert.AlertType.ERROR, "Error", "Failed to check-in.");
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
    colCheckIn.setCellFactory(cellFactory);
}


    // method to use both btn Cancel and Check-in 
    private void updateRoomStatus(int bookingDetailId, String status) throws SQLException {
        String query = "UPDATE hall_booked_detail SET booking_status=? WHERE Hall_Booked_Detail_id = ?";


        PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, bookingDetailId);
            ps.executeUpdate();
        
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
  /*  public void searchbydate() throws SQLException{
        list.clear();
        String sql="SELECT hbd.Hall_Booked_Detail_id, g.name, g.ph_no, h.hall_id, hb.book_Date AS booking_date,"
                + " hbd.want_date, hbd.start_time, hbd.end_time, hbd.booking_status,hbd.price FROM hall_booked_detail hbd"
                + " JOIN hall_book hb ON hbd.Hall_Booked_id = hb.Hall_Booked_id JOIN guest g ON hb.guest_id = g.guest_id JOIN hall h ON"
                + " hbd.hall_id = h.hall_id WHERE hbd.booking_status = 'booking' and hbd.want_date=?;";
        pst = con.prepareStatement(sql);
         pst.setString(1, date);
        rs = pst.executeQuery();
        while(rs.next()){
              list.add(new HallBookingList(
    rs.getInt("Hall_Booked_Detail_id"),
    rs.getString("name"),
    rs.getString("ph_no"),
    rs.getString("hall_id"),                             // or String.valueOf(rs.getInt("hall_id"))
    rs.getDate("booking_date").toLocalDate(),            // bookDate
    rs.getDate("want_date").toLocalDate(),               // wantedDate
    rs.getTime("start_time").toLocalTime(),
    rs.getTime("end_time").toLocalTime(),
    rs.getString("booking_status"),                      // ✅ fixed: comma here
    rs.getInt("price")                                   // ✅ fixed: removed semicolon
));
      }
      

    }*/
  public void searchByDateAndName() throws SQLException {
    list.clear(); // Clear the list

    String guestName = txtSearch.getText().trim();
    LocalDate selectedDate = bookingDate.getValue();

    StringBuilder sql = new StringBuilder(
        "SELECT hbd.Hall_Booked_Detail_id, g.name, g.ph_no, h.hall_id, " +
        "hb.book_Date AS booking_date, hbd.want_date, hbd.start_time, hbd.end_time, " +
        "hbd.booking_status, hbd.price " +
        "FROM hall_booked_detail hbd " +
        "JOIN hall_book hb ON hbd.Hall_Booked_id = hb.Hall_Booked_id " +
        "JOIN guest g ON hb.guest_id = g.guest_id " +
        "JOIN hall h ON hbd.hall_id = h.hall_id " +
        "WHERE hbd.booking_status = 'booking'"
    );

    if (selectedDate != null) {
        sql.append(" AND hbd.want_date = ?");
    }

    if (!guestName.isEmpty()) {
        sql.append(" AND REPLACE(LOWER(g.name), ' ', '') LIKE LOWER(?)");
    }

    pst = con.prepareStatement(sql.toString());

    int paramIndex = 1;
    if (selectedDate != null) {
        pst.setString(paramIndex++, selectedDate.toString());
    }

    if (!guestName.isEmpty()) {
        String cleanedInput = guestName.replaceAll("\\s+", "");
        pst.setString(paramIndex, "%" + cleanedInput + "%");
    }

    rs = pst.executeQuery();

    while (rs.next()) {
        list.add(new HallBookingList(
            rs.getInt("Hall_Booked_Detail_id"),
            rs.getString("name"),
            rs.getString("ph_no"),
            rs.getString("hall_id"),
            rs.getDate("booking_date").toLocalDate(),
            rs.getDate("want_date").toLocalDate(),
            rs.getTime("start_time").toLocalTime(),
            rs.getTime("end_time").toLocalTime(),
            rs.getString("booking_status"),
            rs.getInt("price")
        ));
    }

    tb_bookinglist.setItems(list);
  
   
}





    
    

}
