/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import static hotel_management.Hotel_Management.stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.RoomType;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AdminViewRoomCardController implements Initializable {

    @FXML
    private ImageView roomType_imgView;
    @FXML
    private Button btnAddRoom;
    @FXML
    private Button btnEdit;
    @FXML
    private Label lblRoomType;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblCapacity;
    
    private RoomType roomType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleAddRoomAction(ActionEvent event) throws IOException {
        Stage ownerStage = (Stage) btnAddRoom.getScene().getWindow();
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddnewRoomCardAdmin.fxml"));
                            Parent root = loader.load();
                            AddnewRoomCardAdminController modalcontroller = loader.getController();
                            modalcontroller.setData(roomType);
                            
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new Room dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
    }

    @FXML
    private void handleEditAction(ActionEvent event) throws IOException, SQLException {
        Stage ownerStage = (Stage) btnEdit.getScene().getWindow();
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminRoomTypeEditAndAddForm.fxml"));
                            Parent root = loader.load();
                            AdminRoomTypeEditAndAddFormController modalcontroller = loader.getController();
                            modalcontroller.setData(roomType);
                            
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Edit room type's data dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
    }
    
    public void setData(RoomType type){
        roomType = type;
        lblRoomType.setText(roomType.getRoom_type());
        lblPrice.setText(String.valueOf(roomType.getPrice()));
        lblCapacity.setText(String.valueOf(roomType.getCapacity()));
        File file = new File("C:\\Users\\Dell\\OneDrive\\Desktop\\Khant Zaw Win Hotel\\Myat Thu Hotel\\src\\image\\" + roomType.getImage_path());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            roomType_imgView.setImage(image);
        }
        
    }
    
}
