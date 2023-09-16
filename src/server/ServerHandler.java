package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    BufferedWriter bufferedWriter ;
    String result = "";
    String userName = "";
    String opUserName = "";
    String opUserNamePlayer;
    String x ;
    String y ;
    boolean state=true;
    public ServerHandler(Socket socket, Boolean isServerRunning) {
        this.isServerRunning = isServerRunning;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            prrintStream = new PrintStream(socket.getOutputStream());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(prrintStream));
              
        } catch (IOException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        start();
    }

    @Override
    public void run() {
        while (state) {
            try {
            bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
            
                typeOfOperation = bufferedReader.readLine();
                System.out.println("type of operation is " +typeOfOperation);
                
                    if (typeOfOperation != null) {
                if (isServerRunning) {

                    prrintStream.println(Constants.SERVER_RUNNING);

                        if (typeOfOperation.equals(Constants.LOGIN)) {
                            loginChecker();
                        } else if (typeOfOperation.equals(Constants.REGISTER)) {
                            RegisterChecker();
                        } else if (typeOfOperation.equals(Constants.GET_USERS)) {
                            getUsers();
                        } else if (typeOfOperation.equals(Constants.SEND_REQUEST_TO_PLAY)) {
                            playWithOther();
                        } else if (typeOfOperation.equals(Constants.PLAY_WITH_USER_RESPONSE)) {
                         
                         String userName = dataInputStream.readLine();
                         useNameG = userName;
                            
                         String userResponse = dataInputStream.readLine();
                        prrintStream.println(userName);
                        prrintStream.println(Constants.PLAY_WITH_USER_RESPONSE);
                        if (userResponse.equals(Constants.USER_ACCEPTED)) {
                            prrintStream.println(Constants.USER_ACCEPTED);
                            
                            System.out.println("User Accepted");
                            
                        } else {
                            prrintStream.println(Constants.USER_REJECTED);
                            
                            System.out.println("User Accepted");
                            
                        }
                    } else if (typeOfOperation.equals(Constants.PLAY_ONLINE)) {
                        
                        System.out.println("E7na hna k type : "+ typeOfOperation);
                        opUserNamePlayer = bufferedReader.readLine();
                        
                        useNameG = userName;
                        
                        x = bufferedReader.readLine();
                        y = bufferedReader.readLine();
                        System.out.println("USERNAME: " + opUserNamePlayer);
                        System.out.println("x: " + x);
                        System.out.println("y: " + y);
                        
//                        UsersBase.vector.get(3).bufferedWriter.write(x);
//                        
//                            System.out.println(UsersBase.vector.get(3).useNameG);
//
//                        UsersBase.vector.get(3).bufferedWriter.newLine();
//
//                        UsersBase.vector.get(3).bufferedWriter.write(y);
//
//                        UsersBase.vector.get(3).bufferedWriter.flush();                        
                        for(ServerHandler object : UsersBase.vector){
                           // System.out.println(object.useNameG+"  userNameG");
                            if(object.useNameG.equals(opUserNamePlayer)){
//                              object.bufferedWriter.write(x);
//                              object.bufferedWriter.newLine();
//                              object.bufferedWriter.write(y);
//                              object.bufferedWriter.flush();
                            prrintStream.println(x);
                            prrintStream.println(y);
                            System.out.println("sending to op name     " + opUserNamePlayer+" x"+ x+" and y"+ y);
                           break;
                           
                        }
                    }
                    }
                   /* else if(typeOfOperation.equals(Constants.PRINT_NEXT_MOVE)){ 
                            System.out.println("E7na hna k type : "+ typeOfOperation);
                    if(opUserNamePlayer != null ){
                        for(ServerHandler object : UsersBase.vector){
                            System.out.println(object.useNameG);
                            if(object.useNameG.equals(opUserNamePlayer)){
                                System.out.println("sending to op name     " + opUserNamePlayer+" x"+ x+" and y"+ y);
                              object.prrintStream.println(x);
                              object.prrintStream.println(y);
                              opUserName =null;
                              break;
                            }
                            
                        }
                        }
                                         
                    }*/ 
                    else if (typeOfOperation.equals(Constants.CHANGE_STATE)) {
                        String userName = bufferedReader.readLine();
                        String state = bufferedReader.readLine();
                        if (state.equals("offline")) {
                            dataAccess.UpdateState(userName, 1);
                        } else if (state.equals("in_game")) {
                            dataAccess.UpdateState(userName, 2);
                        }
                    } else if (typeOfOperation.equals(Constants.CHANGE_SCORE)) {
                        String userName = bufferedReader.readLine();
                        String gameState = bufferedReader.readLine();
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
                    } else if (typeOfOperation.equals(Constants.RESULT)) {
                        System.out.println("=====I recieved result ");
                        
                        System.out.println("=====I will recieve userName ");
                            
                        String userName = dataInputStream.readLine();
                            System.out.println("=====userName: " + userName);
                            String resultt= dataInputStream.readLine();        
                            System.out.println("====result: " + resultt);// OK
                            
                            System.out.println("=====I will send userName: " + userName);
                         //   prrintStream.println(userName);
                           
                         
                                for (ServerHandler object : UsersBase.vector) {
                                    
                                 if (object.useNameG.equals(userName)) {
                                   System.out.println("=====I will send result: " + resultt);
                                     object.prrintStream.println(resultt);
                                }
                                }
                         //System.out.println("=====I will send result: " + resultt);
                           // prrintStream.println(resultt);
                            
                            
                            
//                                for (ServerHandler object : UsersBase.vector) {
//                                    
//                                    System.out.println(object.useNameG);
//                                     System.out.println(userName);
//
//                                 if (object.useNameG.equals(userName)) {
//                                   System.out.println(object.useNameG);
//                                   System.out.println("=====I will send result: " + result);
//                                     object.prrintStream.println(resultt);
//                                }
//                                 
//                                 
//                                 }
                                
                          
               
                
                        }else {
                        System.out.println("null type");
                    }
                    }else{
                                    prrintStream.println(Constants.SERVER_STOP);

                } 

                } else {
                       state= false;
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
            UsersBase.vector.add(this);
            useNameG = userName;
            
            
            for(ServerHandler object : UsersBase.vector){
            System.out.println("vector :" +object.useNameG);
            }
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
        
        System.out.println("=====I recieved request to play ");

        userName = bufferedReader.readLine();
        System.out.println("====username: " + userName);
        opUserName = bufferedReader.readLine();
        System.out.println("=====op user name : " + opUserName);
        useNameG= userName;
        for (ServerHandler object : UsersBase.vector) {

            if (object.useNameG.equals(opUserName)) {
                System.out.println("=====I will print userName: " + opUserName);
                object.prrintStream.println(userName);
//                result = object.bufferedReader.readLine();
//                System.out.println(result);
            }

//            if (object.useNameG.equals(userName)) {
//                object.prrintStream.println(result);
//            }

        }
    }
}
