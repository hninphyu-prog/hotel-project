/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import static hotel_management.Hotel_Management.stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Rooms;
import model.RoomType;
import controller.Laundary_serviceController;
import controller.UpdateRoomStatusFormAdminController;
import javafx.geometry.Insets;


/**
 * FXML Controller class
 *
 * @author USER
 */
public class AdminRoomController implements Initializable {


    @FXML
    private TextField txtSearchRoomType;
    @FXML
    private Button btnRoomTypeRefresh;
    @FXML
    private Button btnAddRoomType;
    @FXML
    private TableView<Rooms> tbRoomView;
    @FXML
    private TableColumn<?, ?> colRoomNo;
    @FXML
    private TableColumn<?, ?> colRoomType;
    @FXML
    private TableColumn<?, ?> colFloorNo;
    @FXML
    private TableColumn<Rooms, Void> colAction;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private ComboBox<String> combo_roomType;
    @FXML
    private TextField txtSearchRoomNo;
    @FXML
    private Button btnRoomRefresh;
    
    //database
    Connection con;
    ResultSet rs;
    Statement st;
    PreparedStatement pst;
    
    //to used in pagination
    int card_per_page = 4;
    int total_room; // This will be updated dynamically
    int pageCount; // This will be updated updated dynamically
    //to store room Type data
    ObservableList<RoomType> roomTypeList = FXCollections.observableArrayList();
    ObservableList<String> roomTypeName = FXCollections.observableArrayList();
    
    public static ObservableList<Rooms> roomList = FXCollections.observableArrayList();
    @FXML
    private Pagination pagination;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            Database db =new Database();
            con = db.getConnection();
            
            initRoomTypeList();
            initRoomTypeName();
            initRoom();
            
