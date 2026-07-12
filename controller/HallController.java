/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import model.Hall;
import java.sql.ResultSet;

/**
 * FXML Controller class
 *
 * @author May Na Dar
 */
public class HallController implements Initializable {

   @FXML
    private ComboBox<String> comboSearch;
    @FXML
    private ComboBox<String> comboSearchData;
    @FXML
    private Button btnRefresh;
    @FXML
    private Pagination pagination;
    @FXML
    private AnchorPane halleditpane;
    @FXML
    private Button btnadd;
    @FXML
    private ComboBox<String> com_hall_edit;
   
    @FXML
    private TextField txt_price;
     @FXML
    private ImageView imghall;
     @FXML
    private Button btnImageChoose;
    @FXML
    private Label price_field;
    @FXML
    private FontAwesomeIcon f_close;
    @FXML
    private Label capacity_field;  
    
     @FXML
    private Label lblHallNo; 
    
    @FXML
    private AnchorPane hallviewpane;


    @FXML
    private Label lblHallType111;

    @FXML
    private Label lblHallNo1;

    @FXML
    private ComboBox<String> com_hall_edit1;


    @FXML
    private TextField txt_price1;

    @FXML
    private Button btn_save1;

    @FXML
    private Label capacity_field1;

    @FXML
    private Label price_field1;
    
      @FXML
    private Label halltype_field;
    @FXML
    private ImageView imghall1;
    @FXML
    private ComboBox<String> com_capacity1;
    @FXML
    private ComboBox<String> com_capacity;
     @FXML
    private Button btn_clear;
      @FXML
    private Button btn_clear2;
      @FXML
      private Label halltype_field1;
     

    private final int card_per_page = 2;
    private int total_hall;
    private int pageCount;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
    private final ObservableList<Hall> HallList = FXCollections.observableArrayList();
    private final ObservableList<String> HallTypeList = FXCollections.observableArrayList();
    private final ObservableList<String> CapacityList = FXCollections.observableArrayList();

    private Hall selectedHall;

     private File selectedImageFile;
    private String selectedImageName;
    
