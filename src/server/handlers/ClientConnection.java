//package server.handlers;
//
//import server.game.GameState;
//import server.model.Models.User;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//public class ClientConnection extends Thread{
//    private User user;
//    private final Character mainEvent;
//    private final String reqEvent;
//    private DataInputStream dataInputStream;
//    private DataOutputStream dataOutputStream;
//    ClientConnection(Socket socket, User user){
//        this.user = user;
//        this.mainEvent = '2';
//        this.reqEvent = "CHANGE";
//        try {
//            dataInputStream = new DataInputStream(socket.getInputStream());
//            dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        }catch (Exception e){
//            System.out.println("Something went wrong!");
//        }
//    }
//
//    @Override
//    public synchronized void start() {
//        isRunning = true;
//        super.start();
//    }
//
//    public void run(){
//        while (isRunning) {
//            String str = null;
//            try {
//                str = (dataInputStream.readUTF());
//            } catch (IOException ignored) {
//            }
//            if (str != null ) {
//                if (str.charAt(0) == (mainEvent)) {
//                    try {
//
//                        dataOutputStream.writeUTF(gameState.timeBack());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else if (str.equals(reqEvent)){
//                    gameState.changeBoard(playerType);
//                    try {
//                        dataOutputStream.writeUTF(gameState.sendBack(playerType));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
////                    recentEvent = (makeEvent(str));
////                    gameState.changeMatrix(playerType, recentEvent.getClickedPosition());
//                    try {
//                        dataOutputStream.writeUTF(gameState.sendBack(playerType));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    public void sendToClient(String str) throws IOException {
//        dataOutputStream.writeUTF(str);
//    }
//
//
//    public int getPlayerType() {
//        return playerType;
//    }
//}
