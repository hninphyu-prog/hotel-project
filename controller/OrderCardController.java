/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.OrderListController;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import database.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.OrderData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class OrderCardController implements Initializable {

    @FXML
    private Label lblOrderNo;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField txtCurrentStatus;
    @FXML
    private TextField txtNewStatus;
    @FXML
    private Button btnCancel;
    private OrderData data;
    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    
    int new_id;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db = new Database();
            con = db.getConnection();
            txtCurrentStatus.setEditable(false);
            txtNewStatus.setEditable(false);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    

    @FXML
    private void handleUpdateAction(ActionEvent event) throws SQLException {
        int index = OrderListController.order.indexOf(data);
        data.setStatus(txtNewStatus.getText());
        OrderListController.order.set(index, data);
        String sql = "select Order_Status_id from Order_Status where Order_Status=?";
        pst = con.prepareStatement(sql);
        pst.setString(1, txtNewStatus.getText());
        rs = pst.executeQuery();
        if(rs.next()){
            new_id=rs.getInt("Order_Status_id");
        }
        String sql1 = "UPDATE laundary_service_order SET Order_Status_id= ? WHERE order_id=?";
        pst =con.prepareStatement(sql1);
        pst.setInt(1, new_id);
        pst.setInt(2, data.getOrder_id());
        pst.executeUpdate();
        Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
        modalStage.close();
           
        
        
        
    }


    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
        modalStage.close();
        
    }
    
    public void setData(OrderData order){
        this.data=order;
        lblOrderNo.setText(String.valueOf(data.getOrder_id()));
        txtCurrentStatus.setText(data.getStatus());
        String current = txtCurrentStatus.getText();
        if(current.equals(OrderListController.order_statusList.get(0))){
            txtNewStatus.setText(OrderListController.order_statusList.get(1));
        }else if (current.equals(OrderListController.order_statusList.get(1))){
            txtNewStatus.setText(OrderListController.order_statusList.get(2));
        }else{
            txtNewStatus.setText(OrderListController.order_statusList.get(2));
            btnUpdate.setDisable(true);
        }
    }
    
}
