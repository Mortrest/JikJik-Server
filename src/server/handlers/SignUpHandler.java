package server.handlers;

import com.google.gson.Gson;
import server.logic.Users;
import server.model.Models.User;
import server.shared.SignInResponse;
import server.shared.SignUpResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SignUpHandler extends Thread {
    Lobby currentLobby;
    User user;
    Gson gson;
    private String AuthToken;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public SignUpHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.gson = new Gson();
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void start() {
        super.start();
    }

    public void run() {
        while (true) {
            try {
                switch (dataInputStream.readUTF()) {
                    case "SIGN_IN" -> signIn();
                    case "SIGN_UP" -> signUp();
                    case "HOME" -> home();
                    case "EXPLORE" -> explore();
                    case "NOTIF" -> notif();
                    case "MSG" -> messages();
                    case "PROFILE" -> profile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void profile() {
    }


    private void notif() {
    }


    private void messages() {
    }


    private void explore() {
    }

    private void home() throws IOException{

    }

    private void signUp() throws IOException {
        SignUpResponse res = gson.fromJson(dataInputStream.readUTF(), SignUpResponse.class);
        if (Users.signUp(res.getUsername(), res.getfName(), res.getfName(), res.getEmail(), res.getEmail()).equals("Success")) {
            dataOutputStream.writeUTF("Signed Up");
        } else {
            dataOutputStream.writeUTF("Username is already exists");
        }
    }

    private void signIn() throws IOException {
        SignInResponse res = gson.fromJson(dataInputStream.readUTF(), SignInResponse.class);
        boolean str = Users.signIn(res.getUsername(), res.getPassword());
        if (str) {
            user = Users.searchUsername(res.getUsername());
            assert user != null;
            String auth = Users.authMaker(user);
            dataOutputStream.writeUTF("Signed In");
            dataOutputStream.writeUTF(auth);
            // Auth Token
//            if (user != null) {
//                Users.addOnline(this);
//            }
            // Solve onlines
        } else {
            dataOutputStream.writeUTF("Username or Password is incorrect");
        }
    }

    public User getUser() {
        return user;
    }
}
