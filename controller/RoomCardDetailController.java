/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import static controller.RoomHistoryCardController.bookingDetailId;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;

/**
 * FXML Controller class
 *
 * @author May Na Dar
 */
public class RoomCardDetailController implements Initializable {

    @FXML
    private Label roomid;
    @FXML
    private Label name;
    @FXML
    private Label checkIndate;
    @FXML
    private Label checkoutdate;
    @FXML
    private Pagination pagination;

    /**
     * Initializes the controller class.
     */
   @Override
public void initialize(URL url, ResourceBundle rb) {
    pagination.setPageCount(2); 
    pagination.setPageFactory(pageIndex -> {
        try {
            if (pageIndex == 0) {
                return FXMLLoader.load(getClass().getResource("/view/Food.fxml"));

            } else if (pageIndex == 1) {
                return FXMLLoader.load(getClass().getResource("/view/service.fxml"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    });
}


    public void setBookingDetails(String roomNo, String guestName, String checkIn, String checkOut) {
       
        roomid.setText(roomNo);
        name.setText(guestName);
        checkIndate.setText(checkIn);
       checkoutdate.setText(checkOut);
    }
    
}
