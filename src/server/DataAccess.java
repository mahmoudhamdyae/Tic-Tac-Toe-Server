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

    private Connection con;
    private ResultSet rs;

    public DataAccess() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/tic_tac_toe", "root", "root");

            // Initialize ResultSet with user data
            getData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void getData() {
        try {
            Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String queryString = "SELECT * FROM users";
            rs = statement.executeQuery(queryString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Boolean signUp(User user) {
        try {
            // Check if userName exists
            if (isExist(user)) {
                System.out.println("User already exists: " + user.getUserName());
                return false;
            }

            // Add User to the database
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement("INSERT INTO users VALUES (?, ?, ?)");
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getState().getValue());
            pst.addBatch();
            pst.executeBatch();

            pst.close();

            con.commit();
            getData(); // Refresh data

            System.out.println("User signed up successfully: " + user.getUserName());
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Boolean logIn(User user) {
        try {
            ArrayList<User> users = getUsers();
            for (User it : users) {
                if (it.getUserName().equals(user.getUserName()) && it.getPassword().equals(user.getPassword())) {
                    System.out.println("User logged in: " + user.getUserName());
                    return true;
                }
            }
            System.out.println("Login failed for user: " + user.getUserName());
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            while (rs.next()) {
                State state;
                switch (rs.getString(3)) {
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
                User user = new User(rs.getString(1), rs.getString(2), state);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return users;
    }

    private Boolean isExist(User user) {
        try {
            ArrayList<User> users = getUsers();
            for (User it : users) {
                if (it.getUserName().equals(user.getUserName())) {
                    System.out.println("User already exists: " + user.getUserName());
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
