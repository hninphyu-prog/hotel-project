/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Food;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class FoodCardController implements Initializable {
 @FXML
    private ImageView foodImage;
    @FXML
    private Label lblFoodName;
    @FXML
    private Label lblFoodPrice;
  @FXML
    private Button btnOrder;
   private Food food;
       private FoodOrderController foodOrderController;
   @FXML
    private Button btnEdit;

   // NEW: Reference to AdminFoodController
    private AdminFoodController adminFoodController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
           btnEdit.setVisible(false);
        btnOrder.setVisible(false);
        
    }   
     public void setFoodOrderController(FoodOrderController controller) {
        this.foodOrderController = controller;
    }
   public void setAdminFoodController(AdminFoodController controller) {
        this.adminFoodController = controller;
    }
     
   public void setData(Food food){
    this.food = food;
    lblFoodName.setText(food.getFoodName());
    lblFoodPrice.setText(food.getFoodPrice() + "ks");
    
    System.out.println("setData method called for Food: " + food.getFoodName()); // New Line

    File file = new File("C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\" + food.getFoodImage());
    if (file.exists()) {
        Image image = new Image(file.toURI().toString());
        foodImage.setImage(image);
    } else {
        System.err.println("Image not found at path: " + file.getAbsolutePath() + " for food: " + food.getFoodName());
    }
}

@FXML
void handleBtnOrderAction(ActionEvent event) {
    try { 
        
        System.out.println("handleBtnOrderAction called."); 
        if (foodOrderController != null && food != null) {
            foodOrderController.addFoodToCart(this.food);
            System.out.println("Ordered: " + food.getFoodName());
        } else {
            System.err.println("Error: foodOrderController or food object is null in FoodCardController.");
            if (foodOrderController == null) System.err.println("  - foodOrderController is null.");
            if (food == null) System.err.println("  - food is null.");
        }
    } catch (Exception e) {
        System.err.println("Exception occurred in handleBtnOrderAction: " + e.getMessage());
        e.printStackTrace();
    }
}
 @FXML
    void handleBtnEditAction(ActionEvent event) {
        
            if (food != null && adminFoodController != null) {
            adminFoodController.editFood(this.food);
       
        } else {
            System.err.println("Error: Food object or AdminFoodController reference is null for edit action.");
        }
    }
     public void setRole(String role) {
        if ("Admin".equalsIgnoreCase(role)) {
            btnEdit.setVisible(true);
            btnOrder.setVisible(false);
        } else if ("Staff".equalsIgnoreCase(role)) {
            btnEdit.setVisible(false);
            btnOrder.setVisible(true);
        } else {
            // Default: Hide both or handle other roles
            btnEdit.setVisible(false);
            btnOrder.setVisible(false);
        }
    }
   
}