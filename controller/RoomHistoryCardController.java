package controller;
import database.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class RoomHistoryCardController implements Initializable {

    @FXML
    private Label lblRoomNo;
    @FXML
    private Label lblCheckIn;
    @FXML
    private Label lblcheckOut;
    @FXML
    private Label lblstatus;
    @FXML
    private Button btnDetail;

    // Keep guest name as a private field, but don't display it on UI
    private String guestName;
    public static int bookingDetailId;
     Connection con;
    
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        try {
            con = db.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RoomHistoryCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Modify setData to accept guestName
    public void setData(int bookingDetailId,int roomNO, String guestName, String checkIn, String checkOut, String status) {
        this.bookingDetailId=bookingDetailId;
        lblRoomNo.setText(String.valueOf(roomNO));
        this.guestName = guestName;  // store guest name
        lblCheckIn.setText(checkIn);
        lblcheckOut.setText(checkOut);
        lblstatus.setText(status);
    }

    @FXML
    public void handleDetailBtnAction(ActionEvent event) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RoomCardDetail.fxml"));
            AnchorPane pane = loader.load();

            // Get modal controller to pass data
            RoomCardDetailController modalController = loader.getController();

            // Pass details including guest name to modal
            modalController.setBookingDetails(
               
                lblRoomNo.getText(),
                guestName,  // Pass guest name here
                lblCheckIn.getText(),
                lblcheckOut.getText()
            );
            booking_detail_id();
           
            // Create modal stage
            Stage modalStage = new Stage();
            modalStage.setTitle("Booking Details");
            modalStage.setScene(new Scene(pane));
            modalStage.setResizable(false);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void booking_detail_id() throws SQLException{
        String sql="SELECT booking_detail.booking_detail_id FROM room,booking_detail WHERE booking_detail.room_id=room.room_id "
                + "and room.room_id=? AND booking_detail.check_in_date=? AND booking_detail.check_out_date=?;";
        PreparedStatement pst = con.prepareStatement(sql);
          pst.setString(1, lblRoomNo.getText());
          pst.setString(2, lblCheckIn.getText());
          pst.setString(3, lblcheckOut.getText());
          rs = pst.executeQuery();
        if (rs.next()) {
            bookingDetailId = rs.getInt("booking_detail_id");
            System.out.println("Fetched booking_detail_id = " + bookingDetailId);
        } else {
            System.out.println("No booking_detail_id found for given input.");
        }
    }
}