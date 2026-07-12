package controller;

import database.Database;
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
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Food_history;

public class FoodController implements Initializable {

    @FXML
    private TableView<Food_history> tbFood;
    @FXML
    private TableColumn<Food_history, String> colorderDateTime;
    @FXML
    private TableColumn<Food_history, String> colfoodName;
    @FXML
    private TableColumn<Food_history, Integer> colQty;
    @FXML
    private TableColumn<Food_history, Integer> colPrice;

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private ObservableList<Food_history> foodService = FXCollections.observableArrayList();

    // Default ID from other controller, can be overridden
    int bookingDetailIDDisplay = RoomHistoryCardController.bookingDetailId;

    public void setBookingDetailID(int id) {
        this.bookingDetailIDDisplay = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con = new Database().getConnection();

            // Set up table column bindings (MUST match model field names!)
            colorderDateTime.setCellValueFactory(new PropertyValueFactory<>("food_order_date"));
            colfoodName.setCellValueFactory(new PropertyValueFactory<>("Food_Name"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

            // Optional: load data immediately (only if bookingDetailId is set)
            if (bookingDetailIDDisplay > 0) {
                loadfoodData();
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FoodController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FoodController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadfoodData() throws SQLException {
        String sql = "SELECT f.food_name, fod.quantity, fod.price, fo.food_order_date " +
                     "FROM booking_detail bd " +
                     "JOIN booking b ON bd.booking_id = b.booking_id " +
                     "JOIN guest g ON b.guest_id = g.guest_id " +
                     "JOIN room r ON bd.room_id = r.room_id " +
                     "JOIN food_order fo ON bd.booking_detail_id = fo.booking_detail_id " +
                     "JOIN food_order_detail fod ON fo.food_order_id = fod.food_order_id " +
                     "JOIN food f ON fod.food_id = f.food_id " +
                     "WHERE bd.booking_detail_id = ?";

        pst = con.prepareStatement(sql);
        pst.setInt(1, bookingDetailIDDisplay);
        rs = pst.executeQuery();

        foodService.clear(); // Clear old data
        while (rs.next()) {
            foodService.add(new Food_history(
                rs.getString("food_order_date"),
                rs.getString("food_name"),
                rs.getInt("quantity"),
                rs.getInt("price")
            ));
        }

        tbFood.setItems(foodService);
    }

    public void loadFoodDataById(int id) {
        try {
            this.bookingDetailIDDisplay = id;
            loadfoodData();
        } catch (SQLException ex) {
            Logger.getLogger(FoodController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
