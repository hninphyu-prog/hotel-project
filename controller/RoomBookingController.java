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
import java.time.Period;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import model.Room;

/**
 * FXML Controller class
 *
 * @author MITUSER-1
 */
public class RoomBookingController implements Initializable {

    @FXML
    private AnchorPane whiteAnchor;
    @FXML
    private ImageView whiteMenu;
    @FXML
    private AnchorPane RoomAnchorPane;
    @FXML
    private Pagination pagination;
    @FXML
    private DatePicker start_date_dp;
    @FXML
    private DatePicker end_date_dp;
    @FXML
    private ComboBox<String> roomType_cb_box;
    @FXML
    private Button btn_refresh;
    @FXML
    private TableView<Room> tbRoomCart;
    @FXML
    private TableColumn<?, ?> colRoomNo;
    @FXML
    private TableColumn<?, ?> colRoomType;
    @FXML
    private TableColumn<?, ?> colStartDate;
    @FXML
    private TableColumn<?, ?> colEndDate;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private Button btnConfirmBooking;
   
    private TableColumn<Room, Void> colAction;
    
    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    
    //to used in pagination
    int card_per_page =4;
    int total_room ;
    int pageCount ;
    
    //to store room data
    ObservableList<Room> roomList = FXCollections.observableArrayList();
    //to store room_type
    ObservableList<String> roomTypeList = FXCollections.observableArrayList(); 
    //to store cart
    public static ObservableList<Room> cartList = FXCollections.observableArrayList(); 
    
    //to record check_in_check_out date for room
    public static String check_in = "";
    public static String check_out ="";
    
    //calculation of charges
    public  int total;
    public  double deposit_rate;
    public  double deposit_charges;
    public  int total_night;
    @FXML
    private Label lblDeposit_rate;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtDeposit;
    @FXML
    private Button btnCancelBooking;
  
    @FXML
    private TextField txtTodayDate;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db = new Database();
            con = db.getConnection();
            //may nandar code
          
         
            //this is default for showing all rooms (xiao yang)
            initallRoom();
            initRoomType();
            roomType_cb_box.setItems(roomTypeList);
            roomType_cb_box.setValue(roomTypeList.get(0));
            
