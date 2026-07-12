package controller;

import database.Database;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HallBillController implements Initializable {

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
    private AnchorPane AnchorButton;
    @FXML
    private Button btnsave;
    @FXML
    private Button btnexist;
    @FXML
    private AnchorPane hallbillpane;
    
    @FXML
    private ImageView imgdeposit;
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    private int bookId;
    private String guestName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        try {
            con = db.getConnection();
            imgdeposit.setVisible(false);
            setupGridColumns();
            countbookId();
            guestName();
            lblname.setText(guestName);
            lbldate.setText(LocalDate.now().toString());
            initHallSaleList();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HallBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

@FXML
private void handlersaveAction(ActionEvent event) {
    System.out.println("jiji");
    imgdeposit.setVisible(true);
      Printer print=Printer.getDefaultPrinter();
        if(print==null){
            System.out.print("No Printer");
            return;
        }
        PrinterJob job=PrinterJob.createPrinterJob(print);
        if(job!=null && job.showPrintDialog(hallbillpane.getScene().getWindow())){
            boolean success =job.printPage(hallbillpane);
            if(success){
                job.endJob();
                System.out.println("Voucher printed Successfully...");
            }else{
                System.out.println("Printing Failed...");
            }
        }
   
}


    @FXML
    private void handlerExistAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

   

    public void guestName() throws SQLException {
        String sql = "SELECT g.name AS guestName FROM guest g "
                   + "JOIN hall_book hb ON g.guest_id = hb.guest_id "
                   + "WHERE hb.hall_booked_id = ?";
        pst = con.prepareStatement(sql);
        pst.setInt(1, bookId);
        rs = pst.executeQuery();
        if (rs.next()) {
            guestName = rs.getString("guestName");
            System.out.println("Guest Name: " + guestName);
        }
    }

    public void countbookId() throws SQLException {
        String sql = "SELECT MAX(hall_booked_id) AS bookingId FROM hall_book;";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        if (rs.next()) {
            bookId = rs.getInt("bookingId");
            System.out.println("Booking ID: " + bookId);
        }
    }

     private void addStyledLabelToGrid(String text, int colIndex, int rowIndex) {
        Label label = new Label(text);
        label.setFont(Font.font("Courier New", 13));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        GridPane.setFillWidth(label, true);
        GridPane.setFillHeight(label, true);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);

        gridpane.add(label, colIndex, rowIndex);
    }

    public void initHallSaleList() throws SQLException {
        String sql = "SELECT h.hall_id AS hall_no, ht.hall_type, hbd.want_date, "
                   + "hbd.start_time, hbd.end_time, hbd.price "
                   + "FROM hall_book hb "
                   + "JOIN hall_booked_detail hbd ON hb.hall_booked_id = hbd.hall_booked_id "
                   + "JOIN hall h ON hbd.hall_id = h.hall_id "
                   + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
                   + "WHERE hb.hall_booked_id = ?";

        pst = con.prepareStatement(sql);
        pst.setInt(1, bookId);
        rs = pst.executeQuery();

        int rowIndex = 1;
        double total = 0.0;
        gridpane.getRowConstraints().clear();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // MySQL TIME format

        while (rs.next()) {
            String hallNo = rs.getString("hall_no");
            String hallType = rs.getString("hall_type");
            String wantDate = rs.getString("want_date");
            String startTimeStr = rs.getString("start_time");
            String endTimeStr = rs.getString("end_time");
            double pricePerHour = rs.getDouble("price");

            try {
                LocalTime startTime = LocalTime.parse(startTimeStr, timeFormatter);
                LocalTime endTime = LocalTime.parse(endTimeStr, timeFormatter);

                long hours = Duration.between(startTime, endTime).toHours();
                if (hours <= 0) hours = 1;

                double totalPrice = pricePerHour * hours;
                total += totalPrice;
            //    String displaytotal=Strin
             
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(40);
                rowConstraints.setVgrow(Priority.ALWAYS);
                gridpane.getRowConstraints().add(rowConstraints);

                addStyledLabelToGrid(hallNo, 0, rowIndex);
                addStyledLabelToGrid(hallType, 1, rowIndex);
                addStyledLabelToGrid(wantDate, 2, rowIndex);
                addStyledLabelToGrid(startTimeStr , 3, rowIndex);
                addStyledLabelToGrid(endTimeStr , 4, rowIndex);
                addStyledLabelToGrid(String.valueOf(totalPrice), 5, rowIndex);

                rowIndex++;

            } catch (Exception e) {
                System.err.println("Time parse error: " + e.getMessage());
            }
        }

        lbltotal.setText(String.format("%.2f", total));
        double deposit = total * 0.3;
        lbldepoist.setText(String.format("%.2f", deposit));
        System.out.println("Total Hall Price: " + total);
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
}
