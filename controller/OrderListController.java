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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.OrderData;

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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.OccupiedRoomData;
import model.Service;
import controller.OrderCardController;
import controller.OrderCardController;
import static controller.Check_outController.booking_detail_id;
import static controller.Check_outController.check_out_date;
import static controller.Check_outController.room_id;
import static controller.Laundary_serviceController.cartList;
import javafx.scene.Node;
import javafx.stage.StageStyle;
/**
 * FXML Controller class
 *
 * @author USER
 */
public class OrderListController implements Initializable {


    @FXML
    private DatePicker dp_searchdate;
    @FXML
    private TextField txtSearchBar;
    @FXML
    private TableView<OrderData> tbOrderLists;
    @FXML
    private TableColumn<?, ?> colOrderNo;
    @FXML
    private TableColumn<?, ?> colRoomNo;
    @FXML
    private TableColumn<?, ?> colOrderDate;
    @FXML
    private TableColumn<?, ?> colDeliveryDate;
    @FXML
    private ComboBox<String> order_status_cb_box;
    @FXML
    private Label lblstaffName;
    
    @FXML
    private Button btnTakeOrder;
    @FXML
    private AnchorPane vocher_pane;
    @FXML
    private Label lblOrderNo;
    @FXML
    private Label lblDeliveryDate;
    @FXML
    private Label lblOrderDate;
    @FXML
    private Label lblRoomNo;
    @FXML
    private TableView<OrderData> tbOrderDetailLists;
    @FXML
    private TableColumn<?, ?> colNo;
    @FXML
    private TableColumn<?, ?> colClothName;
    @FXML
    private TableColumn<?, ?> colServiceType;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TextField txtGrandTotal;
    Parent root;
    //database
    Connection con;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    //to insert in combo box
    public static ObservableList<String> order_statusList = FXCollections.observableArrayList();
    
    //to use in order
    public static ObservableList<OrderData> order = FXCollections.observableArrayList();
    @FXML
    private TableColumn<OrderData, Void> colDetails;
    @FXML
    private TableColumn<OrderData, Void> colAction;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private Button btnRefresh;
    
    //to use in order searching
    String status;
    
    //to show orderDetail
    int order_id;
    int room_id;
    String order_date;
    String deliver_date;
    ObservableList<OrderData> orderDetail = FXCollections.observableArrayList();
      private StaffPageController staffPageController;
    
    public void setStaffPageController(StaffPageController staffPageController) {
        this.staffPageController = staffPageController;
    }
       /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db = new Database();
            con = db.getConnection();
          
            //lbltodayDate.setText(LocalDate.now().toString());
            initOrderStatuslist();
            order_status_cb_box.setItems(order_statusList);
            order_status_cb_box.setValue(order_statusList.get(0));
            loadOrderList();
            
