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
    
    DataInputStream dataInputStream ;
    PrintStream prrintStream;
    Boolean isServerRunning;
     DataAccess dataAccess = new DataAccess();
            String typeOfOperation;
                 BufferedReader bufferedReader;

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
    
    boolean IsServerRunn(){
    
    return false;
    }
    
 
    
    @Override
    public void run() {
        while(true) {
            try {
           bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
                 typeOfOperation = bufferedReader.readLine();

                if (isServerRunning) {

                 prrintStream.println(Constants.SERVER_RUNNING); // Server Running
             
               
                 
                 if(typeOfOperation!=null){

                     if (typeOfOperation.equals(Constants.LOGIN)) { loginChecker();  }
                
                     else if (typeOfOperation.equals(Constants.REGISTER)) {RegisterChecker();}
                
                     else if (typeOfOperation.equals(Constants.GET_USERS)) {getUsers();} 
     
                
                     
                     else if (typeOfOperation.equals(Constants.SEBD_REQUEST_TO_PLAY)) {
                       
                        String userName = bufferedReader.readLine();
                        String opUserName = bufferedReader.readLine();
                           bufferedReader.readLine();
                        if(isServerRunning){
                                                prrintStream.println(Constants.SERVER_RUNNING);
                                                bufferedReader.readLine();
                                                bufferedReader.readLine();
                                                
                                                                               prrintStream.println(opUserName);
                            prrintStream.println(userName); // Return UserName2
                        prrintStream.println(Constants.SEBD_REQUEST_TO_PLAY); // Return PLAY_WITH_USER
                            
                        


                        }else{
                        
                  prrintStream.println(Constants.SERVER_STOP);
                        }

                        
                    } 
                     
                     
                     
                     
                     
                     
                     
                     
                     else if (typeOfOperation.equals(Constants.PLAY_WITH_USER_RESPONSE)) {
                        String userName = dataInputStream.readLine();
                        String userResponse = dataInputStream.readLine();
                    bufferedReader.readLine();
                        if(isServerRunning){
                                                prrintStream.println(Constants.SERVER_RUNNING);
                                                bufferedReader.readLine();
                                                bufferedReader.readLine();
                                                
                                                                        prrintStream.println(userName);

                                                                       prrintStream.println(Constants.PLAY_WITH_USER_RESPONSE);
                                                                       
                                                                             if (userResponse.equals(Constants.USER_ACCEPTED)) {
                            prrintStream.println(Constants.USER_ACCEPTED);
                            
                            System.out.println("User Accepted");
                            
                        } else {
                            prrintStream.println(Constants.USER_REJECTED);
                            
                            System.out.println("User Accepted");
                            
                        }


                        }else{
                        
                  prrintStream.println(Constants.SERVER_STOP);
                        }                   
                  
                    }
                }
                 
                 
                } else {
                    // Server Stopped
                    prrintStream.println(Constants.SERVER_STOP);
}
            } catch (IOException ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

    
    
    
    
            void loginChecker(){
        try{

                    String userName = bufferedReader.readLine();
   String password = bufferedReader.readLine();
   try {
       prrintStream.println(userName); // Return UserName
       prrintStream.println(Constants.LOGIN); // Return Log In
       User user = new User(userName, password, server.models.State.ONLINE);
       if (dataAccess.logIn(user)) {
           // Log In Complete
           prrintStream.println(Constants.VALID_LOGIN);
           // Send the connected user to UsersBase
       } else {
           // Wrong UserName or Password
           prrintStream.println(Constants.NOT_VALID_LOGIN);
       }
   } catch (Exception ex) {
       Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
   }

        } catch (IOException ex) {
Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
} 

        }
          void RegisterChecker(){
        try{

            // Sign up
   String userName = bufferedReader.readLine();
   String password = bufferedReader.readLine();
   try {
       prrintStream.println(userName); // Return UserName
       prrintStream.println(Constants.REGISTER); // Return Sign Up
       User user = new User(userName, password, server.models.State.ONLINE);
       if (dataAccess.signUp(user)) {
           // Sign Up Complete
           prrintStream.println(Constants.VALID_REGISTER);
           // Send the connected user to UsersBase
       } else {
           // Sign Up Failed
           prrintStream.println(Constants.NOT_VALID_REGISTER);
       }
   } catch (SQLException ex) {
       Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
   } catch (Exception ex) {
       Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
   }

        } catch (IOException ex) {
Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
} 

        }
          void getUsers(){
        try{

   // Client wants to get all users
   String userName1 = bufferedReader.readLine();
   prrintStream.println(userName1); // Return UserName
   prrintStream.println(Constants.GET_USERS); // Return GetUsers

   // Return Users
   ArrayList<String> users = dataAccess.getOnlineUsers();
   int noOfUsers = users.size();
   prrintStream.println(noOfUsers);
   for (int i = 0; i < noOfUsers; i++) {
       prrintStream.println(users.get(i));
   }

        } catch (IOException ex) {
Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
} catch (Exception ex) { 
Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
} 

        }
}
