package controller;

import database.Database;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class BillController implements Initializable {

    @FXML
    private AnchorPane roomBillPane;

    @FXML
    private Label lblname;
    @FXML
    private Label lbldate;
    @FXML
    private GridPane gridpane;
    @FXML
    private Label lbltotal;
    @FXML
    private Label lbldepoist;
  
     @FXML
    private Button btnBack;


    @FXML
    private Label lbDeposit;

    private Statement st;
    private Connection con;
    private ResultSet rs;
    private PreparedStatement pst;
    private int bookId;
    private String guestName;
    boolean print = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Database db = new Database();
            con = db.getConnection();
            setupGridColumns(); // Setup equal column widths and padding
            countbookId();
            guestName();
            lblname.setText(guestName);
            btnBack.setVisible(print);
            LocalDate date = LocalDate.now();
            lbldate.setText(date.toString());
            String status=getPaymentStatus();
            lbDeposit.setText(status);
            initSalelist();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void countbookId() throws SQLException {
        String sql = "SELECT max(Booking_id) as bookingId FROM booking;";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        if (rs.next()) {
            bookId = rs.getInt("bookingId");
            System.out.println("Booking ID: " + bookId);
        }
    }

    public void guestName() throws SQLException {
        String sql = "SELECT name AS guestName FROM guest, booking WHERE guest.guest_id = booking.guest_id AND booking.booking_id = ?;";
        pst = con.prepareStatement(sql);
        pst.setInt(1, bookId);
        rs = pst.executeQuery();
        if (rs.next()) {
            guestName = rs.getString("guestName");
            System.out.println("Guest Name: " + guestName);
        }
    }
  public double getDepositRate() throws SQLException {
        // Query to get the deposit rate from payment_status table
        String sql = "SELECT rate FROM payment_status WHERE payment_status LIKE '%Deposit%';";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getDouble("rate");
        }
        return 0.5;
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
       String sql = "SELECT room.room_id, room_type.room_type, booking_detail.check_in_date, " +
                     "booking_detail.check_out_date, booking_detail.price " +
                     "FROM room, room_type, booking_detail, booking " +
                     "WHERE room.room_type_id = room_type.room_type_id " +
                     "AND room.room_id = booking_detail.room_id " +
                     "AND booking_detail.booking_id = booking.booking_id " +
                     "AND booking.booking_id = ?;";

        pst = con.prepareStatement(sql);
        pst.setInt(1, bookId);
       ResultSet rsSale = pst.executeQuery();

        int rowIndex = 1;
        double total = 0.0;
        double rate=getDepositRate();
        System.out.println(rate);
        gridpane.getRowConstraints().clear(); // clear old rows if needed

        while (rsSale.next()) {
            String roomNo = rsSale.getString("room_id");
            String roomType = rsSale.getString("room_type");
            String checkIn = rsSale.getString("check_in_date");
            String checkOut = rsSale.getString("check_out_date");
            double price = rsSale.getDouble("price");
            Period period = Period.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut));
            double totalDay= period.getDays();
            total += price*totalDay;

            // Add equal row height constraint
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(40); // Fixed equal height
            rowConstraints.setVgrow(Priority.ALWAYS);
            gridpane.getRowConstraints().add(rowConstraints);

            // Add data to grid
            addStyledLabelToGrid(roomNo, 0, rowIndex);
            addStyledLabelToGrid(roomType, 1, rowIndex);
            addStyledLabelToGrid(checkIn, 2, rowIndex);
            addStyledLabelToGrid(checkOut, 3, rowIndex);
            addStyledLabelToGrid(String.format("%.2f", price), 4, rowIndex);

            rowIndex++;
        }

        lbltotal.setText(String.format("%.2f", total));
        lbldepoist.setText(String.format("%.2f", total * rate )); // 50% deposit
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
            col.setPercentWidth(20); // Equal width for 5 columns
            col.setHalignment(HPos.CENTER);
            gridpane.getColumnConstraints().add(col);
        }

       
        gridpane.setVgap(8);   // Vertical gap between cells
        gridpane.setPadding(new Insets(10)); // Padding around the grid
    }
       @FXML
    void handleBackAction(ActionEvent event) {
         if(!print){
            JOptionPane.showMessageDialog(null, "Please print voucher first");
        }else{
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void handlePrintAction(ActionEvent event) {
         Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            System.out.println("No printers installed");
            return;
        }
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if(job != null && job.showPrintDialog(roomBillPane.getScene().getWindow())){
            boolean success = job.printPage(roomBillPane);
            if(success){
                job.endJob();
                System.out.println("Voucher printed successfully.");
            }
            else{
                System.out.println("Printing failed.");
            }
        }
        print = true;
        btnBack.setVisible(print);
    }
}
