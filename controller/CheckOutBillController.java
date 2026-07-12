package controller;

import database.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import java.sql.Statement;
import java.time.Period;
import javafx.event.ActionEvent;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class CheckOutBillController implements Initializable {

    @FXML
    private Label lbName;
    @FXML
    private Label lbDate;
    @FXML
    private GridPane gridpane;
    @FXML
    private Label calTotal;
    @FXML
    private Label calDeposit;
    @FXML
    private Label lbRemaining;
    @FXML
    private Label lbDeposit;
    
    private Statement st;
    private Connection con;
    private ResultSet rs;
    private PreparedStatement pst;
    
    private String guestName;
    int detail_id;
    @FXML
    private AnchorPane voucherAnchor;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnPrint;
    
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {

       
        try {
            // Set the current date
            lbDate.setText(LocalDate.now().toString());
         
            Database db = new Database();
            setupGridColumns(); // Setup equal column widths and padding
            
            
            con = db.getConnection();
            if (con != null) {
                guestName();
                lbName.setText(guestName);
                String status=getPaymentStatus();
                lbDeposit.setText(status);
                initSalelist();
                btnBack.setVisible(false);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CheckOutBillController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CheckOutBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        } 
    


    public void guestName() throws SQLException {
        // Corrected query based on the 'myat_hotel (2).sql' schema
        String sql = "SELECT g.name FROM guest AS g " +
                     "JOIN booking AS b ON g.guest_id = b.guest_id " +
                     "JOIN booking_detail AS bd ON b.booking_id = bd.booking_id " +
                     "WHERE bd.booking_detail_id = ?;";
        pst = con.prepareStatement(sql);
        pst.setInt(1, Check_outController.booking_detail_id);
        rs = pst.executeQuery();
        if (rs.next()) {
            guestName = rs.getString("name");
        }
    }
    //example
    public void setData(int detail_id){
        this.detail_id = detail_id;
    }
    
    public double getDepositRate() throws SQLException {
        // Query to get the deposit rate from payment_status table
        String sql = "SELECT rate FROM payment_status WHERE payment_status LIKE '%Deposit%';";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getDouble("rate");
        }
        return 50;
    }
    
    public String getPaymentStatus() throws SQLException {
        // Query to get the payment status string
        String sql = "SELECT payment_status FROM payment_status WHERE payment_status LIKE '%deposit%';";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getString("payment_status");
        }
        return "Deposit";
    }

    public void initSalelist() throws SQLException {
        String sql = "SELECT room.room_id , room_type.room_type, booking_detail.check_in_date, booking_detail.check_out_date, booking_detail.price FROM booking JOIN booking_detail ON booking.booking_id = booking_detail.booking_id JOIN room ON booking_detail.room_id = room.room_id JOIN room_type ON room.room_type_id = room_type.room_type_id WHERE booking_detail.booking_detail_id = ?";
        pst = con.prepareStatement(sql);
        pst.setInt(1, Check_outController.booking_detail_id);
        ResultSet rs1 = pst.executeQuery();

        System.out.println("booking_detail_id used: " + Check_outController.booking_detail_id);

        int rowIndex = 1;
        double total = 0.0;
        double rate = getDepositRate();

        gridpane.getChildren().removeIf(node -> {
            Integer row = GridPane.getRowIndex(node);
            return row != null && row > 0;
        });

        gridpane.getRowConstraints().clear();
        gridpane.getRowConstraints().add(new RowConstraints(40, 40, Double.MAX_VALUE));
        
        if (rs1.next()) {
            System.out.println("Query returned data.");
            String roomNo = rs1.getString("room_id");
            String roomType = rs1.getString("room_type");
            String checkIn = rs1.getString("check_in_date");
            String checkOut = rs1.getString("check_out_date");
            double price = rs1.getDouble("price");
            Period p = Period.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut));
            double totalDay = p.getDays();
            total += price*totalDay;

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(40);
            rowConstraints.setVgrow(Priority.ALWAYS);
            gridpane.getRowConstraints().add(rowConstraints);

            addStyledLabelToGrid(roomNo, 0, rowIndex);
            addStyledLabelToGrid(roomType, 1, rowIndex);
            addStyledLabelToGrid(checkIn, 2, rowIndex);
            addStyledLabelToGrid(checkOut, 3, rowIndex);
            addStyledLabelToGrid(String.format("%.2f", price), 4, rowIndex);

            rowIndex++;
        } else {
             System.out.println("Query returned no data for the provided booking_detail_id.");
        }
       
        calTotal.setText(String.format("%.2f", total));
        double depositAmount = total * rate;
        calDeposit.setText(String.format("%.2f", depositAmount));
        lbRemaining.setText(String.format("%.2f", total - depositAmount));
    }

    private void addStyledLabelToGrid(String text, int colIndex, int rowIndex) {
        Label label = new Label(text);
        label.setFont(Font.font("Times New Roman", 16));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        GridPane.setFillWidth(label, true);
        GridPane.setFillHeight(label, true);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);

        gridpane.add(label, colIndex, rowIndex);
    }
    
    private void setupGridColumns() {
        gridpane.getColumnConstraints().clear();
        for (int i = 0; i < 5; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(20);
            col.setHalignment(HPos.CENTER);
            gridpane.getColumnConstraints().add(col);
        }
        
        gridpane.setVgap(8);
        gridpane.setPadding(new Insets(10));
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        Stage stage = (Stage)btnBack.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handlePrintAction(ActionEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            System.out.println("No printers installed");
            return;
        }
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if(job != null && job.showPrintDialog(voucherAnchor.getScene().getWindow())){
            boolean success = job.printPage(voucherAnchor);
            if(success){
                job.endJob();
                System.out.println("Voucher printed successfully.");
            }
            else{
                System.out.println("Printing failed.");
            }
        }
        btnBack.setVisible(true);
    }
}