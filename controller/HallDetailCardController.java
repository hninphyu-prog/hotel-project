
package controller;

import database.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javax.swing.JOptionPane;
import model.Hall;

public class HallDetailCardController implements Initializable {

    @FXML private DatePicker dateBicker;
    @FXML private ScrollPane ScrollPane;
    @FXML private VBox cardContainer;
    @FXML private ComboBox<String> comboSearch;
    @FXML private ComboBox<String> comboSearchDate;
    @FXML private Button btnRefresh;
    @FXML private AnchorPane HistoryDetail;
    @FXML private Label lblHallNo, lblGuestName, lblbookedDate, lblStartTime, lblEndTime, lblPrice, lblHallType, lblCapacity, lblWantedName, lblPayname;
    @FXML private Label closeLabel;

    private Connection con;
    ObservableList<Hall> HallBookList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        try {
            con = db.getConnection();
            HistoryDetail.setVisible(false);
            comboSearch.getItems().addAll("Hall No", "Booking Status");
            comboSearch.setValue("Hall No");

            comboSearch.setOnAction(e -> handleComboSelection());

            comboSearchDate.setOnAction(e -> {
                try {
                    filterHallHistory();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            dateBicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        filterHallHistory();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                }
            });

            handleComboSelection();
            loadCurrentMonthHallHistory();

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(HallDetailCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void HandleRefreshAction(ActionEvent event) {
        try {
            loadCurrentMonthHallHistory();
            comboSearchDate.setValue("All");
            dateBicker.setValue(null);
           HistoryDetail.setVisible(false);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error refreshing data: " + ex.getMessage());
        }
    }

    private void handleComboSelection() {
        comboSearchDate.getItems().clear();
        comboSearchDate.setValue(null);

        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.add("All");

            if ("Hall No".equals(comboSearch.getValue())) {
                String sql = "SELECT DISTINCT hall_id FROM hall";
                try (PreparedStatement pst = con.prepareStatement(sql);
                     ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        items.add(rs.getString("hall_id"));
                    }
                }
            } else if ("Booking Status".equals(comboSearch.getValue())) {
                items.addAll("booking", "Cancel", "Check in", "Check Out");
            }

            comboSearchDate.setItems(items);
            comboSearchDate.setValue("All");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading combo data: " + ex.getMessage());
        }
    }

