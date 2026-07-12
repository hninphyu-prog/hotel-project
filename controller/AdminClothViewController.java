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
import model.Cloth;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AdminClothViewController implements Initializable {

    @FXML
    private ImageView cloth_imgView;
    @FXML
    private Label lblClothName;
    @FXML
    private Button btnAddService;
    @FXML
    private Button btnEditClothData;
    private Cloth cloth;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleAddServiceAction(ActionEvent event) throws IOException {
        Stage ownerStage = (Stage) btnAddService.getScene().getWindow();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addServiceAdminForm.fxml"));
                            Parent root = loader.load();
                            AddServiceAdminFormController modalcontroller = loader.getController();
                            modalcontroller.setData(cloth.getCloth_id(),lblClothName.getText());
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new Service Dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
    }

    @FXML
    private void handleEditClothDataAction(ActionEvent event) throws IOException, SQLException {
        Stage ownerStage = (Stage) btnEditClothData.getScene().getWindow();
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminClothCardEditAndAddForm.fxml"));
                            Parent root = loader.load();
                            AdminClothCardEditAndAddFormController modalcontroller = loader.getController();
                            modalcontroller.setData(cloth);
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Edit cloth photo");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
                           
    }
    
    public void setData(Cloth c){
        cloth=c;
        lblClothName.setText(cloth.getCloth_name());
        // Load image from file path
        File file = new File("C:\\Users\\Dell\\OneDrive\\Desktop\\Khant Zaw Win Hotel\\Myat Thu Hotel\\src\\image\\" + cloth.getCloth_img());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            cloth_imgView.setImage(image);
        }

    
        cloth_imgView.setPreserveRatio(true);
    }
    
    
    
}
