package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import server.models.State;
import server.models.User;

public class DataAccess {
    
    Connection con;
    ResultSet rs;
    Statement statement;
    
public DataAccess() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/tic_tac_toe", "root", "root");           
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT * FROM USERS");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
public Boolean signUp(User user) throws SQLException, Exception {
        
        if (isUserNameExist(user)){return false;} 
  
        addUserToDataBase(user);
   return true;
    }
  
public Boolean logIn(User user) throws Exception {
            if (rs != null) {
           while(rs.next()) {
               if (user.getUserName().equals(rs.getString(1)) && user.getPassword().equals(rs.getString(2))) {
                    return true;
                }  
            }
        }
        rs.beforeFirst();
       return false;
        }
    
private Boolean isUserNameExist(User user) throws Exception {
          if (rs != null) {
           while(rs.next()) {
               if (user.getUserName().equals(rs.getString(1)) ) {
                   rs.beforeFirst();
                    return true;
                }
            }
        }
       return false;
}

public ArrayList<User> getUsers() throws SQLException, Exception {
        ArrayList<User> users = new ArrayList<>();
        rs.beforeFirst();
        while(rs.next()) {
            State state;
            switch(rs.getString(3)) {
                case "online":
                    state = State.ONLINE;
                    break;
                case "in_game":
                    state = State.IN_GAME;
                    break;
                case "offline":
                    state = State.OFFLINE;
                    break;
                default:
                    throw new Exception("Error State");
            }
            User user = new User(
                    rs.getString(1),
                    rs.getString(2),
                    state
            );
            users.add(user);
        }
        return users;
    }

public ArrayList<String> getOnlineUsers() throws SQLException, Exception {
        ArrayList<String> users = new ArrayList<>();
        rs.beforeFirst();
        while(rs.next()) {
            if (rs.getString(3).equals("online")) {
                users.add(rs.getString(1));
            }            
        }
        
        return users;
    }

private void addUserToDataBase(User user) throws SQLException, Exception{
        con.setAutoCommit(false);
        PreparedStatement pst = con.prepareStatement("INSERT INTO USERS VALUES (?, ?, ?)");
        pst.setString(1, user.getUserName());
        pst.setString(2, user.getPassword());
        pst.setString(3, user.getState().getValue());
        pst.addBatch();
        pst.executeBatch();
       con.commit();
       pst.close();
}
public void UpdateScore(String username, int result) throws SQLException {
        int scoreToAdd = 0;
        con.setAutoCommit(false);
        String sql = "UPDATE USERS SET SCORE = SCORE + ? WHERE USER_NAME = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        switch(result){
            //winning case
            case 0: scoreToAdd = 5;
            //losing case
            case 1: scoreToAdd = -5;
            //draw case
            case 2: scoreToAdd =0 ;
            default: scoreToAdd = 0;
        }
        pst.setInt(1,scoreToAdd );
        pst.setString(2, username);
        pst.addBatch();
        pst.executeBatch();
        con.commit();
        pst.close();
        
        
    
}

public void UpdateState(String username, int result) throws SQLException {
        String stateToChange = "offline";
        con.setAutoCommit(false);
        String sql = "UPDATE USERS SET STATE =  ? WHERE USER_NAME = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        switch(result){
            //Login case
            case 0: stateToChange ="online";
            //Logout case
            case 1: stateToChange ="offline";
            //inGame case
            case 2: stateToChange ="in_game" ;
            default: stateToChange ="offline";
        }
        pst.setString(1,stateToChange );
        pst.setString(2, username);
        pst.addBatch();
        pst.executeBatch();
        con.commit();
        pst.close();
        
        
    
}

}
