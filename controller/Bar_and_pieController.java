/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.Database;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class Bar_and_pieController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private PieChart p_chart;
    @FXML
    private ComboBox<String> TimeRangeCombo;
    @FXML
    private Button btnfood;

    @FXML
    private Button btnLaundry;

    @FXML
    private Button btnRoom;
    
    @FXML
    private PieChart pie_chart2;
    
    @FXML
    private Button btnhall;

    
    private String currentChartType = "food"; // default
    private String currentTimeRange = "Monthly"; // default
    
    Connection con;
    Statement st;
    ResultSet rs;
    PreparedStatement ps;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            TimeRangeCombo.getItems().addAll("Monthly", "Yearly");
            TimeRangeCombo.setValue("Monthly");
            currentTimeRange = "Monthly";
            currentChartType = "food";
            Database db = new Database();
            con=db.getConnection();
            loadChartData(currentChartType, currentTimeRange);
            loadPieChartData();
            loadPieChartData2();
            
            
                try {
                    loadChartData(currentChartType,currentTimeRange);
                } catch (SQLException ex) {
                    Logger.getLogger(Bar_and_pieController.class.getName()).log(Level.SEVERE, null, ex);
                }
            TimeRangeCombo.setOnAction(e -> handleTimeRangeChange());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Bar_and_pieController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Bar_and_pieController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

   private void loadChartData(String chartType, String timeRange) throws SQLException {
    barChart.getData().clear();
    barChart.layout(); 

    String dateColumn = "";
    switch (chartType.toLowerCase()) {
        case "food":
            dateColumn = "food_order_date";
            break;
        case "room":
            dateColumn = "booking_date";
            break;
        case "laundry":
            dateColumn = "delivery_date";
            break;
        case "hall":
            dateColumn= "want_date";
    }

    String formatSql;
    switch (timeRange) {
        /*case "Weekly":
            formatSql = "CONCAT(YEAR(" + dateColumn + "), '-W', LPAD(WEEK(" + dateColumn + ", 1), 2, '0'))";
            break;*/
        case "Monthly":
            formatSql = "DATE_FORMAT(" + dateColumn + ", '%Y-%m')";
            break;
        case "Yearly":
            formatSql = "YEAR(" + dateColumn + ")";
            break;
        default:
            formatSql = "DATE_FORMAT(" + dateColumn + ", '%Y-%m')";
    }

        String sql = "";
     switch (chartType.toLowerCase()) {
    case "food":
        sql = "SELECT " + formatSql + " AS period, SUM(price*quantity) AS total " +
              "FROM food_order A JOIN food_order_detail B ON A.food_order_id = B.food_order_id " +
              "GROUP BY period ORDER BY period";
        break;
    case "room":
        sql = "SELECT period, SUM(amount) AS total FROM (" +
              "SELECT " + formatSql.replace(dateColumn, "B.booking_date") + " AS period, " +
              "DATEDIFF(D.check_out_date, D.check_in_date) * D.price * 0.2 AS amount " +
              "FROM booking B JOIN booking_detail D ON B.booking_id = D.booking_id " +
              "UNION ALL " +
              "SELECT " + formatSql.replace(dateColumn, "D.check_in_date") + " AS period, " +
              "DATEDIFF(D.check_out_date, D.check_in_date) * D.price * 0.8 AS amount " +
              "FROM booking B JOIN booking_detail D ON B.booking_id = D.booking_id WHERE room_status='check-out' " +
              ") AS combined GROUP BY period ORDER BY period";
        break;
    case "laundry":
        sql = "SELECT " + formatSql + " AS period, SUM(price*quantity) AS total " +
              "FROM laundary_service_order E JOIN laundary_service_order_detail F ON E.order_id = F.order_id " +
              "GROUP BY period ORDER BY period";
        break;
    case "hall":
    sql = "SELECT period, SUM(amount) AS total FROM (" +
          // 20% from booking date
          "SELECT " + formatSql.replace(dateColumn, "HB.book_date") + " AS period, " +
          "TIMESTAMPDIFF(HOUR, HBD.start_time, HBD.end_time) * HBD.price * 0.2 AS amount " +
          "FROM hall_booked_detail HBD " +
          "JOIN hall_book HB ON HBD.hall_booked_id = HB.hall_booked_id " +

          "UNION ALL " +

          // 80% from event date (check_out only)
          "SELECT " + formatSql.replace(dateColumn, "HBD.want_date") + " AS period, " +
          "TIMESTAMPDIFF(HOUR, HBD.start_time, HBD.end_time) * HBD.price * 0.8 AS amount " +
          "FROM hall_booked_detail HBD " +
          "JOIN hall_book HB ON HBD.hall_booked_id = HB.hall_booked_id " +
          "WHERE HBD.booking_status = 'check-out' " +
          ") AS combined GROUP BY period ORDER BY period";
    break;

}


    // Now execute SQL and load chart as before...


    ps = con.prepareStatement(sql);
    rs = ps.executeQuery();
    Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

    //XYChart.Series<String, Number> series = new XYChart.Series<>();
    //series.setName(chartType.substring(0, 1).toUpperCase() + chartType.substring(1)); // Capitalize

    while (rs.next()) {
        String period = rs.getString("period");
        double total = rs.getDouble("total");
        seriesMap.putIfAbsent(period, new XYChart.Series<>());
        XYChart.Series<String, Number> series = seriesMap.get(period);
        series.setName(period);
        series.getData().add(new XYChart.Data<>(period, total));
    }

    // Add all series to bar chart
    barChart.getData().addAll(seriesMap.values());

   // barChart.getData().add(series);
    xAxis.setLabel(timeRange + " Period");
    yAxis.setLabel("Total Sales");
    barChart.setTitle("Sales Overview by " + chartType);
}

     @FXML
