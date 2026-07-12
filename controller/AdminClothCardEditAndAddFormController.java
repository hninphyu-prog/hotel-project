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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Cloth;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AdminClothCardEditAndAddFormController implements Initializable {

    @FXML
    private Label lblClothId;
    @FXML
    private TextField txtClothName;
    @FXML
    private RadioButton rd_Men;
    @FXML
    private ToggleGroup gender;
    @FXML
    private RadioButton rd_Women;
    @FXML
    private ImageView cloth_imgView;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;
    
    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    String savedImageFileName;
    File selectedFileForUpload;
    private final String IMAGE_STORAGE_DIR = "C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\";
    @FXML
    private Button btnUploadImage;
    @FXML
    private Label lblclothAlert;
    @FXML
    private Label lblgenderAlert;
    @FXML
    private Label lblImageAlert;
    @FXML
    private Button btn_changeImage;
    private Cloth cloth;
    boolean isEdit = false;
    

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
            
            
            
            
            // 3. Add an action (click event handler)
        
                
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminClothCardEditAndAddFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AdminClothCardEditAndAddFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage modalStage = (Stage)btnCancel.getScene().getWindow();
        modalStage.close();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) throws IOException, SQLException {
        
        
        // Copy the selected file to the image directory
            if (selectedFileForUpload != null && savedImageFileName != null) {
                Path destDir = Paths.get(IMAGE_STORAGE_DIR);
                if (!Files.exists(destDir)) {
                    Files.createDirectories(destDir); // Create the directory if it doesn't exist
                }
                Path destFile = destDir.resolve(savedImageFileName);
                Files.copy(selectedFileForUpload.toPath(), destFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image copied to: " + destFile.toAbsolutePath());
                String sql = "update cloth set cloth_img = ? where cloth_id=?";
                pst =con.prepareStatement(sql);
                pst.setString(1, savedImageFileName);
                pst.setInt(2, cloth.getCloth_id());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Update successfully");
                Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
                modalStage.close();
            } else {
                 JOptionPane.showMessageDialog(null, "image is original and not saved");
                Stage modalStage = (Stage)btnUpdate.getScene().getWindow();
                modalStage.close();
            }
            
                    
            
            
        
    }

    @FXML
    private void handleAddAction(ActionEvent event) throws SQLException, IOException {
        lblclothAlert.setVisible(false);
        lblgenderAlert.setVisible(false);
        lblImageAlert.setVisible(false);
        if(txtClothName.getText().isEmpty()){
            lblclothAlert.setVisible(true);
        }
        if(!rd_Men.isSelected() && !rd_Women.isSelected()){
            lblgenderAlert.setVisible(true);
        }
        if(savedImageFileName==null){
            lblImageAlert.setVisible(true);
        }
        if(!txtClothName.getText().isEmpty() && (rd_Men.isSelected() || rd_Women.isSelected()) && savedImageFileName!=null){
            int clothId =Integer.parseInt(lblClothId.getText());
            String clothName = txtClothName.getText();
            String genderType = "";
            if (gender.getSelectedToggle() == rd_Men) {
                genderType = "Men";
            } else if (gender.getSelectedToggle() == rd_Women) {
                genderType = "Women";
            }
            int gender_type = 0;
            String sql = "SELECT clothing_type_id FROM clothing_type WHERE clothing_type=?";
            pst =con.prepareStatement(sql);
            pst.setString(1, genderType);
            rs = pst.executeQuery();
            if(rs.next()){
                gender_type=rs.getInt("clothing_type_id");
            }
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
            String sql1 = """
                         INSERT INTO cloth (cloth_id,clothing_type_id,cloth_name,cloth_img) VALUES (?,?,?,?)
                         """;
            pst = con.prepareStatement(sql1);
            pst.setInt(1, clothId);
            pst.setInt(2, gender_type);
            pst.setString(3, clothName);
            pst.setString(4, savedImageFileName);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cloth add successfully");
            Stage modalStage = (Stage)btnAdd.getScene().getWindow();
            modalStage.close();
        }
        
        
    }
    
    public void initClothId() throws SQLException{
        String sql = """
                     SELECT COUNT(cloth_id) AS total FROM cloth
                     """;
        st = con.createStatement();
        rs =st.executeQuery(sql);
        if(rs.next()){
            lblClothId.setText(String.valueOf(rs.getInt("total")+1));
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
                cloth_imgView.setImage(img);
                cloth_imgView.setPreserveRatio(true); // Maintain aspect ratio
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
                cloth_imgView.setImage(img);
                cloth_imgView.setPreserveRatio(true); // Maintain aspect ratio
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
    
    public void setData(Cloth c) throws SQLException{
        cloth = c;
        
        isEdit=true;
        lblClothId.setText(String.valueOf(cloth.getCloth_id()));
        txtClothName.setText(cloth.getCloth_name());
        if(cloth.getClothing_type().equals("Men")){
            rd_Men.setSelected(true);
            rd_Women.setDisable(true);
        }else{
            rd_Women.setSelected(true);
            rd_Men.setDisable(true);
        }
        // Load image from file path
        File file = new File(IMAGE_STORAGE_DIR + cloth.getCloth_img());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            cloth_imgView.setImage(image);
        }

    
        cloth_imgView.setPreserveRatio(true);
        check();
        
        
        
    }
    public void check() throws SQLException{
        if(isEdit){
                btnAdd.setVisible(false);
                btnUploadImage.setVisible(false);
                txtClothName.setEditable(false);
                btn_changeImage.setVisible(true);
                btnUpdate.setVisible(true);
                
            }else{
                btnUpdate.setVisible(false);
                btn_changeImage.setVisible(false);
                initClothId();
                rd_Men.setSelected(false);
                rd_Women.setSelected(false);
            }
    }
    public Cloth getData(){
        return cloth;
    }
    
}
