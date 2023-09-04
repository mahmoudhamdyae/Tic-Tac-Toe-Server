package server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLDocumentBase root = new FXMLDocumentBase();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
