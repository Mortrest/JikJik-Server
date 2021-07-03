package server.model;

public class User {
    private final String username;
    private final String password;
    private int wins;
    private int losses;
    private int score;
    private boolean isOnline;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.wins = 0;
        this.losses = 0;
        this.isOnline = false;
    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
