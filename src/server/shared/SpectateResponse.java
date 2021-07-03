package server.shared;

import java.util.LinkedList;

public class SpectateResponse {
    String player1;
    String player2;
    int destroyed;
    int turns;

    public SpectateResponse(String player1, String player2, int destroyed, int turns){
        this.player1 = player1;
        this.player2 = player2;
        this.destroyed = destroyed;
        this.turns = turns;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getDestroyed() {
        return destroyed;
    }

    public int getTurns() {
        return turns;
    }
}
