/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import static hotel_management.Hotel_Management.stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.RoomType;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AdminRoomTypeEditAndAddFormController implements Initializable {

    @FXML
    private Label lblRoomTypeId;
    @FXML
    private TextField txtRoomTypeName;
    @FXML
    private TextField txtRoomTypePrice;
    @FXML
    private TextField txtCapacity;
    @FXML
    private ImageView room_imgView;
    @FXML
    private Button btnUploadImage;
    @FXML
    private Button btnChangeImage;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Label lblRoomTypeAlert;
    @FXML
    private Label lblPriceAlert;
    @FXML
    private Label lblCapacityAlert;
    @FXML
    private Label lblImageAlert;
    
    boolean isEdit = false;
    private RoomType roomType;
    
    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    
    String savedImageFileName;
    File selectedFileForUpload;
    private final String IMAGE_STORAGE_DIR = "C:\\Users\\Dell\\OneDrive\\Desktop\\Khant Zaw Win Hotel\\Myat Thu Hotel\\src\\image\\";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db = new Database();
            con = db.getConnection();
            check();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminRoomTypeEditAndAddFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AdminRoomTypeEditAndAddFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleUploadImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFileForUpload = fileChooser.showOpenDialog(stage); // Use your main stage reference

        if (selectedFileForUpload != null) {
            try {
                // Get just the filename (e.g., "myimage.png")
                savedImageFileName = selectedFileForUpload.getName();

                // Display image preview immediately after selection
                Image img = new Image(selectedFileForUpload.toURI().toString());
                room_imgView.setImage(img);
                room_imgView.setPreserveRatio(true); // Maintain aspect ratio
                System.out.println("Image selected for upload: " + selectedFileForUpload.getAbsolutePath());

                // The actual file copy will happen when 'Add' or 'Update' is clicked.
            } catch (Exception e) {
                
                savedImageFileName = null; // Clear filename if loading fails
                selectedFileForUpload = null; // Clear the selected file
            }
        } else {
            System.out.println("No image file selected.");
            // If user cancels, we don't change savedImageFileName or cloth_imgView unless explicitly cleared.
        }
    }

    @FXML
    private void handleChangeImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFileForUpload = fileChooser.showOpenDialog(stage); // Use your main stage reference

        if (selectedFileForUpload != null) {
            try {
                // Get just the filename (e.g., "myimage.png")
                savedImageFileName = selectedFileForUpload.getName();

                // Display image preview immediately after selection
                Image img = new Image(selectedFileForUpload.toURI().toString());
                room_imgView.setImage(img);
                room_imgView.setPreserveRatio(true); // Maintain aspect ratio
                System.out.println("Image selected for upload: " + selectedFileForUpload.getAbsolutePath());

                // The actual file copy will happen when 'Add' or 'Update' is clicked.
            } catch (Exception e) {
                
                savedImageFileName = null; // Clear filename if loading fails
                selectedFileForUpload = null; // Clear the selected file
            }
        } else {
            System.out.println("No image file selected.");
            // If user cancels, we don't change savedImageFileName or cloth_imgView unless explicitly cleared.
        }
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
         Stage modalStage = (Stage)btnCancel.getScene().getWindow();
         modalStage.close();
    }

    @FXML
    private void handleAddAction(ActionEvent event) throws SQLException, IOException {
        lblRoomTypeAlert.setVisible(false);
        lblPriceAlert.setVisible(false);
        lblCapacityAlert.setVisible(false);
        lblImageAlert.setVisible(false);
        boolean complete = true;
        
        String pattern="[0-9]{1,}";
        //exit or not exit text
        if(txtRoomTypeName.getText().isEmpty()){
            lblRoomTypeAlert.setText("enter room type's name");
            lblRoomTypeAlert.setVisible(true);
            complete= false;
        }
        //exist or not exist and is number or not
        if(txtRoomTypePrice.getText().isEmpty()){
            lblPriceAlert.setText("enter room price");
            lblPriceAlert.setVisible(true);
            complete = false;
        }else if(!txtRoomTypePrice.getText().matches(pattern)){
            lblPriceAlert.setText("Room price can only be number");
            lblPriceAlert.setVisible(true);
            complete=false;
        }else if(Integer.parseInt(txtRoomTypePrice.getText())==0){
            lblPriceAlert.setText("room price can not be zero");
            lblPriceAlert.setVisible(true);
            complete=false;
        }
        //exist or not exist and is number or not
        if(txtCapacity.getText().isEmpty()){
            lblCapacityAlert.setText("enter room capacity");
            lblCapacityAlert.setVisible(true);
            complete = false;
        }else if(!txtCapacity.getText().matches(pattern)){
            lblCapacityAlert.setText("room capacity can only be number");
            lblCapacityAlert.setVisible(true);
            complete = false;
        }else if(Integer.parseInt(txtCapacity.getText())==0){
            lblCapacityAlert.setText("room capacity can not be zero");
            lblCapacityAlert.setVisible(true);
            complete = false;
        }
        
        if(savedImageFileName==null){
            lblImageAlert.setText("Choose room image");
            lblImageAlert.setVisible(true);
            complete = false;
        }
        
        
        if(complete){
            JOptionPane.showMessageDialog(null, "all right");
            // Copy the selected file to the image directory
            if (selectedFileForUpload != null && savedImageFileName != null) {
                Path destDir = Paths.get(IMAGE_STORAGE_DIR);
                if (!Files.exists(destDir)) {
                    Files.createDirectories(destDir); // Create the directory if it doesn't exist
                }
                Path destFile = destDir.resolve(savedImageFileName);
                Files.copy(selectedFileForUpload.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image copied to: " + destFile.toAbsolutePath());
            } else {
                 return;
            }
            String sql = "INSERT INTO room_type VALUES (?,?,?,?,?)";
            pst =con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(lblRoomTypeId.getText()));
            pst.setString(2, txtRoomTypeName.getText());
            pst.setInt(3, Integer.parseInt(txtCapacity.getText()));
            pst.setString(4, savedImageFileName);
            pst.setInt(5,Integer.parseInt(txtRoomTypePrice.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Add New RoomType sucessfully");
            Stage modalStage = (Stage)btnAdd.getScene().getWindow();
            modalStage.close();
            
        }
        
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) throws IOException, SQLException {
        lblPriceAlert.setVisible(false);
        boolean complete =true;
        String pattern="[0-9]{1,}";
        //exist or not exist and is number or not
        if(txtRoomTypePrice.getText().isEmpty()){
            lblPriceAlert.setText("enter room price");
            lblPriceAlert.setVisible(true);
            complete = false;
        }else if(!txtRoomTypePrice.getText().matches(pattern)){
            lblPriceAlert.setText("Room price can only be number");
            lblPriceAlert.setVisible(true);
            complete=false;
        }else if(Integer.parseInt(txtRoomTypePrice.getText())==0){
            lblPriceAlert.setText("room price can not be zero");
            lblPriceAlert.setVisible(true);
            complete=false;
        }
        if(complete){
            JOptionPane.showMessageDialog(null, "all right");
            if (selectedFileForUpload != null && savedImageFileName != null) {
                Path destDir = Paths.get(IMAGE_STORAGE_DIR);
                if (!Files.exists(destDir)) {
                    Files.createDirectories(destDir); // Create the directory if it doesn't exist
                }
                Path destFile = destDir.resolve(savedImageFileName);
                Files.copy(selectedFileForUpload.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image copied to: " + destFile.toAbsolutePath());
                String sql= "update room_type set image_path=?,price=? where room_type_id=?";
                pst =con.prepareStatement(sql);
                pst.setString(1, savedImageFileName);
                pst.setInt(2, Integer.parseInt(txtRoomTypePrice.getText()));
                pst.setInt(3, Integer.parseInt(lblRoomTypeId.getText()));
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Price and image is updated.");
                Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
                modalStage.close();
            } else {
                 String sql = "update room_type set price=? where room_type_id=?";
                 pst =con.prepareStatement(sql);
                 pst.setInt(1, Integer.parseInt(txtRoomTypePrice.getText()));
                 pst.setInt(2, Integer.parseInt(lblRoomTypeId.getText()));
                 pst.executeUpdate();
                 JOptionPane.showMessageDialog(null, "Price only update and image is original");
                 Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
                 modalStage.close();
                 
            }
        }
        
        
    }
    
    public void check() throws SQLException{
        if(isEdit){
                btnAdd.setVisible(false);
                btnUploadImage.setVisible(false);
                txtRoomTypeName.setEditable(false);
                txtCapacity.setEditable(false);
                btnChangeImage.setVisible(true);
                btnUpdate.setVisible(true);
                
            }else{
                btnUpdate.setVisible(false);
                btnChangeImage.setVisible(false);
                initRoomTypeId();
                
            }
    }
    
    public void setData(RoomType type) throws SQLException{
        roomType = type;
        isEdit = true;
        lblRoomTypeId.setText(String.valueOf(roomType.getRoom_type_id()));
        txtRoomTypeName.setText(roomType.getRoom_type());
        txtRoomTypePrice.setText(String.valueOf(roomType.getPrice()));
        txtCapacity.setText(String.valueOf(roomType.getCapacity()));
        // Load image from file path
        File file = new File(IMAGE_STORAGE_DIR + roomType.getImage_path());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            room_imgView.setImage(image);
        }

    
        room_imgView.setPreserveRatio(true);
        check();
    }
    public void initRoomTypeId() throws SQLException{
        String sql = "select count(*) as total from room_type";
        st= con.createStatement();
        rs = st.executeQuery(sql);
        if(rs.next()){
            lblRoomTypeId.setText(String.valueOf(rs.getInt("total")+1));
        }
        
    }
    
}