            //to add data in carttable
            // Add data to cart table columns
            colRoomNo.setCellValueFactory(new PropertyValueFactory<>("roomno"));
            colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("roomprice"));
            colStartDate.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
            colEndDate.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
            tbRoomCart.setItems(cartList); // Set items once
            // --- START OF FIXES FOR REMOVE BUTTON ---
            txtTodayDate.setText(LocalDate.now().toString());
            //start
            // Add a listener to react to changes
        cartList.addListener(new ListChangeListener<Room>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Room> change) {
                
                // You can iterate through the changes to see what exactly happened
                while (change.next()) {
                    if (change.wasAdded()) {
                        calculate();
                    }
                    if (change.wasRemoved()) {
                        calculate();
                    }
                    
                }
                System.out.println("---");
            }
        });
            //end
            // Create the Action column only once
            colAction = new TableColumn<>("Action"); // Initialize the field

            // Define the cell factory for the Action column
            Callback<TableColumn<Room, Void>, TableCell<Room, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<Room, Void> call(final TableColumn<Room, Void> param) {
                    final TableCell<Room, Void> cell = new TableCell<>() {
                        // Create the button once per cell instance
                        private final Button btn = new Button("Remove");

                        { // This block runs once when the cell is created
                            btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            btn.setOnAction((event) -> {
                                Room roomToRemove = getTableView().getItems().get(getIndex());

                                // Confirmation dialog
                                int response = JOptionPane.showConfirmDialog(null,
                                    "Are you sure you want to remove Room No: " + roomToRemove.getRoomno() + " from the cart?",
                                    "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                if (response == JOptionPane.YES_OPTION) {
                                    cartList.remove(roomToRemove);
                                    // --- IMPORTANT FIX: Force TableView refresh ---
                                    tbRoomCart.refresh(); // This re-renders all visible cells, including buttons
                                    
                                    // Refresh the main room display after removal
                                    
                                        if(end_date_dp.getValue()!=null){
                                        try {
                                            handle_search_room_action();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(RoomBookingController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        }else{
                                        try {
                                            filter_by_roomType();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(RoomBookingController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
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
                                setGraphic(btn); // Set the button as the graphic for non-empty cells
                            }
                        }
                    };
                    return cell;
                }
            };

            colAction.setCellFactory(cellFactory);
            // ADD THE ACTION COLUMN ONLY ONCE!
            tbRoomCart.getColumns().add(colAction); // This is the correct and only place to add it.

            // --- END OF FIXES FOR REMOVE BUTTON ---
            
            // 3. Set the page count for pagination
            // Calculate total pages based on total_rooms and cards per page
            pageCount = (int) Math.ceil((double) total_room / card_per_page);
            pagination.setPageCount(pageCount > 0 ? pageCount : 1); // Ensure at least 1 page if no rooms
            pagination.setCurrentPageIndex(0); // Start at the first page
            
            // 4. Set the PageFactory
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return createPage(pageIndex);
                    } catch (IOException ex) {
                        return new GridPane(); // Return an empty grid or error message
                    }
                }
            });
            
            //for initial disable end_date picker
            end_date_dp.setDisable(true);
            disablePassDate_start_dp();
            txtTotal.setEditable(false);
            txtDeposit.setEditable(false);
            String sql = "select * from payment_status where payment_status like 'Deposit%'";
            st = con.createStatement();
            rs= st.executeQuery(sql);
            if(rs.next()){
                lblDeposit_rate.setText(rs.getString("payment_status"));
                deposit_rate=rs.getDouble("rate");
            }
            txtDeposit.setText("0");
            txtTotal.setText("0");
           
            
                
            
            
        
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RoomBookingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RoomBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    

    @FXML
    private void handle_start_date_action(ActionEvent event) {
        
        end_date_dp.setDisable(false);
        end_date_dp.setValue(null);
        if(start_date_dp.getValue()!=null){
            check_in=start_date_dp.getValue().toString();
            disablePassDate_end_dp(start_date_dp.getValue());
        }
        
    }

    @FXML
    private void handle_end_date_action(ActionEvent event) throws SQLException {
        if(end_date_dp.getValue()!=null){
            
            handle_search_room_action();
        }
    }

    @FXML
    private void handle_room_type_action(ActionEvent event) throws SQLException {
        if(end_date_dp.getValue()==null){
            filter_by_roomType();
        }else{
            handle_search_room_action();
        }
    }

    @FXML
    private void handleRefreshAction(ActionEvent event) {
        start_date_dp.setValue(null);
        end_date_dp.setValue(null);
        end_date_dp.setDisable(true);
        roomType_cb_box.setValue("All");
        check_in="";
        check_out="";
        
    }

    @FXML
    private void handleConfirmAction(ActionEvent event) {
        if(cartList.isEmpty()){
             JOptionPane.showMessageDialog(null, "Your cart is empty. Please book rooms.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
             return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerInfo.fxml")); // Adjust path if needed
            Parent root = loader.load();
            
            CustomerInfoController customerInfoController = loader.getController();
            customerInfoController.setCartList(cartList); // Pass the cartList to the CustomerInfoController

            Stage customerInfoStage = new Stage();
            customerInfoStage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal window
            customerInfoStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets the owner window
            customerInfoStage.setTitle("Customer Information");
            customerInfoStage.setScene(new Scene(root));
            customerInfoStage.initStyle(StageStyle.UNDECORATED); // Optional: remove window decorations
            customerInfoStage.showAndWait(); // Show the window and wait for it to be closed

                        // Load the Bill FXML
                        loader = new FXMLLoader(getClass().getResource("/view/bill.fxml")); // Adjust path if different
                         root = loader.load();

                        // Get the BillController instance
                        BillController billController = loader.getController();

                        // Create a new stage for the bill pane
                        Stage billStage = new Stage();
                        billStage.initModality(Modality.APPLICATION_MODAL); // Makes it block other windows until closed
                      //  billStage.initStyle(StageStyle.UNDECORATED); // Optional: removes window decorations
                        billStage.setScene(new Scene(root));
                        billStage.setTitle("Bill Details"); // Set a title for the window
                        billStage.showAndWait();// Display the bill pane

            // After CustomerInfo dialog closes, you might want to clear the cart
            // if the booking was successfully completed, or if the user cancels
            // within the CustomerInfo dialog.
            // For now, we'll assume a successful booking clears the cart.
            // You might need a return value from CustomerInfoController to confirm success.
            // For simplicity, let's clear the cart here.
            // A more robust solution would involve a property or callback from CustomerInfoController
            // to indicate whether the booking was finalized.
            cartList.clear();
            calculate(); // Update total fields to 0
            
        } catch (IOException e) {
            Logger.getLogger(RoomBookingController.class.getName()).log(Level.SEVERE, "Error loading CustomerInfo.fxml", e);
            JOptionPane.showMessageDialog(null, "Error opening customer information form: " + e.getMessage());
        }
    }
    
    public Node createPage(int pageIndex) throws IOException {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(20);
        // You might want to set padding or alignment here as well, e.g.:
        // grid.setPadding(new Insets(20)); 
        // grid.setAlignment(Pos.CENTER);

        int startIndex = pageIndex * card_per_page;
        int endIndex = Math.min(startIndex + card_per_page, total_room);

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            if (i < roomList.size()) { // Defensive check to ensure index is within roomList bounds
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/CardRoom.fxml")); // Use full path if necessary
                AnchorPane pane = loader.load();
                controller.CardRoomController paneController = loader.getController();
                paneController.setData(roomList.get(i));
                

                grid.add(pane, col, row);

                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            } else {
                // This case should ideally not be reached if total_rooms and roomList are consistent
                // but good for debugging if something is off.
                System.err.println("Attempted to access roomList index " + i + " which is out of bounds.");
            }
        }
        return grid;
    }
    
    //may nandar code
  
        
    
    public void initallRoom() throws SQLException{
        String sql = "select room_id,room_type,floor_no,price,image_path from room,room_type where room.status=1 and room.room_type_id=room_type.room_type_id  ";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        roomList.clear();
        while(rs.next()){
            roomList.add(new Room(String.valueOf(rs.getInt("room_id")), rs.getString("room_type"), String.valueOf(rs.getInt("floor_no")), rs.getInt("price"), rs.getString("image_path")));            
        }
        total_room = roomList.size();
        System.out.print(total_room);
        
    }
    
    //to add value in combo box
    public void initRoomType() throws SQLException{
        roomTypeList.add("All");
        String sql = "select room_type from room_type";
        st =con.createStatement();
        rs =st.executeQuery(sql);
        while(rs.next()){
            roomTypeList.add(rs.getString("room_type"));
        }
    }
    
    public void filter_by_roomType() throws SQLException{
        String roomType = roomType_cb_box.getValue();
        if(roomType.equals("All")){
            initallRoom();
            refreshPageCount(roomList.size());
        }else{
            String sql = "select room_id,room_type,floor_no,price,image_path from room,room_type where room.room_type_id=room_type.room_type_id and room_type=?";
        pst =con.prepareStatement(sql);
        pst.setString(1, roomType);
        rs =pst.executeQuery();
        roomList.clear();
        while(rs.next()){
            roomList.add(new Room(String.valueOf(rs.getInt("room_id")), rs.getString("room_type"), String.valueOf(rs.getInt("floor_no")), rs.getInt("price"), rs.getString("image_path")));

        }
        refreshPageCount(roomList.size());
        }
    }
    
    public void refreshPageCount(int total){
        
        total_room=total;
         pageCount= (int) Math.ceil((double) total_room / card_per_page);
       
            pagination.setPageCount(pageCount > 0 ? pageCount : 1); // Ensure at least 1 page if no rooms
            
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return createPage(pageIndex);
                    } catch (IOException ex) {
                        return new GridPane(); // Return an empty grid or error message
                    }
                }
            });
    
    }
    public void disablePassDate_start_dp(){
        
        start_date_dp.setDayCellFactory(dp -> new DateCell(){
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);

				// Disable today's date and future dates
				if (date != null && !empty && ( date.isBefore(LocalDate.now()))) {
					setDisable(true);
					setStyle("-fx-background-color: #d3d3d3;"); // Gray out the disabled dates
				}
			}
		});
    }
    public void disablePassDate_end_dp(LocalDate st_date){
        end_date_dp.setDayCellFactory(dp -> new DateCell(){
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);

				// Disable today's date and future dates
				if (date != null && !empty && ( date.isBefore(st_date.plusDays(1)))) {
					setDisable(true);
					setStyle("-fx-background-color: #d3d3d3;"); // Gray out the disabled dates
				}
			}
		});
    }
    
    public void handle_search_room_action() throws SQLException{
        check_in = start_date_dp.getValue().toString();
        check_out = end_date_dp.getValue().toString();
        String roomType = roomType_cb_box.getValue();
        if(roomType.equals("All")){
            String sql= """
                        SELECT *
                        FROM room,room_type
                        WHERE room.room_type_id=room_type.room_type_id AND NOT EXISTS (
                            SELECT *
                            FROM booking_detail
                            WHERE booking_detail.room_id = room.room_id AND 
                            (
                            room_status='Booking' OR room_status = 'Check In'
                                )
                            AND (
                                (check_in_date < ? AND check_out_date > ?)
                            )
                        );
                        """;
            pst = con.prepareStatement(sql);
            pst.setString(1, check_out);
            pst.setString(2, check_in);
            rs = pst.executeQuery();
            roomList.clear();
            while(rs.next()){
                roomList.add(new Room(String.valueOf(rs.getInt("room_id")), rs.getString("room_type"), String.valueOf(rs.getInt("floor_no")), rs.getInt("price"), rs.getString("image_path")));

            }
            refreshPageCount(roomList.size());
        }else{
            String sql= """
                        SELECT *
                        FROM room,room_type
                        WHERE room.room_type_id=room_type.room_type_id AND room_type.room_type= ? AND NOT EXISTS (
                            SELECT *
                            FROM booking_detail
                            WHERE booking_detail.room_id = room.room_id AND 
                            (
                            room_status='Booking' OR room_status = 'Check In'
                                )
                            AND (
                                (check_in_date < ? AND check_out_date > ?)
                            )
                        );
                        """;
            pst = con.prepareStatement(sql);
            pst.setString(1, roomType );
            pst.setString(2, check_out);
            pst.setString(3, check_in);
            rs = pst.executeQuery();
            roomList.clear();
            while(rs.next()){
                roomList.add(new Room(String.valueOf(rs.getInt("room_id")), rs.getString("room_type"), String.valueOf(rs.getInt("floor_no")), rs.getInt("price"), rs.getString("image_path")));

            }
            refreshPageCount(roomList.size());
        }
    }
   
    public void calculate(){
        total=0;
        if(!cartList.isEmpty()){
            for(Room r:cartList){
                LocalDate start=LocalDate.parse(r.getCheckInDate()) ;
                LocalDate end = LocalDate.parse(r.getCheckOutDate());
                Period period = Period.between(start, end);
                total_night = period.getDays();
                total +=total_night * r.getRoomprice();
                
                
            }
            deposit_charges = total*deposit_rate;
            System.out.println(deposit_charges);
            txtTotal.setText(String.valueOf(total));
            txtDeposit.setText(String.valueOf(deposit_charges));
        }
        else{
            txtTotal.setText("0");
            txtDeposit.setText("0");
        }
    }

    @FXML
    private void handleCancelBookingAction(ActionEvent event) {
        if(!cartList.isEmpty()){
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel booking", "confrim Cancel", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(response == JOptionPane.YES_OPTION){
                cartList.clear();
            }
        }
        
    }
}
