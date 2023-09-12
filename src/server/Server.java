package server;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        UsersBase root = new UsersBase();
        DashboardScreen dashboard = new DashboardScreen();
        
        Scene scene = new Scene(root);
        Scene dahboardScene = new Scene(dashboard);
        
        stage.setScene(scene);
        stage.show();
        
        root.dashboardButton.setOnAction((ActionEvent event) -> {
            stage.setScene(dahboardScene);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
