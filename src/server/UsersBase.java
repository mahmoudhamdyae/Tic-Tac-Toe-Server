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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.models.User;

public class UsersBase extends AnchorPane {

    protected final Label label;
    protected final Label label0;
    protected final Label label1;
    protected final Button startButton;
    protected final Button stopButton;
    protected final Button dashboardButton;
    protected final ListView<User> offlineList;
    protected final ListView<User> inGameList;
    protected final ListView<User> onlineList;
    ServerSocket serverSocket;
    private AtomicBoolean isServerRunning = new AtomicBoolean(false); // Initialize as false

    // Create observable lists for each user state
    private ObservableList<User> offlineUsers = FXCollections.observableArrayList();
    private ObservableList<User> inGameUsers = FXCollections.observableArrayList();
    private ObservableList<User> onlineUsers = FXCollections.observableArrayList();

    private static UsersBase instance; // Static instance variable

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

        // Stop Button on click
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
        stopButton.setDisable(true);
        

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

        // Server Socket
        try {
            serverSocket = new ServerSocket(5007);
        } catch (IOException ex) {
            Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Start Background Thread
        Thread thread = new Thread(() -> {
            while (isServerRunning.get()) {
                try {
                    Socket s = serverSocket.accept();
                    ServerHandler handler = new ServerHandler(s, isServerRunning);

                    // When a new user connects, add them to the appropriate list
                    // The user information is available in the ServerHandler class
                    addConnectedUser(handler.getUser());
                } catch (IOException ex) {
                    Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();

        // Start Button on click
        startButton.setOnAction((ActionEvent event) -> {
            if (!isServerRunning.get()) {
                // Only start if it's not already running
                isServerRunning.set(true);
                startServer();

                // Disable the start button once it's clicked
                startButton.setDisable(true);

                // Enable the stop button when the server is started
                stopButton.setDisable(false);
            }
        });

        // Stop Button on click
        stopButton.setOnAction((ActionEvent event) -> {
            if (isServerRunning.get()) {
                // Only stop if it's currently running
                isServerRunning.set(false);
                stopServer();

                // Enable the start button when the server is stopped
                startButton.setDisable(false);

                // Disable the stop button once it's clicked
                stopButton.setDisable(true);
            }
        });

        // Dashboard Button on click
        dashboardButton.setOnAction((ActionEvent event) -> {
        });
    }

    private void startServer() {
        // Start the server and handle connections
        isServerRunning.set(true); // Set to true before entering the loop
        Thread thread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5008); // Change the port number here
                System.out.println("Server started");
                while (isServerRunning.get()) {
                    Socket s = serverSocket.accept();
                    ServerHandler handler = new ServerHandler(s, isServerRunning);
                    addConnectedUser(handler.getUser());
                }
                serverSocket.close();
                System.out.println("Server stopped");
            } catch (IOException ex) {
                Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        thread.start();
    }

    private void stopServer() {
    // Stop the server by setting the flag to false
    isServerRunning.set(false);

    try {
        // Close the serverSocket to stop accepting new connections
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            serverSocket = null; // Set the serverSocket to null after closing
            System.out.println("Server stopped");
        }

        
    } catch (IOException ex) {
        Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    //Singleton Pattern
    // Static method to get an instance of UsersBase
    public static UsersBase getInstance() {
        if (instance == null) {
            instance = new UsersBase();
        }
        return instance;
    }

    // Method to add a connected user to the appropriate list
    void addConnectedUser(User user) {
        switch (user.getState()) {
            case OFFLINE:
                offlineUsers.add(user);
                break;
            case IN_GAME:
                inGameUsers.add(user);
                break;
            case ONLINE:
                onlineUsers.add(user);
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
