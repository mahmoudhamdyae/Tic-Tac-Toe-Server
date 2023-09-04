package server.models;

public class User {
    
    private String userName;
    private String password;
    private State state;
    
    public User(String userName, String password, State state) {
        this.userName = userName;
        this.password = password;
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
