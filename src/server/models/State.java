package server.models;

public enum State {
    ONLINE("online"),
    IN_GAME("in_game"),
    OFFLINE("offline");
    
    String value;
    
    State(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
