package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.models.User;
import server.utils.Constants;

public class ServerHandler extends Thread {
    
    DataInputStream dis ;
    PrintStream ps;
    Boolean isServerRunning;
            DataAccess dataAccess = new DataAccess();

    public ServerHandler(Socket socket, Boolean isServerRunning) {
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
                            boolean ss=dataAccess.logIn(new User(userName, password, server.models.State.ONLINE));
                            System.out.println(ss+"");
                            if (ss) {
                                // Log In Complete
                                ps.println(Constants.VALID_LOGIN);
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
                            if (dataAccess.signUp(new User(userName, password, server.models.State.ONLINE))) {
                                // Sign Up Complete
                                ps.println(Constants.VALID_REGISTER);
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
                }
                } else {
                    // Server Stopped
                    ps.println(Constants.SERVER_STOP);
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
