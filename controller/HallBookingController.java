
package controller;

import database.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import model.Hall;


import java.util.Date;
import java.util.List;
import java.util.Locale;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.swing.JOptionPane;

public class HallBookingController implements Initializable {

    @FXML private DatePicker Booked_Date;
    @FXML private  ComboBox<String> comboHallType;
    @FXML private ComboBox<String> comboStartTime;
    @FXML private ComboBox<String> comboEndTime;
    @FXML private Pagination pagination;
   
   

    @FXML
    private TextField txtTotalCharges;

    @FXML
    private Button btncancle;

  
    @FXML
private TableView<Hall> tbHallBooked;

@FXML
private TableColumn<?, ?> colhallno;
@FXML
private TableColumn<?, ?> colhalltype;
@FXML
private TableColumn<?, ?> colbookedDate;
@FXML
private TableColumn<?, ?> colstartTime;
@FXML
private TableColumn<?, ?> colEndTime;
@FXML
private TableColumn<?, ?> colPrice;
@FXML
private TableColumn<Hall,Void> colAction;



  
     @FXML
    private TextField txtdeposit;
     @FXML
    private Button btnrefresh;
    @FXML private Button btnconfirm;
   

    private final int card_per_page = 2;
    private int total_hall;
    private int pageCount;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
  private double total = 0;
private double deposit = 0;

    public static String date = "";
    public static String type = "";
    public static String startTime="";
    public static String endTime="";
   static  String startTime24;
   static String endTime24;

