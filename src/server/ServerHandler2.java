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
import server.models.State;

public class ServerHandler2 extends Thread {
    
    DataInputStream dis ;
    PrintStream ps;
    Boolean isServerRunning;
            DataAccess dataAccess = new DataAccess();

    public ServerHandler2(Socket socket, Boolean isServerRunning) {
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
        
        while(true) {
            String str;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(dis));
                  str = br.readLine();
                if (isServerRunning) {
                 ps.println(Constants.SERVER_RUNNING); // Server Running
                 if(str!=null){
                    if (str.equals(Constants.LOGIN)) {
                        // Log in
                        String userName = br.readLine();
                        String password = br.readLine();
                        try {
                            ps.println(userName); // Return UserName
                            ps.println(Constants.LOGIN); // Return Log In
                            User user = new User(userName, password, server.models.State.ONLINE);
                            if (dataAccess.logIn(user)) {
                                // Log In Complete
                                ps.println(Constants.VALID_LOGIN);
                                // Send the connected user to UsersBase
//                                sendConnectedUserToUsersBase(user);
                            } else {
                                // Wrong UserName or Password
                                ps.println(Constants.NOT_VALID_LOGIN);
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    } else if (str.equals(Constants.REGISTER)) {
                        // Sign up
                        String userName = br.readLine();
                        String password = br.readLine();
                        try {
                            ps.println(userName); // Return UserName
                            ps.println(Constants.REGISTER); // Return Sign Up
                            User user = new User(userName, password, server.models.State.ONLINE);
                            if (dataAccess.signUp(user)) {
                                // Sign Up Complete
                                ps.println(Constants.VALID_REGISTER);
                                // Send the connected user to UsersBase
//                                sendConnectedUserToUsersBase(user);
                            } else {
                                // Sign Up Failed
                                ps.println(Constants.NOT_VALID_REGISTER);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    else if (str.equals(Constants.GET_USERS)) {
                        // Client wants to get all users
                        String userName1 = br.readLine();
                        ps.println(userName1); // Return UserName
                        ps.println(Constants.GET_USERS); // Return GetUsers
                        
                        // Return Users
                        ArrayList<String> users = dataAccess.getOnlineUsers();
                        int noOfUsers = users.size();
                        ps.println(noOfUsers);
                        for (int i = 0; i < noOfUsers; i++) {
                            ps.println(users.get(i));
                        }
                        
//                    } else if (str.equals(Constants.PLAY_WITH_USER)) {
//                        // Client clicked on one of the users to play with
//                        String userName1 = br.readLine();
//                        String userName2 = br.readLine();
//                        
//                        ps.println(userName2); // Return UserName2
//                        ps.println(Constants.PLAY_WITH_USER); // Return PLAY_WITH_USER
                        
                    } else if (str.equals(Constants.PLAY_WITH_USER_RESPONSE)) {
                        String userName = dis.readLine();
                        String userResponse = dis.readLine();
                        ps.println(userName);
                        ps.println(Constants.PLAY_WITH_USER_RESPONSE);
                        if (userResponse.equals(Constants.USER_ACCEPTED)) {
                            ps.println(Constants.USER_ACCEPTED);
                            
                            System.out.println("User Accepted");
                            
                        } else {
                            ps.println(Constants.USER_REJECTED);
                            
                            System.out.println("User Accepted");
                            
                        }
                    } else if (str.equals(Constants.PLAY_ONLINE)) {
                        String userName = br.readLine();
                        String x = br.readLine();
                        String y = br.readLine();
                        System.out.println("USERNAME: " + userName);
                        System.out.println("x: " + x);
                        System.out.println("y: " + y);
                        
                        ps.println("Guest-X");
                        ps.println(1);
                        ps.println(1);
                    } else if (str.equals(Constants.CHANGE_STATE)) {
                        String userName = br.readLine();
                        String state = br.readLine();
                        if (state.equals("offline")) {
                            dataAccess.UpdateState(userName, 1);
                        } else if (state.equals("in_game")) {
                            dataAccess.UpdateState(userName, 2);
                        }
                    } else if (str.equals(Constants.CHANGE_SCORE)) {
                        String userName = br.readLine();
                        String gameState = br.readLine();
//                        dataAccess.UpdateState(userName, 0);
                        switch(gameState) {
                            case "win":
                                dataAccess.UpdateScore(userName, 0);
                                break;
                            case "lose":
                                dataAccess.UpdateScore(userName, 1);
                                break;
                            case "draw":
                                dataAccess.UpdateScore(userName, 2);
                                break;
                        }
                    }
                }
                } else {
                    // Server Stopped
                    ps.println(Constants.SERVER_STOP);
}
            } catch (IOException ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Send the connected user to the UsersBase class
//    private void sendConnectedUserToUsersBase(User user) {
//        if (UsersBase.getInstance() != null) {
//            UsersBase.getInstance().addConnectedUser(user);
//        }
//    }
}
