package controller;

import javafx.scene.paint.Color;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.HallBookingList;

public class FinalSlipController implements Initializable {

    @FXML
    private AnchorPane slippane;
    @FXML
    private GridPane slipgrid;
    @FXML
    private Label txttotal;
    @FXML
    private Label lbldate;
    @FXML
    private Label txtdepoist;
    @FXML
    private Label txtremaincharges;
    @FXML
    private ImageView paidimage;
    @FXML
    private Button btnpayment;
    @FXML
    private Button btnback;
    int price;

  //  private double pricePerHour = 100.0;  // example hourly rate, can be customized

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paidimage.setVisible(false);
    }

    @FXML
    private void hanblepaymentAction(ActionEvent event) {
      paidimage.setVisible(true);
        Printer print=Printer.getDefaultPrinter();
        if(print==null){
            System.out.print("No Printer");
            return;
        }
        PrinterJob job=PrinterJob.createPrinterJob(print);
        if(job!=null && job.showPrintDialog(slippane.getScene().getWindow())){
            boolean success =job.printPage(slippane);
            if(success){
                job.endJob();
                System.out.println("Voucher printed Successfully...");
            }else{
                System.out.println("Printing Failed...");
            }
        }
    }

    @FXML
    private void handlebackaction(ActionEvent event) {
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Helper method to create styled label for slip grid
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Courier New", FontWeight.NORMAL, 13));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.BLACK);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    // Helper method to add label to grid and center align it
    private void addLabelToGridCenter(Label label, int col, int row) {
        slipgrid.add(label, col, row);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
    }

    // Call this method to populate the slip with data from HallBookingList
    public void populateFinalSlip(HallBookingList data) {
        // Clear previous data rows, keep header row intact (row 0)
        slipgrid.getChildren().removeIf(node -> {
            Integer row = GridPane.getRowIndex(node);
            return row != null && row > 0;
        });

        // Add booking details in row 1
        addLabelToGridCenter(createStyledLabel(data.getGuestName()), 0, 1);
        addLabelToGridCenter(createStyledLabel(data.getHallId()), 1, 1);
        addLabelToGridCenter(createStyledLabel(data.getBookDate().toString()), 2, 1);
        addLabelToGridCenter(createStyledLabel(data.getWantedDate().toString()), 3, 1);
        addLabelToGridCenter(createStyledLabel(data.getStartTime().toString()), 4, 1);
        addLabelToGridCenter(createStyledLabel(data.getEndTime().toString()), 5, 1);
        addLabelToGridCenter(createStyledLabel(String.valueOf(data.getPrice())), 6, 1);
        price=data.getPrice();

        // Calculate total price based on duration
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime startTime = LocalTime.parse(data.getStartTime().toString(), timeFormatter);
        LocalTime endTime = LocalTime.parse(data.getEndTime().toString(), timeFormatter);

        long hours = Duration.between(startTime, endTime).toHours();
        if (hours <= 0) hours = 1; // minimum 1 hour charge

        double totalPrice =  price * hours;
        double deposit=totalPrice*0.3;
        double remain=totalPrice-deposit;

        txttotal.setText(String.format("%.2f", totalPrice));
        txtdepoist.setText(String.format("%.2f",deposit));
        txtremaincharges.setText(String.format("%.2f",remain));
        
    }
}