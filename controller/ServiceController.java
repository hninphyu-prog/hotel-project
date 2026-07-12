package controller;

import database.Database;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ServiceDetail;

public class ServiceController implements Initializable {

    @FXML
    private TableView<ServiceDetail> tbservice;
    @FXML
    private TableColumn<ServiceDetail, String> colClothName;
    @FXML
    private TableColumn<ServiceDetail, String> colServiceType;
    @FXML
    private TableColumn<ServiceDetail, Integer> colQty;
    @FXML
    private TableColumn<ServiceDetail, Double> colPrice;
    @FXML
    private TableColumn<ServiceDetail, String> colOrderDate;
    @FXML
    private TableColumn<ServiceDetail, String> colOrderStatus;

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private ObservableList<ServiceDetail> serviceData = FXCollections.observableArrayList();

    int bookingDetailIDdisplay = RoomHistoryCardController.bookingDetailId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con = new Database().getConnection();

            // Setup table columns - names must match model fields!
            colClothName.setCellValueFactory(new PropertyValueFactory<>("clothName"));
            colServiceType.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
            colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

            // Load service data
            if (bookingDetailIDdisplay > 0) {
                loadServiceData();
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadServiceData() throws SQLException {
        String sql = "SELECT cloth.cloth_name, laundary_service_type.service_type, " +
                     "laundary_service_order_detail.quantity, laundary_service_order_detail.price, " +
                     "laundary_service_order.order_date, order_status.Order_Status " +
                     "FROM booking_detail " +
                     "JOIN booking ON booking_detail.booking_id = booking.booking_id " +
                     "JOIN guest ON guest.guest_id = booking.guest_id " +
                     "JOIN room ON booking_detail.room_id = room.room_id " +
                     "JOIN laundary_service_order ON booking_detail.booking_detail_id = laundary_service_order.booking_detail_id " +
                     "JOIN laundary_service_order_detail ON laundary_service_order.order_id = laundary_service_order_detail.order_id " +
                     "JOIN laundary_service ON laundary_service_order_detail.service_id = laundary_service.service_id " +
                     "JOIN cloth ON laundary_service.cloth_id = cloth.cloth_id " +
                     "JOIN laundary_service_type ON laundary_service.service_type_id = laundary_service_type.service_type_id " +
                     "JOIN order_status ON laundary_service_order.Order_Status_id = order_status.Order_Status_id " +
                     "WHERE booking_detail.booking_detail_id = ?";

        pst = con.prepareStatement(sql);
        pst.setInt(1, bookingDetailIDdisplay);
        rs = pst.executeQuery();

        serviceData.clear(); // clear old data if reloaded

        while (rs.next()) {
            serviceData.add(new ServiceDetail(
                rs.getString("cloth_name"),
                rs.getString("service_type"),
                rs.getInt("quantity"),
                rs.getDouble("price"),
                rs.getString("order_date"),
                rs.getString("Order_Status")
            ));
        }

        tbservice.setItems(serviceData);
    }

    // Optional: If another controller sets this dynamically
    public void loadServiceDataById(int bookingDetailId) {
        try {
            this.bookingDetailIDdisplay = bookingDetailId;
            loadServiceData();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