    /**
     * Initializes the controller class.
     */
    @Override
   
public void initialize(URL url, ResourceBundle rb) {

    Database db = new Database();
    try {
        con = db.getConnection();
        comboSearch.getItems().addAll("Hall No", "Hall Type");
        comboSearch.setValue("Hall No");
        handleComboSelection(); // <-- Important to populate Hall IDs

        comboSearch.setOnAction(e -> handleComboSelection());
        //edit 
       clear();

        //com_hall_edit.getItems().addAll("Wedding Hall", "Conference Hall", "Meeting Hall");
        //com_hall_edit1.getItems().addAll("Wedding Hall", "Conference Hall", "Meeting Hall");

        
       /* txt_capacity.textProperty().addListener((obs, oldText, newText) -> {
        txt_capacity.setStyle(null);
        capacity_field.setVisible(false);
    });*/

    txt_price.textProperty().addListener((obs, oldText, newText) -> {
        txt_price.setStyle(null);
        price_field.setVisible(false);
    });
        

        initallHall();

        // Setup listener for comboSearchData to filter halls
        comboSearchData.setOnAction(e -> {
            String searchCategory = comboSearch.getValue();
            String searchValue = comboSearchData.getValue();

            if (searchValue == null || searchValue.equals("All")) {
                try {
                    initallHall();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    filterHall(searchCategory, searchValue);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
         initHallType();
            com_hall_edit.setItems(HallTypeList);
            com_hall_edit1.setItems(HallTypeList);
            //Capacity Combo box
        initCapacityCombo();
            com_capacity.setItems(CapacityList);
            com_capacity1.setItems(CapacityList);

    } catch (ClassNotFoundException | SQLException ex) {
        Logger.getLogger(HallController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void setupPagination() {
        pageCount = (int) Math.ceil((double) total_hall / card_per_page);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(pageIndex -> {
            try {
                return createPage(pageIndex);
            } catch (IOException ex) {
                return new GridPane();
            }
        });
    }  
  public Node createPage(int pageIndex) throws IOException {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        int startIndex = pageIndex * card_per_page;
        int endIndex = Math.min(startIndex + card_per_page, HallList.size());

        int col = 0, row = 0;
        for (int i = startIndex; i < endIndex; i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/HallCard.fxml")); // Use full path if necessary
                AnchorPane pane = loader.load();
                controller.HallCardController paneController=loader.getController();
                paneController.setData(HallList.get(i), this); 
           


            grid.add(pane, col++, row);
            if (col == 2) {
                col = 0;
                row++;
            }
        }
        return grid;
    }
    public void refreshPageCount(int total) {
        total_hall = total;
        pageCount = (int) Math.ceil((double) total_hall / card_per_page);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setPageFactory(pageIndex -> {
            try {
                return createPage(pageIndex);
            } catch (IOException ex) {
                return new GridPane();
            }
        });
    }
       public void initallHall() throws SQLException {
        HallList.clear();
        String sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity FROM hall h "
                   + "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id "
                   + "JOIN capacity c ON h.CapacityID= c.CapacityID;";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
            HallList.add(new Hall(
                    rs.getString("hall_type"),
                    rs.getString("hall_id"),
                    rs.getString("Capacity"),
                    rs.getInt("price"),
                    rs.getString("image")
            ));
        }
        total_hall = HallList.size();
        refreshPageCount(total_hall);
    }
       public void initHallType() throws SQLException {
        HallTypeList.clear();
        String sql = "SELECT hall_type FROM hall_type";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
            HallTypeList.add(rs.getString("hall_type"));
        }
    }
       private void handleComboSelection() {
        comboSearchData.getItems().clear();
        comboSearchData.setValue(null);

        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.add("All");

            if ("Hall No".equals(comboSearch.getValue())) {
                String sql = "SELECT DISTINCT hall_id FROM hall";
                try (PreparedStatement pst = con.prepareStatement(sql);
                     ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        items.add(rs.getString("hall_id"));
                    }
                }
            } 
            if ("Hall Type".equals(comboSearch.getValue())) {
                String sql = "SELECT hall_type from hall_type;";
                try (PreparedStatement pst = con.prepareStatement(sql);
                     ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        items.add(rs.getString("hall_type"));
                    }
                }
            }

            comboSearchData.setItems(items);
            comboSearchData.setValue("All");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading combo data: " + ex.getMessage());
        }
    }

    @FXML
    private void handlerRefreshAction(ActionEvent event) {
        
        try {
            clear();
            initallHall();
            comboSearch.setValue("Hall No");
            comboSearchData.setValue("All");
        } catch (SQLException ex) {
            Logger.getLogger(HallController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        @FXML
void handleAddAction(ActionEvent event) throws SQLException {//Add button save
    boolean success = HallSaver.ValidateAndSave(
    com_hall_edit1, com_capacity1, txt_price1,
    halltype_field1, capacity_field1, price_field1, 
    false,selectedImageName,null
);

    if (success) {
        System.out.println("New hall added!");
        initallHall();
        clear();
    }
}
     private void clear(){
         halleditpane.setVisible(false);
        halleditpane.setDisable(true);
        hallviewpane.setVisible(false);
        hallviewpane.setDisable(true);
     }

    
    @FXML
private void handlerAddAction(ActionEvent event) throws SQLException {
    
   /* Scene scene = ((Node)event.getSource()).getScene();
System.out.println("Root: " + scene.getRoot());
for (Node node : ((Pane)scene.getRoot()).getChildrenUnmodifiable()) {
    System.out.println("Child: " + node);
}*/
    roomno();
    
    // Show the add pane
    hallviewpane.toFront();
    hallviewpane.setVisible(true);
    hallviewpane.setManaged(true);
    hallviewpane.setDisable(false);

// Hide the edit pane
    halleditpane.setVisible(false);
    halleditpane.setManaged(false);
    halleditpane.setDisable(true);
    halleditpane.toBack();
    // Reset fields
        save();
}

    
    @FXML
void handleSaveAction(ActionEvent event) throws SQLException {
    if (selectedHall == null) return;

    // Optional image validation for edit:
    if ((selectedImageFile == null) && (selectedImageName == null || selectedImageName.isEmpty())) {
        halltype_field.setText("Image is required.");
        halltype_field.setVisible(true);
        return;
    }
     Hall selected = this.selectedHall;
     String hallNo = selected != null ? selected.getHallNo() : null;
    boolean success = HallSaver.ValidateAndSave(
        com_hall_edit, com_capacity, txt_price,
        halltype_field, capacity_field, price_field,
        true, selectedImageName,hallNo
    );

    if (success) {
        JOptionPane.showMessageDialog(null, "Update Successfully");
       initallHall();
        clear();
    }
}
    

   @FXML
void handleImage(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Hall Image");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
    );

    File file = fileChooser.showOpenDialog(null);
    if (file != null) {
        try {
            // Use original file name
            selectedImageName = file.getName();

            // 1. Save to runtime folder (e.g., "images/")
            File destFolder = new File("images/");
            if (!destFolder.exists()) destFolder.mkdirs();
            File destFile = new File(destFolder, selectedImageName);
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 2. Also copy to src/image/ folder for design-time access
            File srcFolder = new File("src/image/");
            if (!srcFolder.exists()) srcFolder.mkdirs();
            File srcFile = new File(srcFolder, selectedImageName);
            Files.copy(file.toPath(), srcFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Show image in ImageView
            selectedImageFile = destFile;
            imghall1.setImage(new Image(destFile.toURI().toString()));

            System.out.println("Image saved to: " + destFile.getAbsolutePath());
            System.out.println("Image also copied to: " + srcFile.getAbsolutePath());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


   @FXML
void handleImageChoose(ActionEvent event) { // Hall Edit Image Action
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Image");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
    );

    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
        try {
            // Destination 1: Runtime image folder (outside src)
            File runtimeFolder = new File("images/");
            if (!runtimeFolder.exists()) runtimeFolder.mkdirs();
            File runtimeDest = new File(runtimeFolder, selectedFile.getName());
            Files.copy(selectedFile.toPath(), runtimeDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Destination 2: Project source folder (src/image/)
            File projectFolder = new File("src/image/");
            if (!projectFolder.exists()) projectFolder.mkdirs();
            File projectDest = new File(projectFolder, selectedFile.getName());
            Files.copy(selectedFile.toPath(), projectDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Show image in ImageView
            selectedImageFile = runtimeDest; // You can choose which to keep reference to
            imghall.setImage(new Image(runtimeDest.toURI().toString()));

            // Save name for database
            selectedImageName = selectedFile.getName();

           // System.out.println("Image saved to:");
           // System.out.println(" - " + runtimeDest.getAbsolutePath());
           // System.out.println(" - " + projectDest.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    private void save(){
    txt_price1.clear();
    com_hall_edit1.setValue(null);
    com_hall_edit1.setStyle(null);
    com_capacity1.setValue(null);
    com_capacity1.setStyle(null);
    capacity_field1.setVisible(false);
    price_field1.setVisible(false);
    txt_price1.setStyle(null);
    halltype_field1.setText("");
    imghall1.setImage(new Image("C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\imageupade.png"));
    selectedImageName = null;
    selectedImageFile = null;
    }
    

    private void filterHall(String category, String value) throws SQLException {
    HallList.clear();
    String sql;

    if ("Hall No".equals(category)) {
        sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity FROM hall h " +
              "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id " +
              "JOIN capacity c ON h.CapacityID = c.CapacityID " +
              "WHERE h.hall_id = ?";
    } else {
        sql = "SELECT ht.hall_type, h.hall_id, h.price, h.image, c.Capacity FROM hall h " +
              "JOIN hall_type ht ON h.hall_type_id = ht.hall_type_id " +
              "JOIN capacity c ON h.CapacityID = c.CapacityID " +
              "WHERE ht.hall_type = ?";
    }

    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, value);
        rs = pst.executeQuery();
        while (rs.next()) {
            HallList.add(new Hall(
                rs.getString("hall_type"),
                rs.getString("hall_id"),
                rs.getString("Capacity"),
                rs.getInt("price"),
                rs.getString("image")
            ));
        }
        total_hall = HallList.size();
        refreshPageCount(total_hall);
    }
}
    public void handleEdit(Hall hall) {
    selectedHall = hall;
    selectedImageName = hall.getImagename(); 
    capacity_field.setText("");
    halltype_field.setText("");
    price_field.setText("");
    com_hall_edit.setStyle("-fx-border-color: grey;");
    com_capacity.setStyle("-fx-border-color: grey;");
    txt_price.setStyle("-fx-border-color: grey;");
    // Fill data
    lblHallNo.setText(String.valueOf(hall.getHallNo()));
    com_hall_edit.setValue(hall.getHalltype());
    com_capacity.setValue(hall.getCapacity());
    txt_price.setText(String.valueOf(hall.getPrice()));
    if (selectedImageName != null) {
        File imageFile = new File("src/image/" + selectedImageName);
        if (imageFile.exists()) {
            imghall.setImage(new Image(imageFile.toURI().toString()));

        }
    

}

    boolean isEdit = true; 

    // Hide the add pane
    hallviewpane.setVisible(false);
    hallviewpane.setDisable(true);

    // Show the edit pane only
    halleditpane.setVisible(true);
    halleditpane.setDisable(false);
}
    @FXML
    void clearAction(ActionEvent event) {
    com_hall_edit1.getSelectionModel().clearSelection();
    com_capacity1.getSelectionModel().clearSelection();

    // Clear text field
    txt_price1.clear();
    imghall1.setImage(new Image("C:\\Users\\Dell\\Documents\\NetBeansProjects\\Grand Vista Hotel(final)\\Myat Thu Hotel\\src\\image\\imageupade.png"));
    }
    @FXML
    void clearEditAction(ActionEvent event){
    com_hall_edit.getSelectionModel().clearSelection();
    com_capacity.getSelectionModel().clearSelection();

    // Clear text field
    txt_price.clear();
}

  

public class HallSaver {

    public static boolean ValidateAndSave(
        ComboBox<String> comboBoxhall,ComboBox<String> comboCapacity, TextField txtPrice,
        Label halltypeField, Label capacityField, Label priceField,
        boolean isEdit,
        String imageName,
        String hallNo
    ) {
        boolean hasError = false;
        Hall hall = new Hall();

        // Validate hall type
        if (comboBoxhall.getValue() == null || comboBoxhall.getValue().isEmpty()) {
            halltypeField.setText("Hall type is required.");
            halltypeField.setVisible(true);
            comboBoxhall.setStyle("-fx-border-color: red;");
            hasError = true;
        } else {
            hall.setHalltype(comboBoxhall.getValue());
            halltypeField.setVisible(false);
            comboBoxhall.setStyle(null);
        }
       
        // Validate capacity
        if (comboCapacity.getValue() == null || comboCapacity.getValue().isEmpty()) {
            capacityField.setText("Capacity is required.");
           // capacityField.setStyle("-fx-text-fill: red;");
            capacityField.setVisible(true);
            comboCapacity.setStyle("-fx-border-color: red;");
            hasError = true;
        } else {
            hall.setCapacity(comboCapacity.getValue());
            capacityField.setVisible(false);
            comboCapacity.setStyle(null);
        }

       // Validate price
    String priceText = txtPrice.getText().trim();
           if (priceText.isEmpty()) {
        priceField.setText("Price is required.");
      //  priceField.setStyle("-fx-text-fill: red; ");
        priceField.setVisible(true);
        txtPrice.setStyle("-fx-border-color: red;");
        hasError = true;
    } else if (!priceText.matches("\\d+")) {  // only digits allowed (no +, -, letters, decimals)
        priceField.setText("Price must be a positive number.");
       // priceField.setStyle("-fx-text-fill: red; ");
        priceField.setVisible(true);
        txtPrice.setStyle("-fx-border-color: red;");
        hasError = true;
    } else {
    try {
        int price = Integer.parseInt(priceText);
        if (price <= 0) {
            priceField.setText("Price must be greater than zero.");
            //priceField.setStyle("-fx-text-fill: red; ");
            priceField.setVisible(true);
            txtPrice.setStyle("-fx-border-color: red;");
            hasError = true;
        } else {
            hall.setPrice(price);
            priceField.setVisible(false);
            txtPrice.setStyle(null);
        }
    } catch (NumberFormatException e) {
        priceField.setText("Price must be a valid number.");
       // priceField.setStyle("-fx-text-fill: pink; ");
        priceField.setVisible(true);
        txtPrice.setStyle("-fx-border-color: red;");
        hasError = true;
    }
}

       
        // Validate image
        if (imageName == null || imageName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Image Required!!Please Choose Image");
            return false;
        } else {
            hall.setImagename(imageName);
        }
        if (isEdit && hallNo != null) {
               hall.setHallNo(hallNo);  // 🔑 Set hallNo before updating
         }

        if (hasError) return false;

        // Save to DB
        try (Connection con = new Database().getConnection()) {
            if (isEdit) {
                updateHall(con, hall); // implement if needed
            } else
            
            {
                insertNewHall(con, hall);
                JOptionPane.showMessageDialog(null,"Save Successfully");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private static void insertNewHall(Connection con, Hall hall) throws SQLException {
        String insertSQL = """
            INSERT INTO hall(hall_id,hall_type_id,CapacityID,price,image)
            VALUES (?,
                (SELECT hall_type_id FROM hall_type WHERE hall_type = ?),
                (SELECT CapacityID FROM capacity WHERE Capacity = ?),
                ?,
                ?
            )
        """;

        try (PreparedStatement pst = con.prepareStatement(insertSQL)) {
            pst.setString(1, hall.getHallNo());
            pst.setString(2, hall.getHalltype());
            pst.setString(3, hall.getCapacity());
            pst.setInt(4, hall.getPrice());
            pst.setString(5, hall.getImagename());
            pst.executeUpdate();
        }
    }
    
private static void updateHall(Connection con, Hall hall) throws SQLException {
    String updateSQL = """
        UPDATE hall
        SET hall_type_id = (SELECT hall_type_id FROM hall_type WHERE hall_type = ?),
            CapacityID = (SELECT CapacityID FROM capacity WHERE Capacity = ?),
            price = ?,
            image = ?
        WHERE hall_id = ?
    """;

    try (PreparedStatement pst = con.prepareStatement(updateSQL)) {
            pst.setString(1, hall.getHalltype());
            pst.setString(2, hall.getCapacity());
            pst.setInt(3, hall.getPrice());
            pst.setString(4, hall.getImagename());
            pst.setString(5, hall.getHallNo()); // hall_id
            pst.executeUpdate();
    }
}

       
    }
public void roomno() throws SQLException {
    String sql = "SELECT MAX(hall_id) AS hall_no FROM hall";

    pst = con.prepareStatement(sql);
    rs = pst.executeQuery();

    int nextHallNo = 1; // default if no records exist

    if (rs.next()) {
        int lastId = rs.getInt("hall_no"); // get 12
        if (!rs.wasNull()) {
            nextHallNo = lastId + 1; // 12 + 1 = 13
        }
    }

    lblHallNo1.setText(String.valueOf(nextHallNo)); // sets 13
}
     public void initCapacityCombo() throws SQLException {
        CapacityList.clear();
        String sql = "SELECT Capacity FROM capacity";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                CapacityList.add(rs.getString("Capacity"));
            }
        }
    }
   



    }


