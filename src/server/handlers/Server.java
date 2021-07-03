package server.handlers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server extends Thread {
    LinkedList<Lobby> lobbies;

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();
                SignUpHandler signUpHandler = new SignUpHandler(socket);
                signUpHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Server(){
        System.out.println("---Server Is Running---");
        lobbies = new LinkedList<>();

    }

    }

