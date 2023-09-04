package server;

import server.models.User;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import server.models.State;

public class FXMLDocumentBase extends AnchorPane {

    protected final Button button;
    protected final Label label;

    public FXMLDocumentBase() {

        button = new Button();
        label = new Label();

        setId("AnchorPane");
        setPrefHeight(200);
        setPrefWidth(320);

        button.setLayoutX(126);
        button.setLayoutY(90);
        button.setText("Click Me!");

        label.setLayoutX(126);
        label.setLayoutY(120);
        label.setMinHeight(16);
        label.setMinWidth(69);

        getChildren().add(button);
        getChildren().add(label);
        
        DataAccess dataAccess = new DataAccess();
        
        // Insert a User
        try {
            dataAccess.createUser(new User("userName1", "password", State.ONLINE));
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Get Users
        try {
            System.out.println("Size: " + dataAccess.getUsers().size());
//            System.out.println("Size: " + dataAccess.getUsers().get(0).getUserName());
//            System.out.println("Size: " + dataAccess.getUsers().get(0).getPassword());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
