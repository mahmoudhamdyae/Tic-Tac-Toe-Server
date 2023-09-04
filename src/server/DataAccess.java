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
        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String queryString = "SELECT * FROM users";
        rs = statement.executeQuery(queryString);
    }
    
    public void createUser(User user) throws SQLException {
        con.setAutoCommit(false);
        PreparedStatement pst = con.prepareStatement("INSERT INTO users VALUES (?, ?, ?)");
        pst.setString(1, user.getUserName());
        pst.setString(2, user.getPassword());
        pst.setString(3, user.getState().getValue());
        pst.addBatch();
        
        pst.executeBatch();
        
        
        pst.close();
        
        con.commit();
        
        getData();
    }
    
    public ArrayList<User> getUsers() throws SQLException, Exception {
        ArrayList<User> users = new ArrayList<>();
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
    
}