            colRoomNo.setCellValueFactory(new PropertyValueFactory<>("roomno"));
            colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
            colFloorNo.setCellValueFactory(new PropertyValueFactory<>("floornno"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            tbRoomView.setItems(roomList);
            colAction.setCellFactory(col->new TableCell<Rooms,Void>(){
                private final Button btnEdit = new Button("Change Status");
                private final HBox box = new HBox(btnEdit);
                
                {   
                    box.setAlignment(Pos.CENTER);
                    btnEdit.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    
                    btnEdit.setOnAction(e->{
                        try {
                            Rooms room= getTableView().getItems().get(getIndex());
                            int index = roomList.indexOf(room);
                            Stage ownerStage = (Stage) btnEdit.getScene().getWindow();
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateRoomStatusFormAdmin.fxml"));
                            Parent root = loader.load();
                            UpdateRoomStatusFormAdminController modalcontroller = loader.getController();
                            modalcontroller.setData(index,room);
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new Room Type dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
                        } catch (IOException ex) {
                            Logger.getLogger(AdminRoomController.class.getName()).log(Level.SEVERE, null, ex);
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
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminRoomController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AdminRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    @FXML
    private void handleRoomTypeRefreshAction(ActionEvent event) throws SQLException {
        txtSearchRoomType.setText("");
        initRoomTypeList();
        initRoomTypeName();
        
        
        
        
        
        
        
    }

    @FXML
    private void handleCombo_roomTypeAction(ActionEvent event) throws SQLException {
        if(combo_roomType.getValue()==null){
            searchRoom();
        }else if(txtSearchRoomNo.getText().isEmpty()){
            initRoom();
        }else{
            togetherSearch();
        }
        
    }

    @FXML
    private void handleSearchRoomNoAction(ActionEvent event) throws SQLException {
        if(txtSearchRoomNo.getText().isEmpty()){
            
        }else{
            combo_roomType.setValue(null);
            searchRoom();
            
        }
        
        
    }
    

    @FXML
    private void handleRoomRefreshAction(ActionEvent event) throws SQLException {
        txtSearchRoomNo.setText("");
        combo_roomType.setValue(roomTypeName.get(0));
        initRoom();
        
       
    }
    // New method to encapsulate pagination setup/refresh logic
    private void setupPagination() {
        total_room = roomTypeList.size(); // Ensure total_room is up-to-date
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
        grid.setPadding(new Insets(20));

        int startIndex = pageIndex * card_per_page;
        // Ensure endIndex does not exceed the actual size of serviceList
        int endIndex = Math.min(startIndex + card_per_page, roomTypeList.size());

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/adminViewRoomCard.fxml"));
            AnchorPane pane = loader.load();
            AdminViewRoomCardController paneController = loader.getController();
            paneController.setData(roomTypeList.get(i)); // Pass the Service object to the card controller

            grid.add(pane, col, row);

            col++;
            if (col == 2) { // Assuming 3 columns per row
                col = 0;
                row++;
            }
        }
        return grid;
    }


    
    public void initRoomTypeList() throws SQLException{
        String sql = "select * from room_type";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        roomTypeList.clear();
        
        while(rs.next()){
            
            roomTypeList.add(new RoomType(rs.getInt("room_type_id"), rs.getString("room_type"),rs.getInt("capacity"),rs.getInt("price"), rs.getString("image_path")));
        }
        setupPagination();
    }
    public void initRoomTypeName() throws SQLException{
        ObservableList<String> type = FXCollections.observableArrayList();
        String sql = "select room_type from room_type";
        st =con.createStatement();
        rs = st.executeQuery(sql);
        
        while(rs.next()){
            type.add(rs.getString("room_type"));
        }
        roomTypeName.clear();
        roomTypeName.addAll(type);        
        combo_roomType.setItems(roomTypeName);
        combo_roomType.setValue(roomTypeName.get(0));
        
        
        
       
    }
    
    public void initRoom() throws SQLException{
        
        String type = combo_roomType.getValue();
        String sql = "SELECT * FROM room,room_type WHERE room.room_type_id = room_type.room_type_id AND room_type.room_type=?";
        pst = con.prepareStatement(sql);
        pst.setString(1, type);
        rs = pst.executeQuery();
        roomList.clear();
        while(rs.next()){
            String status= "Available";
            if(rs.getInt("status")==0){
                status= "Not Available";
            }
            roomList.add(new Rooms(String.valueOf(rs.getInt("room_id")), rs.getInt("room_type_id"), rs.getString("room_type"), String.valueOf(rs.getInt("floor_no")), status));
        }
        
    }

    @FXML
    private void handleAddRoomTypeAction(ActionEvent event) throws IOException, SQLException {
        Stage ownerStage = (Stage) btnAddRoomType.getScene().getWindow();
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminRoomTypeEditAndAddForm.fxml"));
                            Parent root = loader.load();
                            AdminRoomTypeEditAndAddFormController modalcontroller = loader.getController();
                            
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new Room Type dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            stage = ownerStage;
                            modalStage.showAndWait();
                            
                            
    }

    @FXML
    private void handleSearchRoomTypeAction(ActionEvent event) throws SQLException {
        if(txtSearchRoomType.getText().isEmpty()){
            initRoomTypeList();
            setupPagination();
        }else{
            searchRoomType();
            
        }
        
    }
    public void searchRoom() throws SQLException{
        String pattern="[0-9]{1,}";
        if(txtSearchRoomNo.getText().matches(pattern)&& Integer.parseInt(txtSearchRoomNo.getText())!=0 ){
            int roomNo = Integer.parseInt(txtSearchRoomNo.getText());
            String sql = "select * from room,room_type where room.room_type_id=room_type.room_type_id and room_id= ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, roomNo);
            rs = pst.executeQuery();
            roomList.clear();
            if(rs.next()){
                String status= "Available";
            if(rs.getInt("status")==0){
                status= "Not Available";
            }
            roomList.add(new Rooms(String.valueOf(rs.getInt("room_id")), rs.getInt("room_type_id"), rs.getString("room_type"), String.valueOf(rs.getInt("floor_no")), status));

            }
        }else{
            roomList.clear();
        }
        
    }
    public void togetherSearch() throws SQLException{
        String type = combo_roomType.getValue();
        String pattern="[0-9]{1,}";
        if(txtSearchRoomNo.getText().matches(pattern)&& Integer.parseInt(txtSearchRoomNo.getText())!=0 ){
            int roomNo = Integer.parseInt(txtSearchRoomNo.getText());
            String sql = "select * from room,room_type where room.room_type_id=room_type.room_type_id and room_id= ? AND room_type.room_type=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, roomNo);
            pst.setString(2, type);
            rs = pst.executeQuery();
            roomList.clear();
            if(rs.next()){
                String status= "Available";
            if(rs.getInt("status")==0){
                status= "Not Available";
            }
            roomList.add(new Rooms(String.valueOf(rs.getInt("room_id")), rs.getInt("room_type_id"), rs.getString("room_type"), String.valueOf(rs.getInt("floor_no")), status));

            }
        }else{
            roomList.clear();
        }
    }
    public void searchRoomType() throws SQLException{
        String type = "%"+txtSearchRoomType.getText()+"%";
        String sql = "select * from room_type where room_type like ?";
        pst = con.prepareStatement(sql);
        pst.setString(1, type);
        rs = pst.executeQuery();
        roomTypeList.clear();
        
        while(rs.next()){
            
            roomTypeList.add(new RoomType(rs.getInt("room_type_id"), rs.getString("room_type"),rs.getInt("capacity"),rs.getInt("price"), rs.getString("image_path")));
        }
        setupPagination();    
    }

}
