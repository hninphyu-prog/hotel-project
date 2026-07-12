/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Hall;

/**
 * FXML Controller class
 *
 * @author May Na Dar
 */
public class HallCardController implements Initializable {

    @FXML
    private ImageView HallimageView;
    @FXML
    private Label lblHallType;
    @FXML
    private Label lblHallNo;
    @FXML
    private Label lblPrice;
    @FXML
    private Button btnedit;
    @FXML
    private Label lblcapacity;
    @FXML
    private Button btnView;
    
    Hall hall;
    public HallController hallController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    @FXML
    private void handleeditAction(ActionEvent event){
       if (hallController != null) {
        hallController.handleEdit(hall);
     // call the right controller
    }
         
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
    }
      
       public void setData(Hall hall, HallController hallController) {
    this.hall = hall;
    this.hallController = hallController;

    lblHallType.setText(hall.getHalltype());
    lblHallNo.setText(hall.getHallNo());
    lblPrice.setText(String.valueOf(hall.getPrice()));
    lblcapacity.setText(String.valueOf(hall.getCapacity()));

    if (hall.getImagename()!= null && !hall.getImagename().isEmpty()) {
        File file = new File("C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\" + hall.getImagename());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            HallimageView.setImage(image);
        }
    }
    
}
   
    

    
}
