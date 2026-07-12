/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.Laundary_serviceController;
import java.io.File;
import java.net.URL;
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
import model.Service;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class Cloth_CardController implements Initializable {

    @FXML
    private ImageView cloth_img;
    @FXML
    private Label service_price;
    
    private Service service;
    @FXML
    private Label cloth_name;
    @FXML
    private Button btnAdd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleAddAction(ActionEvent event) {
        String pattern="[0-9]{1,}";
        String qtyString = JOptionPane.showInputDialog(
            null,
            "Enter your quantity:",
            "Quantity Input", // Custom title
            JOptionPane.QUESTION_MESSAGE // Icon for a question
        );
        if(qtyString.isEmpty()){
            
        }else if(qtyString.matches(pattern)&& Integer.parseInt(qtyString)!=0){
            JOptionPane.showMessageDialog(null, "Valid");
            int service_id= service.getService_id();
            int qty = Integer.parseInt(qtyString);
            int total = qty * service.getService_price();
            if(Laundary_serviceController.cartList.isEmpty()){
                Laundary_serviceController.cartList.add(new Service(service_id, service.getService_name(), service.getCloth_id(), service.getCloth_name(), service.getService_price(),qty , total));
            }else{
                boolean found= false;
                for(Service ser:Laundary_serviceController.cartList){
                    if(service_id==ser.getService_id()){
                        int finalQty = qty+ser.getQty();
                        int finalTotal = total+ser.getTotal();
                        int index = Laundary_serviceController.cartList.indexOf(ser);
                        Laundary_serviceController.cartList.set(index, new Service(service_id, service.getService_name(), service.getCloth_id(), service.getCloth_name(), service.getService_price(),finalQty , finalTotal));
                        
                        found=true;
                    }

                    
                }
                if(!found)
                Laundary_serviceController.cartList.add(new Service(service_id, service.getService_name(), service.getCloth_id(), service.getCloth_name(), service.getService_price(),qty , total));

            }
        }else{
            JOptionPane.showMessageDialog(null, "InValid Input");
            
        }
        
    }
    
    public void setData(Service service){
        this.service=service;
        cloth_name.setText(service.getCloth_name());
        service_price.setText(String.valueOf(service.getService_price()));
        File file = new File("C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\" + service.getCloth_img());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            cloth_img.setImage(image);
        }
        
    }

    
}
