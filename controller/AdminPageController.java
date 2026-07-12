/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class AdminPageController implements Initializable {

    @FXML
    private AnchorPane whiteAnchor;
    @FXML
    private ImageView whiteMenu;
    @FXML
    private AnchorPane defaultAnchor;
    @FXML
    private AnchorPane BlackAnchor;
    @FXML
    private ImageView BlackMenu;
    @FXML
    public Label lbAdminName;
  @FXML
    private Button btnHistory;
    public static int empID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         String userName = utils.UserSession.getUserName();
         
         lbAdminName.setText(userName);
         empID=utils.UserSession.getEmployeeId();
         BlackAnchor.setTranslateX(-180);
         BlackAnchor.setVisible(false);
        hiddenViewMenu();
        try {
            loadFXMLIntoDefaultAnchor("/view/bar_and_pie.fxml");
        } catch (IOException ex) {
            Logger.getLogger(AdminPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void handleDashboard(ActionEvent event) throws IOException {
        loadFXMLIntoDefaultAnchor("/view/bar_and_pie.fxml");
        hideBlackMenu();
    }

    @FXML
    private void handleEmp(ActionEvent event) throws IOException {
        loadFXMLIntoDefaultAnchor("/view/Employee_1.fxml");
        hideBlackMenu();
    }

    @FXML
    private void handleRoom(ActionEvent event) throws IOException {
        loadFXMLIntoDefaultAnchor("/view/adminRoom.fxml");
        hideBlackMenu();
    }

    @FXML
    private void handleFood(ActionEvent event) throws IOException {
                  loadFXMLIntoDefaultAnchor("/view/AdminFood.fxml");
         hideBlackMenu();

    }
    @FXML
    void handleAdminHall(ActionEvent event) throws IOException {
         loadFXMLIntoDefaultAnchor("/view/Hall.fxml");
            hideBlackMenu();
    }
        @FXML
    void handleUserAccount(ActionEvent event) throws IOException {
            loadFXMLIntoDefaultAnchor("/view/viewUserData.fxml");
            hideBlackMenu();

    }
    
    @FXML
    void handleLaundry(ActionEvent event) throws IOException {
         loadFXMLIntoDefaultAnchor("/view/adminLaundary.fxml");
         hideBlackMenu();

    }
  @FXML
    void handleHallHistory(ActionEvent event) throws IOException {
        hideBlackMenu();
           loadFXMLIntoDefaultAnchor("/view/hallDetailCard.fxml");
    }
    @FXML
    void handleHistory(ActionEvent event) throws IOException {
hideBlackMenu();
           loadFXMLIntoDefaultAnchor("/view/roombookingHistory.fxml");
    }
   

    void loadFXMLIntoDefaultAnchor(String fxmlPath) throws IOException {
          FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        AnchorPane loadedPane = loader.load();
        defaultAnchor.getChildren().clear();
        defaultAnchor.getChildren().add(loadedPane);

    }

      public void hiddenViewMenu(){
         whiteMenu.setOnMouseClicked(e -> {
             TranslateTransition slider = new TranslateTransition();
            slider.setDuration(Duration.seconds(0.6));
            slider.setNode(BlackAnchor);
            slider.setToX(0);
            slider.play();
            slider.setOnFinished(e1 -> {
            defaultAnchor.setDisable(true); 
            defaultAnchor.setOpacity(0.2);
            whiteAnchor.setVisible(false); 
            BlackAnchor.setVisible(true); 
            BlackMenu.setVisible(true); 
            });
        });

          BlackMenu.setOnMouseClicked(e -> {
            hideBlackMenu(); // Call the new method to hide the menu
        });
    }
      public void hideBlackMenu() {
        whiteAnchor.setVisible(true); 
        BlackMenu.setVisible(false); 
        defaultAnchor.setDisable(false); 
        defaultAnchor.setOpacity(1.0); 

        TranslateTransition slider = new TranslateTransition();        slider.setDuration(Duration.seconds(0.1));
        slider.setNode(BlackAnchor);
        slider.setToX(-185);
        slider.play();
        slider.setOnFinished(e1 -> {
            BlackAnchor.setVisible(false); 
        });
    }
      
    @FXML
    void handleImageAction(MouseEvent event) throws IOException {
         System.out.println("clicked");
                        int adminId = utils.UserSession.getEmployeeId();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ViewEmpitself.fxml")); // Adjust path if needed
            Parent root = loader.load();
            
            ViewEmpitselfController viewEmpitselfController = loader.getController();
            viewEmpitselfController.setEmployeeId(adminId);
            Stage viewStage = new Stage();
            viewStage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal window
            viewStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets the owner window
            viewStage.setTitle("Profile");
            viewStage.setScene(new Scene(root));
           // viewStage.initStyle(StageStyle.UNDECORATED); // Optional: remove window decorations
            viewStage.showAndWait(); // Show the window and wait for it to be closed

    }

    
}
