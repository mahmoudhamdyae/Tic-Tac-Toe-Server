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

private void addUserToDataBase(User user)throws SQLException, Exception{
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

}
