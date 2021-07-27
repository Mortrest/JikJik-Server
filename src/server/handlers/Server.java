package server.handlers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server extends Thread {

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();
                MainHandler signUpHandler = new MainHandler(socket);
                signUpHandler.start();
            }
        } catch (IOException e) {
        }
    }
    public Server(){
        System.out.println("---Server Is Running---");

    }

    }

