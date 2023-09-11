package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.models.User;

public class ServerHandler extends Thread {

    private Socket socket;
    private DataInputStream dis;
    private PrintStream ps;
    private Boolean isServerRunning;

    public ServerHandler(Socket socket, Boolean isServerRunning) {
        this.socket = socket;
        this.isServerRunning = isServerRunning;
        try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        start();
    }

    
    public void run() {
        DataAccess dataAccess = new DataAccess();

        while (isServerRunning) {
            String str;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(dis));
                str = br.readLine();
                if (isServerRunning) {
                    if (str.equals("0")) {
                        // Log in
                        String userName = br.readLine();
                        String password = br.readLine();
                        try {
                            ps.println(userName); // Return UserName
                            ps.println("0"); // Return Log In
                            User user = new User(userName, password, server.models.State.ONLINE);
                            if (dataAccess.logIn(user)) {
                                // Log In Complete
                                ps.println("1");
                                // Send the connected user to UsersBase
                                sendConnectedUserToUsersBase(user);
                            } else {
                                // Wrong UserName or Password
                                ps.println("0");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (str.equals("1")) {
                        // Sign up
                        String userName = br.readLine();
                        String password = br.readLine();
                        try {
                            ps.println(userName); // Return UserName
                            ps.println("1"); // Return Sign Up
                            User user = new User(userName, password, server.models.State.ONLINE);
                            if (dataAccess.signUp(user)) {
                                // Sign Up Complete
                                ps.println("1");
                                // Send the connected user to UsersBase
                                sendConnectedUserToUsersBase(user);
                            } else {
                                // Sign Up Failed
                                ps.println("0");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    // Server Stopped
                    ps.println("-1");
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Send the connected user to the UsersBase class
    private void sendConnectedUserToUsersBase(User user) {
        if (UsersBase.getInstance() != null) {
            UsersBase.getInstance().addConnectedUser(user);
        }
    }
}