package server.shared;

public class Response {

    private final int[][] myMatrix;
    private final int[][] oppMatrix;
    private final int[][] myShips;
    int type;

    public Response(int[][] matrix, int[][] oppMatrix, int[][] myShips, int type) {
        this.myMatrix = matrix;
        this.oppMatrix = oppMatrix;
        this.myShips = myShips;
        this.type = type;
    }
}