    private final ObservableList<Hall> HallList = FXCollections.observableArrayList();
    private final ObservableList<String> HallTypeList = FXCollections.observableArrayList();
   //to store cart
    public static ObservableList<Hall> hallcartList = FXCollections.observableArrayList(); 
    @Override
public void initialize(URL url, ResourceBundle rb) {
    try {
        con = new Database().getConnection();
        comboStartTime.setDisable(true);
        Booked_Date.setDisable(true);
        //lblDate.setText(LocalDate.now().toString());
        initallHall();
        initHallType();
        comboHallType.setItems(HallTypeList);
        comboHallType.setValue("All");

        setupPagination();
        setupTimeCombos();
        setupDatePicker();
        setupListeners();

        // Set up table columns
        colhallno.setCellValueFactory(new PropertyValueFactory<>("HallNo"));
        colhalltype.setCellValueFactory(new PropertyValueFactory<>("halltype"));
        colbookedDate.setCellValueFactory(new PropertyValueFactory<>("BookingDate"));
        colstartTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tbHallBooked.setItems(hallcartList); 
        hallcartList.addListener(new ListChangeListener<Hall>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Hall> change) {
                
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
        
        
        
        
        
        
        
        
        colAction = new TableColumn<>("Action"); // Initialize the field
             Callback<TableColumn<Hall, Void>, TableCell<Hall, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<Hall, Void> call(final TableColumn<Hall, Void> param) {
                    final TableCell<Hall, Void> cell = new TableCell<>() {
                        // Create the button once per cell instance
                        private final Button btn = new Button("Remove");

                        { // This block runs once when the cell is created
                            btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            btn.setOnAction((event) -> {
                                Hall roomToRemove = getTableView().getItems().get(getIndex());

                                // Confirmation dialog
                                int response = JOptionPane.showConfirmDialog(null,
                                    "Are you sure you want to remove Room No: " + roomToRemove.getHallNo() + " from the cart?",
                                    "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                if (response == JOptionPane.YES_OPTION) {
                                    hallcartList.remove(roomToRemove);
                                    // --- IMPORTANT FIX: Force TableView refresh ---
                                    tbHallBooked.refresh(); // This re-renders all visible cells, including buttons
                                    
                                    // Refresh the main room display after removal
                                    
                                       
                                    
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
             tbHallBooked.getColumns().add(colAction);
            
    } catch (Exception ex) {
        ex.printStackTrace();
    }
  

 
}
private void setupPagination() {
        pageCount = (int) Math.ceil((double) total_hall / card_per_page);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(pageIndex -> {
            try {
                return createPage(pageIndex);
            } catch (IOException ex) {
                return new GridPane();
            }
        });
    }

    private void setupDatePicker() {
        Booked_Date.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                LocalDate min = today.plusMonths(1);
                LocalDate max = today.plusMonths(3);
                if (date.isBefore(min) || date.isAfter(max)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");
                }
            }
           
        });
    }
    private void setupTimeCombos() {
    ObservableList<String> startTimes = FXCollections.observableArrayList(
        "10:00 AM", "11:00 AM", "12:00 PM",
        "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM",
        "5:00 PM", "6:00 PM"
    );
    ObservableList<String> allEndTimes = FXCollections.observableArrayList(
        "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM",
        "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM",
        "8:00 PM", "9:00 PM", "10:00 PM"
    );

    comboStartTime.setItems(startTimes);
    comboEndTime.setDisable(true);

    comboStartTime.setOnAction(e -> {
        String selectedStart = comboStartTime.getValue();

        if (selectedStart == null || selectedStart.trim().isEmpty()) {
            comboEndTime.getItems().clear();
            comboEndTime.setDisable(true);
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            Date start = sdf.parse(selectedStart);
            Date noon = sdf.parse("12:00 PM");

            ObservableList<String> filteredEndTimes = FXCollections.observableArrayList();
            for (String endTime : allEndTimes) {
                Date end = sdf.parse(endTime);
                if (end.after(start) && end.after(noon)) {
                    filteredEndTimes.add(endTime);
                }
            }

            comboEndTime.setItems(filteredEndTimes);
            comboEndTime.setDisable(filteredEndTimes.isEmpty());

            if (!filteredEndTimes.isEmpty()) {
                comboEndTime.setValue(filteredEndTimes.get(0));
            } else {
                comboEndTime.setValue(null);
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    });
}


   

   private void setupListeners() {
       Booked_Date.setOnAction(e -> {
      
    if (Booked_Date.getValue() != null) {
       LocalDate dates = Booked_Date.getValue();
       DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
       date =dates.format(formatter);
        try {
            searchHallByDate();
        } catch (SQLException ex) {
            Logger.getLogger(HallBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
           if(comboHallType.getValue()!=null)
            searchHallByTypeDate();
        } catch (SQLException ex) {
            Logger.getLogger(HallBookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     
     
        
    
    comboStartTime.setDisable(false);
});

comboHallType.setOnAction(e -> {
  
        try {
             type = comboHallType.getValue(); 
        if(comboHallType.getValue().equals("All"))
            initallHall();
        else{
            searchHallType();
        }
        Booked_Date.setDisable(false);
    } catch (SQLException ex) {
        Logger.getLogger(HallBookingController.class.getName()).log(Level.SEVERE, null, ex);
    }
          
        });

        comboEndTime.setOnAction(e -> {
            try {
                searchHallByTypeDateTime();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
      

  
        });
    }
   public String convertTo24Hour(String time) {
    try {
        time = time.trim();
        SimpleDateFormat inputFormat12 = new SimpleDateFormat("h:mm a");
        SimpleDateFormat inputFormat24 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");

        Date date;

        if (time.toUpperCase().contains("AM") || time.toUpperCase().contains("PM")) {
            date = inputFormat12.parse(time);
        } else {
            date = inputFormat24.parse(time);
        }

        return outputFormat.format(date);
    } catch (ParseException e) {
        e.printStackTrace();
        return time; // fallback to input if parsing fails
    }
}

   
    

    public void initHallType() throws SQLException {
        HallTypeList.clear();
        HallTypeList.add("All");
        String sql = "SELECT hall_type FROM hall_type";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
            HallTypeList.add(rs.getString("hall_type"));
        }
    }

    public void initallHall() throws SQLException {
        HallList.clear();
        String sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity FROM hall h "
                   + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
                   + "JOIN capacity c ON h.CapacityID= c.CapacityID;";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
            HallList.add(new Hall(
                    rs.getString("hall_type"),
                    rs.getString("hall_id"),
                    rs.getString("Capacity"),
                    rs.getInt("price"),
                    rs.getString("image")
            ));
        }
        total_hall = HallList.size();
        refreshPageCount(total_hall);
    }

    public void searchHallByDate() throws SQLException {
        if (Booked_Date.getValue() == null) return;

        date = Booked_Date.getValue().toString();
        String sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity FROM hall h "
                   + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
                   + "JOIN capacity c ON h.CapacityID = c.CapacityID "
                   + "WHERE h.hall_id NOT IN ( "
                   + "SELECT hbd.hall_id FROM hall_book hb "
                   + "JOIN hall_booked_detail hbd ON hb.Hall_Booked_id = hbd.Hall_Booked_id "
                   + "WHERE hbd.want_date = ? AND hbd.booking_status = 'booking')";

        pst = con.prepareStatement(sql);
        pst.setString(1, date);
        rs = pst.executeQuery();
        HallList.clear();
        while (rs.next()) {
            HallList.add(new Hall(
                    rs.getString("hall_type"),
                    rs.getString("hall_id"),
                    rs.getString("Capacity"),
                    rs.getInt("price"),
                    rs.getString("image")
            ));
        }
        refreshPageCount(HallList.size());
    }

    
     public void searchHallType() throws SQLException {
      //  if (Booked_Date.getValue() == null) return;

      //  date = Booked_Date.getValue().toString();
        String sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity " +
                 "FROM hall h " +
                 "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id " +
                 "JOIN capacity c ON h.CapacityID = c.CapacityID " +
                 "WHERE ht.hall_type = ?";


        pst = con.prepareStatement(sql);
        pst.setString(1, type);
        rs = pst.executeQuery();
        HallList.clear();
        while (rs.next()) {
            HallList.add(new Hall(
                    rs.getString("hall_type"),
                    rs.getString("hall_id"),
                    rs.getString("Capacity"),
                    rs.getInt("price"),
                    rs.getString("image")
            ));
        }
        refreshPageCount(HallList.size());
    }
  

 
  public void searchHallByTypeDateTime() throws SQLException {
    if (Booked_Date.getValue() == null || comboStartTime.getValue() == null || comboEndTime.getValue() == null)
        return;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    date = Booked_Date.getValue().format(formatter);
    type = (comboHallType.getValue() == null) ? "All" : comboHallType.getValue().trim();

    // Convert to 24-hour format for SQL comparison
    startTime = convertTo24Hour(comboStartTime.getValue());
    endTime = convertTo24Hour(comboEndTime.getValue());

    // Debug output
    System.out.println("Start Time 24-Hour: " + startTime);
    System.out.println("End Time 24-Hour: " + endTime);
    System.out.println("Date: " + date);
    System.out.println("Type: [" + type + "]");

    String sql;
    if (type.equalsIgnoreCase("All")) {
        sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity "
            + "FROM hall h "
            + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
            + "JOIN capacity c ON h.CapacityID = c.CapacityID "
            + "WHERE h.hall_id NOT IN ( "
            + "    SELECT hbd.hall_id FROM hall_book hb "
            + "    JOIN hall_booked_detail hbd ON hb.Hall_Booked_id = hbd.Hall_Booked_id "
            + "    WHERE hbd.want_date = ? AND hbd.booking_status = 'booking' "
            + "    AND (CAST(hbd.start_time AS TIME) < CAST(? AS TIME) "
            + "         AND CAST(hbd.end_time AS TIME) > CAST(? AS TIME)) "
            + ")";
        pst = con.prepareStatement(sql);
        pst.setString(1, date);       // want_date
        pst.setString(2, endTime);    // desired end
        pst.setString(3, startTime);  // desired start
    } else {
        sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity "
            + "FROM hall h "
            + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
            + "JOIN capacity c ON h.CapacityID = c.CapacityID "
            + "WHERE ht.hall_type = ? AND h.hall_id NOT IN ( "
            + "    SELECT hbd.hall_id FROM hall_book hb "
            + "    JOIN hall_booked_detail hbd ON hb.Hall_Booked_id = hbd.Hall_Booked_id "
            + "    WHERE hbd.want_date = ? AND hbd.booking_status = 'booking' "
            + "    AND (CAST(hbd.start_time AS TIME) < CAST(? AS TIME) "
            + "         AND CAST(hbd.end_time AS TIME) > CAST(? AS TIME)) "
            + ")";
        pst = con.prepareStatement(sql);
        pst.setString(1, type);       // hall_type
        pst.setString(2, date);       // want_date
        pst.setString(3, endTime);    // desired end
        pst.setString(4, startTime);  // desired start
    }

    rs = pst.executeQuery();
    HallList.clear();
    while (rs.next()) {
        HallList.add(new Hall(
                rs.getString("hall_type"),
                rs.getString("hall_id"),
                rs.getString("Capacity"),
                rs.getInt("price"),
                rs.getString("image")
        ));
    }
    refreshPageCount(HallList.size());
}




    public Node createPage(int pageIndex) throws IOException {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        int startIndex = pageIndex * card_per_page;
        int endIndex = Math.min(startIndex + card_per_page, HallList.size());

        int col = 0, row = 0;
        for (int i = startIndex; i < endIndex; i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/CardHall.fxml")); // Use full path if necessary
                AnchorPane pane = loader.load();
                controller.CardHallController paneController = loader.getController();
                paneController.setData(HallList.get(i)); 
           


            grid.add(pane, col++, row);
            if (col == 2) {
                col = 0;
                row++;
            }
        }
        return grid;
    }

    public void refreshPageCount(int total) {
        total_hall = total;
        pageCount = (int) Math.ceil((double) total_hall / card_per_page);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setPageFactory(pageIndex -> {
            try {
                return createPage(pageIndex);
            } catch (IOException ex) {
                return new GridPane();
            }
        });
    }

    @FXML
    void handleRefreshAction(ActionEvent event) {
        Booked_Date.setValue(LocalDate.now());
      comboHallType.setValue("All");
      comboStartTime.setValue(null);
      comboStartTime.getSelectionModel().clearSelection();
      comboEndTime.setValue(null);
      comboEndTime.getSelectionModel().clearSelection();

    }

   @FXML
    void handlecomfirmAction(ActionEvent event) throws IOException {
            if(hallcartList.isEmpty()){
             JOptionPane.showMessageDialog(null, "Your cart is empty. Please book rooms.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
        return;
            }
            
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/hallCustomerData.fxml")); // Adjust path if needed
            Parent root = loader.load();
            
            HallCustomerDataController customerInfoController = loader.getController();
            customerInfoController.setCartList(hallcartList); // Pass the cartList to the CustomerInfoController

            Stage customerInfoStage = new Stage();
            customerInfoStage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal window
            customerInfoStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets the owner window
            customerInfoStage.setTitle("Customer Information");
            customerInfoStage.setScene(new Scene(root));
      //      customerInfoStage.initStyle(StageStyle.UNDECORATED); // Optional: remove window decorations
            customerInfoStage.showAndWait(); // Show the window and wait for it to be closed
             loader =new FXMLLoader(getClass().getResource("/view/HallBill.fxml"));
    root=loader.load();
    HallBillController hallbill=loader.getController();
    Stage hallbillstage=new Stage();
   hallbillstage.setScene(new Scene(root));
   hallbillstage.showAndWait();
            // After CustomerInfo dialog closes, you might want to clear the cart
            // if the booking was successfully completed, or if the user cancels
            // within the CustomerInfo dialog.
            // For now, we'll assume a successful booking clears the cart.
            // You might need a return value from CustomerInfoController to confirm success.
            // For simplicity, let's clear the cart here.
            // A more robust solution would involve a property or callback from CustomerInfoController
            // to indicate whether the booking was finalized.
            hallcartList.clear();
         
            
        } catch (IOException e) {
            Logger.getLogger(HallBookingController.class.getName()).log(Level.SEVERE, "Error loading CustomerInfo.fxml", e);
            JOptionPane.showMessageDialog(null, "Error opening customer information form: " + e.getMessage());
        }
            
            
       
       
       
    }
      public void searchHallByTypeDate() throws SQLException {
   //     if (Booked_Date.getValue() == null || type.equals("All")) return;

       date = Booked_Date.getValue().toString();
       String sql;
    if (type.equalsIgnoreCase("All")) {
        sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity FROM hall h "
                + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id JOIN capacity c"
                + " ON h.CapacityID = c.CapacityID WHERE h.hall_id NOT IN "
                + "( SELECT hbd.hall_id FROM hall_book hb JOIN hall_booked_detail hbd ON hb.Hall_Booked_id = hbd.Hall_Booked_id WHERE hbd.want_date =? AND hbd.booking_status = 'booking'); ";
        pst = con.prepareStatement(sql);
        pst.setString(1, date);
       
    } 
       else {
        sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity FROM hall h JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id JOIN capacity c "
                + "ON h.CapacityID = c.CapacityID WHERE ht.hall_type=? and h.hall_id NOT IN "
                + "( SELECT hbd.hall_id FROM hall_book hb JOIN hall_booked_detail hbd ON hb.Hall_Booked_id = hbd.Hall_Booked_id WHERE hbd.want_date =? AND hbd.booking_status = 'booking'); ";
        pst = con.prepareStatement(sql);
        pst.setString(1, type);
        pst.setString(2, date);
       
    }

    rs = pst.executeQuery();
    HallList.clear();
    while (rs.next()) {
        HallList.add(new Hall(
                rs.getString("hall_type"),
                rs.getString("hall_id"),
                rs.getString("Capacity"),
                rs.getInt("price"),
                rs.getString("image")
        ));
    }
    refreshPageCount(HallList.size());
    
     }
       @FXML
    void handlerCancelAction(ActionEvent event) {
          if(!hallcartList.isEmpty()){
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel booking", "confrim Cancel", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(response == JOptionPane.YES_OPTION){
                hallcartList.clear();
            }
        }
        

    }
    
    
    
    
    
   public void calculate() {
    total = 0;

    if (!hallcartList.isEmpty()) {
        for (Hall h : hallcartList) {
            try {
                LocalTime startTime = LocalTime.parse(convertTo24Hour(h.getStartTime()));
                LocalTime endTime = LocalTime.parse(convertTo24Hour(h.getEndTime()));

                int pricePerHour = h.getPrice();

                long hours = Duration.between(startTime, endTime).toHours();
                if (hours <= 0) hours = 1;

                double totalPrice = pricePerHour * hours;
                total += totalPrice;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        deposit = total * 0.3;
        double remain = total - deposit;

        txtTotalCharges.setText(String.valueOf((int) total));
        txtdeposit.setText(String.format("%.0f", deposit));
      

    } else {
        txtTotalCharges.setText("0");
        txtdeposit.setText("0");
       
    }
}


}