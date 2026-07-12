package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Hall;

public class HallHistoryCardController implements Initializable {

    @FXML
    private Label lblHallNo;

    @FXML
    private Label lblWantedDate;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnDetail;

    private Hall currentHall;

    // Add a reference to the main controller for callback
    private HallDetailCardController mainController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
    }    

    @FXML
    private void handleDetailAction(ActionEvent event) {
        if (currentHall != null && mainController != null) {
            mainController.showDetail(currentHall);
        }
    }

    public void setData(Hall hall) {
        this.currentHall = hall;
        lblHallNo.setText(hall.getHallNo());
        lblWantedDate.setText(hall.getBookingDate());
        lblStatus.setText(hall.getBookingStatus() != null ? hall.getBookingStatus() : "Booked");
    }

    

     public void setMainController(HallDetailCardController controller) {
        this.mainController = controller;
    }
}
