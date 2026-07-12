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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;
import model.BookDetail;

public class RoombookingHistoryController implements Initializable {

    @FXML
    private ScrollPane scrollpane;
    @FXML
    private Pane paneHistory;
    @FXML
    private VBox cardContainer;
    @FXML
    private Button btnRefresh;
    @FXML
    private DatePicker SearchDate;
    @FXML
    private ComboBox<String> combo;
    @FXML
    private ComboBox<String> combosearch;

    private Connection con;
    ObservableList<BookDetail> BookingList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Database db = new Database();
            con = db.getConnection();

            combo.getItems().addAll("Room No", "Booking Status");
            combo.setValue("Room No");

            combo.setOnAction(e -> handleComboSelection());

            combosearch.setOnAction(e -> {
                try {
                    filterRoomHistory();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            SearchDate.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        searchBookingsByDate(newValue.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                }
            });

            handleComboSelection();
            loadCurrentMonthRoomHistory();

        } catch (Exception ex) {
            Logger.getLogger(RoombookingHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleComboSelection() {
        combosearch.getItems().clear();
        combosearch.setValue(null);

        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.add("All");

            if ("Room No".equals(combo.getValue())) {
                String sql = "SELECT DISTINCT room_id FROM room";
                try (PreparedStatement pst = con.prepareStatement(sql);
                     ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        items.add(rs.getString("room_id"));
                    }
                }
            } else if ("Booking Status".equals(combo.getValue())) {
                items.addAll("Booking", "Cancelled", "Check In", "Check Out");
            }

            combosearch.setItems(items);
            combosearch.setValue("All");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading combo data: " + ex.getMessage());
        }
    }

    private void filterRoomHistory() throws SQLException, IOException {
        cardContainer.getChildren().clear();
        BookingList.clear();

        String filterType = combo.getValue();
        String selectedValue = combosearch.getValue();

        if (selectedValue == null || selectedValue.equals("All")) {
            loadAllRoomHistory();
            return;
        }

        String dbValue = selectedValue.toLowerCase();

        String sql = "SELECT DISTINCT room.room_id, guest.name, booking_detail.check_in_date, " +
                     "booking_detail.check_out_date, booking_detail.room_status, booking_detail.booking_detail_id " +
                     "FROM guest, room, booking, booking_detail " +
                     "WHERE guest.guest_id = booking.guest_id " +
                     "AND booking.booking_id = booking_detail.booking_id " +
                     "AND room.room_id = booking_detail.room_id ";

        if ("Room No".equals(filterType)) {
            sql += "AND room.room_id = ?";
        } else if ("Booking Status".equals(filterType)) {
            sql += "AND LOWER(booking_detail.room_status) = ?";
        }

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dbValue);
            try (ResultSet rs = pst.executeQuery()) {
                loadCardsFromResultSet(rs);
            }
        }
    }

    private void loadCurrentMonthRoomHistory() throws SQLException, IOException {
        cardContainer.getChildren().clear();
        BookingList.clear();

        String sql = "SELECT DISTINCT room.room_id, guest.name, booking_detail.check_in_date, " +
                     "booking_detail.check_out_date, booking_detail.room_status, booking_detail.booking_detail_id " +
                     "FROM guest, booking, booking_detail, room " +
                     "WHERE guest.guest_id = booking.guest_id " +
                     "AND booking.booking_id = booking_detail.booking_id " +
                     "AND room.room_id = booking_detail.room_id " +
                     "AND MONTH(booking_detail.check_in_date) = MONTH(CURRENT_DATE()) " +
                     "AND YEAR(booking_detail.check_in_date) = YEAR(CURRENT_DATE())";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            loadCardsFromResultSet(rs);
        }
    }

    private void searchBookingsByDate(String selectedDate) throws SQLException, IOException {
        String sql = "SELECT DISTINCT room.room_id, guest.name, booking_detail.check_in_date, " +
                     "booking_detail.check_out_date, booking_detail.room_status, booking_detail.booking_detail_id " +
                     "FROM guest, room, booking, booking_detail " +
                     "WHERE guest.guest_id = booking.guest_id " +
                     "AND booking.booking_id = booking_detail.booking_id " +
                     "AND room.room_id = booking_detail.room_id " +
                     "AND (booking_detail.check_in_date = ? OR booking_detail.check_out_date = ?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, selectedDate);
            pst.setString(2, selectedDate);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    cardContainer.getChildren().clear();
                    JOptionPane.showMessageDialog(null, "No booking found for selected date: " + selectedDate);
                    return;
                }

                loadCardsFromResultSet(rs);
            }
        }
    }

    @FXML
    private void handlerefreshButton(javafx.event.ActionEvent event) {
        try {
            SearchDate.setValue(null);
            combosearch.setValue("All");
            loadAllRoomHistory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadAllRoomHistory() throws SQLException, IOException {
        String sql = "SELECT DISTINCT room.room_id, guest.name, booking_detail.check_in_date, " +
                     "booking_detail.check_out_date, booking_detail.room_status, booking_detail.booking_detail_id " +
                     "FROM guest, booking, booking_detail, room " +
                     "WHERE guest.guest_id = booking.guest_id " +
                     "AND booking.booking_id = booking_detail.booking_id " +
                     "AND room.room_id = booking_detail.room_id";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            loadCardsFromResultSet(rs);
        }
    }

    private void loadCardsFromResultSet(ResultSet rs) throws SQLException, IOException {
        cardContainer.getChildren().clear();
        BookingList.clear();

        while (rs.next()) {
            BookingList.add(new BookDetail(
                rs.getInt("booking_detail_id"),
                rs.getInt("room_id"),
                rs.getString("name"),
                rs.getString("check_in_date"),
                rs.getString("check_out_date"),
                rs.getString("room_status")
            ));
        }

        for (BookDetail data : BookingList) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RoomHistoryCard.fxml"));
            AnchorPane card = loader.load();
            RoomHistoryCardController controller = loader.getController();
            controller.setData(
                data.getBooking_detailId(),
                data.getRoom_Id(),
                data.getName(),
                data.getCheckIn(),
                data.getCheckOut(),
                data.getRoomstatus()
            );
            VBox.setMargin(card, new Insets(4));
            cardContainer.getChildren().add(card);
        }
    }

    void BookDetail(String roomno, String name, String checkInDate, String checkOutDate, String status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
