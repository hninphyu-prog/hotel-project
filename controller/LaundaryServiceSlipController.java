/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.Laundary_serviceController;
import database.Database;
import static hotel_management.Hotel_Management.stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import model.Service;
import controller.Check_outController;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.OrderData;

/**
 * FXML Controller class
 *
 * @author USER
 */

public class LaundaryServiceSlipController implements Initializable {

   
    
    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    
    ObservableList<Service> slipDataList = FXCollections.observableArrayList();
    @FXML
    private TableView<Service> tbSlipData;
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
    int grand_total ;
    @FXML
    private Label lblOrderNo;
    @FXML
    private Label lblDeliveryDate;
    @FXML
    private Label lblOrderDate;
    @FXML
    private Label lblRoomNo;
    @FXML
    private Button btnPrint;
    Parent root;
    @FXML
    private AnchorPane vocher_pane;
    @FXML
    private Button btnBack;
    boolean print = false;
    OrderData orderData;
 private StaffPageController staffPageController; // New field to hold the reference

    public void setStaffPageController(StaffPageController staffPageController) {
        this.staffPageController = staffPageController; // Setter method
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
            
            btnBack.setVisible(false);
            colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
            colServiceType.setCellValueFactory(new PropertyValueFactory<>("service_name"));
            colClothName.setCellValueFactory(new PropertyValueFactory<>("cloth_name"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("service_price"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            tbSlipData.setItems(slipDataList);
            
            
            
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LaundaryServiceSlipController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    public void initSlipData() throws SQLException{
        
        
        
        String sql = """
                     select * from laundary_service_order_detail,laundary_service,cloth,laundary_service_type where order_id= ? AND
                     laundary_service.service_id=laundary_service_order_detail.service_id AND
                     laundary_service_type.service_type_id=laundary_service.service_type_id AND
                     cloth.cloth_id = laundary_service.cloth_id""";
        pst = con.prepareStatement(sql);
        pst.setInt(1, orderData.getOrder_id());
        rs =pst.executeQuery();
        int i=0;
        while(rs.next()){
            i++;
            int total = rs.getInt("quantity")*rs.getInt("price");
            slipDataList.add(new Service(i,rs.getString("service_type"),rs.getString("cloth_name"),rs.getInt("quantity"),rs.getInt("price"),total));
        }
        
        
        
        
    }
    public void calculate(){
        grand_total=0;
        for(Service ser: slipDataList){
            grand_total += ser.getTotal();
        }
        
    }
    
    @FXML
    private void handlePrintAction(ActionEvent event) throws IOException {
        if(print==true){
            JOptionPane.showMessageDialog(null, "You already Printed");
        }else{
            Check_outController.booking_detail_id=0;
            Check_outController.room_id=0;
            Check_outController.check_out_date="";
            Printer printer = Printer.getDefaultPrinter();
            if(printer==null){
                System.out.println("NO printer installed");//
                return;
            }
            PrinterJob job = PrinterJob.createPrinterJob(printer);
            if(job!=null && job.showPrintDialog(vocher_pane.getScene().getWindow())){
                boolean success = job.printPage(vocher_pane);
                if(success){
                    job.endJob();
                    System.out.println("Voucher printed successfully");
                }else{
                    System.out.println("Printing failed");
                }
            }
            print =true;
            btnBack.setVisible(true);
        }
        

        
    }

    @FXML
    private void handleBackAction(ActionEvent event) throws IOException {
        Stage stage = (Stage)btnBack.getScene().getWindow();
        stage.close();

        
    }
    public void setData(OrderData data) throws SQLException{
        orderData= data;
        initSlipData();
        calculate();
        txtGrandTotal.setText(String.valueOf(grand_total));
            txtGrandTotal.setEditable(false);
            lblOrderNo.setText(String.valueOf(orderData.getOrder_id()));
            lblDeliveryDate.setText(orderData.getDelivery_date());
            lblRoomNo.setText(String.valueOf(orderData.getRoom_id()));
            lblOrderDate.setText(orderData.getOrder_date());
        
    }
}
