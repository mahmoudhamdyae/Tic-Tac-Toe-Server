package server;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import server.models.User;

public class DashboardScreen extends AnchorPane {
    
    ObservableList<PieChart.Data> pieChartData;
    Label noUsersLabel;
    
    public DashboardScreen() {
        
        setPrefHeight(400.0);
        setPrefWidth(500.0);
        
        // Get Users from Database
        DataAccess dataAccess = new DataAccess();
        ArrayList<User> users = null;
        try {
            users = dataAccess.getUsers();
        } catch (Exception ex) {
            Logger.getLogger(DashboardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Preparing ObservbleList object 
        int noOfUsers = users.size();
        int noOfOnline = 0, noOfOffline = 0, noOfInGame = 0;
        for (int i = 0; i < noOfUsers; i++) {
            switch(users.get(i).getState()) {
                case ONLINE:
                    noOfOnline++;
                    break;
                case OFFLINE:
                    noOfOffline++;
                    break;
                case IN_GAME:
                    noOfInGame++;
                    break;
            }
        }

        pieChartData = FXCollections.observableArrayList( 
                new PieChart.Data("Online", noOfOnline), 
                new PieChart.Data("Offline", noOfOffline), 
                new PieChart.Data("In Game", noOfInGame)
        );

        // Creating a Pie chart 
        PieChart pieChart = new PieChart(pieChartData);
        
        // Setting the title of the Pie chart 
        pieChart.setTitle("Tic Tac Toe Users");
        
        // Setting the direction to arrange the data 
        pieChart.setClockwise(true);
        
        // Setting the length of the label line 
        pieChart.setLabelLineLength(50);
        
        // Setting the labels of the pie chart visible  
        pieChart.setLabelsVisible(true);
        
        // Setting the start angle of the pie chart 
        pieChart.setStartAngle(180); 
        
        if(noOfUsers == 0) {
            noUsersLabel = new Label();
            noUsersLabel.setFont(new Font(18.0));
            noUsersLabel.setLayoutX(140.0);
            noUsersLabel.setLayoutY(175.0);
            noUsersLabel.setText("There is no user in Database");
            getChildren().add(noUsersLabel);
        } else {
            getChildren().add(pieChart);
        }
    }
}
