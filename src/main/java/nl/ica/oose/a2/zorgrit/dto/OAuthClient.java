package nl.ica.oose.a2.zorgrit.dto;

public class OAuthClient {

    private String token;

    private int userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int client) {
        this.userId = client;
    }
}
