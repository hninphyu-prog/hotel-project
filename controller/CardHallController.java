package controller;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import model.Hall;

import controller.HallBookingController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * FXML Controller class for each Hall card.
 */
public class CardHallController implements Initializable {

  

    @FXML
    private ImageView HallimageView;
    @FXML
    private Label lblHallType;
    @FXML
    private Label lblHallNo;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblcapacity;


  
    private HallBookingController hallBookingController;
   String bookeddate=hallBookingController.date;
    Hall hall;
    @FXML
    private Button btnedit;
    @FXML
    private Button btnView;
    public void setHall(Hall hall) {
    this.hall = hall;
} 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }
 @FXML
 void btnBookingHandlerAction(ActionEvent event) {

    if (controller.HallBookingController.date == null || controller.HallBookingController.date.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please Select the Booked Date");
        return;
    }
    if (controller.HallBookingController.type == null || controller.HallBookingController.type.isEmpty()
        || controller.HallBookingController.type.equals("All")) {
        JOptionPane.showMessageDialog(null, "Please Select the Hall Type");
        return;
    }
    if (controller.HallBookingController.startTime == null || controller.HallBookingController.startTime.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please fill the Start Time");
        return;
    }
    if (controller.HallBookingController.endTime == null || controller.HallBookingController.endTime.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please fill the End Time");
        return;
    }

    try {
        // Parse booked date
        String bookeddate = controller.HallBookingController.date;
        // DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       //   LocalDate bookedDate = LocalDate.parse(bookeddate);
      //   String bookedDateStr = bookedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Parse start and end times
        LocalTime starttime = LocalTime.parse(controller.HallBookingController.startTime);
        LocalTime endtime = LocalTime.parse(controller.HallBookingController.endTime);

        if (!starttime.isBefore(endtime)) {
            JOptionPane.showMessageDialog(null, "Start time must be before end time.");
            return;
        }

        // Prevent double booking
        for (Hall existing : controller.HallBookingController.hallcartList) {
            if (existing.getHallNo().equals(hall.getHallNo())
                && existing.getBookingDate().equals(bookeddate)) {

                LocalTime existingStart = LocalTime.parse(existing.getStartTime());
                LocalTime existingEnd = LocalTime.parse(existing.getEndTime());

                if (starttime.isBefore(existingEnd) && endtime.isAfter(existingStart)) {
                    JOptionPane.showMessageDialog(null,
                            "Hall " + hall.getHallNo() + " is already booked on " + bookeddate +
                            " from " + existing.getStartTime() + " to " + existing.getEndTime());
                    return;
                }
            }
        }

        // Add new booking
        Hall bookedHall = new Hall(
                hall.getHallNo(),
                hall.getHalltype(),
                bookeddate,
                starttime.toString(),
                endtime.toString(),
                hall.getPrice()
        );

        controller.HallBookingController.hallcartList.add(bookedHall);

        JOptionPane.showMessageDialog(null,
                "Hall " + bookedHall.getHallNo() + " booked successfully on " +
                bookedHall.getBookingDate() + " from " + starttime + " to " + endtime + "!");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Booking error: " + e.getMessage());
        e.printStackTrace();
    }
}


    public void setData(Hall hall) {
         this.hall = hall;
        lblHallType.setText(hall.getHalltype());
        lblHallNo.setText(hall.getHallNo());
        lblcapacity.setText(hall.getCapacity());
        lblPrice.setText("$" + hall.getPrice());

        // Load image from local file path (adjust path to your environment)
        File file = new File("C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\" + hall.getImagename());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            HallimageView.setImage(image);
        } else {
            HallimageView.setImage(null); // or set a placeholder image if you want
        }
        HallimageView.setPreserveRatio(true);

      
    }

  

    
   

    

   
}   





    

