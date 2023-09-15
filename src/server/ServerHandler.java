package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.models.User;
import server.utils.Constants;

public class ServerHandler extends Thread {

    DataInputStream dataInputStream;
    PrintStream prrintStream;
    String useNameG;
    Boolean isServerRunning;
    DataAccess dataAccess = new DataAccess();
    String typeOfOperation;
    BufferedReader bufferedReader;
    String result = "";
    String userName = "";
    String opUserName = "";

    public ServerHandler(Socket socket, Boolean isServerRunning) {
        this.isServerRunning = isServerRunning;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            prrintStream = new PrintStream(socket.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
                typeOfOperation = bufferedReader.readLine();
                System.out.println(typeOfOperation);
                if (isServerRunning) {

                    prrintStream.println(Constants.SERVER_RUNNING);

                    if (typeOfOperation != null) {
                        if (typeOfOperation.equals(Constants.LOGIN)) {
                            loginChecker();
                        } else if (typeOfOperation.equals(Constants.REGISTER)) {
                            RegisterChecker();
                        } else if (typeOfOperation.equals(Constants.GET_USERS)) {
                            getUsers();
                        } else if (typeOfOperation.equals(Constants.SEND_REQUEST_TO_PLAY)) {
                            playWithOther();
                        }
                    } else {
                        System.out.println("null type");
                    }

                } else {
                    prrintStream.println(Constants.SERVER_STOP);
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void loginChecker() throws Exception {
        String userName = bufferedReader.readLine();
        String password = bufferedReader.readLine();
        prrintStream.println(userName); // Return UserName
        prrintStream.println(Constants.LOGIN); // Return Log In
        User user = new User(userName, password, server.models.State.ONLINE);
        if (dataAccess.logIn(user)) {
            prrintStream.println(Constants.VALID_LOGIN);
            useNameG = userName;
        } else {
            prrintStream.println(Constants.NOT_VALID_LOGIN);
        }

    }

    void RegisterChecker() throws Exception {

        String userName = bufferedReader.readLine();
        String password = bufferedReader.readLine();

        prrintStream.println(userName);
        prrintStream.println(Constants.REGISTER);
        User user = new User(userName, password, server.models.State.ONLINE);
        if (dataAccess.signUp(user)) {
            prrintStream.println(Constants.VALID_REGISTER);
            useNameG = userName;
        } else {
            prrintStream.println(Constants.NOT_VALID_REGISTER);
        }

    }

    void getUsers() throws Exception {

        // Client wants to get all users
        String userName1 = bufferedReader.readLine();
        prrintStream.println(userName1); // Return UserName
        prrintStream.println(Constants.GET_USERS); // Return GetUsers

        for (ServerHandler object : UsersBase.vector) {
            System.out.println(object.useNameG);
        }

        // Return Users
        ArrayList<String> users = dataAccess.getOnlineUsers();
        int noOfUsers = users.size();
        prrintStream.println(noOfUsers);
        for (int i = 0; i < noOfUsers; i++) {
            prrintStream.println(users.get(i));
        }

    }

    void playWithOther() throws IOException {

        userName = bufferedReader.readLine();
        opUserName = bufferedReader.readLine();

        for (ServerHandler object : UsersBase.vector) {

            if (object.useNameG.equals(opUserName)) {
                object.prrintStream.println(userName);
                result = object.bufferedReader.readLine();
                System.out.println(result);
            }

            if (object.useNameG.equals(userName)) {
                object.prrintStream.println(result);
            }

        }
    }
}
