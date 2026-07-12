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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
// import javafx.scene.control.Button; // Unused, can remove if not needed (already removed from previous suggestion)
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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
import javafx.util.Callback;
import javax.swing.JOptionPane;
// import javax.swing.JOptionPane; // Consider using JavaFX dialogs for better integration (already noted in previous suggestion)
import model.Clothing_Type;
import model.Room;
import model.Service;
import controller.Check_outController;
import controller.Cloth_CardController;
import static controller.RoomBookingController.cartList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class Laundary_serviceController implements Initializable {


    //database
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    Statement st;

    // Removed @FXML if not directly injected in FXML
    // private HBox category_bar; // Still not used, consider removing unless you plan to use it for other categories
    @FXML // Ensure this is correctly injected in your FXML
    private ComboBox<String> service_type_cb_box;
    @FXML // Unused, can remove if not needed
    private Label lbl_date_today;
    @FXML // Ensure this is correctly injected in your FXML
    private HBox clothing_bar; // This is where the buttons should go!
    @FXML
    private Pagination pagination;

    //to used in pagination
    int card_per_page = 6;
    int total_room; // This will be updated dynamically
    int pageCount; // This will be updated updated dynamically
    //to show service data
    ObservableList<Service> serviceList = FXCollections.observableArrayList();
    //to use in service_type_cb_box
    ObservableList<String> serviceTypeList = FXCollections.observableArrayList();
    //to use in clothing_bar;
    ObservableList<Clothing_Type> clothing_type = FXCollections.observableArrayList();
    //to use in cart
    public static ObservableList<Service> cartList = FXCollections.observableArrayList();
 
    // Keep track of the currently disabled button
    private Button currentlyClickedButton = null;
    
    //to show order_id
    public static String order_id = "";
    public static String delivery_date;
    //to input sql query of order table
    int status_id;
    //to show grand_total
    public  int grand_total;
    @FXML
    private Label lbl_order_id;
    @FXML
    private TableView<Service> tbCartList;
    @FXML
    private TableColumn<?, ?> colService_Name;
    @FXML
    private TableColumn<?, ?> col_cloth_name;
    @FXML
    private TableColumn<Service, Integer> colQty;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TextField txtGrand_Total;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnEnter;
    
    Parent root;
    
    @FXML
    private DatePicker deliverDate_dp;
    @FXML
    private Button btnViewOrder;
    @FXML
    private TextField txtsearchClothName;
    @FXML
    private Button btnRefresh;
private StaffPageController staffPageController;
    
    public void setStaffPageController(StaffPageController staffPageController) {
        this.staffPageController = staffPageController;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Database db = new Database();
            con = db.getConnection();

            initServiceType();
            initClothingType();
            initOrderID();
            
            lbl_date_today.setText(LocalDate.now().toString());

            calculate();
            reset_clothing_btn();


           
            service_type_cb_box.setItems(serviceTypeList);
            service_type_cb_box.setValue(serviceTypeList.get(0)); // Set initial selection
           

            search_service(); // Populate serviceList initially
            setupPagination(); // Setup pagination with initial data
            
            colService_Name.setCellValueFactory(new PropertyValueFactory<>("service_name"));
            col_cloth_name.setCellValueFactory(new PropertyValueFactory<>("cloth_name"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("service_price"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            tbCartList.setItems(cartList);
            
            colQty.setCellFactory(column -> new TableCell<Service,Integer>(){
                private final Button btnInc = new Button("+");
                private final Button btnDec = new Button("-");
                private final Label lblQty= new Label();
                //private final Label lbl1 = new Label("");
                //private final Label lbl2 = new Label("");
                //private final Label lblAmount = new Label();
                private final HBox hbox = new HBox(5,btnDec,lblQty,btnInc);
                
                
                {
                    hbox.setAlignment(Pos.CENTER);
                    lblQty.setPrefWidth(35);
                    
                    btnInc.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    btnDec.setStyle("-fx-background-color: darkred;-fx-text-fill:white;");
                    btnInc.setOnAction(e->{
                        Service ser =getTableView().getItems().get(getIndex());
                        int index = cartList.indexOf(ser);
                        ser.setQty(ser.getQty()+1);
                        ser.setTotal(ser.getService_price()*ser.getQty());
                        lblQty.setText(String.valueOf(ser.getQty()));
                        cartList.set(index , ser);
                        
                        
                        
                        
                    });
                    btnDec.setOnAction(e->{
                        Service ser = getTableView().getItems().get(getIndex());
                        int index = cartList.indexOf(ser);
                        if(ser.getQty()>1){
                            ser.setQty(ser.getQty()-1);
                            ser.setTotal(ser.getService_price()*ser.getQty());
                            lblQty.setText(String.valueOf(ser.getQty()));
                            cartList.set(index, ser);
                           
                        
                            
                                    
                        }
                    });
                }
                @Override
                protected void updateItem(Integer qty,boolean  empty){
                    super.updateItem(qty, empty);
                    if(empty|| qty==null){
                        setGraphic(null);
                    }else{
                        Service ser =getTableView().getItems().get(getIndex());
                        lblQty.setText(String.valueOf(ser.getQty()));
                        setGraphic(hbox);
                    }
                }
            });
            //to input query of order table status id
            String sql = "select Order_Status_id from order_status where Order_Status='At Laundary'";
            st =con.createStatement();
            rs= st.executeQuery(sql);
            if(rs.next()){
                status_id=rs.getInt("Order_Status_id");
            }
             /*  remove button */
            TableColumn<Service, Void> colaction = new TableColumn<>("Action");

            Callback<TableColumn<Service, Void>, TableCell<Service, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<Service, Void> call(final TableColumn<Service, Void> param) {
                    final TableCell<Service, Void> cell = new TableCell<>() {

                        private final Button btn = new Button("Remove");
                        
                        {
                             btn.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
                            btn.setOnAction((event) -> {
                                Service ser = getTableView().getItems().get(getIndex());
                                getTableView().getItems().remove(ser); // Remove from UI
                                cartList.remove(ser);
                                  
                            
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btn);
                            }
                        }
                    };
                    return cell;
                }
            };

            colaction.setCellFactory(cellFactory);
            tbCartList.getColumns().add(colaction);
            
            // Add a listener to react to changes
        cartList.addListener(new ListChangeListener<Service>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Service> change) {
                
                // You can iterate through the changes to see what exactly happened
                while (change.next()) {
                    if (change.wasAdded()) {
                        calculate();
                    }
                    if (change.wasRemoved()) {
                        calculate();
                    }
                    if (change.wasUpdated()) {
                        calculate();
                    }
                }
                System.out.println("---");
            }
        });
        
        //for roomno btn
        setUpRoomNoBtn();
        
        
                    

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Laundary_serviceController.class.getName()).log(Level.SEVERE, null, ex);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Connection Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Database driver not found: " + ex.getMessage());
            errorAlert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(Laundary_serviceController.class.getName()).log(Level.SEVERE, null, ex);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Connection Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Database connection error: " + ex.getMessage());
            errorAlert.showAndWait();
        }
    }

    // New method to encapsulate pagination setup/refresh logic
    private void setupPagination() {
        total_room = serviceList.size(); // Ensure total_room is up-to-date
        pageCount = (int) Math.ceil((double) total_room / card_per_page);
        pagination.setPageCount(pageCount > 0 ? pageCount : 1);
        pagination.setCurrentPageIndex(0); // Always start at the first page for new data

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

    @FXML
    private void handle_service_box_action(ActionEvent event) throws SQLException {
        if(currentlyClickedButton.getText().equals("All")){
            if(txtsearchClothName.getText().isEmpty()){
                search_service();
            }else{
                search_service_byName("%"+txtsearchClothName.getText()+"%");
            }
            
        }else{
            if(txtsearchClothName.getText().isEmpty()){
                handleFilterAction(currentlyClickedButton.getText());
            }else{
                handleFilterAction_byName(currentlyClickedButton.getText(), "%"+txtsearchClothName.getText()+"%");
            }
            
        }
         
        setupPagination();
        
    }

    public Node createPage(int pageIndex) throws IOException {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(20);
        
        int startIndex = pageIndex * card_per_page;
        // Ensure endIndex does not exceed the actual size of serviceList
        int endIndex = Math.min(startIndex + card_per_page, serviceList.size());

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Cloth_Card.fxml"));
            AnchorPane pane = loader.load();
            Cloth_CardController paneController = loader.getController();
            paneController.setData(serviceList.get(i)); // Pass the Service object to the card controller

            grid.add(pane, col, row);

            col++;
            if (col == 3) { // Assuming 3 columns per row
                col = 0;
                row++;
            }
        }
        return grid;
    }

    public void initServiceType() throws SQLException {
        serviceTypeList.clear(); // Clear existing items before populating
        String sql = "select service_type from laundary_service_type";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
            serviceTypeList.add(rs.getString("service_type"));
        }
    }

    public void search_service() throws SQLException {
        String serviceType = service_type_cb_box.getValue();
        

        String sql = """
                     SELECT service_id, service_type, cloth.cloth_id, cloth_name, base_price, cloth_img
                     FROM laundary_service
                     JOIN laundary_service_type ON laundary_service.service_type_id = laundary_service_type.service_type_id
                     JOIN cloth ON laundary_service.cloth_id = cloth.cloth_id
                     WHERE laundary_service_type.service_type = ? and status= '1'
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, serviceType);
        rs = pst.executeQuery();
        serviceList.clear(); // Clear previous search results
        while (rs.next()) {
            serviceList.add(new Service(
                rs.getInt("service_id"),
                rs.getString("service_type"),
                rs.getInt("cloth_id"),
                rs.getString("cloth_name"),
                rs.getInt("base_price"),
                rs.getString("cloth_img")
            ));
        }
        
    }

    public void initClothingType() throws SQLException {
        clothing_type.clear(); 
        String sql = "SELECT clothing_type,icon_name FROM clothing_type";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        clothing_type.add(new Clothing_Type("All", "GENDERLESS")); // Add "All" option
        while (rs.next()) {
            clothing_type.add(new Clothing_Type(rs.getString("clothing_type"), rs.getString("icon_name")));
        }
    }
    
    public void handleFilterAction(String clothingType) throws SQLException{
        String serviceType = service_type_cb_box.getValue();
        

        String sql = """
                     SELECT service_id, service_type, cloth.cloth_id, cloth_name, base_price, cloth_img
                                          FROM laundary_service
                                          JOIN laundary_service_type ON laundary_service.service_type_id = laundary_service_type.service_type_id
                                          JOIN cloth ON laundary_service.cloth_id = cloth.cloth_id
                                          JOIN clothing_type ON cloth.clothing_type_id=clothing_type.clothing_type_id
                                          WHERE laundary_service_type.service_type =? AND clothing_type.clothing_type=? and status='1'
                                           
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, serviceType);
        pst.setString(2, clothingType);
        rs = pst.executeQuery();
        serviceList.clear(); // Clear previous search results
        while (rs.next()) {
            serviceList.add(new Service(
                rs.getInt("service_id"),
                rs.getString("service_type"),
                rs.getInt("cloth_id"),
                rs.getString("cloth_name"),
                rs.getInt("base_price"),
                rs.getString("cloth_img")
            ));
        }
        
        
    }
    public void initOrderID() throws SQLException{
        String sql = "SELECT COUNT(*) AS order_id FROM laundary_service_order";
        st =con.createStatement();
        rs= st.executeQuery(sql);
        if(rs.next()){
            order_id= String.valueOf(rs.getInt("order_id")+1) ;
        }
        lbl_order_id.setText(order_id);
    }
    public void calculate(){
        grand_total=0;
        for(Service ser: cartList){
            grand_total += ser.getTotal();
        }
        txtGrand_Total.setText(String.valueOf(grand_total));
    }

    @FXML
    private void handle_Cancel_Action(ActionEvent event) {
        if(!cartList.isEmpty()){
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel Service Order", "confrim Cancel", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(response == JOptionPane.YES_OPTION){
                cartList.clear();
            }
        }
        
    }

    @FXML
    private void handle_ConfirmAction(ActionEvent event) throws SQLException, IOException {
        if(cartList.isEmpty()){
             JOptionPane.showMessageDialog(null, "Your cart is empty. Please Order", "Cart Empty", JOptionPane.WARNING_MESSAGE);
        }else if(btnEnter.getText().equals("Enter Room No")){
            JOptionPane.showMessageDialog(null, "No Room selected,Please Choose room Number", "Room Number Required", JOptionPane.WARNING_MESSAGE);
        }else if(deliverDate_dp.getValue()==null){
            JOptionPane.showMessageDialog(null, "please Choose Pick up date");
        }else{
            JOptionPane.showMessageDialog(null, "completed");
            orderdataInput();
        }
        
    }

    @FXML
    private void handleEnterAction(ActionEvent event) throws IOException {
      if (staffPageController != null) {
            staffPageController.loadFXMLIntoDefaultAnchor("/view/Check_out.fxml");
        } else {
            System.err.println("StaffPageController reference is null. Cannot load FXML.");
           
        }

    }
    
    public void orderdataInput() throws SQLException, IOException{
        delivery_date = deliverDate_dp.getValue().toString();
        String sql1 = "insert into laundary_service_order values(?,?,?,?,?,?)";
        pst = con.prepareStatement(sql1);
        pst.setInt(1, Integer.parseInt(order_id));
        pst.setInt(2, Check_outController.booking_detail_id);
        pst.setString(3, lbl_date_today.getText());
        pst.setString(4, delivery_date);
        pst.setInt(5,StaffPageController.empID);
        pst.setInt(6, status_id);
        pst.executeUpdate();
        for(Service ser:cartList){
            String sql2 = "insert into laundary_service_order_detail (order_id,service_id,quantity,price) values (?,?,?,?)";
            pst = con.prepareStatement(sql2);
            pst.setInt(1, Integer.parseInt(order_id));
            pst.setInt(2, ser.getService_id());
            pst.setInt(3, ser.getQty());
            pst.setInt(4, ser.getService_price());
            pst.executeUpdate();
            
        }
        
        cartList.clear();
        Check_outController.booking_detail_id=0;
        Check_outController.room_id=0;
        Check_outController.check_out_date=""; 
        initOrderID();
        deliverDate_dp.setValue(null);
        setUpRoomNoBtn();
        
     
                       
    }

    @FXML
    private void handleViewOrderAction(ActionEvent event) throws IOException {
          if (staffPageController != null) {
            staffPageController.loadFXMLIntoDefaultAnchor("/view/OrderList.fxml");
        } else {
            System.err.println("StaffPageController reference is null. Cannot load FXML.");
           
        }
    }

    @FXML
    private void handleSearchClothAction(ActionEvent event) throws SQLException {
        if(txtsearchClothName.getText().isEmpty()){
            
        }else{
            if(currentlyClickedButton.getText().equals("All")){
                search_service_byName("%"+txtsearchClothName.getText()+"%");
            }else{
                handleFilterAction_byName(currentlyClickedButton.getText(),"%"+txtsearchClothName.getText()+"%");
            }
            setupPagination();
        }
    }

    @FXML
    private void handlRefreshAction(ActionEvent event) throws SQLException {
        txtsearchClothName.setText("");
        reset_clothing_btn();
        service_type_cb_box.setValue(serviceTypeList.get(0));
        search_service();
        setupPagination();
        
        
    }
    
    public void search_service_byName(String clothName) throws SQLException{
        String serviceType = service_type_cb_box.getValue();
        

        String sql = """
                     SELECT service_id, service_type, cloth.cloth_id, cloth_name, base_price, cloth_img
                     FROM laundary_service
                     JOIN laundary_service_type ON laundary_service.service_type_id = laundary_service_type.service_type_id
                     JOIN cloth ON laundary_service.cloth_id = cloth.cloth_id
                     WHERE laundary_service_type.service_type = ? and status= '1' and cloth_name like ?
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, serviceType);
        pst.setString(2, clothName);
        rs = pst.executeQuery();
        serviceList.clear(); // Clear previous search results
        while (rs.next()) {
            serviceList.add(new Service(
                rs.getInt("service_id"),
                rs.getString("service_type"),
                rs.getInt("cloth_id"),
                rs.getString("cloth_name"),
                rs.getInt("base_price"),
                rs.getString("cloth_img")
            ));
        }
        
    }
    public void handleFilterAction_byName(String clothingType,String clothName) throws SQLException{
        String serviceType = service_type_cb_box.getValue();
        

        String sql = """
                     SELECT service_id, service_type, cloth.cloth_id, cloth_name, base_price, cloth_img
                                          FROM laundary_service
                                          JOIN laundary_service_type ON laundary_service.service_type_id = laundary_service_type.service_type_id
                                          JOIN cloth ON laundary_service.cloth_id = cloth.cloth_id
                                          JOIN clothing_type ON cloth.clothing_type_id=clothing_type.clothing_type_id
                                          WHERE laundary_service_type.service_type =? AND clothing_type.clothing_type=? and status='1' and cloth_name like ?
                                           
                     """;
        pst = con.prepareStatement(sql);
        pst.setString(1, serviceType);
        pst.setString(2, clothingType);
        pst.setString(3, clothName);
        rs = pst.executeQuery();
        serviceList.clear(); // Clear previous search results
        while (rs.next()) {
            serviceList.add(new Service(
                rs.getInt("service_id"),
                rs.getString("service_type"),
                rs.getInt("cloth_id"),
                rs.getString("cloth_name"),
                rs.getInt("base_price"),
                rs.getString("cloth_img")
            ));
        }
    }
    public void reset_clothing_btn(){
        clothing_bar.getChildren().clear();
        GridPane clothingButtonsGrid = new GridPane();
            clothingButtonsGrid.setHgap(10); 
            clothingButtonsGrid.setVgap(10);

            for (int i = 0; i < clothing_type.size(); i++) {
                Clothing_Type clothing = clothing_type.get(i);
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
                        String clothingType = btn.getText();
                        if(clothingType.equals("All")){
                            if(txtsearchClothName.getText().isEmpty()){
                                search_service();
                            }else{
                                search_service_byName("%"+txtsearchClothName.getText()+"%");
                            }
                            
                        }else{
                            if(txtsearchClothName.getText().isEmpty()){
                                handleFilterAction(clothingType);
                            }else{
                                handleFilterAction_byName(clothingType,"%"+txtsearchClothName.getText()+"%");
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
            
            clothing_bar.getChildren().add(clothingButtonsGrid);
    }
    
    public void setUpRoomNoBtn(){
        if(Check_outController.room_id==0){
            btnEnter.setText("Enter Room No");
            deliverDate_dp.setDisable(true);
            
        }else{
            btnEnter.setText(String.valueOf(Check_outController.room_id));
            deliverDate_dp.setDisable(false);
        }
        if(Check_outController.check_out_date==null || Check_outController.check_out_date.isBlank() || Check_outController.check_out_date.isEmpty()){
            System.out.println("hello null");
        }else{
            LocalDate check_out = LocalDate.parse(Check_outController.check_out_date);
            deliverDate_dp.setDayCellFactory(dp -> new DateCell(){
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);

				// Disable today's date and future dates
				if (date != null && !empty && ( date.isBefore(LocalDate.now()))) {
					setDisable(true);
					setStyle("-fx-background-color: #d3d3d3;"); // Gray out the disabled dates
				}
                                if (date != null && !empty && ( date.isAfter(check_out))) {
					setDisable(true);
					setStyle("-fx-background-color: #d3d3d3;"); // Gray out the disabled dates
				}
			}
            });
        }
        
    }
}
