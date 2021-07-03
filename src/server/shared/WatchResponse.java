package server.shared;


public class WatchResponse {

    int[][] myMatrix;
    int[][] oppMatrix;
    int[][] myShips;
    int[][] oppShips;
    int time;
    public int turn;

    public WatchResponse(int[][] matrix, int[][] oppMatrix, int[][] myShips, int[][] oppShips, int time, int turn){
        this.myMatrix = matrix;
        this.oppMatrix = oppMatrix;
        this.myShips = myShips;
        this.oppShips = oppShips;
        this.time = time;
        this.turn = turn;
    }

    public int[][] getMyMatrix() {
        return myMatrix;
    }

    public int[][] getOppMatrix() {
        return oppMatrix;
    }

    public int[][] getMyShips() {
        return myShips;
    }

    public int[][] getOppShips() {
        return oppShips;
    }

    public int getTime() {
        return time;
    }

    public int getTurn() {
        return turn;
    }
}



