/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.HallBookingList;

/**
 * FXML Controller class
 *
 * @author May Na Dar
 */
public class Hall_Check_outController implements Initializable {

    @FXML
    private TextField txtSearchBar;
    @FXML
    private DatePicker DatePicker;
    @FXML
    private Button btnsearchbar;
   
    @FXML
    private TableView<HallBookingList> tb_checkout;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colHallNo;
    @FXML
    private TableColumn<?, ?> colPhNo;
    @FXML
    private TableColumn<?, ?> colBookDate;
    @FXML
    private TableColumn<?, ?> colWantedDate;
    @FXML
    private TableColumn<?, ?> colStartTime;
    @FXML
    private TableColumn<?, ?> colEndTime;
    @FXML
    private TableColumn<HallBookingList, Void> colstatus;
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
     Database db=new Database();
        try {
            con=db.getConnection();
            //lblDate.setText(LocalDate.now().toString());
                
    colName.setCellValueFactory(new PropertyValueFactory<>("guestName"));
    colPhNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
    colHallNo.setCellValueFactory(new PropertyValueFactory<>("hallId"));
//    colBookDate.setCellValueFactory(new PropertyValueFactory<>("bookDate"));
    colWantedDate.setCellValueFactory(new PropertyValueFactory<>("wantedDate"));
   colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
   colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
 //  colstatus.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));
   addCheckInButtonToTable();
   loadBookingList();
     txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
        try {
            searchByDateAndName();
        } catch (SQLException ex) {
            Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });

    // 👇 Dynamic search by selecting date
    DatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
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
   
   
   
   
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    } 
     private void loadBookingList() {
    

    String query = "SELECT hbd.Hall_Booked_Detail_id, g.name, g.ph_no, h.hall_id, hb.book_Date "
            + "AS booking_date, hbd.want_date, hbd.start_time, hbd.end_time, hbd.booking_status,hbd.price"
            + " FROM hall_booked_detail hbd JOIN hall_book hb ON hbd.Hall_Booked_id = hb.Hall_Booked_id "
            + "JOIN guest g ON hb.guest_id = g.guest_id JOIN hall h ON hbd.hall_id = h.hall_id WHERE"
            + " hbd.booking_status = 'Check in';";
    try ( Statement st = con.createStatement(); 
            ResultSet rs = st.executeQuery(query)) {
        list.clear(); // move clear here, before the loop
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

    tb_checkout.setItems(list);
}
      private void addCheckInButtonToTable() {
        Callback<TableColumn<HallBookingList, Void>, TableCell<HallBookingList, Void>> cellFactory = new Callback<TableColumn<HallBookingList, Void>, TableCell<HallBookingList, Void>>() {
            @Override
            public TableCell<HallBookingList, Void> call(final TableColumn<HallBookingList, Void> param) {
                final TableCell<HallBookingList, Void> cell = new TableCell<HallBookingList, Void>() {

                    private final Button btn = new Button("Check out");

                    {
                        btn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;"); // Green background
                        btn.setOnAction((ActionEvent event) -> {
                            HallBookingList data = getTableView().getItems().get(getIndex());
                            try {
                                // Update room status to 'Check-in' in database
                                updateRoomStatus(data.getBookingDetailId(), "Check out");
                                 // ✅ Load FinalSlip.fxml
                                        showAlert(Alert.AlertType.INFORMATION, "Check-out Successful", data.getGuestName() + " has been checked out from Hall " + data.getHallId() + ".");
                       FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FinalSlip.fxml"));
                        Parent root = loader.load();

                        // Pass data to the controller
                        FinalSlipController slipController = loader.getController();
                        slipController.populateFinalSlip(data); // data = HallBookingList

                        // Create modal stage
                        Stage stage = new Stage();
                        stage.setTitle("Final Slip");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL); // modal = blocks interaction with other windows
                        stage.showAndWait(); // use showAndWait for modality

                            } catch (SQLException ex) {
                                Logger.getLogger(HallBookingListController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Hall_Check_outController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    //     showAlert(Alert.AlertType.INFORMATION, "Check-out Successful", data.getGuestName() + " has been checked out from Hall " + data.getHallId() + ".");

                            loadBookingList(); // Refresh table to remove checked-in booking
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
        colstatus.setCellFactory(cellFactory);
    }
         private void updateRoomStatus(int bookingDetailId, String status) throws SQLException {
       String query = "UPDATE hall_booked_detail SET booking_status = ?, payment_status_id = ? WHERE Hall_Booked_Detail_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, "Check out");
        ps.setInt(2, 2); // or whatever payment status ID you want to set
       ps.setInt(3, bookingDetailId);
        ps.executeUpdate();
    
        
    }

   

    @FXML
    private void handlerefreshAction(ActionEvent event) {
        txtSearchBar.setText("");
        DatePicker.setValue(null);
        loadBookingList();
    }
     public void searchByDateAndName() throws SQLException {
    list.clear(); // Clear the list

    String guestName = txtSearchBar.getText().trim();
    LocalDate selectedDate = DatePicker.getValue();

    StringBuilder sql = new StringBuilder(
        "SELECT hbd.Hall_Booked_Detail_id, g.name, g.ph_no, h.hall_id, " +
        "hb.book_Date AS booking_date, hbd.want_date, hbd.start_time, hbd.end_time, " +
        "hbd.booking_status, hbd.price " +
        "FROM hall_booked_detail hbd " +
        "JOIN hall_book hb ON hbd.Hall_Booked_id = hb.Hall_Booked_id " +
        "JOIN guest g ON hb.guest_id = g.guest_id " +
        "JOIN hall h ON hbd.hall_id = h.hall_id " +
        "WHERE hbd.booking_status = 'Check in'"
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

    tb_checkout.setItems(list);
  
   
}
     private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
