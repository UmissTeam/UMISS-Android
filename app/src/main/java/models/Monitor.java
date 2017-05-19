package models;

public class Monitor {

    private String userName;
    private String password;
    private String chairToken;
    private String androidToken;

    public Monitor(String userName, String password, String chairToken, String androidToken) {
        this.userName = userName;
        this.password = password;
        this.chairToken = chairToken;
        this.androidToken = androidToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getChairToken() {
        return chairToken;
    }

    public String getAndroidToken() {
        return androidToken;
    }
}
