package server;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.models.User;
import server.utils.Constants;

public class UsersBase extends AnchorPane {

    protected final Label label;
    protected final Label label0;
    protected final Label label1;
    protected final Button startButton;
    protected final Button stopButton;
    protected final Button dashboardButton;
    protected final ListView<String> offlineList;
    protected final ListView<String> inGameList;
    protected final ListView<String> onlineList;
    ServerSocket serverSocket;
    private Boolean isServerRunning = true;
    // Create observable lists for each user state
    private ObservableList<String> offlineUsers = FXCollections.observableArrayList();
    private ObservableList<String> inGameUsers = FXCollections.observableArrayList();
    private ObservableList<String> onlineUsers = FXCollections.observableArrayList();
    static  Vector<ServerHandler> vector = new Vector<>();;
    private static UsersBase instance; 

    public UsersBase() {
        label = new Label();
        label0 = new Label();
        label1 = new Label();
        startButton = new Button();
        stopButton = new Button();
        dashboardButton = new Button();
        offlineList = new ListView<>();
        inGameList = new ListView<>();
        onlineList = new ListView<>();
    
        setId("AnchorPane");
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        label.setLayoutX(64.0);
        label.setLayoutY(27.0);
        label.setText("Offline");
        label.setFont(new Font(18.0));

        label0.setLayoutX(266.0);
        label0.setLayoutY(26.0);
        label0.setText("In Game");
        label0.setFont(new Font(18.0));

        label1.setLayoutX(475.0);
        label1.setLayoutY(27.0);
        label1.setText("Online");
        label1.setFont(new Font(18.0));

        startButton.setLayoutX(14.0);
        startButton.setLayoutY(349.0);
        startButton.setMnemonicParsing(false);
        startButton.setPrefHeight(31.0);
        startButton.setPrefWidth(161.0);
        startButton.setStyle("-fx-background-color: #008000;");
        startButton.setText("Start");
        startButton.setTextFill(javafx.scene.paint.Color.WHITE);
        startButton.setFont(new Font(18.0));

        stopButton.setLayoutX(220.0);
        stopButton.setLayoutY(349.0);
        stopButton.setMnemonicParsing(false);
        stopButton.setPrefHeight(31.0);
        stopButton.setPrefWidth(161.0);
        stopButton.setStyle("-fx-background-color: #c90c0c;");
        stopButton.setText("Stop");
        stopButton.setTextFill(javafx.scene.paint.Color.WHITE);
        stopButton.setFont(new Font(18.0));

        // Initially, the stop button and dashboard button should be disabled
        startButton.setDisable(true);

        dashboardButton.setLayoutX(416.0);
        dashboardButton.setLayoutY(349.0);
        dashboardButton.setMnemonicParsing(false);
        dashboardButton.setPrefHeight(31.0);
        dashboardButton.setPrefWidth(161.0);
        dashboardButton.setStyle("-fx-background-color: #A52A2A");
        dashboardButton.setText("Dashboard");
        dashboardButton.setTextFill(javafx.scene.paint.Color.WHITE);
        dashboardButton.setFont(new Font(18.0));

        offlineList.setLayoutX(31.0);
        offlineList.setLayoutY(58.0);
        offlineList.setPrefHeight(267.0);
        offlineList.setPrefWidth(134.0);

        inGameList.setLayoutX(234.0);
        inGameList.setLayoutY(58.0);
        inGameList.setPrefHeight(267.0);
        inGameList.setPrefWidth(134.0);

        onlineList.setLayoutX(430.0);
        onlineList.setLayoutY(58.0);
        onlineList.setPrefHeight(267.0);
        onlineList.setPrefWidth(134.0);

        getChildren().add(label);
        getChildren().add(label0);
        getChildren().add(label1);
        getChildren().add(startButton);
        getChildren().add(stopButton);
        getChildren().add(dashboardButton);
        getChildren().add(offlineList);
        getChildren().add(inGameList);
        getChildren().add(onlineList);

        // Get Users
        DataAccess dataAccess = new DataAccess();
        try {
            ArrayList<User> users = dataAccess.getUsers();
            for (User user: users) {
                addConnectedUser(user);
            }
        } catch (Exception ex) {
            Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Server Socket
        try {
            serverSocket = new ServerSocket(Constants.PORT);
        } catch (IOException ex) {
            Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Start Background Thread
        Thread thread = new Thread(() -> {
            while (isServerRunning) {
                try {
                    Socket s = serverSocket.accept();

                    ServerHandler handler = new ServerHandler(s, isServerRunning);
//                    vector.add(handler);
//                    System.out.println("vector size" + vector.size()+ " vector name "+ vector.get(vector.size()-1).useNameG);
//                    
//                   
//                    System.out.println("vector" + vector.size());
                } catch (IOException ex) {
                    Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();

        // Start Button on click
        startButton.setOnAction((ActionEvent event) -> {
            if (!isServerRunning) {
                // Only start if it's not already running
                isServerRunning = true;

                // Disable the start button once it's clicked
                startButton.setDisable(true);

                // Enable the stop button when the server is started
                stopButton.setDisable(false);
            }
        });

        // Stop Button on click
        stopButton.setOnAction((ActionEvent event) -> {
            if (isServerRunning) {
                // Only stop if it's currently running
                isServerRunning = false;

                // Enable the start button when the server is stopped
                startButton.setDisable(false);

                // Disable the stop button once it's clicked
                stopButton.setDisable(true);
            }
        });

        // Dashboard Button on click
//        dashboardButton.setOnAction((ActionEvent event) -> {
//        });
    }

    // Singleton Pattern
    // Static method to get an instance of UsersBase
//    public static UsersBase getInstance() {
//        if (instance == null) {
//            instance = new UsersBase();
//        }
//        return instance;
//    }

    // Method to add a connected user to the appropriate list
    void addConnectedUser(User user) {
        switch (user.getState()) {
            case OFFLINE:
                offlineUsers.add(user.getUserName());
                break;
            case IN_GAME:
                inGameUsers.add(user.getUserName());
                break;
            case ONLINE:
                onlineUsers.add(user.getUserName());
                break;
            default:
                // Handle other states as needed
                break;
        }

        // Update the ListView components with the new data
        offlineList.setItems(offlineUsers);
        inGameList.setItems(inGameUsers);
        onlineList.setItems(onlineUsers);
    }
}