    private void filterHallHistory() throws SQLException, IOException {
        cardContainer.getChildren().clear();
        HallBookList.clear();

        String filterType = comboSearch.getValue();
        String selectedValue = comboSearchDate.getValue();
        String selectedDate = (dateBicker.getValue() != null) ? dateBicker.getValue().toString() : null;

        StringBuilder sql = new StringBuilder("SELECT DISTINCT h.hall_id, ht.hall_type, cap.Capacity, g.name AS guest_name, ")
            .append("hbd.want_date, hbd.start_time, hbd.End_time, hbd.booking_status, hbd.price, hbd.Hall_Booked_Detail_id ")
            .append("FROM guest g ")
            .append("JOIN hall_book hb ON g.guest_id = hb.guest_id ")
            .append("JOIN hall_booked_detail hbd ON hb.Hall_Booked_id = hbd.Hall_Booked_id ")
            .append("JOIN hall h ON hbd.hall_id = h.hall_id ")
            .append("JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id ")
            .append("JOIN capacity cap ON h.CapacityID = cap.CapacityID ");

        boolean hasCondition = false;

        if (selectedValue != null && !"All".equals(selectedValue)) {
            sql.append("WHERE ");
            if ("Hall No".equals(filterType)) {
                sql.append("h.hall_id = ? ");
            } else if ("Booking Status".equals(filterType)) {
                sql.append("LOWER(hbd.booking_status) = ? ");
                selectedValue = selectedValue.toLowerCase();
            }
            hasCondition = true;
        }

        if (selectedDate != null) {
            if (!hasCondition) {
                sql.append("WHERE ");
            } else {
                sql.append("AND ");
            }
            sql.append("hbd.want_date = ? ");
        }

        try (PreparedStatement pst = con.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (selectedValue != null && !"All".equals(selectedValue)) {
                pst.setString(paramIndex++, selectedValue);
            }

            if (selectedDate != null) {
                pst.setString(paramIndex, selectedDate);
            }

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    cardContainer.getChildren().clear();
                    JOptionPane.showMessageDialog(null, "No booking found for the selected filters.");
                    return;
                }
                loadCardsFromResultSet(rs);
            }
        }
    }

    private void loadCardsFromResultSet(ResultSet rs) throws SQLException, IOException {
        cardContainer.getChildren().clear();
        HallBookList.clear();

        while (rs.next()) {
            HallBookList.add(new Hall(
                rs.getInt("Hall_Booked_Detail_id"),
                rs.getString("hall_type"),
                String.valueOf(rs.getInt("hall_id")),
                rs.getInt("price"),
                rs.getString("Capacity"),
                rs.getString("start_time"),
                rs.getString("End_time"),
                rs.getString("want_date"),
                rs.getString("guest_name"),
                rs.getString("booking_status")
            ));
        }

        for (Hall data : HallBookList) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HallHistoryCard.fxml"));
            AnchorPane card = loader.load();

            HallHistoryCardController cardController = loader.getController();
            cardController.setData(data);
            cardController.setMainController(this);

            VBox.setMargin(card, new Insets(4));
            cardContainer.getChildren().add(card);
        }
    }

    public void loadAllHallHistory() throws SQLException, IOException {
        String sql = "SELECT hbd.Hall_Booked_Detail_id, h.hall_id, ht.hall_type, cap.Capacity, "
                   + "hbd.start_time, hbd.End_time, hbd.want_date, hbd.booking_status, hbd.price, "
                   + "g.name AS guest_name FROM hall_booked_detail hbd "
                   + "JOIN hall_book hb ON hbd.Hall_Booked_id = hb.Hall_Booked_id "
                   + "JOIN guest g ON hb.guest_id = g.guest_id "
                   + "JOIN hall h ON hbd.hall_id = h.hall_id "
                   + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
                   + "JOIN capacity cap ON h.CapacityID = cap.CapacityID";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            loadCardsFromResultSet(rs);
        }
    }

    private void loadCurrentMonthHallHistory() throws SQLException, IOException {
        cardContainer.getChildren().clear();
        HallBookList.clear();

        String sql = "SELECT hbd.Hall_Booked_Detail_id, h.hall_id, ht.hall_type, cap.Capacity, "
                   + "hbd.start_time, hbd.End_time, hbd.want_date, hbd.booking_status, hbd.price, "
                   + "g.name AS guest_name "
                   + "FROM hall_booked_detail hbd "
                   + "JOIN hall_book hb ON hbd.Hall_Booked_id = hb.Hall_Booked_id "
                   + "JOIN guest g ON hb.guest_id = g.guest_id "
                   + "JOIN hall h ON hbd.hall_id = h.hall_id "
                   + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
                   + "JOIN capacity cap ON h.CapacityID = cap.CapacityID "
                   + "WHERE MONTH(hbd.want_date) = MONTH(CURRENT_DATE()) "
                   + "AND YEAR(hbd.want_date) = YEAR(CURRENT_DATE())";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            loadCardsFromResultSet(rs);
        }
    }

    public void showDetail(Hall hall) {
        lblHallNo.setText(hall.getHallNo());
        lblGuestName.setText(hall.getName());
        lblbookedDate.setText(hall.getBookingDate());
        lblStartTime.setText(hall.getStartTime());
        lblEndTime.setText(hall.getEndTime());
        lblHallType.setText(hall.getHalltype());
        lblCapacity.setText(hall.getCapacity());
        lblWantedName.setText(hall.getBookingStatus());

        try {
            LocalTime startTime = LocalTime.parse(hall.getStartTime());
            LocalTime endTime = LocalTime.parse(hall.getEndTime());
            int pricePerHour = hall.getPrice();
            long hours = Duration.between(startTime, endTime).toHours();
            if (hours <= 0) hours = 1;

            double totalPrice = pricePerHour * hours;
            double deposit = totalPrice * 0.3;

            if (hall.getBookingStatus().equalsIgnoreCase("Check in")
                || hall.getBookingStatus().equalsIgnoreCase("Cancel")
                || hall.getBookingStatus().equalsIgnoreCase("Booking")) {
                lblPayname.setText("Deposit(30%)");
                lblPrice.setText(String.valueOf(deposit));
            } else {
                lblPayname.setText("FullPaid");
                lblPrice.setText(String.valueOf(totalPrice));
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblPrice.setText("Error");
        }

        HistoryDetail.setVisible(true);
    }

    @FXML
    private void onEnterClose() {
        closeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red; -fx-cursor: hand;");
    }

    @FXML
    private void onExitClose() {
        closeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
    }

    @FXML
    private void onClickClose() {
        HistoryDetail.setVisible(false);
    }
}