void handleFoodChart(ActionEvent event) {
    currentChartType = "food";
    reloadChart();
    btnfood.getParent().requestFocus();
}


    @FXML
    void handleLaundryChart(ActionEvent event) {
        currentChartType = "laundry"; 
        reloadChart();
        btnLaundry.getParent().requestFocus();
    }

    @FXML
    void handleRoomChart(ActionEvent event) {
          currentChartType = "room"; 
        reloadChart();
          btnRoom.getParent().requestFocus();
    }
      @FXML
    void handlehallChart(ActionEvent event) {
        currentChartType = "hall";
        reloadChart();
        btnhall.getParent().requestFocus();
    }

    
    @FXML
    void handleTimeRange(ActionEvent event) {
        currentTimeRange = TimeRangeCombo.getValue();
    try {
        loadChartData(currentChartType, currentTimeRange);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    }

    
    private void loadPieChartData() {
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    try {

        double foodTotal = getTotalFromQuery("SELECT SUM(price*quantity)AS Amount FROM food_order_detail");
        double laundryTotal = getTotalFromQuery("SELECT SUM(price*quantity) FROM  laundary_service_order_detail");
        double roomTotal = getTotalFromQuery("""
                SELECT SUM(amount) AS total FROM (
                SELECT DATEDIFF(D.check_out_date, D.check_in_date) * D.price * 0.2 AS amount
                FROM booking B
                JOIN booking_detail D ON B.booking_id = D.booking_id
                UNION ALL
                SELECT DATEDIFF(D.check_out_date, D.check_in_date) * D.price * 0.8 AS amount
                FROM booking B
                JOIN booking_detail D ON B.booking_id = D.booking_id WHERE room_status='check-out'
                ) AS combined
                 """);
         double hallTotal = getTotalFromQuery("""
                SELECT SUM(amount) AS total FROM (
               SELECT TIMESTAMPDIFF(HOUR, HBD.start_time, HBD.end_time) * HBD.price * 0.2 AS amount
                FROM hall_booked_detail HBD
               JOIN hall_book HB ON HBD.hall_booked_id = HB.hall_booked_id

               UNION ALL

                 SELECT TIMESTAMPDIFF(HOUR, HBD.start_time, HBD.end_time) * HBD.price * 0.8 AS amount
                 FROM hall_booked_detail HBD
                 JOIN hall_book HB ON HBD.hall_booked_id = HB.hall_booked_id
                 WHERE HBD.booking_status = 'check-out'
        ) AS combined
    """);
        double grandTotal = foodTotal + laundryTotal + roomTotal +hallTotal;
        
        System.out.println("Food: " + foodTotal);
        System.out.println("Laundry: " + laundryTotal);
        System.out.println("Room: " + roomTotal);
        System.out.println("Hall: " + hallTotal);
        System.out.println("Grand Total: " + grandTotal);

       if (grandTotal > 0) {
            pieChartData.add(new PieChart.Data(String.format("Food (%.1f%%)", (foodTotal / grandTotal) * 100), foodTotal));
            pieChartData.add(new PieChart.Data(String.format("Laundry (%.1f%%)", (laundryTotal / grandTotal) * 100), laundryTotal));
            pieChartData.add(new PieChart.Data(String.format("Room (%.1f%%)", (roomTotal / grandTotal) * 100), roomTotal));
            pieChartData.add(new PieChart.Data(String.format("hall (%.1f%%)", (hallTotal / grandTotal) * 100), hallTotal));

        } else {
            // In case of zero total, fallback labels
            pieChartData.add(new PieChart.Data("Food", 0));
            pieChartData.add(new PieChart.Data("Laundry", 0));
            pieChartData.add(new PieChart.Data("Room", 0));
            pieChartData.add(new PieChart.Data("hall", 0));
        }
       // pieChartData.add(new PieChart.Data("Hall", hallTotal));
   
     p_chart.setLabelsVisible(true);
        p_chart.setData(pieChartData);
        p_chart.setTitle("Total Income Distribution");
        
        for (PieChart.Data data : pieChartData) {
            Tooltip tooltip = new Tooltip(data.getName() + ": " + data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);
        }
       p_chart.setLegendVisible(false);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    private void loadPieChartData2() {
    ObservableList<PieChart.Data> p_ChartData = FXCollections.observableArrayList();

    try {
        double burmesetotal = getTotalFromQuery("SELECT SUM(price * quantity) FROM food X JOIN food_order_detail Y ON X.food_id = Y.food_id WHERE X.food_type_id = 1;");
        double chinese_total = getTotalFromQuery("SELECT SUM(price * quantity) FROM food X JOIN food_order_detail Y ON X.food_id = Y.food_id WHERE X.food_type_id = 2;");
        double thai_total = getTotalFromQuery("SELECT SUM(price * quantity) FROM food X JOIN food_order_detail Y ON X.food_id = Y.food_id WHERE X.food_type_id = 3;");
        double shan_total = getTotalFromQuery("SELECT SUM(price * quantity) FROM food X JOIN food_order_detail Y ON X.food_id = Y.food_id WHERE X.food_type_id = 4;");
        double korea_total = getTotalFromQuery("SELECT SUM(price * quantity) FROM food X JOIN food_order_detail Y ON X.food_id = Y.food_id WHERE X.food_type_id = 5;");

        double grandTotal = burmesetotal + chinese_total + thai_total+shan_total+korea_total ;

       if (grandTotal > 0) {
            p_ChartData.add(new PieChart.Data(String.format("Burmese Food (%.1f%%)", (burmesetotal / grandTotal) * 100), burmesetotal));
            p_ChartData.add(new PieChart.Data(String.format("Chiese Food (%.1f%%)", (chinese_total / grandTotal) * 100), chinese_total));
            p_ChartData.add(new PieChart.Data(String.format("Thai Food (%.1f%%)", (thai_total / grandTotal) * 100), thai_total));
            p_ChartData.add(new PieChart.Data(String.format("Shan Food (%.1f%%)", (shan_total / grandTotal) * 100), shan_total));
            p_ChartData.add(new PieChart.Data(String.format("Korean Food (%.1f%%)", (korea_total / grandTotal) * 100), korea_total));

        } else {
            // In case of zero total, fallback labels
            p_ChartData.add(new PieChart.Data("Burmese Food", 0));
            p_ChartData.add(new PieChart.Data("Chiese Food", 0));
            p_ChartData.add(new PieChart.Data("Thai Food", 0));
            p_ChartData.add(new PieChart.Data("Shan Food", 0));
            p_ChartData.add(new PieChart.Data("Korean Food", 0));

        }
       // pieChartData.add(new PieChart.Data("Hall", hallTotal));

        pie_chart2.setData(p_ChartData);
        pie_chart2.setTitle("Food Type Distribution");
        
        for (PieChart.Data data : p_ChartData) {
            Tooltip tooltip = new Tooltip(data.getName() + ": " + data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);
        }
        //p_chart.setLegendVisible(false);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
// Helper method to get SUM from a query
private double getTotalFromQuery(String sql) throws SQLException {
    ps = con.prepareStatement(sql);
    rs = ps.executeQuery();
    if (rs.next()) {
        return rs.getDouble(1);
    }
    return 0;
}


@FXML
private void handleTimeRangeChange() {
    currentTimeRange = TimeRangeCombo.getValue();
    try {
        loadChartData(currentChartType, currentTimeRange);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void reloadChart() {
    try {
        loadChartData(currentChartType, currentTimeRange);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
           
}
       
  
    
  
