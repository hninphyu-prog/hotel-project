/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import static hotel_management.Hotel_Management.stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.OccupiedRoomData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Room;
import model.Service;
import static controller.Laundary_serviceController.cartList;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MITUSER-1
 */
public class Check_outController implements Initializable {

    
    @FXML
    private TableColumn<?, ?> colRoomNo;
    @FXML
    private TableColumn<?, ?> colGuestName;
    
    @FXML
    private TableColumn<?, ?> colCheckOutDate;
    
    
    //For Database
    Connection con;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    
    ObservableList<OccupiedRoomData> occupiedRoomList= FXCollections.observableArrayList();
    @FXML
    private TableView<OccupiedRoomData> tbOccupiedRoom;
    @FXML
    private TableColumn<?, ?> colCheckInDate;
    @FXML
    private TableColumn<OccupiedRoomData, Void> colFoodOrder;
    @FXML
    private TableColumn<OccupiedRoomData, Void> colLaundary;
    @FXML
    private TextField txtSearchBar;
    
    private TableColumn<OccupiedRoomData,Void> colaction;
        
    private StaffPageController staffPageController;

    public void setStaffPageController(StaffPageController staffPageController) {
        this.staffPageController = staffPageController;
    }
     private String currentUserRole; // To store the role

public void setCurrentUserRole(String role) {
    this.currentUserRole = role;
    
}
    //to use in slip
    public static int booking_detail_id;
    public static int bookingId;
    public static int room_id ;
    public static String check_out_date;
    Parent root;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Database db = new Database();
            con = db.getConnection();
            initOccupiedRoomList();
            colRoomNo.setCellValueFactory(new PropertyValueFactory<>("room_id"));
            colGuestName.setCellValueFactory(new PropertyValueFactory<>("guest_name"));
            colCheckInDate.setCellValueFactory(new PropertyValueFactory<>("check_in"));
            colCheckOutDate.setCellValueFactory(new PropertyValueFactory<>("check_out"));
            
