package server.handlers;

import server.game.GameState;
import server.handlers.ClientConnection;
import server.handlers.SignUpHandler;

import java.io.IOException;
import java.util.LinkedList;

public class Lobby extends Thread {

    private GameState gameState;
    private SignUpHandler player11;
    ClientConnection player1;
    ClientConnection player2;
    private SignUpHandler player22;
    private boolean isReady;
    private boolean isFull;
    private boolean isSomeOneWaiting;
    private boolean isFinished;
    private LinkedList<Integer> nums;

    public Lobby(GameState gameState) {
        this.isReady = false;
        this.gameState = gameState;
        isSomeOneWaiting = false;
        nums = new LinkedList<>();
        nums.add(1);
        nums.add(2);
    }

    @Override
    public synchronized void start() {
        super.start();
        System.out.println("raftim prep");
        try {
            prepPhase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isFinished) {
//            player2.setCurrentState(gameState.sendBack(2));
//            player1.setCurrentState(gameState.sendBack(1));
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        }
    }


    public synchronized void joinMatch(SignUpHandler player) {
        if (player11 != null) {
            this.player22 = player;
//            start();
            isReady = true;
            isFull = true;
            System.out.println("We're Ready");
        } else {
            player11 = player;
            isSomeOneWaiting = true;
        }
    }

    public void prepPhase() throws IOException {
//        gameState = new GameState();
        isFinished = false;
        gameState.startGame();
        System.out.println("game shoro shod");
//        player1.sendToClient(gameState.sendBack(1));
//        player2.sendToClient(gameState.sendBack(2));
    }

    public int getNumber(){
        int a = nums.getLast();
        nums.removeLast();
        return a;
    }

    public boolean isFull() {
        return isFull;
    }

    public boolean isSomeOneWaiting() {
        return isSomeOneWaiting;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ClientConnection getPlayer1() {
        return player1;
    }

    public void setPlayer1(ClientConnection player1) {
        this.player1 = player1;
    }

    public ClientConnection getPlayer2() {
        return player2;
    }

    public void setPlayer2(ClientConnection player2) {
        this.player2 = player2;
    }

    public boolean isReady() {
        return isReady;
    }
}