            colOrderNo.setCellValueFactory(new PropertyValueFactory<>("order_id"));
            colRoomNo.setCellValueFactory(new PropertyValueFactory<>("room_id"));
            colOrderDate.setCellValueFactory(new PropertyValueFactory<>("order_date"));
            colDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("delivery_date"));          
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colAction.setCellFactory(col->new TableCell<OrderData,Void>(){
                private final Button btnChange = new Button("Change Status");
                private final HBox box = new HBox(btnChange);
                
                {   
                    box.setAlignment(Pos.CENTER);
                    btnChange.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    
                    btnChange.setOnAction(e->{
                        if(getTableView().getItems().get(getIndex()).getStatus().equals("Delivered")){
                           
                        }else{
                            try {
                           // JOptionPane.showMessageDialog(null, "Change status");
                            Stage ownerStage = (Stage) btnChange.getScene().getWindow();
                            OrderData data = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderCard.fxml"));
                            Parent root = loader.load();
                            OrderCardController modalcontroller = loader.getController();
                            modalcontroller.setData(data);
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Change Status Dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
                            if(getTableView().getItems().get(getIndex()).getStatus().equals("Delivered")){
                                    try {                         
                                        FXMLLoader loaders = new FXMLLoader(getClass().getResource("/view/LaundaryServiceSlip.fxml")); // Adjust path if needed
                                        root = loaders.load();
                                        
                                        LaundaryServiceSlipController modalController = loaders.getController();
                                        modalController.setData(getTableView().getItems().get(getIndex()));
                                        Stage modalstage = new Stage();
                                        modalstage.setTitle("Change Status Dialog");
                                        modalstage.setScene(new Scene(root));
                                        modalstage.initModality(Modality.WINDOW_MODAL);
                                        modalstage.initOwner(ownerStage);
                                        stage = ownerStage;
                                        modalstage.showAndWait();
                                        tbOrderLists.refresh();
                                        
                                    } catch (IOException ex) {
                                        Logger.getLogger(OrderListController.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (SQLException ex) {
                                        Logger.getLogger(OrderListController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            
                            
                            
                        } catch (IOException ex) {
                            Logger.getLogger(OrderListController.class.getName()).log(Level.SEVERE, null, ex);
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
                                
                                if(getTableView().getItems().get(getIndex()).getStatus().equals("Delivered")){
                                    btnChange.setStyle("-fx-background-color: gray;-fx-text-fill:white;");
                                    setGraphic(box);
                                    
                                }else{
                                    btnChange.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                                    setGraphic(box);
                                }
                            }
                        }
            });
            colDetails.setCellFactory(col->new TableCell<OrderData,Void>(){
                private final Button btnview = new Button("View");
                private final HBox box = new HBox(btnview);
                {
                    box.setAlignment(Pos.CENTER);
                    btnview.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    btnview.setOnAction(e->{
                        try {
                            
                            OrderData data = getTableView().getItems().get(getIndex());
                            order_id = data.getOrder_id();
                            room_id= data.getRoom_id();
                            order_date=data.getOrder_date();
                            deliver_date=data.getDelivery_date();
                            
                            loadOrderDetail();
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(OrderListController.class.getName()).log(Level.SEVERE, null, ex);
                            
                        }
                        
                    });
                }
                @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(box);
                               
                                
                            }
                        }
            });
            

            


            tbOrderLists.setItems(order);
            colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
            colServiceType.setCellValueFactory(new PropertyValueFactory<>("service_name"));
            colClothName.setCellValueFactory(new PropertyValueFactory<>("cloth_name"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("service_price"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            tbOrderDetailLists.setItems(orderDetail);
            
            txtGrandTotal.setEditable(false);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderListController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(OrderListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void handleSearchDateAction(ActionEvent event) throws SQLException {
        if(dp_searchdate.getValue()==null){
            
        }else{
            if(order_status_cb_box.getValue()!=null){
                status = order_status_cb_box.getValue();
                order_status_cb_box.setValue(null);
            }
            if(!txtSearchBar.getText().isEmpty() && !txtSearchBar.getText().isBlank()){
                if(inputValidation()){
                    togetherSearch();
                }else{
                    txtSearchBar.setText("");
                    searchdate();
                }
            }else{
                    searchdate();
                    
                }
            
        }
        
    }

    @FXML
    private void handleSearchBarAction(ActionEvent event) throws SQLException {
        
        if(txtSearchBar.getText()==null || txtSearchBar.getText().isBlank()|| txtSearchBar.getText().isBlank()){
            
        }else {
            if(order_status_cb_box.getValue()!=null){
                status = order_status_cb_box.getValue();
                order_status_cb_box.setValue(null);
            }
            if(inputValidation()){
                if(dp_searchdate.getValue()!=null){
                    togetherSearch();
                }else{
                    int id = Integer.parseInt(txtSearchBar.getText());
                    String sql = """
                    SELECT laundary_service_order.order_id,room.room_id,laundary_service_order.order_date,laundary_service_order.delivery_date,Order_Status 
                                          FROM order_status,laundary_service_order,booking_detail,room
                                          WHERE booking_detail.booking_detail_id=laundary_service_order.booking_detail_id AND
                                          order_status.Order_Status_id=laundary_service_order.Order_Status_id AND
                                          room.room_id=booking_detail.room_id AND
                                          (room.room_id= ? OR order_id= ?) 
                                         
                     """;
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, id);
                    pst.setInt(2, id);
                    rs = pst.executeQuery();
                    order.clear();
                    while(rs.next()){
                        order.add(new OrderData(rs.getInt("order_id"), rs.getInt("room_id"), rs.getString("order_date"), rs.getString("delivery_date"), rs.getString("Order_Status")));

                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Can only search your order no or room no");
            }
            
            
        
        }
            
        
        
        
        
        
    }

    @FXML
    private void handle_cb_boxAction(ActionEvent event) throws SQLException {
        if(order_status_cb_box.getValue()==null){
            System.out.println("hello");
        }else{
            loadOrderList();
        }
    }

    @FXML
    private void handleTakeOrderAction(ActionEvent event) throws IOException {
         if (staffPageController != null) {
            staffPageController.loadFXMLIntoDefaultAnchor("/view/laundary_service.fxml");
            staffPageController.hideBlackMenu(); // Hide the side menu after navigation if it's open.
        } else {
            System.err.println("StaffPageController is not set. Cannot navigate to Laundary_service page.");
            //showAlert(AlertType.ERROR, "Navigation Error", "Cannot navigate to Food Order page. Staff page controller is missing.");
        }
    }
    
    public void initOrderStatuslist() throws SQLException{
        String sql = "SELECT Order_Status FROM order_status";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while(rs.next()){
            order_statusList.add(rs.getString("Order_Status"));
        }
    }
    public void loadOrderList() throws SQLException{
        String orderStatus = order_status_cb_box.getValue();
        String sql = """
                     SELECT laundary_service_order.order_id,room.room_id,laundary_service_order.order_date,laundary_service_order.delivery_date,Order_Status 
                     FROM order_status,laundary_service_order,booking_detail,room
                     WHERE booking_detail.booking_detail_id=laundary_service_order.booking_detail_id AND
                     order_status.Order_Status_id=laundary_service_order.Order_Status_id AND
                     room.room_id=booking_detail.room_id AND
                     Order_Status = ? ORDER BY delivery_date
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, orderStatus);
        rs = pst.executeQuery();
        order.clear();
        while(rs.next()){
            order.add(new OrderData(rs.getInt("order_id"), rs.getInt("room_id"), rs.getString("order_date"), rs.getString("delivery_date"), rs.getString("Order_Status")));
        }
    }

    @FXML
    private void handleRefreshAction(ActionEvent event) {
        if(status==null){
            status=order_status_cb_box.getValue();
            order_status_cb_box.setValue(null);
            order_status_cb_box.setValue(status);
        }else if(!txtSearchBar.getText().isBlank() || !txtSearchBar.getText().isEmpty() || dp_searchdate.getValue()!=null){
            dp_searchdate.setValue(null);
            txtSearchBar.setText("");
            order_status_cb_box.setValue(null);
            order_status_cb_box.setValue(status);
        }else {        
            status=order_status_cb_box.getValue();
            order_status_cb_box.setValue(null);
            order_status_cb_box.setValue(status);
        }
            
        
    }
    
    public void togetherSearch() throws SQLException{
        int id = Integer.parseInt(txtSearchBar.getText());
        String date = dp_searchdate.getValue().toString();
        String sql = """
                     SELECT laundary_service_order.order_id,room.room_id,laundary_service_order.order_date,laundary_service_order.delivery_date,Order_Status 
                                                               FROM order_status,laundary_service_order,booking_detail,room
                                                               WHERE booking_detail.booking_detail_id=laundary_service_order.booking_detail_id AND
                                                               order_status.Order_Status_id=laundary_service_order.Order_Status_id AND
                                                               room.room_id=booking_detail.room_id AND
                                                               (order_date=? OR delivery_date=? OR order_id= ? OR room.room_id= ?)
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, date);
        pst.setString(2, date);
        pst.setInt(3, id);
        pst.setInt(4, id);
        rs = pst.executeQuery();
        order.clear();
        while(rs.next()){
            order.add(new OrderData(rs.getInt("order_id"), rs.getInt("room_id"), rs.getString("order_date"), rs.getString("delivery_date"), rs.getString("Order_Status")));            
        }
        
    }
    
    public boolean inputValidation(){
        String pattern="[0-9]{1,}";
        String int_string = txtSearchBar.getText();
        return int_string.matches(pattern) && Integer.parseInt(int_string)!=0;
                
        
    }
    
    public void searchdate() throws SQLException{
        String date = dp_searchdate.getValue().toString();
                    String sql = """
                         SELECT laundary_service_order.order_id,room.room_id,laundary_service_order.order_date,laundary_service_order.delivery_date,Order_Status 
                                                                   FROM order_status,laundary_service_order,booking_detail,room
                                                                   WHERE booking_detail.booking_detail_id=laundary_service_order.booking_detail_id AND
                                                                   order_status.Order_Status_id=laundary_service_order.Order_Status_id AND
                                                                   room.room_id=booking_detail.room_id AND
                                                                   (order_date=? OR delivery_date=?)
                         """;
                    pst =con.prepareStatement(sql);
                    pst.setString(1, date);
                    pst.setString(2, date);
                    rs = pst.executeQuery();
                    order.clear();
                    while(rs.next()){
                        order.add(new OrderData(rs.getInt("order_id"), rs.getInt("room_id"), rs.getString("order_date"), rs.getString("delivery_date"), rs.getString("Order_Status")));

                    }
    }
    
    public void loadOrderDetail() throws SQLException{
        lblOrderNo.setText(String.valueOf(order_id));
        lblRoomNo.setText(String.valueOf(room_id));
        lblOrderDate.setText(order_date);
        lblDeliveryDate.setText(deliver_date);
        
        String sql = """
                     select * from laundary_service_order_detail,laundary_service,cloth,laundary_service_type where order_id= ? AND
                     laundary_service.service_id=laundary_service_order_detail.service_id AND
                     laundary_service_type.service_type_id=laundary_service.service_type_id AND
                     cloth.cloth_id = laundary_service.cloth_id""";
        pst = con.prepareStatement(sql);
        pst.setInt(1, order_id);
        rs =pst.executeQuery();
        int i=0;
        orderDetail.clear();
        while(rs.next()){
            i++;
            int total = rs.getInt("quantity")*rs.getInt("price");
            orderDetail.add(new OrderData(i,rs.getString("service_type"),rs.getString("cloth_name"),rs.getInt("quantity"),rs.getInt("price"),total));
        }
        calculateGrandTotal();
    }
    
    public void calculateGrandTotal(){
        int grand_total=0;
        for(OrderData detailData: orderDetail){
            grand_total += detailData.getTotal();
        }
        txtGrandTotal.setText(String.valueOf(grand_total));
    }

}
