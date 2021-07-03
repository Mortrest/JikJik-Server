package server.game;

import com.google.gson.Gson;
import server.logic.Users;
import server.shared.MainResponse;
import server.shared.Response;
import server.shared.ShipsProp;
import server.shared.WatchResponse;

import java.util.Random;


public class GameState {

    private int destroyed;
    private int turns;
    private boolean isFinished;
    private int destroyed1;
    private int destroyed2;

    String player1;
    String player2;
    private int[][] player1Ships = ShipsProp.player1Ships;
    private int[][] player2Ships = ShipsProp.player2Ships;

    private int[][] player1Board = new int[10][10];
    private int[][] player2Board = new int[10][10];

    Timer timer;
    public GameState() {
        isFinished = false;
    }

    synchronized public String timeBack(){
        int turn;
        if (timer.isTurn1){
            turn = 1;
        } else {
            turn = 2;
        }
        MainResponse mainResponse = new MainResponse(timer.time,turn,isFinished);
        Gson gson = new Gson();
        return gson.toJson(mainResponse);
    }

    synchronized public String sendBack(int type){
        Response response;
        if (type == 1) {
            response = new Response(player1Board, player2Board, player1Ships,1);
        } else {
            response = new Response(player2Board, player1Board, player2Ships,2);
        }
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    synchronized public void changeMatrix(int type, int[] xoy) {
        timer.isTurn1 = !timer.isTurn1;
        turns++;
        timer.time = 30;
        int x = xoy[0];
        int y = xoy[1];
        int[][] oppShips;
        int[][] oppBoard;

        if (type == 1) {
             oppShips = player2Ships;
             oppBoard = player2Board;
        } else {
             oppShips = player1Ships;
             oppBoard = player1Board;

        }
        if ((oppShips[x][y] == 1)) {
            oppBoard[x][y] = 2;
            destroyed++;
            if (type == 1){
                destroyed1 = destroyed1 + 1;
            } else {
                destroyed2 = destroyed2 + 1;
            }
            checkWin();
        } else {
            oppBoard[x][y] = 1;
        }

        if (type == 1){
            player2Board = oppBoard;
        } else {
            player1Board = oppBoard;
        }

    }

    public void checkWin(){
        if (destroyed1 == 5){
            Users.won(player1,player2);
            isFinished = true;
        } else if (destroyed2 == 5){
            Users.won(player2,player1);
            isFinished = true;
        }
    }



    public void startGame() {
        isFinished = false;
        timer = new Timer();
        timer.start();
    }


    public void changeBoard(int playerType){
        Random random = new Random();
        int num = random.nextInt(3);
        if (playerType == 1){
            player1Ships = ShipsProp.ships[num];
        } else {
            player2Ships = ShipsProp.ships[num];
        }
    }

    public String spectate() {
        WatchResponse watchResponse = new WatchResponse(player1Board,player2Board,player1Ships,player2Ships,timer.time,turns);
        Gson gson = new Gson();
        return gson.toJson(watchResponse);
    }

    public int getDestroyed1() {
        return destroyed1;
    }

    public void setDestroyed1(int destroyed1) {
        this.destroyed1 = destroyed1;
    }

    public int getDestroyed2() {
        return destroyed2;
    }

    public void setDestroyed2(int destroyed2) {
        this.destroyed2 = destroyed2;
    }

    public int getDestroyed() {
        return destroyed;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getTurns() {
        return turns;
    }


}
