/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import static hotel_management.Hotel_Management.stage;
import java.io.IOException;
import java.net.URL;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Cloth;
import model.Clothing_Type;
import model.OrderData;
import model.Service;
import controller.Laundary_serviceController;
import static controller.Laundary_serviceController.cartList;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AdminLaundaryController implements Initializable {

    @FXML
    private Pagination pagination;
    @FXML
    private HBox genderHbox;

    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;
    
    //to use in clothing_bar;
    ObservableList<Clothing_Type> gender_type = FXCollections.observableArrayList();
    //to show cloth data
    public static ObservableList<Cloth> clothList = FXCollections.observableArrayList();
    
    ObservableList<String> service_status = FXCollections.observableArrayList();
    ObservableList<Service> servicelist = FXCollections.observableArrayList();
    ObservableList<String> servicetypelist = FXCollections.observableArrayList();
    
    //to used in pagination
    int card_per_page = 4;
    int total_room; // This will be updated dynamically
    int pageCount; // This will be updated updated dynamically
    
    // Keep track of the currently disabled button
    private Button currentlyClickedButton = null;
    @FXML
    private TextField txtsearchclothbar;
    @FXML
    private Button btnClothRefresh;
    @FXML
    private Button btnaddCloth;
    @FXML
    private TableView<Service> tbServiceView;
    @FXML
    private TableColumn<?, ?> colService_id;
    @FXML
    private TableColumn<?, ?> col_serviceType;
    @FXML
    private TableColumn<?, ?> col_clothName;
    @FXML
    private TableColumn<?, ?> colGender;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<Service, Void> colAction;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private Button btnServiceRefresh;
    @FXML
    private ComboBox<String> combo_serviceType;
    @FXML
    private ComboBox<String> combo_serviceStatus;
    @FXML
    private TextField txtSearchServiceBar;
    @FXML
    private Button btnAddNewServiceType;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db = new Database();
            con = db.getConnection();
            initClothingType();
            initAllCloth();
            setupPagination();
            initGenderBtn();
            initAllserviceType();
            
            service_status.add("All");
            service_status.add("Available");
            service_status.add("Not Available");
            combo_serviceStatus.setItems(service_status);
            combo_serviceStatus.setValue(service_status.get(0));
            initService();
            colService_id.setCellValueFactory(new PropertyValueFactory<>("service_id"));
            col_serviceType.setCellValueFactory(new PropertyValueFactory<>("service_name"));
            col_clothName.setCellValueFactory(new PropertyValueFactory<>("cloth_name"));
            colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("service_price"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colAction.setCellFactory(col->new TableCell<Service,Void>(){
                private final Button btnEdit = new Button("Edit");
                private final HBox box = new HBox(btnEdit);
                
                {   
                    box.setAlignment(Pos.CENTER);
                    btnEdit.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    
                    btnEdit.setOnAction(e->{
                        try {
                            Service ser =getTableView().getItems().get(getIndex());
                            int index = servicelist.indexOf(ser);
                            JOptionPane.showMessageDialog(null, "Change status");
                            Stage ownerStage = (Stage) btnEdit.getScene().getWindow();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminEditServiceForm.fxml"));
                            Parent root = loader.load();
                            AdminEditServiceFormController modalcontroller = loader.getController();
                            modalcontroller.setData(getTableView().getItems().get(getIndex()));
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Edit Service Detail Dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
                            Service service = modalcontroller.getData();
                            servicelist.set(index, service);
                            
                            
                            
                        } catch (IOException ex) {
                            Logger.getLogger(AdminLaundaryController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(box);
                                
                            }
                        }
            });
            tbServiceView.setItems(servicelist);
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminLaundaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AdminLaundaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public void initClothingType() throws SQLException {
        gender_type.clear(); 
        String sql = "SELECT clothing_type,icon_name FROM clothing_type";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        gender_type.add(new Clothing_Type("All", "GENDERLESS")); // Add "All" option
        while (rs.next()) {
            gender_type.add(new Clothing_Type(rs.getString("clothing_type"), rs.getString("icon_name")));
        }
    }
    
    // New method to encapsulate pagination setup/refresh logic
    private void setupPagination() {
        total_room = clothList.size(); // Ensure total_room is up-to-date
        pageCount = (int) Math.ceil((double) total_room / card_per_page);
        pagination.setPageCount(pageCount > 0 ? pageCount : 1);
        

        // Set the PageFactory - this tells Pagination how to create content for each page
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                try {
                    return createPage(pageIndex);
                } catch (IOException ex) {
                    Logger.getLogger(Laundary_serviceController.class.getName()).log(Level.SEVERE, "Error creating pagination page", ex);
                    return new Label("Error loading page content."); // Provide a user-friendly error
                }
            }
        });
    }
    public Node createPage(int pageIndex) throws IOException {
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        
        int startIndex = pageIndex * card_per_page;
        // Ensure endIndex does not exceed the actual size of serviceList
        int endIndex = Math.min(startIndex + card_per_page, clothList.size());

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/adminClothView.fxml"));
            AnchorPane pane = loader.load();
            AdminClothViewController paneController = loader.getController();
            paneController.setData(clothList.get(i)); // Pass the Service object to the card controller

            grid.add(pane, col, row);

            col++;
            if (col == 2) { // Assuming 3 columns per row
                col = 0;
                row++;
            }
        }
        return grid;
    }


    
    
    public void initAllCloth() throws SQLException{
        String sql = """
                     SELECT cloth_id,clothing_type.clothing_type_id,clothing_type,cloth_name,cloth_img FROM cloth,clothing_type 
                     WHERE cloth.clothing_type_id=clothing_type.clothing_type_id 
                     """;
        st =con.createStatement();
        rs= st.executeQuery(sql);
        clothList.clear();
        while(rs.next()){
            clothList.add(new Cloth(rs.getInt("cloth_id"), rs.getInt("clothing_type_id"), rs.getString("clothing_type"), rs.getString("cloth_name"), rs.getString("cloth_img")));
        }
        
    }
    
    public void handleFilterAction(String gender) throws SQLException{
        String sql = """
                     SELECT cloth_id,clothing_type.clothing_type_id,clothing_type,cloth_name,cloth_img FROM cloth,clothing_type 
                     WHERE cloth.clothing_type_id=clothing_type.clothing_type_id AND clothing_type=?
                     """;
        pst =con.prepareStatement(sql);
        pst.setString(1, gender);
        rs=pst.executeQuery();
        clothList.clear();
        while(rs.next()){
            clothList.add(new Cloth(rs.getInt("cloth_id"), rs.getInt("clothing_type_id"), rs.getString("clothing_type"), rs.getString("cloth_name"), rs.getString("cloth_img")));
        }
    }

    @FXML
    private void handleSearchClothBarAction(ActionEvent event) throws SQLException {
        
        if(txtsearchclothbar.getText().isEmpty()){
            
        }else{
            String clothName = "%"+txtsearchclothbar.getText()+"%";
            String gender = currentlyClickedButton.getText();
            if(gender.equals("All")){
                searchClothName(clothName);
            }else{
                searchClothNamebyGender(gender,clothName);
            }
            setupPagination();
        }
        
        
    }

    @FXML
    private void handleClothRefreshAction(ActionEvent event) throws SQLException {
        txtsearchclothbar.setText("");
        genderHbox.getChildren().clear();
        initGenderBtn();
        initAllCloth();
        
        
        setupPagination();
        
    }
    
    public void searchClothName(String clothName) throws SQLException{
        String sql= """
                    SELECT cloth_id,clothing_type.clothing_type_id,clothing_type,cloth_name,cloth_img FROM cloth,clothing_type 
                                         WHERE cloth.clothing_type_id=clothing_type.clothing_type_id AND
                                         cloth_name LIKE ?
                    """;
        pst =con.prepareStatement(sql);
        pst.setString(1, clothName);
        rs =pst.executeQuery();
        clothList.clear();
        while(rs.next()){
            clothList.add(new Cloth(rs.getInt("cloth_id"), rs.getInt("clothing_type_id"), rs.getString("clothing_type"), rs.getString("cloth_name"), rs.getString("cloth_img")));
            
        }
        
        
    }
    public void searchClothNamebyGender(String gender,String clothName) throws SQLException{
        String sql= """
                    SELECT cloth_id,clothing_type.clothing_type_id,clothing_type,cloth_name,cloth_img FROM cloth,clothing_type 
                                         WHERE cloth.clothing_type_id=clothing_type.clothing_type_id AND clothing_type= ? AND
                                         cloth_name LIKE ?
                    """;
        pst =con.prepareStatement(sql);
        pst.setString(1, gender);
        pst.setString(2, clothName);
        rs = pst.executeQuery();
        clothList.clear();
        while(rs.next()){
            clothList.add(new Cloth(rs.getInt("cloth_id"), rs.getInt("clothing_type_id"), rs.getString("clothing_type"), rs.getString("cloth_name"), rs.getString("cloth_img")));
            
        }
        
        
    }
    public void initGenderBtn(){
        GridPane clothingButtonsGrid = new GridPane();
            clothingButtonsGrid.setHgap(10); 
            clothingButtonsGrid.setVgap(10);

            for (int i = 0; i < gender_type.size(); i++) {
                Clothing_Type clothing = gender_type.get(i);
                Button btn = new Button(clothing.getClothing_type());
                FontAwesomeIcon icon = new FontAwesomeIcon(); 
                icon.setGlyphName(clothing.getIcon_name());
                btn.setGraphic(icon);
                btn.setPrefSize(100, 40); 
                btn.setStyle("-fx-background-color:white;");
                
                clothingButtonsGrid.add(btn, i, 0); 


                
                btn.setOnAction(e -> {
                    
                    try {
                        if (currentlyClickedButton != null) {
                            currentlyClickedButton.setStyle("-fx-background-color:white;");
                        }
                        
                        
                        btn.setStyle("-fx-background-color:gold;");
                        
                        
                        currentlyClickedButton = btn;
                        String gender = btn.getText();
                        if(!txtsearchclothbar.getText().isEmpty()){
                            String clothName = "%"+txtsearchclothbar.getText()+"%";
                            if(gender.equals("All")){
                                searchClothName(clothName);
                            
                            }else{
                                searchClothNamebyGender(gender, clothName);
                            } 
                        }else{
                            if(gender.equals("All")){
                                initAllCloth();
                            
                            }else{
                                handleFilterAction(gender);
                            } 
                        }
                                              
                        setupPagination();
                    } catch (SQLException ex) {
                        Logger.getLogger(Laundary_serviceController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    
                    
                });

               
                if (i == 0) {
                    currentlyClickedButton = btn;
                    currentlyClickedButton.setStyle("-fx-background-color:gold;");
                    
                }
            }
            
            genderHbox.getChildren().add(clothingButtonsGrid);
    }

    @FXML
    private void handleAddClothAction(ActionEvent event) throws IOException, SQLException {
        Stage ownerStage = (Stage) btnaddCloth.getScene().getWindow();
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminClothCardEditAndAddForm.fxml"));
                            Parent root = loader.load();
                            AdminClothCardEditAndAddFormController modalcontroller = loader.getController();
                            
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new cloth dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
                            /*initAllCloth();
                            genderHbox.getChildren().clear();
                            initGenderBtn();
                            setupPagination();*/
                            
                            
    }

    @FXML
    private void handleServiceRefreshAction(ActionEvent event) throws SQLException {
        txtSearchServiceBar.setText("");
        
        combo_serviceStatus.setValue(service_status.get(0));
        combo_serviceType.setValue(servicetypelist.get(0));
        initService();
    }

    @FXML
    private void handleserviceTypeComboAction(ActionEvent event) throws SQLException {
        if(txtSearchServiceBar.getText().isEmpty()){
            if(combo_serviceStatus.getValue().equals(service_status.get(0))){
            initService();
        }else{
            int s = 0;
            String status = combo_serviceStatus.getValue();
            if(status.equals(service_status.get(1))){
                s=1;
            }
            filterbystatus(s);
            
            
        
        }
        }else{
            searchServiceWithText();
        }
        
    }

    @FXML
    private void handle_statusComboAction(ActionEvent event) throws SQLException {
        if(txtSearchServiceBar.getText().isEmpty()){
            if(combo_serviceStatus.getValue().equals(service_status.get(0))){
            initService();
        }else{
            int s = 0;
            String status = combo_serviceStatus.getValue();
            if(status.equals(service_status.get(1))){
                s=1;
            }
            filterbystatus(s);
            
            
        
        }
        }else{
            searchServiceWithText();
        }
        
    }

    @FXML
    private void handleSearchServiceBarAction(ActionEvent event) throws SQLException {
        if(txtSearchServiceBar.getText().isEmpty()){
            
        }else{
            searchServiceWithText();
        }
    }
    
    public void initService() throws SQLException{
        String serviceType = combo_serviceType.getValue();
        String sql = """
                     SELECT service_id,laundary_service_type.service_type,cloth.cloth_name,clothing_type.clothing_type ,laundary_service.base_price,laundary_service.status FROM laundary_service,laundary_service_type,cloth,clothing_type 
                     WHERE laundary_service.service_type_id=laundary_service_type.service_type_id AND laundary_service.cloth_id=cloth.cloth_id AND cloth.clothing_type_id=clothing_type.clothing_type_id and laundary_service_type.service_type=?;
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, serviceType);
        rs = pst.executeQuery();
        servicelist.clear();
        while(rs.next()){
            String status="";
            int s = rs.getInt("status");
            if(s==0){
                status=service_status.get(2);
            }else{
                status= service_status.get(1);
            }
            servicelist.add(new Service(rs.getInt("service_id"),rs.getString("service_type"),rs.getString("cloth_name"),rs.getString("clothing_type"),rs.getInt("base_price"),status));
        }
        
    }
    public void filterbystatus(int wantedStatus) throws SQLException{
        String serviceType = combo_serviceType.getValue();
        String sql = """
                     SELECT service_id,laundary_service_type.service_type,cloth.cloth_name,clothing_type.clothing_type ,laundary_service.base_price,laundary_service.status FROM laundary_service,laundary_service_type,cloth,clothing_type 
                     WHERE laundary_service.service_type_id=laundary_service_type.service_type_id AND laundary_service.cloth_id=cloth.cloth_id AND cloth.clothing_type_id=clothing_type.clothing_type_id and status=? and laundary_service_type.service_type=?;
                     """;
        pst = con.prepareStatement(sql);
        pst.setInt(1, wantedStatus);
        pst.setString(2, serviceType);
        rs = pst.executeQuery();
        servicelist.clear();
        while(rs.next()){
            String status="";
            int s = rs.getInt("status");
            if(s==0){
                status=service_status.get(2);
            }else{
                status= service_status.get(1);
            }
            servicelist.add(new Service(rs.getInt("service_id"),rs.getString("service_type"),rs.getString("cloth_name"),rs.getString("clothing_type"),rs.getInt("base_price"),status));
        }
    }
    
    public void initAllserviceType() throws SQLException{
        ObservableList<String> type = FXCollections.observableArrayList();
        String sql = "select service_type from laundary_service_type";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
            type.add(rs.getString("service_type"));
        }
        servicetypelist.clear();
        servicetypelist.addAll(type);
        combo_serviceType.setItems(servicetypelist);
        combo_serviceType.setValue(servicetypelist.get(0));
    }
    
    public void searchServiceWithText() throws SQLException{
        String clothName = "%"+txtSearchServiceBar.getText()+"%";
        String statusName = combo_serviceStatus.getValue();
        String type= combo_serviceType.getValue();
        if(statusName.equals("All")){
            String sql = """
                     SELECT service_id,laundary_service_type.service_type,cloth.cloth_name,clothing_type.clothing_type ,laundary_service.base_price,laundary_service.status FROM laundary_service,laundary_service_type,cloth,clothing_type 
                     WHERE laundary_service.service_type_id=laundary_service_type.service_type_id AND laundary_service.cloth_id=cloth.cloth_id AND cloth.clothing_type_id=clothing_type.clothing_type_id and laundary_service_type.service_type=? and cloth.cloth_name like ?;
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, type);
        pst.setString(2, clothName);
        rs = pst.executeQuery();
        servicelist.clear();
        while(rs.next()){
            String status="";
            int s = rs.getInt("status");
            if(s==0){
                status=service_status.get(2);
            }else{
                status= service_status.get(1);
            }
            servicelist.add(new Service(rs.getInt("service_id"),rs.getString("service_type"),rs.getString("cloth_name"),rs.getString("clothing_type"),rs.getInt("base_price"),status));
        }
        }else{
            int wantedStatus= 0;
            if(statusName.equals(service_status.get(1))){
                wantedStatus=1;
            }
            
        String sql = """
                     SELECT service_id,laundary_service_type.service_type,cloth.cloth_name,clothing_type.clothing_type ,laundary_service.base_price,laundary_service.status FROM laundary_service,laundary_service_type,cloth,clothing_type 
                     WHERE laundary_service.service_type_id=laundary_service_type.service_type_id AND laundary_service.cloth_id=cloth.cloth_id AND cloth.clothing_type_id=clothing_type.clothing_type_id and status=? and laundary_service_type.service_type=? and cloth.cloth_name like ?;
                     """;
        pst = con.prepareStatement(sql);
        pst.setInt(1, wantedStatus);
        pst.setString(2, type);
        pst.setString(3, clothName);
        rs = pst.executeQuery();
        servicelist.clear();
        while(rs.next()){
            String status="";
            int s = rs.getInt("status");
            if(s==0){
                status=service_status.get(2);
            }else{
                status= service_status.get(1);
            }
            servicelist.add(new Service(rs.getInt("service_id"),rs.getString("service_type"),rs.getString("cloth_name"),rs.getString("clothing_type"),rs.getInt("base_price"),status));
        }
        }
        
    }

    @FXML
    private void handleAddNewServiceTypeAction(ActionEvent event) throws IOException, SQLException {
        Stage ownerStage = (Stage) btnAddNewServiceType.getScene().getWindow();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addnewserviceTypeFormAdmin.fxml"));
                            Parent root = loader.load();
                            AddnewserviceTypeFormAdminController modalcontroller = loader.getController();
                            
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new Service Type Dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
                            initAllserviceType();
    }
    
}
