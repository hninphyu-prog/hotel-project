/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import model.User;
import database.Database;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ViewUserDataController implements Initializable {

    @FXML
    private TextField txtSearch;
   
    @FXML
    private TableView<User> tbUser;
    @FXML
    private TableColumn<?, ?> ID;
    @FXML
    private TableColumn<?, ?> UserName;
    @FXML
    private TableColumn<?, ?> UserType;
    @FXML
    private TableColumn<?, ?> Status;
    @FXML
    private TableColumn<User, Void> ResetPassword;

    @FXML
    private TableColumn<User,Void> Action;
     @FXML
    private Button btnRefresh;

    

    /**
     * Initializes the controller class.
     */
    Connection con;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;
    ObservableList<User> UserList;
    private User u;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db=new Database();

        try {
            con= db.getConnection();
            initUserList();
            
            ID.setCellValueFactory(new PropertyValueFactory("employee_id"));
            UserName.setCellValueFactory(new PropertyValueFactory("user_name"));
            UserType.setCellValueFactory(new PropertyValueFactory("user_role"));
            Status.setCellValueFactory(new PropertyValueFactory("status"));
           
            tbUser.setItems(UserList);
            
             Action.setCellFactory(col->new TableCell<User,Void>(){
                private final Button btnEdit = new Button("Change Status");
                private final HBox box = new HBox(btnEdit);
                
                {   
                    box.setAlignment(Pos.CENTER);
                    btnEdit.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    
                    btnEdit.setOnAction(e->{
                        try {
                            User u= getTableView().getItems().get(getIndex());
                            int index = UserList.indexOf(u);
                            Stage ownerStage = (Stage) btnEdit.getScene().getWindow();
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/a.fxml"));
                            Parent root = loader.load();
                            AController modalcontroller = loader.getController();                            
                            modalcontroller.setData(index,u);
                            modalcontroller.setUserList(UserList);
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new Room Type dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            Stage stage = ownerStage;
                            modalStage.showAndWait();
                        } catch (IOException ex) {
                            Logger.getLogger(ViewUserDataController.class.getName()).log(Level.SEVERE, null, ex);
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
             ResetPassword.setCellFactory(col->new TableCell<User,Void>(){
                private final Button btnReset = new Button("Reset Password");
                private final HBox box = new HBox(btnReset);
                
                {   
                    box.setAlignment(Pos.CENTER);
                    btnReset.setStyle("-fx-background-color: green;-fx-text-fill:white;");
                    
                    btnReset.setOnAction(e->{
                        try {
                            User u= getTableView().getItems().get(getIndex());
                            int index = UserList.indexOf(u);
                            Stage ownerStage = (Stage) btnReset.getScene().getWindow();
                            JOptionPane.showMessageDialog(null,"Are you sure to reset new password!");
                            
                            
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ResetPassword.fxml"));
                            Parent root = loader.load();
                            ResetPasswordController modalcontroller = loader.getController();                            
                            modalcontroller.setData(index,u);
                            modalcontroller.setUserList(UserList);
                            Stage modalStage = new Stage();
                            modalStage.setTitle("Add new Room Type dialog");
                            modalStage.setScene(new Scene(root));
                            modalStage.initModality(Modality.WINDOW_MODAL); 
                            modalStage.initOwner(ownerStage);
                            Stage stage = ownerStage;
                            modalStage.showAndWait();
                        } catch (IOException ex) {
                            Logger.getLogger(ViewUserDataController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ViewUserDataController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewUserDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleSearchAction(ActionEvent event) throws SQLException {
        
    if(txtSearch.getText().isEmpty()){
        JOptionPane.showMessageDialog(null,"Please search by user name!");
        initUserList();
    } else {
        searchUserType();
    }
    tbUser.setItems(UserList); // ✅ Refresh TableView items after filtering
    
    
    
}

    @FXML
    void handleRefreshAction(ActionEvent event) throws SQLException {
    txtSearch.setText("");       // Clear the search field
    initUserList();              // Reload all users from the database
    tbUser.setItems(UserList);   // ✅ Reset TableView's items
}
    public void searchUserType() throws SQLException{
        String name = "%"+txtSearch.getText()+"%";
        String sql = "select * from user where user_name like ?";
        pst = con.prepareStatement(sql);
        pst.setString(1, name);
        rs = pst.executeQuery();
        UserList.clear();
        while(rs.next()){
             UserList.add(new User(rs.getInt("employee_id"),rs.getString("user_name"),rs.getString("user_role"),rs.getString("status")));
        }
           
    }


    
    public void initUserList() throws SQLException{
        UserList=FXCollections.observableArrayList();
        String sql="Select employee_id,user_name,user_role,status from user;";
        st=con.createStatement();
        rs=st.executeQuery(sql);// for database return
         while(rs.next()){
             UserList.add(new User(rs.getInt("employee_id"),rs.getString("user_name"),rs.getString("user_role"),rs.getString("status")));
         }
        
        
    }
    
}
