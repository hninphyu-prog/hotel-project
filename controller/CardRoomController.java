/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import model.Room;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class CardRoomController implements Initializable {

    @FXML
    private AnchorPane cardAnchor;
    @FXML
    private ImageView cardImage;
    @FXML
    private Label lblroomtype;
    @FXML
    private Label lblroomno;
    @FXML
    private Label lblRoomPrice;
    @FXML
    private Button btnbook;
    @FXML
    private Label lblfloorno;
    
    private Room room;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleActionbook(ActionEvent event) {
        if(controller.RoomBookingController.check_in.isEmpty()){
            JOptionPane.showMessageDialog(null, "please choose check in check out date");
        }else if(controller.RoomBookingController.check_out.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please choose check out date");
        }else{
            LocalDate start = LocalDate.parse(controller.RoomBookingController.check_in);
            LocalDate end = LocalDate.parse(controller.RoomBookingController.check_out);
            Room selectedRoom = new Room(room.getRoomno(), room.getRoomType(), room.getRoomprice(), start.toString(), end.toString());
            if (controller.RoomBookingController.cartList.contains(selectedRoom)){
                JOptionPane.showMessageDialog(null, "You have already added this specific room and date range to your cart.");
                return;
            }
            for(Room r:controller.RoomBookingController.cartList){
                if(r.getRoomno().equals(room.getRoomno())){
                    LocalDate cartStart = LocalDate.parse(r.getCheckInDate());
                    LocalDate cartEnd = LocalDate.parse(r.getCheckOutDate());
                    // --- REVISED OVERLAP LOGIC ---
                    // An overlap occurs if (new_start < existing_end) AND (new_end > existing_start)
                    // This allows a new booking to start on the same day an old one checks out.
                    boolean isOverlapping = start.isBefore(cartEnd) && end.isAfter(cartStart);

                    if (isOverlapping) {
                        JOptionPane.showMessageDialog(null, "Room " + room.getRoomno() + " is not available for the selected dates because it overlaps with an existing booking.");
                        return; // Stop if an overlap is found
                    }
                }
            }
            controller.RoomBookingController.cartList.add(selectedRoom);
            JOptionPane.showMessageDialog(null, "Room " + room.getRoomno() + " booked successfully from " + start + " to " + end + "!");
            
            // After checking for all existing bookings, if no overlap, then add the room
            // Also, ensure the Room class has a proper equals() and hashCode() as discussed previously,
            // where equality is based on room number, check-in, and check-out dates.
            //Room selectedRoom = new Room(room.getRoomno(), room.getRoomType(), room.getRoomprice(), start.toString(), end.toString());
    
            // This check is to prevent adding the exact same booking (room+dates) multiple times.
            // If the equals() method of Room considers only room number and dates, then this is useful.
            /*if (!view.RoomBookingController.cartList.contains(selectedRoom)) {
                view.RoomBookingController.cartList.add(selectedRoom);
                JOptionPane.showMessageDialog(null, "Room " + room.getRoomno() + " booked successfully from " + start + " to " + end + "!");
            } else {
                JOptionPane.showMessageDialog(null, "You have already added this specific room and date range to your cart.");
            }*/
        }
    }
    
    public void setData(Room room) {
        this.room=room;
        lblroomno.setText(room.getRoomno());
        lblroomtype.setText(room.getRoomType());
        lblfloorno.setText(room.getFloornno()+" floor");
        lblRoomPrice.setText("$" + room.getRoomprice());

        // Load image from file path
        File file = new File("C:\\Users\\USER\\Desktop\\Hotel_Management (2)\\Hotel_Management\\src\\image\\" + room.getImageName());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            cardImage.setImage(image);
        }

    
        cardImage.setPreserveRatio(true);
    
}
    
    
}