            tbOccupiedRoom.setItems(occupiedRoomList);
            colFoodOrder.setCellFactory(col->new TableCell<OccupiedRoomData,Void>(){
                private final Button btnFoodOrder = new Button("Food Order");
                {
                    btnFoodOrder.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    btnFoodOrder.setOnAction(e->{
                        OccupiedRoomData roomData = getTableView().getItems().get(getIndex());
                        booking_detail_id = roomData.getBooking_detail_id();
                        room_id=roomData.getRoom_id();
                         if (staffPageController != null) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FoodOrder.fxml"));
                            try {
                                Parent foodOrderPane = loader.load();
                                FoodOrderController foodOrderController = loader.getController();
                                if (foodOrderController != null) {
                                    
                                    foodOrderController.setStaffPageController(staffPageController);
                                    foodOrderController.setCurrentUserRole(currentUserRole);

                                }
                                staffPageController.defaultAnchor.getChildren().clear();
                                staffPageController.defaultAnchor.getChildren().add(foodOrderPane);
                                staffPageController.hideBlackMenu();
                            } catch (IOException ex) {
                                Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            System.err.println("StaffPageController is not set for Check_outController.");
                        }
                    });
                }
                @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btnFoodOrder);
                            }
                        }
            });
            colLaundary.setCellFactory(col->new TableCell<OccupiedRoomData,Void>(){
                private final Button btnLaundary = new Button("Get Service");
                {
                    btnLaundary.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    btnLaundary.setOnAction(e->{
                        
                            OccupiedRoomData roomData = getTableView().getItems().get(getIndex());
                            booking_detail_id = roomData.getBooking_detail_id();
                            room_id=roomData.getRoom_id();
                            check_out_date=roomData.getCheck_out();
                             if (staffPageController != null) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/laundary_service.fxml"));
                            try {
                                Parent LaundaryPane = loader.load();
                                Laundary_serviceController laundaryServiceController = loader.getController();
                                if (laundaryServiceController != null) {
                                    
                                    laundaryServiceController.setStaffPageController(staffPageController);
                                }
                                staffPageController.defaultAnchor.getChildren().clear();
                                staffPageController.defaultAnchor.getChildren().add(LaundaryPane);
                                staffPageController.hideBlackMenu();
                            } catch (IOException ex) {
                                Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            System.err.println("StaffPageController is not set for Check_outController.");
                        }
                        
                        
                    });
                }
                @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btnLaundary);
                            }
                        }
            });
            colaction = new TableColumn<>("Action");
            Callback<TableColumn<OccupiedRoomData, Void>, TableCell<OccupiedRoomData, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<OccupiedRoomData, Void> call(final TableColumn<OccupiedRoomData, Void> param) {
                    final TableCell<OccupiedRoomData, Void> cell = new TableCell<>() {

                        private final Button btn = new Button("Check Out");
                        
                        {
                            btn.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
                            btn.setOnAction((event) -> {
                                OccupiedRoomData roomData= getTableView().getItems().get(getIndex());
                                // Get the booking ID from the selected row
                                 bookingId = roomData.getBooking_id();
                                 booking_detail_id = roomData.getBooking_detail_id();

                                try {
                                    // Step 1: Update the room status in the database to 'Check-out'
                                    String updateSql = "UPDATE booking_detail SET room_status = 'Check-out' WHERE booking_detail_id = ?";
                                    pst = con.prepareStatement(updateSql);
                                    pst.setInt(1, booking_detail_id);
                                    int rowsAffected = pst.executeUpdate();
                                    if(rowsAffected > 0) {
                                        System.out.println("Room status updated to 'Check-out' for booking_detail_id: " + booking_detail_id);
                                        
                                        // No longer updating the room table as requested
                                    } else {
                                        System.err.println("Failed to update room status for booking_detail_id: " + booking_detail_id);
                                        JOptionPane.showMessageDialog(null, "Failed to check out. Please try again.");
                                        return;
                                    }

                                    // Step 2: Load the Bill form and pass the booking ID
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CheckOutBill.fxml"));
                                    Parent billPane = loader.load();
                                    CheckOutBillController checkOutBillController = loader.getController();
                                  //  billController.setData(roomData.getBooking_detail_id());
                                    System.out.print("*"+bookingId);

                                    System.out.print("*"+booking_detail_id);

                                    Scene scene = new Scene(billPane);
                                    Stage billStage = new Stage();
                                    billStage.setScene(scene);
                                    billStage.setTitle("Check Out Bill");
                                    billStage.showAndWait();

                                } catch (SQLException ex) {
                                    Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                                } catch (IOException ex) {
                                    Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(null, "Error loading Bill form: " + ex.getMessage());
                                } finally {
                                    // Finally, refresh the occupied room list to reflect the changes
                                    try {
                                        initOccupiedRoomList();
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
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

            colaction.setCellFactory(cellFactory);
            tbOccupiedRoom.getColumns().add(colaction);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Check_outController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
        
        
    }   
    public void initOccupiedRoomList() throws SQLException{
        occupiedRoomList.clear();
        String sql ="""
                     SELECT booking_detail_id, booking.booking_id, room.room_id, name, check_in_date, check_out_date, booking_detail.room_status
                     FROM booking_detail
                     JOIN booking ON booking.booking_id=booking_detail.booking_id
                     JOIN guest ON booking.guest_id=guest.guest_id
                     JOIN room ON booking_detail.room_id=room.room_id
                     WHERE booking_detail.room_status = 'Check-in'
                     ORDER BY booking_detail.check_out_date
                     """;
                     
        pst =con.prepareStatement(sql);
        rs =pst.executeQuery(sql);
        while(rs.next()){
            occupiedRoomList.add(new OccupiedRoomData(rs.getInt("booking_detail_id"), rs.getInt("booking_id"), rs.getInt("room_id"), rs.getString("name"), rs.getString("check_in_date"), rs.getString("check_out_date")));
            
        }
        
        
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        JOptionPane.showMessageDialog(null, txtSearchBar.getText());
    }
    
}