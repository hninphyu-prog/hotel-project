/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class StaffPageController implements Initializable {
@FXML
    private AnchorPane nameAnchor;
    @FXML
    private AnchorPane whiteAnchor;
    @FXML
    private ImageView whiteMenu;
   
    @FXML
    private AnchorPane BlackAnchor;
    @FXML
    private ImageView BlackMenu;
    @FXML
    public AnchorPane defaultAnchor;
        @FXML
    public Label lbStaffName;
       @FXML
    public Label lbDate;
    public static int empID;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         String userName = utils.UserSession.getUserName();
         lbStaffName.setText(userName);
         empID=utils.UserSession.getEmployeeId();
         
         BlackAnchor.setTranslateX(-180);
         BlackAnchor.setVisible(false);
        hiddenViewMenu();
        lbDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.M.yyyy")));
       loadFXMLIntoDefaultAnchor("/view/RoomBooking.fxml");

       
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
            defaultAnchor.setOpacity(0.7);
            nameAnchor.setOpacity(0.7);
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
        nameAnchor.setOpacity(1.0);
        TranslateTransition slider = new TranslateTransition();
        slider.setDuration(Duration.seconds(0.1));
        slider.setNode(BlackAnchor);
        slider.setToX(-185);
        slider.play();
        slider.setOnFinished(e1 -> {
            BlackAnchor.setVisible(false); 
        });
    }
     // In StaffPageController.java
void loadFXMLIntoDefaultAnchor(String fxmlPath) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        AnchorPane loadedPane = loader.load();
        defaultAnchor.getChildren().clear();
        defaultAnchor.getChildren().add(loadedPane);

        if (fxmlPath.equals("/view/FoodOrder.fxml")) {
            FoodOrderController foodOrderController = loader.getController();
            if (foodOrderController != null) {
                foodOrderController.setStaffPageController(this);
                foodOrderController.setCurrentUserRole("Staff");
            }
        } else if (fxmlPath.equals("/view/Check_out.fxml")) {
            Check_outController checkOutController = loader.getController();
            if (checkOutController != null) {
                checkOutController.setStaffPageController(this);
                checkOutController.setCurrentUserRole("Staff");
            }
        } else if (fxmlPath.equals("/view/laundary_service.fxml")) { // ADD THIS BLOCK
            Laundary_serviceController laundaryServiceController = loader.getController();
            if (laundaryServiceController != null) {
                laundaryServiceController.setStaffPageController(this);
                // If you have a currentUserRole to set for LaundaryServiceController, set it here too
                // laundaryServiceController.setCurrentUserRole("Staff");
            }
        }else if (fxmlPath.equals("/view/FoodOrderList.fxml")) {
            FoodOrderListController foodOrderListController = loader.getController();
            if (foodOrderListController != null) {
                foodOrderListController.setStaffPageController(this);
            }
        }else if (fxmlPath.equals("/view/OrderList.fxml")) {
            OrderListController OrderListController = loader.getController();
            if (OrderListController != null) {
                OrderListController.setStaffPageController(this);
            }
        }


    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error loading FXML: " + fxmlPath + " - " + e.getMessage());
    }
}
       @FXML
    void handleBookingList(ActionEvent event) {
                loadFXMLIntoDefaultAnchor("/view/BookingList.fxml");

         hideBlackMenu();
       
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
                loadFXMLIntoDefaultAnchor("/view/Check_out.fxml");
         hideBlackMenu();

    }

    @FXML
    void handleFoodOrder(ActionEvent event) {
        loadFXMLIntoDefaultAnchor("/view/FoodOrder.fxml");
         hideBlackMenu();

    }

   @FXML
    void handleHallBookingList(ActionEvent event) {
        hideBlackMenu();
        loadFXMLIntoDefaultAnchor("/view/HallBookingList.fxml");
    }

   @FXML
    void handleHallCheckOut(ActionEvent event) {
        hideBlackMenu();
        loadFXMLIntoDefaultAnchor("/view/HallCheck_out.fxml");
    }
    @FXML
    void handleHallBooking(ActionEvent event) {
        hideBlackMenu();
        loadFXMLIntoDefaultAnchor("/view/HallBooking.fxml");

    }


    @FXML
    void handleRoomBooking(ActionEvent event) throws IOException {
              loadFXMLIntoDefaultAnchor("/view/RoomBooking.fxml");
         hideBlackMenu();
         RoomBookingController.cartList.clear();
         


    }
     @FXML
    void handleLaundaryService(ActionEvent event) {
           loadFXMLIntoDefaultAnchor("/view/laundary_service.fxml");
         hideBlackMenu();

    }
       @FXML
    void handleImageAction(MouseEvent event) throws IOException {
           System.out.println("clicked");
               int currentStaffId = utils.UserSession.getEmployeeId();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ViewEmpitself.fxml")); // Adjust path if needed
            Parent root = loader.load();
            
            ViewEmpitselfController viewEmpitselfController = loader.getController();
        viewEmpitselfController.setEmployeeId(currentStaffId);

            Stage viewStage = new Stage();
            viewStage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal window
            viewStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets the owner window
            viewStage.setTitle("Profile");
            viewStage.setScene(new Scene(root));
           // viewStage.initStyle(StageStyle.UNDECORATED); // Optional: remove window decorations
            viewStage.showAndWait(); // Show the window and wait for it to be closed
               loader = new FXMLLoader(getClass().getResource("/view/Update_Password.fxml")); // Adjust path if different
                         root = loader.load();

                        // Get the BillController instance
                        Update_PasswordController update_PasswordController = loader.getController();
                        update_PasswordController.setEmployeeId(currentStaffId);
                        // Create a new stage for the bill pane
                        Stage updStage = new Stage();
                        updStage.initModality(Modality.APPLICATION_MODAL); // Makes it block other windows until closed
                      //  billStage.initStyle(StageStyle.UNDECORATED); // Optional: removes window decorations
                        updStage.setScene(new Scene(root));
                        updStage.setTitle("Update Password"); // Set a title for the window
                        updStage.showAndWait();// Display the bill pane

    }

}
