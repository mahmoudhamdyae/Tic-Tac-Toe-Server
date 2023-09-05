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

public class ServerHandler extends Thread {
    
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dis ;
    PrintStream ps;
    Boolean isServerRunning;
    
    static Vector<ServerHandler> clientsVector = new Vector<ServerHandler>();
    
    public ServerHandler(Socket socket, Boolean isServerRunning) {
        this.isServerRunning = isServerRunning;
        try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientsVector.add(this);
        start();
    }
    
    public void run() {
        DataAccess dataAccess = new DataAccess();
        
        while(true) {
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
                            if (dataAccess.logIn(new User(userName, password, server.models.State.ONLINE))) {
                                // Log In Complete
                                ps.println("1");
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
                            if (dataAccess.signUp(new User(userName, password, server.models.State.ONLINE))) {
                                // SIgn Up Complete
                                ps.println("1");
                            } else {
                                // Sign Up Failed
                                ps.println("0");
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
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
    
    void sendMessageToAll(String msg) {
        for(int i = 0 ; i < clientsVector.size(); i++) {
            clientsVector.get(i).ps.println(msg);
        }
    }
}
