package server.game;

import com.google.gson.Gson;

public class GameResponse {

    private int[][] myBoard;
    private int[][] oppBoard;

    GameResponse(int[][] myBoard,int[][] oppBoard){
        this.myBoard = myBoard;
        this.oppBoard = oppBoard;
    }

    public String Jsonify(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
