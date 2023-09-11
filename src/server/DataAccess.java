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
            getData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void getData() throws SQLException {
         statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        rs = statement.executeQuery("SELECT * FROM USERS");
    }
    
    public Boolean signUp(User user) throws SQLException, Exception {
        
        // Check if userName exist
        if (isExist(user)){
        System.out.println("is  Exist");
        return false;
                } else{
         System.out.println("is not  Exist");
              // Add User to database
        con.setAutoCommit(false);
        PreparedStatement pst = con.prepareStatement("INSERT INTO USERS VALUES (?, ?, ?)");
        pst.setString(1, user.getUserName());
        pst.setString(2, user.getPassword());
        pst.setString(3, user.getState().getValue());
        pst.addBatch();
//     pst.executeBatch();
       
       pst.close();
        
        con.commit();
        
        getData();
        
        return true;
        }
    }
    
      public Boolean logIn(User user) throws Exception {
            if (rs != null) {
           while(rs.next()) {
               if (user.getUserName().equals(rs.getString(1)) 
                        && user.getPassword().equals(rs.getString(2))) {
                   //  rs.beforeFirst();
                    return true;
                }
                if (rs.isLast()) {
                      rs.beforeFirst();
               return false;
                }
               
            }
        }else{
            System.out.println("rs is null");
        }
       return false;
        }
    
    public ArrayList<User> getUsers() throws SQLException, Exception {
        ArrayList<User> users = new ArrayList<>();
           if (rs.isLast()) {
                      rs.beforeFirst();
                }
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
    
private Boolean isExist(User user) throws Exception {
          if (rs != null) {
           while(rs.next()) {
               if (user.getUserName().equals(rs.getString(1)) 
                        && user.getPassword().equals(rs.getString(2))) {
                   rs.beforeFirst();
                    return true;
                }
                if (rs.isLast()) {
                      rs.beforeFirst();
               return false;
                }
               
            }
        }else{
            System.out.println("rs is null");
        }
       return false;
}

}
