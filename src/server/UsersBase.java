package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UsersBase extends AnchorPane {

    protected final Label label;
    protected final Label label0;
    protected final Label label1;
    protected final Button startButton;
    protected final ScrollPane offlineList;
    protected final AnchorPane anchorPane;
    protected final ImageView userImage;
    protected final Label userNameLabel;
    protected final Button stopButton;
    protected final Button dashboardButton;
    
    ServerSocket serverSocket;
    Boolean isServerRunning;

    public UsersBase() {

        label = new Label();
        label0 = new Label();
        label1 = new Label();
        startButton = new Button();
        offlineList = new ScrollPane();
        anchorPane = new AnchorPane();
        userImage = new ImageView();
        userNameLabel = new Label();
        stopButton = new Button();
        dashboardButton = new Button();

        setId("AnchorPane");
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        label.setLayoutX(59.0);
        label.setLayoutY(40.0);
        label.setText("Offline");

        label0.setLayoutX(272.0);
        label0.setLayoutY(40.0);
        label0.setText("In Game");

        label1.setLayoutX(474.0);
        label1.setLayoutY(40.0);
        label1.setText("Online");

        startButton.setLayoutX(14.0);
        startButton.setLayoutY(349.0);
        startButton.setMnemonicParsing(false);
        startButton.setPrefHeight(31.0);
        startButton.setPrefWidth(161.0);
        startButton.setStyle("-fx-background-color: green;");
        startButton.setText("Start");
        startButton.setTextFill(javafx.scene.paint.Color.WHITE);

        offlineList.setLayoutX(14.0);
        offlineList.setLayoutY(107.0);
        offlineList.setPrefHeight(200.0);
        offlineList.setPrefWidth(161.0);

        anchorPane.setMinHeight(0.0);
        anchorPane.setMinWidth(0.0);
        anchorPane.setPrefHeight(243.0);
        anchorPane.setPrefWidth(165.0);

        userImage.setFitHeight(53.0);
        userImage.setFitWidth(51.0);
        userImage.setLayoutY(11.0);
        userImage.setPickOnBounds(true);
        userImage.setPreserveRatio(true);

        userNameLabel.setLayoutX(65.0);
        userNameLabel.setLayoutY(16.0);
        userNameLabel.setText("Label");
        offlineList.setContent(anchorPane);

        stopButton.setLayoutX(220.0);
        stopButton.setLayoutY(349.0);
        stopButton.setMnemonicParsing(false);
        stopButton.setPrefHeight(31.0);
        stopButton.setPrefWidth(161.0);
        stopButton.setStyle("-fx-background-color: #c90c0c;");
        stopButton.setText("Stop");
        stopButton.setTextFill(javafx.scene.paint.Color.WHITE);

        dashboardButton.setLayoutX(416.0);
        dashboardButton.setLayoutY(349.0);
        dashboardButton.setMnemonicParsing(false);
        dashboardButton.setPrefHeight(31.0);
        dashboardButton.setPrefWidth(161.0);
        dashboardButton.setStyle("-fx-border-color: green; -fx-background-color: white;");
        dashboardButton.setText("Dashboard");
        dashboardButton.setTextFill(javafx.scene.paint.Color.LIME);

        getChildren().add(label);
        getChildren().add(label0);
        getChildren().add(label1);
        getChildren().add(startButton);
        anchorPane.getChildren().add(userImage);
        anchorPane.getChildren().add(userNameLabel);
        getChildren().add(offlineList);
        getChildren().add(stopButton);
        getChildren().add(dashboardButton);
        
        // Server Socket
        try {
            serverSocket = new ServerSocket(5007);
        } catch (IOException ex) {
            Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        isServerRunning = true;
        
        // Start Background Thread
        Thread thread = new Thread(() -> {
            while(isServerRunning) {
                try {
                    Socket s = serverSocket.accept();
                    new ServerHandler(s, isServerRunning);
                } catch (IOException ex) {
                    Logger.getLogger(UsersBase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
        
        // Start Button on click
        startButton.setOnAction((ActionEvent event) -> {
            isServerRunning = true;
        });
            
        // Stop Button on click
        stopButton.setOnAction((ActionEvent event) -> {
            isServerRunning= false;
        });
        
        // Dashboard Button on click
        dashboardButton.setOnAction((ActionEvent event) -> {
            // todo: navigate to dashboard screen
        });
    }
}
